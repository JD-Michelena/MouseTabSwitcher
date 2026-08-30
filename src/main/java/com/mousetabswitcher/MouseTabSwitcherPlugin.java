package com.mousetabswitcher;

import com.google.inject.Provides;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
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
	private MouseTabSwitcherConfig config;

	private AWTEventListener awtEventListener;

	@Override
	protected void startUp()
	{
		log.info("Mouse Tab Switcher started");

		awtEventListener = this::onAwtEvent;

		Toolkit.getDefaultToolkit().addAWTEventListener(
				awtEventListener,
				AWTEvent.MOUSE_EVENT_MASK
		);

		log.info("AWT mouse listener registered");
	}

	@Override
	protected void shutDown()
	{
		if (awtEventListener != null)
		{
			Toolkit.getDefaultToolkit().removeAWTEventListener(awtEventListener);
			awtEventListener = null;
		}

		log.info("Mouse Tab Switcher stopped");
	}

	/**
	 * Detects Mouse 4 and Mouse 5.
	 */
	private void onAwtEvent(AWTEvent event)
	{
		if (!(event instanceof MouseEvent))
		{
			return;
		}

		MouseEvent mouseEvent = (MouseEvent) event;

		if (mouseEvent.getID() != MouseEvent.MOUSE_PRESSED)
		{
			return;
		}

		int button = mouseEvent.getButton();

		/*
		 * =========================
		 * MOUSE 4
		 * =========================
		 */
		if (button == 4)
		{
			if (config.disableMouse4())
			{
				log.info("Mouse 4 is disabled");
				return;
			}

			log.info("MOUSE 4 DETECTED");

			mouseEvent.consume();

			MouseTabSwitcherConfig.Tab tab = config.mouse4Tab();

			log.info("Mouse 4 selected tab: {}", tab);

			clientThread.invokeLater(() ->
			{
				openTab(tab);
			});
		}

		/*
		 * =========================
		 * MOUSE 5
		 * =========================
		 */
		else if (button == 5)
		{
			if (config.disableMouse5())
			{
				log.info("Mouse 5 is disabled");
				return;
			}

			log.info("MOUSE 5 DETECTED");

			mouseEvent.consume();

			MouseTabSwitcherConfig.Tab tab = config.mouse5Tab();

			log.info("Mouse 5 selected tab: {}", tab);

			clientThread.invokeLater(() ->
			{
				openTab(tab);
			});
		}
	}

	/**
	 * Opens the selected RuneLite tab.
	 */
	private void openTab(MouseTabSwitcherConfig.Tab tab)
	{
		if (tab == null)
		{
			log.warn("Selected tab is NULL");
			return;
		}

		int widgetId = tab.getWidgetId();

		Widget widget = client.getWidget(widgetId);

		if (widget == null)
		{
			log.warn("Widget is NULL for tab: {} (ID: {})", tab, widgetId);
			return;
		}

		log.info("TAB WIDGET FOUND!");
		log.info("Tab: {}", tab);
		log.info("Widget ID: {}", widget.getId());
		log.info("Widget actions: {}", widget.getActions());

		/*
		 * This is the same CC_OP action
		 * used by the normal RuneLite tab.
		 */
		client.menuAction(
				-1,
				widgetId,
				MenuAction.CC_OP,
				1,
				-1,
				tab.toString(),
				""
		);

		log.info("TAB ACTION EXECUTED: {}", tab);
	}

	@Provides
	MouseTabSwitcherConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MouseTabSwitcherConfig.class);
	}
}