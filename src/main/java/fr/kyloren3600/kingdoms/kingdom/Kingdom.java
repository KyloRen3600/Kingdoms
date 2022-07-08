package fr.kyloren3600.kingdoms.kingdom;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomConqueredEvent;
import fr.kyloren3600.kingdoms.teams.Team;
import org.bukkit.World;

public final class Kingdom {

	public static final Kingdom NONE = new Kingdom("NONE", "NONE", null, Team.WILDERNESS);

	private final String id;
	private final String name;
	private final World world;
	private Team owner;

	public Kingdom(String id, String name, World world, Team owner) {
		this.id = id;
		this.name = name;
		this.world = world;
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}

	public Team getOwner() {
		return owner;
	}

	public void conquer(Team newOwner) {
		Team oldOwner = owner;
		this.owner = newOwner;
		Kingdoms.callEvent(new KingdomConqueredEvent(this, oldOwner, newOwner));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Kingdom other) {
			return this.id.equals(other.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
