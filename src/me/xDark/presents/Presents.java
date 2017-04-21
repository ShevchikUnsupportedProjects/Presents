package me.xDark.presents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDark.presents.listener.PresentsPlaceListener;
import me.xDark.presents.listener.PresentsClickListener;

public class Presents extends JavaPlugin implements Listener {

	private static Presents instance;

	public static Presents getInstance() {
		return instance;
	}

	public Presents() {
		instance = this;
	}

	private final PresentsPlaceListener adminListener = new PresentsPlaceListener();
	private final PresentsClickListener playerListener = new PresentsClickListener();

	private final HashSet<Location> activePresents = new HashSet<>();

	public HashSet<Location> getActivePresents() {
		return activePresents;
	}

	private final HashMap<UUID, Statistic> statistics = new HashMap<>();

	public HashMap<UUID, Statistic> getStatistics() {
		return statistics;
	}

	private final File presentsData = new File(getDataFolder(), "presents.yml");
	private final File playerdata = new File(getDataFolder(), "playerdata.yml");

	@Override
	public void onEnable() {
		saveDefaultConfig();
		Settings.update();
		loadPresentsData();
		loadPlayerData();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(adminListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		for (Player player : Bukkit.getOnlinePlayers()) {
			initStatistic(player);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		initStatistic(e.getPlayer());
	}

	public void initStatistic(Player player) {
		if (!statistics.containsKey(player.getUniqueId())) {
			statistics.put(player.getUniqueId(), new Statistic(Collections.emptyList()));
		}
	}

	@Override
	public void onDisable() {
		savePresentsData();
		savePlayerData();
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
		if (!s.hasPermission("presents.editmode")) {
			return true;
		}
		if (!(s instanceof Player)) {
			return true;
		}
		Statistic statistic = statistics.get(((Player) s).getUniqueId());
		statistic.setEditMode(!statistic.editMode());
		s.sendMessage(String.valueOf(statistic.editMode()));
		return true;
	}


	private static final String presentsLocationsKeyName = "presentsLocations";

	@SuppressWarnings("unchecked")
	private void loadPresentsData() {
		activePresents.clear();
		YamlConfiguration dataYML = YamlConfiguration.loadConfiguration(presentsData);
		activePresents.addAll((Collection<? extends Location>) dataYML.getList(presentsLocationsKeyName, new ArrayList<>()));
	}

	private void savePresentsData() {
		YamlConfiguration dataYML = new YamlConfiguration();
		dataYML.set(presentsLocationsKeyName, new ArrayList<>(activePresents));
		try {
			dataYML.save(presentsData);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Error while saving data", e);
		}
	}


	private static final String foundPresentsLocationsKeyName = "foundLocations";

	@SuppressWarnings("unchecked")
	private void loadPlayerData() {
		statistics.clear();
		YamlConfiguration dataYML = YamlConfiguration.loadConfiguration(playerdata);
		for (String playerUUID : dataYML.getKeys(false)) {
			UUID uuid = UUID.fromString(playerUUID);
			if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
				continue;
			}
			ConfigurationSection section = dataYML.getConfigurationSection(playerUUID);
			List<Location> locations = new ArrayList<Location>((Collection<? extends Location>) section.getList(foundPresentsLocationsKeyName, new ArrayList<>()));
			locations.retainAll(activePresents);
			statistics.put(uuid, new Statistic(locations));
		}
	}

	private void savePlayerData() {
		YamlConfiguration dataYML = new YamlConfiguration();
		for (Map.Entry<UUID, Statistic> entry : statistics.entrySet()) {
			ConfigurationSection section = dataYML.createSection(entry.getKey().toString());
			section.set(foundPresentsLocationsKeyName, new ArrayList<>(entry.getValue().getFoundPresents()));
		}
		try {
			dataYML.save(playerdata);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Error while saving data", e);
		}
	}

}
