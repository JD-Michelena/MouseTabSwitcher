package com.mousetabswitcher;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MouseTabSwitcherPlugin.class);
		RuneLite.main(args);
	}
}