package com.mousetabswitcher;

import com.google.inject.Provides;

import java.awt.event.MouseEvent;

import javax.inject.Inject;

import net.runelite.api.Client;
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

		int widgetId = tab.getWidgetId();

		clientThread.invokeLater(() -> executeTabAction(widgetId));
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