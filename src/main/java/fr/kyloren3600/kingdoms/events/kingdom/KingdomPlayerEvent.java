package fr.kyloren3600.kingdoms.events.kingdom;

import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.kingdom.KingdomPlayer;
import org.bukkit.entity.Player;

public abstract class KingdomPlayerEvent extends KingdomEvent {

	private final KingdomPlayer kingdomPlayer;

	protected KingdomPlayerEvent(Kingdom kingdom, KingdomPlayer kingdomPlayer) {
		super(kingdom);
		this.kingdomPlayer = kingdomPlayer;
	}

	public KingdomPlayer getKingdomPlayer() {
		return kingdomPlayer;
	}

	public Player getBukkitPlayer() {
		return kingdomPlayer.getBukkitPlayer();
	}
}
