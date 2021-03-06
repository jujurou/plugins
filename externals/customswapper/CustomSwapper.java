/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.customswapper;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.customswapper.utils.PrayerMap;
import net.runelite.client.plugins.externals.customswapper.utils.Spells;
import net.runelite.client.plugins.externals.customswapper.utils.Tab;
import net.runelite.client.plugins.externals.customswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.Clipboard;
import net.runelite.client.util.HotkeyListener;
import org.apache.commons.lang3.tuple.Pair;

@PluginDescriptor(
		name = "Custom Swapper",
		description = "Use plugin in PvP situations for best results",
		tags = {"op", "af"},
		type = PluginType.EXTERNAL
)
@Slf4j
@SuppressWarnings("unused")
public class CustomSwapper extends Plugin
{
	private static final Splitter NEWLINE_SPLITTER = Splitter
			.on("\n")
			.omitEmptyStrings()
			.trimResults();

	@Inject
	private Client client;
	@Inject
	private KeyManager keyManager;
	@Inject
	private CustomSwapperConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private EventBus eventBus;
	@Inject
	private TabUtils tabUtils;

	private ExecutorService executor;
	private Robot robot;

	@Provides
	CustomSwapperConfig getConfig(ConfigManager manager)
	{
		return manager.getConfig(CustomSwapperConfig.class);
	}

