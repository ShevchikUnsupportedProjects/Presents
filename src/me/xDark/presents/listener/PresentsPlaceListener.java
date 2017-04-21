package me.xDark.presents.listener;

import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.xDark.presents.Presents;
import me.xDark.presents.Settings;
import me.xDark.presents.Statistic;

public class PresentsPlaceListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType() != Material.SKULL) {
			return;
		}
		Player p = e.getPlayer();
		Statistic statistic = Presents.getInstance().getStatistics().get(p.getUniqueId());
		if (!statistic.editMode()) {
			return;
		}
		Presents.getInstance().getActivePresents().add(e.getBlock().getLocation());
		Skull skull = (Skull) e.getBlock().getState();
		skull.setOwner(Settings.SKULL_OWNER);
		skull.update(true);
		p.sendMessage(Settings.PRESENT_PLACED);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRemove(BlockBreakEvent e) {
		Presents.getInstance().getActivePresents().remove(e.getBlock().getLocation());
	}

}
