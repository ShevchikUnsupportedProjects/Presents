package me.xDark.presents;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;

public class Statistic {

	private final HashSet<Location> foundPresents = new HashSet<>();

	private boolean editMode = false;

	public Statistic(Collection<Location> foundPresents) {
		this.foundPresents.addAll(foundPresents);
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean editMode() {
		return editMode;
	}

	public HashSet<Location> getFoundPresents() {
		return foundPresents;
	}

}
