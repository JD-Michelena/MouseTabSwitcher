package com.mousetabswitcher;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SettingsMigrationTest
{
	@Test
	public void keepsTheChosenTab()
	{
		// Every tab name the 1.0.0 config could store.
		String[] oldTabs = {"COMBAT", "SKILLS", "QUESTS", "INVENTORY", "EQUIPMENT", "PRAYER", "MAGIC",
			"FRIENDS", "ACCOUNT", "SETTINGS", "EMOTES", "LOGOUT"};
		for (String oldTab : oldTabs)
		{
			assertEquals(TabAction.valueOf(oldTab), SettingsMigration.fromOld(oldTab, "false", TabAction.INVENTORY));
		}
	}

	@Test
	public void disabledButtonDoesNothing()
	{
		assertEquals(TabAction.NONE, SettingsMigration.fromOld("PRAYER", "true", TabAction.INVENTORY));
		assertEquals(TabAction.NONE, SettingsMigration.fromOld(null, "true", TabAction.EQUIPMENT));
	}

	@Test
	public void missingTabUsesTheOldDefault()
	{
		assertEquals(TabAction.INVENTORY, SettingsMigration.fromOld(null, "false", TabAction.INVENTORY));
		assertEquals(TabAction.EQUIPMENT, SettingsMigration.fromOld(null, null, TabAction.EQUIPMENT));
	}

	@Test
	public void unknownValueUsesTheOldDefault()
	{
		assertEquals(TabAction.INVENTORY, SettingsMigration.fromOld("NOT_A_TAB", null, TabAction.INVENTORY));
		// NEXT/PREVIOUS/NONE are not tabs, so they never came from 1.0.0.
		assertEquals(TabAction.EQUIPMENT, SettingsMigration.fromOld("NEXT", null, TabAction.EQUIPMENT));
	}
}
