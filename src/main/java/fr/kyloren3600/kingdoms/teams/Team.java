package fr.kyloren3600.kingdoms.teams;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Team {

	public static final Team WILDERNESS = new Team("-1", "Wilderness");

	private final String id;
	private final String name;
	private final Set<Team> allies;
	private final Set<Team> alliesView;

	Team(String id, String name) {
		this.id = id;
		this.name = name;

		allies = new HashSet<>();
		alliesView = Collections.unmodifiableSet(allies);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void addAlly(Team team) {
		allies.add(team);
	}

	public void removeAlly(Team team) {
		allies.remove(team);
	}

	public Set<Team> getAllies() {
		return alliesView;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Team other) {
			return this.id.equals(other.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
