package com.mousetabswitcher;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

@Slf4j
@PluginDescriptor(
	name = "Mouse Tab Switcher",
	description = "Switch interface tabs with your mouse buttons: open a tab, step to the next/previous tab, or use double-click actions",
	tags = {"mouse", "tab", "buttons", "interface", "next", "previous", "cycle"}
)
public class MouseTabSwitcherPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private MouseManager mouseManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private MouseTabSwitcherConfig config;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private ConfigManager configManager;

	private final MouseAdapter mouseListener = new MouseAdapter()
	{
		@Override
		public MouseEvent mousePressed(MouseEvent event)
		{
			if (!isBound(event))
			{
				return event;
			}

			onPress(event.getButton(), config.shiftReverses() && event.isShiftDown());
			event.consume();
			return event;
		}

		@Override
		public MouseEvent mouseReleased(MouseEvent event)
		{
			// Keep the game from seeing the other half of a click we handled.
			if (isBound(event))
			{
				event.consume();
			}
			return event;
		}

		@Override
		public MouseEvent mouseClicked(MouseEvent event)
		{
			if (isBound(event))
			{
				event.consume();
			}
			return event;
		}
	};

	/**
	 * A first press that may still turn into a double-click. Guarded by {@code this}.
	 */
	private static final class PendingPress
	{
		final int button;
		final long time;
		final TabAction action;
		final boolean reverse;
		ScheduledFuture<?> single;

		PendingPress(int button, long time, TabAction action, boolean reverse)
		{
			this.button = button;
			this.time = time;
			this.action = action;
			this.reverse = reverse;
		}

		/** @return true if the single action had not run yet and now never will */
		boolean cancel()
		{
			return single != null && single.cancel(false);
		}
	}

	private PendingPress pending;

	private final HotkeyListener nextKeyListener = new HotkeyListener(() -> config.nextTabKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invokeLater(() -> perform(TabAction.NEXT, false));
		}
	};

	private final HotkeyListener previousKeyListener = new HotkeyListener(() -> config.previousTabKey())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invokeLater(() -> perform(TabAction.PREVIOUS, false));
		}
	};

	@Override
	protected void startUp()
	{
		migrateOldSettings();

		// Register first: RuneLite's "Block extra mouse buttons" consumes buttons 4+ before the
		// listeners run, and a consumed event only reaches the first listener. Being first means
		// the plugin works with that option on, and the option keeps the game itself from
		// seeing the side buttons.
		mouseManager.registerMouseListener(0, mouseListener);
		keyManager.registerKeyListener(nextKeyListener);
		keyManager.registerKeyListener(previousKeyListener);
	}

	@Override
	protected void shutDown()
	{
		mouseManager.unregisterMouseListener(mouseListener);
		keyManager.unregisterKeyListener(nextKeyListener);
		keyManager.unregisterKeyListener(previousKeyListener);
		synchronized (this)
		{
			if (pending != null)
			{
				pending.cancel();
				pending = null;
			}
		}
	}

	/**
	 * Moves the 1.0.0 settings (one tab per side button) to the new button actions, so users
	 * keep the tabs they chose. The old keys are removed afterwards, so this runs only once.
	 */
	private void migrateOldSettings()
	{
		migrateButton(MouseTabSwitcherConfig.OLD_BUTTON4_TAB, MouseTabSwitcherConfig.OLD_BUTTON4_DISABLED,
			"button4", TabAction.INVENTORY);
		migrateButton(MouseTabSwitcherConfig.OLD_BUTTON5_TAB, MouseTabSwitcherConfig.OLD_BUTTON5_DISABLED,
			"button5", TabAction.EQUIPMENT);
	}

	private void migrateButton(String tabKey, String disabledKey, String newKey, TabAction oldDefault)
	{
		String group = MouseTabSwitcherConfig.GROUP;
		String oldTab = configManager.getConfiguration(group, tabKey);
		String disabled = configManager.getConfiguration(group, disabledKey);
		if (oldTab == null && disabled == null)
		{
			return;
		}

		TabAction action = SettingsMigration.fromOld(oldTab, disabled, oldDefault);
		configManager.setConfiguration(group, newKey, action);
		configManager.unsetConfiguration(group, tabKey);
		configManager.unsetConfiguration(group, disabledKey);
		log.debug("Moved old setting {}={} (disabled={}) to {}={}", tabKey, oldTab, disabled, newKey, action);
	}

	/**
	 * @return true when this event's button has a single or double-click action and the plugin should handle it
	 */
	private boolean isBound(MouseEvent event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return false;
		}
		int button = event.getButton();
		return singleAction(button) != null || doubleAction(button) != null;
	}

	/**
	 * Runs the single action straight away when the button has no double-click action. Otherwise
	 * the single action waits for the double-click window, and a second press inside it cancels
	 * the single action and runs the double-click action instead.
	 */
	private synchronized void onPress(int button, boolean reverse)
	{
		TabAction single = singleAction(button);
		TabAction dbl = doubleAction(button);
		long now = System.currentTimeMillis();

		if (dbl == null)
		{
			cancelPending();
			run(single, reverse);
			return;
		}

		PendingPress p = pending;
		pending = null;
		if (p != null && p.button == button && now - p.time <= config.doubleClickMs()
			&& (p.single == null || p.cancel()))
		{
			run(dbl, reverse);
			return;
		}
		if (p != null && p.button != button && p.cancel())
		{
			// A different button interrupted the wait: do the first button's single action now.
			run(p.action, p.reverse);
		}

		PendingPress press = new PendingPress(button, now, single, reverse);
		if (single != null)
		{
			press.single = executor.schedule(() -> firePending(press), config.doubleClickMs(), TimeUnit.MILLISECONDS);
		}
		pending = press;
	}

	private synchronized void firePending(PendingPress press)
	{
		if (pending == press)
		{
			pending = null;
		}
		run(press.action, press.reverse);
	}

	/** Runs a waiting single action right away (another button is being handled) and clears it. */
	private synchronized void cancelPending()
	{
		if (pending != null && pending.cancel())
		{
			run(pending.action, pending.reverse);
		}
		pending = null;
	}

	private void run(TabAction action, boolean reverse)
	{
		if (action != null)
		{
			clientThread.invokeLater(() -> perform(action, reverse));
		}
	}

	private TabAction singleAction(int button)
	{
		switch (button)
		{
			case MouseEvent.BUTTON2:
				return bound(config.middleButton());
			case 4:
				return bound(config.button4());
			case 5:
				return bound(config.button5());
			case 6:
				return bound(config.button6());
			case 7:
				return bound(config.button7());
			case 8:
				return bound(config.button8());
			default:
				return null;
		}
	}

	private TabAction doubleAction(int button)
	{
		switch (button)
		{
			case MouseEvent.BUTTON2:
				return bound(config.middleButtonDouble());
			case 4:
				return bound(config.button4Double());
			case 5:
				return bound(config.button5Double());
			case 6:
				return bound(config.button6Double());
			case 7:
				return bound(config.button7Double());
			case 8:
				return bound(config.button8Double());
			default:
				return null;
		}
	}

	private static TabAction bound(TabAction action)
	{
		return action == null || action == TabAction.NONE ? null : action;
	}

	private void perform(TabAction action, boolean reverse)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		int topLevel = client.getTopLevelInterfaceId();
		Map<Tab, Widget> stones = visibleStones(topLevel);
		if (stones.isEmpty())
		{
			log.debug("No tab stones found for top-level interface {}", topLevel);
			return;
		}

		Tab current = currentTab(stones);
		Tab target;
		if (action == TabAction.NEXT || action == TabAction.PREVIOUS)
		{
			boolean forward = (action == TabAction.NEXT) != reverse;
			Map<Tab, Rectangle> bounds = new EnumMap<>(Tab.class);
			stones.forEach((tab, w) -> bounds.put(tab, w.getBounds()));
			List<Tab> ordered = TabCycler.order(bounds);
			Set<Tab> skipped = config.skippedTabs();
			target = TabCycler.step(ordered, current, forward, skipped, config.wrapAround());
			log.debug("{}: layout {} order {} current {} -> {}", action, topLevel, ordered, current, target);
		}
		else
		{
			target = action.getTab();
			if (target == current && !config.closeIfOpen())
			{
				return;
			}
		}

		if (target == null)
		{
			return;
		}

		Widget stone = stones.get(target);
		if (stone != null)
		{
			click(stone);
		}
	}

	/**
	 * The tab stones the player can currently see and click, keyed by tab.
	 */
	private Map<Tab, Widget> visibleStones(int topLevel)
	{
		Map<Tab, Widget> stones = new EnumMap<>(Tab.class);
		for (Tab tab : Tab.values())
		{
			int widgetId = tab.widgetIdFor(topLevel);
			if (widgetId == -1)
			{
				continue;
			}
			Widget w = client.getWidget(widgetId);
			if (w == null || w.isHidden() || w.getOnOpListener() == null || w.getWidth() <= 0 || w.getHeight() <= 0)
			{
				continue;
			}
			stones.put(tab, w);
		}
		return stones;
	}

	private Tab currentTab(Map<Tab, Widget> stones)
	{
		Tab tab = Tab.byStoneIndex(client.getVarcIntValue(VarClientID.TOPLEVEL_PANEL));
		return tab != null && stones.containsKey(tab) ? tab : null;
	}

	private void click(Widget stone)
	{
		Object[] onOp = stone.getOnOpListener();
		if (onOp == null)
		{
			return;
		}

		client.createScriptEventBuilder(onOp)
			.setSource(stone)
			.setOp(1)
			.build()
			.run();
	}

	@Provides
	MouseTabSwitcherConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MouseTabSwitcherConfig.class);
	}
}
