package com.mousetabswitcher;

import com.google.inject.Provides;

import java.awt.event.MouseEvent;

import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
		name = "Mouse Tab Switcher"
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
	private MouseTabSwitcherConfig config;

	private final MouseAdapter mouseListener = new MouseAdapter()
	{
		@Override
		public MouseEvent mousePressed(MouseEvent event)
		{
			int button = event.getButton();

			if (button == 4)
			{
				if (config.disableMouse4())
				{
					event.consume();
					return event;
				}

				switchToTab(config.mouse4Tab());

				event.consume();
				return event;
			}

			if (button == 5)
			{
				if (config.disableMouse5())
				{
					event.consume();
					return event;
				}

				switchToTab(config.mouse5Tab());

				event.consume();
				return event;
			}

			return event;
		}
	};

	@Override
	protected void startUp()
	{
		mouseManager.registerMouseListener(mouseListener);
	}

	@Override
	protected void shutDown()
	{
		mouseManager.unregisterMouseListener(mouseListener);
	}

	private void switchToTab(MouseTabSwitcherConfig.Tab tab)
	{
		if (tab == null)
		{
			return;
		}

		int widgetId = getWidgetIdForCurrentLayout(tab);

		if (widgetId == -1)
		{
			return;
		}

		clientThread.invokeLater(() -> executeTabAction(widgetId));
	}

    /**
     * Gets the widget ID for the selected tab based on the current
     * RuneLite game client layout.
     *
     * Each layout uses a different top-level interface, so the same
     * OSRS tab can have a different widget ID depending on the layout.
     *
     * @param tab the selected tab
     * @return the widget ID for the current layout, or -1 if the layout
     *         is not supported
     */
	private int getWidgetIdForCurrentLayout(MouseTabSwitcherConfig.Tab tab)
	{
		int topLevelInterfaceId = client.getTopLevelInterfaceId();

		if (topLevelInterfaceId == InterfaceID.TOPLEVEL)
		{
			return tab.getFixedWidgetId();
		}

		if (topLevelInterfaceId == InterfaceID.TOPLEVEL_OSRS_STRETCH)
		{
			return tab.getClassicResizableWidgetId();
		}

		if (topLevelInterfaceId == InterfaceID.TOPLEVEL_PRE_EOC)
		{
			return tab.getModernResizableWidgetId();
		}

		return -1;
	}

	private void executeTabAction(int widgetId)
	{
		Widget widget = client.getWidget(widgetId);

		if (widget == null)
		{
			return;
		}

		Object[] onOpListener = widget.getOnOpListener();

		if (onOpListener == null)
		{
			return;
		}

		client.createScriptEventBuilder(onOpListener)
				.setSource(widget)
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