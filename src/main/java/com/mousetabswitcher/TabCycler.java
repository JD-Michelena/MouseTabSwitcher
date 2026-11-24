package com.mousetabswitcher;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Works out the on-screen order of the tab stones and which tab Next/Previous should open.
 * Pure logic with no client access, so it can be unit tested.
 */
final class TabCycler
{
	private TabCycler()
	{
	}

	/**
	 * Orders the visible stones the way the player reads them: row by row from the top,
	 * left to right within a row. A single-row layout (modern "bottom line") becomes one
	 * long row, the two-row layouts become top row then bottom row.
	 */
	static List<Tab> order(Map<Tab, Rectangle> visibleStones)
	{
		List<Map.Entry<Tab, Rectangle>> entries = new ArrayList<>(visibleStones.entrySet());
		entries.sort(Comparator.comparingInt(e -> e.getValue().y));

		List<List<Map.Entry<Tab, Rectangle>>> rows = new ArrayList<>();
		List<Map.Entry<Tab, Rectangle>> row = null;
		int rowTop = Integer.MIN_VALUE;
		for (Map.Entry<Tab, Rectangle> e : entries)
		{
			Rectangle r = e.getValue();
			int tolerance = Math.max(4, r.height / 2);
			if (row == null || r.y - rowTop > tolerance)
			{
				row = new ArrayList<>();
				rows.add(row);
				rowTop = r.y;
			}
			row.add(e);
		}

		List<Tab> ordered = new ArrayList<>();
		for (List<Map.Entry<Tab, Rectangle>> r : rows)
		{
			r.sort(Comparator.comparingInt(e -> e.getValue().x));
			for (Map.Entry<Tab, Rectangle> e : r)
			{
				ordered.add(e.getKey());
			}
		}
		return ordered;
	}

	/**
	 * @param ordered all visible tabs in on-screen order (skipped ones included, so the
	 *                current position is known even when the open tab is a skipped one)
	 * @param current the open tab, or null if none / unknown
	 * @param forward true for Next, false for Previous
	 * @return the tab to open, or null if there is nowhere to go
	 */
	static Tab step(List<Tab> ordered, Tab current, boolean forward, Set<Tab> skipped, boolean wrap)
	{
		int n = ordered.size();
		if (n == 0)
		{
			return null;
		}

		int start = current == null ? -1 : ordered.indexOf(current);
		if (start == -1)
		{
			// Nothing open: Next starts from the first tab, Previous from the last.
			start = forward ? -1 : n;
		}

		int dir = forward ? 1 : -1;
		int i = start;
		for (int steps = 0; steps < n; steps++)
		{
			i += dir;
			if (i >= n || i < 0)
			{
				if (!wrap)
				{
					return null;
				}
				i = (i + n) % n;
			}
			Tab candidate = ordered.get(i);
			if (candidate == current)
			{
				return null;
			}
			if (!skipped.contains(candidate))
			{
				return candidate;
			}
		}
		return null;
	}
}
