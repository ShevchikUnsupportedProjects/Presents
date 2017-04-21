package me.xDark.presents.listener;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.xDark.presents.Presents;
import me.xDark.presents.Settings;
import me.xDark.presents.Statistic;

public class PresentsClickListener implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		Block b = e.getClickedBlock();
		if (b.getType() != Material.SKULL) {
			return;
		}
		HashSet<Location> activePresents = Presents.getInstance().getActivePresents();
		if (!activePresents.contains(b.getLocation())) {
			return;
		}
		Player p = e.getPlayer();
		Statistic statistic = Presents.getInstance().getStatistics().get(p.getUniqueId());
		if (statistic.editMode()) {
			return;
		}
		int max = activePresents.size();
		if (statistic.getFoundPresents().size() >= max) {
			p.sendMessage(Settings.ALREADY_FOUND_ALL);
			return;
		}
		if (statistic.getFoundPresents().contains(b.getLocation())) {
			p.sendMessage(Settings.ALREADY_FOUND);
			return;
		}
		statistic.getFoundPresents().add(b.getLocation());
		p.sendMessage(Settings.FOUND_PRESENT.replace("%current", String.valueOf(statistic.getFoundPresents().size())).replace("%max", String.valueOf(max)));
		for (String s : Settings.COMMANDS_TO_RUN) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', s).replace("%name", p.getName()));
		}
	}

}