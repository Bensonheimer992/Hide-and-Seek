package net.tylermurphy.hideAndSeek.command;

import net.tylermurphy.hideAndSeek.Main;
import net.tylermurphy.hideAndSeek.command.util.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.tylermurphy.hideAndSeek.configuration.Config.errorPrefix;
import static net.tylermurphy.hideAndSeek.configuration.Localization.message;

public class Join implements ICommand {

	public void execute(Player sender, String[] args) {
		if (Main.getInstance().getGame().checkCurrentMap()) {
			sender.sendMessage(errorPrefix + message("GAME_SETUP"));
			return;
		}
		Player player = Bukkit.getServer().getPlayer(sender.getName());
		if (player == null) {
			sender.sendMessage(errorPrefix + message("COMMAND_ERROR"));
			return;
		}
		if (Main.getInstance().getBoard().contains(player)) {
			sender.sendMessage(errorPrefix + message("GAME_INGAME"));
			return;
		}
		Main.getInstance().getGame().join(player);
	}

	public String getLabel() {
		return "join";
	}

	public String getUsage() {
		return "<*map>";
	}

	public String getDescription() {
		return "Joins the lobby if game is set to manual join/leave";
	}

	public List<String> autoComplete(@NotNull String parameter, @NotNull String typed) {
		return null;
	}

}
