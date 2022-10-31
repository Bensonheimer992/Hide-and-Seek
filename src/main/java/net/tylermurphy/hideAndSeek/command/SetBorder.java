/*
 * This file is part of Kenshins Hide and Seek
 *
 * Copyright (c) 2021 Tyler Murphy.
 *
 * Kenshins Hide and Seek free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * he Free Software Foundation version 3.
 *
 * Kenshins Hide and Seek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.tylermurphy.hideAndSeek.command;

import net.tylermurphy.hideAndSeek.Main;
import net.tylermurphy.hideAndSeek.configuration.Map;
import net.tylermurphy.hideAndSeek.configuration.Maps;
import net.tylermurphy.hideAndSeek.game.util.Status;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.tylermurphy.hideAndSeek.configuration.Config.*;
import static net.tylermurphy.hideAndSeek.configuration.Localization.message;

public class SetBorder implements ICommand {

	public void execute(Player sender, String[] args) {
		if (Main.getInstance().getGame().getStatus() != Status.STANDBY) {
			sender.sendMessage(errorPrefix + message("GAME_INPROGRESS"));
			return;
		}
		Map map = Maps.getMap(args[0]);
		if(map == null) {
			sender.sendMessage(errorPrefix + message("INVALID_MAP"));
			return;
		}
		if (map.isSpawnNotSetup()) {
			sender.sendMessage(errorPrefix + message("ERROR_GAME_SPAWN"));
			return;
		}
		if (args.length < 4) {
			map.setWorldBorderData(0, 0, 0, 0, 0);
			addToConfig("worldBorder.enabled",false);
			saveConfig();
			sender.sendMessage(messagePrefix + message("WORLDBORDER_DISABLE"));
			Main.getInstance().getGame().getCurrentMap().getWorldBorder().resetWorldBorder();
			return;
		}
		int num,delay,change;
		try { num = Integer.parseInt(args[0]); } catch (Exception e) {
			sender.sendMessage(errorPrefix + message("WORLDBORDER_INVALID_INPUT").addAmount(args[0]));
			return;
		}
		try { delay = Integer.parseInt(args[1]); } catch (Exception e) {
			sender.sendMessage(errorPrefix + message("WORLDBORDER_INVALID_INPUT").addAmount(args[1]));
			return;
		}
		try { change = Integer.parseInt(args[2]); } catch (Exception e) {
			sender.sendMessage(errorPrefix + message("WORLDBORDER_INVALID_INPUT").addAmount(args[2]));
			return;
		}
		if (num < 100) {
			sender.sendMessage(errorPrefix + message("WORLDBORDER_MIN_SIZE"));
			return;
		}
		if (change < 1) {
			sender.sendMessage(errorPrefix + message("WORLDBORDER_CHANGE_SIZE"));
			return;
		}
		map.setWorldBorderData(
				sender.getLocation().getBlockX(),
				sender.getLocation().getBlockZ(),
				num,
				delay,
				change
		);
		Maps.setMap(map.getName(), map);
		sender.sendMessage(messagePrefix + message("WORLDBORDER_ENABLE").addAmount(num).addAmount(delay));
		map.getWorldBorder().resetWorldBorder();
	}

	public String getLabel() {
		return "setBorder";
	}
	
	public String getUsage() {
		return "<map> <*size> <*delay> <*move>";
	}

	public String getDescription() {
		return "Sets worldboarder's center location, size in blocks, and delay in minutes per shrink. Add no arguments to disable.";
	}

	public List<String> autoComplete(String parameter) {
		if(parameter != null && parameter.equals("map")) {
			return Maps.getAllMaps().stream().map(net.tylermurphy.hideAndSeek.configuration.Map::getName).collect(Collectors.toList());
		}
		return Collections.singletonList(parameter);
	}

}