	@Override
	protected void startUp() throws AWTException
	{
		executor = Executors.newFixedThreadPool(1);
		Flexo.client = client;
		robot = new Robot();
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			keyManager.registerKeyListener(one);
			keyManager.registerKeyListener(two);
			keyManager.registerKeyListener(three);
			keyManager.registerKeyListener(four);
			keyManager.registerKeyListener(five);
			keyManager.registerKeyListener(six);
			keyManager.registerKeyListener(seven);
			keyManager.registerKeyListener(eight);
			keyManager.registerKeyListener(nine);
			keyManager.registerKeyListener(ten);
			keyManager.registerKeyListener(eleven);
			keyManager.registerKeyListener(twelve);
			keyManager.registerKeyListener(thirteen);
			keyManager.registerKeyListener(fourteen);
			keyManager.registerKeyListener(fifteen);
			keyManager.registerKeyListener(sixteen);
			keyManager.registerKeyListener(seventeen);
			keyManager.registerKeyListener(eighteen);
			keyManager.registerKeyListener(nineteen);
			keyManager.registerKeyListener(twenty);
		}
	}

	@Override
	protected void shutDown()
	{
		executor.shutdown();
		keyManager.unregisterKeyListener(one);
		keyManager.unregisterKeyListener(two);
		keyManager.unregisterKeyListener(three);
		keyManager.unregisterKeyListener(four);
		keyManager.unregisterKeyListener(five);
		keyManager.unregisterKeyListener(six);
		keyManager.unregisterKeyListener(seven);
		keyManager.unregisterKeyListener(eight);
		keyManager.unregisterKeyListener(nine);
		keyManager.unregisterKeyListener(ten);
		keyManager.unregisterKeyListener(eleven);
		keyManager.unregisterKeyListener(twelve);
		keyManager.unregisterKeyListener(thirteen);
		keyManager.unregisterKeyListener(fourteen);
		keyManager.unregisterKeyListener(fifteen);
		keyManager.unregisterKeyListener(sixteen);
		keyManager.unregisterKeyListener(seventeen);
		keyManager.unregisterKeyListener(eighteen);
		keyManager.unregisterKeyListener(nineteen);
		keyManager.unregisterKeyListener(twenty);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			keyManager.unregisterKeyListener(one);
			keyManager.unregisterKeyListener(two);
			keyManager.unregisterKeyListener(three);
			keyManager.unregisterKeyListener(four);
			keyManager.unregisterKeyListener(five);
			keyManager.unregisterKeyListener(six);
			keyManager.unregisterKeyListener(seven);
			keyManager.unregisterKeyListener(eight);
			keyManager.unregisterKeyListener(nine);
			keyManager.unregisterKeyListener(ten);
			keyManager.unregisterKeyListener(eleven);
			keyManager.unregisterKeyListener(twelve);
			keyManager.unregisterKeyListener(thirteen);
			keyManager.unregisterKeyListener(fourteen);
			keyManager.unregisterKeyListener(fifteen);
			keyManager.unregisterKeyListener(sixteen);
			keyManager.unregisterKeyListener(seventeen);
			keyManager.unregisterKeyListener(eighteen);
			keyManager.unregisterKeyListener(nineteen);
			keyManager.unregisterKeyListener(twenty);
			return;
		}
		keyManager.registerKeyListener(one);
		keyManager.registerKeyListener(two);
		keyManager.registerKeyListener(three);
		keyManager.registerKeyListener(four);
		keyManager.registerKeyListener(five);
		keyManager.registerKeyListener(six);
		keyManager.registerKeyListener(seven);
		keyManager.registerKeyListener(eight);
		keyManager.registerKeyListener(nine);
		keyManager.registerKeyListener(ten);
		keyManager.registerKeyListener(eleven);
		keyManager.registerKeyListener(twelve);
		keyManager.registerKeyListener(thirteen);
		keyManager.registerKeyListener(fourteen);
		keyManager.registerKeyListener(fifteen);
		keyManager.registerKeyListener(sixteen);
		keyManager.registerKeyListener(seventeen);
		keyManager.registerKeyListener(eighteen);
		keyManager.registerKeyListener(nineteen);
		keyManager.registerKeyListener(twenty);
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted event)
	{
		if (event.getCommand().equalsIgnoreCase("copycs"))
		{
			final ItemContainer e = client.getItemContainer(InventoryID.EQUIPMENT);

			if (e == null)
			{
				return;
			}

			final StringBuilder sb = new StringBuilder();

			for (Item item : e.getItems())
			{
				if (item.getId() == -1 || item.getId() == 0)
				{
					continue;
				}

				sb.append(item.getId());
				sb.append(":");
				sb.append("Equip");
				sb.append("\n");
			}

			final String string = sb.toString();
			Clipboard.store(string);
		}
	}

	private void decode(String string)
	{
		final Map<String, String> map = new LinkedHashMap<>();
		final List<Pair<Tab, Rectangle>> rectPairs = new ArrayList<>();
		final Iterable<String> tmp = NEWLINE_SPLITTER.split(string);

		for (String s : tmp)
		{
			String[] split = s.split(":");
			try
			{
				map.put(split[0], split[1]);
			}
			catch (IndexOutOfBoundsException e)
			{
				return;
			}
		}

		for (Map.Entry<String, String> entry : map.entrySet())
		{
			String param = entry.getKey();
			String command = entry.getValue().toLowerCase();

			switch (command)
			{
				case "equip":
				{
					final Rectangle rect = invBounds(Integer.parseInt(param));

					if (rect == null)
					{
						continue;
					}

					rectPairs.add(Pair.of(Tab.INVENTORY, rect));
				}
				break;
				case "clean":
				{
					final List<Rectangle> rectangleList = listOfBounds(Integer.parseInt(param));

					if (rectangleList.isEmpty())
					{
						continue;
					}

					for (Rectangle rectangle : rectangleList)
					{
						rectPairs.add(Pair.of(Tab.INVENTORY, rectangle));
					}
				}
				break;
				case "remove":
				{
					final Rectangle rect = equipBounds(Integer.parseInt(param));

					if (rect == null)
					{
						continue;
					}

					rectPairs.add(Pair.of(Tab.EQUIPMENT, rect));
				}
				break;
				case "prayer":
				{
					final WidgetInfo info = PrayerMap.getWidget(param);
					final Prayer p = Prayer.valueOf(param.toUpperCase().replace(" ", "_"));

					if (config.enablePrayCheck() && client.isPrayerActive(p))
					{
						continue;
					}

					if (info == null)
					{
						continue;
					}

					final Widget widget = client.getWidget(info);

					if (widget == null)
					{
						continue;
					}

					rectPairs.add(Pair.of(Tab.PRAYER, widget.getBounds()));
				}
				break;
				case "cast":
				{
					final WidgetInfo info = Spells.getWidget(param);

					if (info == null)
					{
						continue;
					}

					final Widget widget = client.getWidget(info);

					if (widget == null)
					{
						continue;
					}

					rectPairs.add(Pair.of(Tab.SPELLBOOK, widget.getBounds()));
				}
				break;
				case "enable":
				{
					final Widget widget = client.getWidget(593, 35);

					if (widget == null)
					{
						continue;
					}

					rectPairs.add(Pair.of(Tab.COMBAT, widget.getBounds()));
				}
				break;
			}
		}

		executor.submit(() ->
		{
			for (Pair<Tab, Rectangle> pair : rectPairs)
			{
				executePair(pair);
			}
			if (config.swapBack())
			{
				robot.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executePair(Pair<Tab, Rectangle> pair)
	{
		switch (pair.getLeft())
		{
			case COMBAT:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.COMBAT.getId())
				{
					robot.delay((int) getMillis());
					robot.keyPress(tabUtils.getTabHotkey(pair.getLeft()));
					robot.delay((int) getMillis());
				}
				handleSwitch(pair.getRight());
				break;
			case EQUIPMENT:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.EQUIPMENT.getId())
				{
					robot.delay((int) getMillis());
					robot.keyPress(tabUtils.getTabHotkey(pair.getLeft()));
					robot.delay((int) getMillis());
				}
				handleSwitch(pair.getRight());
				break;
			case INVENTORY:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.INVENTORY.getId())
				{
					robot.delay((int) getMillis());
					robot.keyPress(tabUtils.getTabHotkey(pair.getLeft()));
					robot.delay((int) getMillis());
				}
				handleSwitch(pair.getRight());
				break;
			case PRAYER:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.PRAYER.getId())
				{
					robot.delay((int) getMillis());
					robot.keyPress(tabUtils.getTabHotkey(pair.getLeft()));
					robot.delay((int) getMillis());
				}
				handleSwitch(pair.getRight());
				break;
			case SPELLBOOK:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.SPELLBOOK.getId())
				{
					robot.delay((int) getMillis());
					robot.keyPress(tabUtils.getTabHotkey(pair.getLeft()));
					robot.delay((int) getMillis());
				}
				handleSwitch(pair.getRight());
				break;
		}
	}

	private Rectangle invBounds(int id)
	{
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		for (WidgetItem item : inventoryWidget.getWidgetItems())
		{
			if (item.getId() == id)
			{
				return item.getCanvasBounds();
			}
		}

		return null;
	}

	private List<Rectangle> listOfBounds(int id)
	{
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<Rectangle> bounds = new ArrayList<>();

		for (WidgetItem item : inventoryWidget.getWidgetItems())
		{
			if (item.getId() == id)
			{
				bounds.add(item.getCanvasBounds());
			}
		}

		return bounds;
	}

	private Rectangle equipBounds(int id)
	{
		final Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

		if (equipmentWidget.getStaticChildren() == null)
		{
			return null;
		}

		for (Widget widgets : equipmentWidget.getStaticChildren())
		{
			for (Widget items : widgets.getDynamicChildren())
			{
				if (items.getItemId() == id)
				{
					return items.getBounds();
				}
			}
		}

		return null;
	}

	private void handleSwitch(Rectangle rectangle)
	{
		if (rectangle == null)
		{
			return;
		}

		final Point cp = getClickPoint(rectangle);

		if (cp.getX() >= 1 && cp.getY() >= 1)
		{
			leftClick(cp.getX(), cp.getY());
			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void leftClick(int x, int y)
	{
		final double scalingFactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
		if (client.isStretchedEnabled())
		{
			if (client.getMouseCanvasPosition().getX() != x ||
					client.getMouseCanvasPosition().getY() != y)
			{
				moveMouse(x, y);
			}

			double scale = 1 + (scalingFactor / 100);

			MouseEvent mousePressed =
					new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased =
					new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked =
					new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseClicked);
		}
		else
		{
			if (client.getMouseCanvasPosition().getX() != x ||
					client.getMouseCanvasPosition().getY() != y)
			{
				moveMouse(x, y);
			}
			MouseEvent mousePressed = new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseMoved);
	}

	private Point getClickPoint(Rectangle rect)
	{
		double scalingFactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();

		int rand = (Math.random() <= 0.5) ? 1 : 2;
		int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
		int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);

		double scale = 1 + (scalingFactor / 100);

		if (client.isStretchedEnabled())
		{
			return new Point((int) (x * scale), (int) (y * scale));
		}

		return new Point(x, y);
	}

	private final HotkeyListener one = new HotkeyListener(() -> config.customOne())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapOne());
		}
	};

	private final HotkeyListener two = new HotkeyListener(() -> config.customTwo())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapTwo());
		}
	};

	private final HotkeyListener three = new HotkeyListener(() -> config.customThree())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapThree());
		}
	};

	private final HotkeyListener four = new HotkeyListener(() -> config.customFour())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapFour());
		}
	};

	private final HotkeyListener five = new HotkeyListener(() -> config.customFive())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapFive());
		}
	};

	private final HotkeyListener six = new HotkeyListener(() -> config.customSix())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapSix());
		}
	};

	private final HotkeyListener seven = new HotkeyListener(() -> config.customSeven())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapSeven());
		}
	};

	private final HotkeyListener eight = new HotkeyListener(() -> config.customEight())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapEight());
		}
	};

	private final HotkeyListener nine = new HotkeyListener(() -> config.customNine())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapNine());
		}
	};

	private final HotkeyListener ten = new HotkeyListener(() -> config.customTen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapTen());
		}
	};

	private final HotkeyListener eleven = new HotkeyListener(() -> config.customEleven())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapEleven());
		}
	};

	private final HotkeyListener twelve = new HotkeyListener(() -> config.customTwelve())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapTwelve());
		}
	};

	private final HotkeyListener thirteen = new HotkeyListener(() -> config.customThirteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapThirteen());
		}
	};

	private final HotkeyListener fourteen = new HotkeyListener(() -> config.customFourteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapFourteen());
		}
	};

	private final HotkeyListener fifteen = new HotkeyListener(() -> config.customFifteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapFifteen());
		}
	};

	private final HotkeyListener sixteen = new HotkeyListener(() -> config.customSixteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapSixteen());
		}
	};

	private final HotkeyListener seventeen = new HotkeyListener(() -> config.customSeventeen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapSeventeen());
		}
	};

	private final HotkeyListener eighteen = new HotkeyListener(() -> config.customEighteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapEighteen());
		}
	};

	private final HotkeyListener nineteen = new HotkeyListener(() -> config.customNineteen())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapNineteen());
		}
	};

	private final HotkeyListener twenty = new HotkeyListener(() -> config.customTwenty())
	{
		@Override
		public void hotkeyPressed()
		{
			decode(config.customSwapTwenty());
		}
	};
}