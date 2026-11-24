# Mouse Tab Switcher

A RuneLite plugin. Use your mouse buttons to switch the OSRS interface tabs.

## Features

- **Many mouse buttons.** Button 4, Button 5, the middle button and Buttons 6–8 each have an action.
- **Open one tab.** A button can open one tab, for example the inventory.
- **Next and previous tab.** A button can go to the next or the previous tab. The order is the order of the
  tabs on your screen: from left to right, then the top row before the bottom row. This works in the Fixed,
  Resizable – Classic, Resizable – Modern and bottom-line layouts. The plugin does not use tabs that the
  layout hides, for example Logout in the modern layout.
- **Double-click actions.** A button can do a different action when you push it two times quickly. You can
  set the speed (250 ms by default). If a button has a double-click action, its single action waits for
  this time. A button without a double-click action works immediately.
- **Wrap around.** After the last tab, go to the first tab again (on by default).
- **Skip tabs.** Next and previous do not go to the tabs that you select (Logout by default). A button that
  opens one tab can still open these tabs.
- **Shift reverses.** Hold Shift to make a Next or Previous button go the other direction (off by default).
- **Keyboard keys.** You can set a key for Next and a key for Previous. Use them if your mouse software
  changes extra side buttons into keys.
- **Close tab if already open.** Off by default: when the tab is already open, a button for that tab does
  not close the side panel. Turn it on to close the panel, like a click on the tab.

## Setup

1. Open the RuneLite configuration panel.
2. Search for **Mouse Tab Switcher**.
3. Set an action for each mouse button.

The plugin works when RuneLite's **Block extra mouse buttons** option is on or off. You can keep it on.

## Settings from version 1.0.0

Version 1.0.0 had one tab for Button 4 and one tab for Button 5. When you update, the plugin moves these
settings to the new **Button 4** and **Button 5** settings. Your buttons open the same tabs as before. If you
disabled a button in 1.0.0, its new action is **None**.

New users get **Previous** on Button 4 and **Next** on Button 5.

## Notes

- On Windows, Java reports only mouse buttons 1–5. Many gaming mice need their own software to change
  extra buttons into keys. Set these keys as the Next and Previous keys.
- If you give the middle button an action, you cannot drag the camera with the middle button.

## Authors

- **JD_Michelena** made the original plugin.
- **Ing-en** added the features of version 2.0.0 (many buttons, next/previous, double-click and the other
  options). These features started as a separate test plugin, Mouse Tab Switcher Extended.

The version 2.0.0 features are vibe coded: an AI assistant ([Claude](https://claude.com/claude-code)) wrote
the code from Ing-en's descriptions. Ing-en tested the features in RuneLite.

## License

BSD 2-Clause License. See [LICENSE](LICENSE).
