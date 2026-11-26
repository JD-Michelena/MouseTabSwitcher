package com.mousetabswitcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.awt.event.KeyEvent;
import org.junit.Test;

public class TabActionTest
{
	@Test
	public void keyActionsPressTheirKey()
	{
		assertTrue(TabAction.SPACE.isKey());
		assertEquals(KeyEvent.VK_SPACE, TabAction.SPACE.getKeyCode());
		assertEquals(' ', TabAction.SPACE.getKeyChar());

		TabAction[] digits = {TabAction.KEY_1, TabAction.KEY_2, TabAction.KEY_3, TabAction.KEY_4, TabAction.KEY_5,
			TabAction.KEY_6, TabAction.KEY_7, TabAction.KEY_8, TabAction.KEY_9};
		for (int i = 0; i < digits.length; i++)
		{
			TabAction a = digits[i];
			assertTrue(a.isKey());
			assertEquals(KeyEvent.VK_1 + i, a.getKeyCode());
			assertEquals((char) ('1' + i), a.getKeyChar());
			assertEquals(String.valueOf(i + 1), a.toString());
			assertNull(a.getTab());
		}
	}

	@Test
	public void tabActionsAreNotKeys()
	{
		for (TabAction a : TabAction.values())
		{
			if (a.getTab() != null || a == TabAction.NONE || a == TabAction.NEXT || a == TabAction.PREVIOUS)
			{
				assertFalse(a + " must not be a key", a.isKey());
			}
		}
	}

	@Test
	public void keyActionsAreNeverMigratedFromOldSettings()
	{
		// 1.0.0 only stored tab names. A key action name must fall back to the old default.
		assertEquals(TabAction.INVENTORY, SettingsMigration.fromOld("SPACE", null, TabAction.INVENTORY));
		assertEquals(TabAction.INVENTORY, SettingsMigration.fromOld("KEY_1", null, TabAction.INVENTORY));
	}
}
