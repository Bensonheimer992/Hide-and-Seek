package net.tylermurphy.hideAndSeek.game.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.ActionBar;
import net.tylermurphy.hideAndSeek.Main;
import net.tylermurphy.hideAndSeek.game.util.Status;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static net.tylermurphy.hideAndSeek.configuration.Config.*;
import static net.tylermurphy.hideAndSeek.configuration.Config.glowPowerupItem;
import static net.tylermurphy.hideAndSeek.configuration.Localization.message;

@SuppressWarnings("deprecation")
public class InteractHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Main.getInstance().getBoard().contains(event.getPlayer())) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && blockedInteracts.contains(event.getClickedBlock().getType().name())) {
            event.setCancelled(true);
            return;
        }
        ItemStack temp = event.getItem();
        if (temp == null) return;
        if (Main.getInstance().getGame().getStatus() == Status.STANDBY)
            onPlayerInteractLobby(temp, event);
        if (Main.getInstance().getGame().getStatus() == Status.PLAYING)
            onPlayerInteractGame(temp, event);
        if (Main.getInstance().getBoard().isSpectator(event.getPlayer()))
            onSpectatorInteract(temp, event);
    }

    private void onPlayerInteractLobby(ItemStack temp, PlayerInteractEvent event) {
        if (temp.isSimilar(lobbyLeaveItem)) {
            event.setCancelled(true);
            Main.getInstance().getGame().leave(event.getPlayer());
        }

        if (temp.isSimilar(lobbyStartItem) && event.getPlayer().hasPermission("hideandseek.start")) {
            event.setCancelled(true);
            if (Main.getInstance().getGame().isNotSetup()) {
                event.getPlayer().sendMessage(errorPrefix + message("GAME_SETUP"));
                return;
            }
            if (Main.getInstance().getGame().getStatus() != Status.STANDBY) {
                event.getPlayer().sendMessage(errorPrefix + message("GAME_INPROGRESS"));
                return;
            }
            if (Main.getInstance().getBoard().size() < minPlayers) {
                event.getPlayer().sendMessage(errorPrefix + message("START_MIN_PLAYERS").addAmount(minPlayers));
                return;
            }
            Main.getInstance().getGame().start();
        }
    }

    private void onPlayerInteractGame(ItemStack temp, PlayerInteractEvent event) {
        if (temp.isSimilar(glowPowerupItem)) {
            if (!glowEnabled) return;
            Player player = event.getPlayer();
            if (Main.getInstance().getBoard().isHider(player)) {
                Main.getInstance().getGame().getGlow().onProjectile();
                player.getInventory().remove(glowPowerupItem);
                assert XMaterial.SNOWBALL.parseMaterial() != null;
                player.getInventory().remove(XMaterial.SNOWBALL.parseMaterial());
                event.setCancelled(true);
            }
        }
    }

    private void onSpectatorInteract(ItemStack temp, PlayerInteractEvent event){
        if(temp.isSimilar(flightToggleItem)){
            boolean isFlying = event.getPlayer().getAllowFlight();
            event.getPlayer().setAllowFlight(!isFlying);
            event.getPlayer().setFlying(!isFlying);
            ActionBar.clearActionBar(event.getPlayer());
            if(!isFlying){
                ActionBar.sendActionBar(event.getPlayer(), message("FLYING_ENABLED").toString());
            } else {
                ActionBar.sendActionBar(event.getPlayer(), message("FLYING_DISABLED").toString());
            }
            return;
        }
        if(temp.isSimilar(teleportItem)){
            int amount = Main.getInstance().getBoard().getHiders().size() + Main.getInstance().getBoard().getSeekers().size();
            Inventory teleportMenu = Main.getInstance().getServer().createInventory(null, 9*(((amount-1)/9)+1), ChatColor.stripColor(teleportItem.getItemMeta().getDisplayName()));
            List<String> hider_lore = new ArrayList<>(); hider_lore.add(message("HIDER_TEAM_NAME").toString());
            Main.getInstance().getBoard().getHiders().forEach(hider -> teleportMenu.addItem(getSkull(hider, hider_lore)));
            List<String> seeker_lore = new ArrayList<>(); seeker_lore.add(message("SEEKER_TEAM_NAME").toString());
            Main.getInstance().getBoard().getSeekers().forEach(seeker -> teleportMenu.addItem(getSkull(seeker, seeker_lore)));
            event.getPlayer().openInventory(teleportMenu);
        }
    }

    private ItemStack getSkull(Player player, List<String> lore){
        assert XMaterial.PLAYER_HEAD.parseMaterial() != null;
        ItemStack playerHead = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (byte) 3);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwner(player.getName());
        playerHeadMeta.setDisplayName(player.getName());
        playerHeadMeta.setLore(lore);
        playerHead.setItemMeta(playerHeadMeta);
        return playerHead;
    }
}
