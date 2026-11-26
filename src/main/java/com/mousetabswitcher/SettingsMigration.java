package com.mousetabswitcher;

/**
 * Moves the settings of Mouse Tab Switcher 1.0.0 to the new per-button actions.
 *
 * <p>Version 1.0.0 had a tab per button ({@code mouse4Tab}, {@code mouse5Tab}) and a
 * "disable" switch per button ({@code disableMouse4}, {@code disableMouse5}). The stored
 * values are the names of the old {@code Tab} enum (COMBAT, SKILLS, QUESTS, INVENTORY,
 * EQUIPMENT, PRAYER, MAGIC, FRIENDS, ACCOUNT, SETTINGS, EMOTES, LOGOUT). Each of these
 * names is also a {@link TabAction}, so a user keeps the same tab on the same button.
 */
final class SettingsMigration
{
	private SettingsMigration()
	{
	}

	/**
	 * @param oldTab     the stored old tab name, or null if it was never stored
	 * @param disabled   the stored old "disable" value, or null if it was never stored
	 * @param defaultTab the action the old version used when no tab was stored
	 * @return the action for the new button key
	 */
	static TabAction fromOld(String oldTab, String disabled, TabAction defaultTab)
	{
		if (Boolean.parseBoolean(disabled))
		{
			return TabAction.NONE;
		}
		if (oldTab == null)
		{
			return defaultTab;
		}
		try
		{
			TabAction action = TabAction.valueOf(oldTab);
			return action.getTab() != null ? action : defaultTab;
		}
		catch (IllegalArgumentException e)
		{
			return defaultTab;
		}
	}
}
