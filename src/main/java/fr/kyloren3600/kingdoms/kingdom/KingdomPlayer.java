package fr.kyloren3600.kingdoms.kingdom;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomEnteredEvent;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomLeftEvent;
import fr.kyloren3600.kingdoms.teams.Team;
import org.bukkit.entity.Player;

public final class KingdomPlayer {

	private final Player bukkitPlayer;
	private Team team;
	private Kingdom kingdom;

	KingdomPlayer(Kingdom kingdom, Team team, Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
		this.team = team;
		this.kingdom = kingdom;
	}

	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	public Team getTeam() {
		return team;
	}

	public Kingdom getKingdom() {
		return kingdom;
	}

	public void enterKingdom(Kingdom kingdom) {
		leaveKingdom();
		this.kingdom = kingdom;
		Kingdoms.callEvent(new KingdomEnteredEvent(kingdom, this));
	}

	public void leaveKingdom() {
		if (!kingdom.equals(Kingdom.NONE)) {
			Kingdom oldKingdom = kingdom;
			Kingdoms.callEvent(new KingdomLeftEvent(oldKingdom, this));
		}
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other instanceof KingdomPlayer that) {
			return this.bukkitPlayer.getUniqueId().equals(that.bukkitPlayer.getUniqueId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return bukkitPlayer.hashCode();
	}
}
