package com.mousetabswitcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class TabCyclerTest
{
	private static final Set<Tab> NONE = Collections.emptySet();

	/** Classic resizable: 7 stones on top, 7 below. */
	private static Map<Tab, Rectangle> twoRows()
	{
		Map<Tab, Rectangle> m = new EnumMap<>(Tab.class);
		Tab[] top = {Tab.COMBAT, Tab.SKILLS, Tab.QUESTS, Tab.INVENTORY, Tab.EQUIPMENT, Tab.PRAYER, Tab.MAGIC};
		Tab[] bottom = {Tab.CLAN, Tab.ACCOUNT, Tab.FRIENDS, Tab.LOGOUT, Tab.SETTINGS, Tab.EMOTES, Tab.MUSIC};
		for (int i = 0; i < 7; i++)
		{
			// Slight y jitter within a row must not split it.
			m.put(top[i], new Rectangle(500 + i * 33, 170 + (i % 2), 33, 36));
			m.put(bottom[i], new Rectangle(500 + i * 33, 466, 33, 36));
		}
		return m;
	}

	/** Modern bottom line: one row of 13, logout hidden. */
	private static Map<Tab, Rectangle> oneRow()
	{
		Map<Tab, Rectangle> m = new EnumMap<>(Tab.class);
		Tab[] row = {Tab.COMBAT, Tab.SKILLS, Tab.QUESTS, Tab.INVENTORY, Tab.EQUIPMENT, Tab.PRAYER, Tab.MAGIC,
			Tab.CLAN, Tab.ACCOUNT, Tab.FRIENDS, Tab.SETTINGS, Tab.EMOTES, Tab.MUSIC};
		for (int i = 0; i < row.length; i++)
		{
			m.put(row[i], new Rectangle(1000 + i * 33, 800, 33, 36));
		}
		return m;
	}

	@Test
	public void ordersTwoRowsRowMajor()
	{
		List<Tab> order = TabCycler.order(twoRows());
		assertEquals(14, order.size());
		assertEquals(Tab.COMBAT, order.get(0));
		assertEquals(Tab.MAGIC, order.get(6));
		assertEquals(Tab.CLAN, order.get(7));
		assertEquals(Tab.MUSIC, order.get(13));
	}

	@Test
	public void magicToClanCrossesRows()
	{
		List<Tab> order = TabCycler.order(twoRows());
		assertEquals(Tab.CLAN, TabCycler.step(order, Tab.MAGIC, true, NONE, true));
		assertEquals(Tab.MAGIC, TabCycler.step(order, Tab.CLAN, false, NONE, true));
	}

	@Test
	public void wrapsAndStopsAtEnds()
	{
		List<Tab> order = TabCycler.order(oneRow());
		assertEquals(Tab.COMBAT, TabCycler.step(order, Tab.MUSIC, true, NONE, true));
		assertEquals(Tab.MUSIC, TabCycler.step(order, Tab.COMBAT, false, NONE, true));
		assertNull(TabCycler.step(order, Tab.MUSIC, true, NONE, false));
		assertNull(TabCycler.step(order, Tab.COMBAT, false, NONE, false));
	}

	@Test
	public void hiddenLogoutIsNotInBottomLine()
	{
		List<Tab> order = TabCycler.order(oneRow());
		assertEquals(Tab.SETTINGS, TabCycler.step(order, Tab.FRIENDS, true, NONE, true));
	}

	@Test
	public void skipsTabs()
	{
		List<Tab> order = TabCycler.order(twoRows());
		Set<Tab> skip = EnumSet.of(Tab.LOGOUT, Tab.SETTINGS);
		assertEquals(Tab.EMOTES, TabCycler.step(order, Tab.FRIENDS, true, skip, true));
		assertEquals(Tab.FRIENDS, TabCycler.step(order, Tab.EMOTES, false, skip, true));
		// Stepping away from a skipped tab that happens to be open still works.
		assertEquals(Tab.EMOTES, TabCycler.step(order, Tab.LOGOUT, true, skip, true));
	}

	@Test
	public void nothingOpenStartsAtTheEnds()
	{
		List<Tab> order = TabCycler.order(twoRows());
		assertEquals(Tab.COMBAT, TabCycler.step(order, null, true, NONE, false));
		assertEquals(Tab.MUSIC, TabCycler.step(order, null, false, NONE, false));
	}

	@Test
	public void allSkippedGoesNowhere()
	{
		List<Tab> order = Arrays.asList(Tab.COMBAT, Tab.SKILLS);
		assertNull(TabCycler.step(order, Tab.COMBAT, true, EnumSet.of(Tab.SKILLS), true));
	}
}
