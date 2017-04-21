package me.xDark.presents;

import java.util.List;
import org.bukkit.ChatColor;

public final class Settings {

	public static String SKULL_OWNER;

	public static String ALREADY_FOUND;
	public static String ALREADY_FOUND_ALL;

	public static String PRESENT_PLACED;
	public static String FOUND_PRESENT;

	public static List<String> COMMANDS_TO_RUN;

	public static void update() {
		SKULL_OWNER = Presents.getInstance().getConfig().getString("settings.skull-owner");
		COMMANDS_TO_RUN = Presents.getInstance().getConfig().getStringList("settings.commands");
		ALREADY_FOUND = ChatColor.translateAlternateColorCodes('&', Presents.getInstance().getConfig().getString("messages.already-found"));
		ALREADY_FOUND_ALL = ChatColor.translateAlternateColorCodes('&', Presents.getInstance().getConfig().getString("messages.already-found-all"));
		PRESENT_PLACED = ChatColor.translateAlternateColorCodes('&', Presents.getInstance().getConfig().getString("messages.present-placed"));
		FOUND_PRESENT = ChatColor.translateAlternateColorCodes('&', Presents.getInstance().getConfig().getString("messages.present-found"));
	}

}
