package fr.kyloren3600.kingdoms.war.kits;

import fr.kyloren3600.kingdoms.Kingdoms;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener {

	private final Player bukkitPlayer;

	public Kit(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}

	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	public void load() {
		Kingdoms.getInstance().getServer().getPluginManager().registerEvents(this, Kingdoms.getInstance());
	}

	public void unload() {
		HandlerList.unregisterAll(this);
	}
}
