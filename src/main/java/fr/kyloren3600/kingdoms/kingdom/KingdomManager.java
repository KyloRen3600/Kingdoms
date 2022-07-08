package fr.kyloren3600.kingdoms.kingdom;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomConqueredEvent;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomEnteredEvent;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomLeftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;

public final class KingdomManager implements Listener {

	private final Map<String, Kingdom> kingdoms;
	private final Map<Player, KingdomPlayer> players;

	public KingdomManager(Map<String, Kingdom> kingdoms) {
		this.kingdoms = Map.copyOf(kingdoms);
		this.players = new HashMap<>();
	}

	public Kingdom getKingdom(String id) {
		return kingdoms.get(id);
	}

	public Collection<Kingdom> getKingdoms() {
		return kingdoms.values();
	}

	public Kingdom getKingdomFromId(String id) {
		return kingdoms.get(id);
	}

	public KingdomPlayer getKingdomPlayer(Player bukkitPlayer) {
		return players.get(bukkitPlayer);
	}

	public void registerPlayer(Player bukkitPlayer) {
		players.put(bukkitPlayer, new KingdomPlayer(Kingdom.NONE, Kingdoms.getTeamManager().getTeamOfBukkitPlayer(bukkitPlayer), bukkitPlayer));
	}

	public void unregisterPlayer(Player bukkitPlayer) {
		players.get(bukkitPlayer).leaveKingdom();
		players.remove(bukkitPlayer);
	}

	public boolean canEnter(KingdomPlayer kingdomPlayer, Kingdom kingdom) {
		if (Kingdoms.getWarManager().isWarGoingOn()) return true;

		if (kingdomPlayer.getKingdom().equals(kingdom)) return true;

		if (kingdom.getOwner().getAllies().contains(kingdomPlayer.getTeam())) return true;

		return false;

	}

	public boolean enter(KingdomPlayer kingdomPlayer, Kingdom kingdom) {
		if (canEnter(kingdomPlayer, kingdom)) {
			//todo teleport player
			kingdomPlayer.enterKingdom(kingdom);
			return true;
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onKingdomConquered(KingdomConqueredEvent event) {
		Kingdoms.getDisplayManager().kingdomConquered(event.getKingdom(), event.getNewOwner());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onKingdomEnteredEvent(KingdomEnteredEvent event) {
		Kingdoms.getDisplayManager().kingdomEntered(event.getKingdom(), event.getKingdomPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onKingdomLeftEvent(KingdomLeftEvent event) {
		Kingdoms.getDisplayManager().kingdomLeft(event.getKingdom(), event.getKingdomPlayer());
	}
}
