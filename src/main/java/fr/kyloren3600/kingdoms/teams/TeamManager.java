package fr.kyloren3600.kingdoms.teams;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

	private final Map<String, Team> teams;

	public TeamManager() {
		this.teams = new HashMap<>(Map.of(Team.WILDERNESS.getId(), Team.WILDERNESS, "nothingness", new Team("nothingness", "NothingNess"), "test", new Team("test", "Test")));
	}

	public Team getTeamFromId(String id) {
		return teams.get(id);
	}

	public Team getTeamOfBukkitPlayer(Player player) {
		return getTeamFromId("nothingness");
	}
}
