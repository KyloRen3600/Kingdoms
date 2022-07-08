package fr.kyloren3600.kingdoms.display;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.zone.Zone;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class HologramsManager {

	private final Map<Zone, List<String>> zoneHolograms;

	protected HologramsManager(String lang, Map<Kingdom, List<String>> kingdomHolograms, Map<Zone, List<String>> zoneHolograms) {
		this.zoneHolograms = Map.copyOf(zoneHolograms);
	}

	protected void hideAllHolograms() {
		List<String> lines = new ArrayList<>();
		zoneHolograms.forEach((zone, holograms) -> {
			for (String hologramName : holograms) {
				setHologramLines(hologramName, lines);
			}
		});
	}

	protected void warStarted() {
		zoneHolograms.forEach((zone, holograms) -> {
			List<String> lines = Arrays.asList(
					zone.getKingdom().getName() + ": " + zone.getName(),
					"Owner: " + zone.getOwnerTeam().getName()
			);
			setLinesOfAllHologramsOfZone(zone, lines);
		});
	}

	protected void warEnded() {
		hideAllHolograms();
	}

	protected void zoneCaptureStarted(Zone zone, Team team) {
		List<String> lines = Arrays.asList(
				zone.getKingdom().getName() + ": " + zone.getName(),
				"Owner: " + zone.getOwnerTeam().getName(),
				"Capturing Team: " + team.getName(),
				"Capture: " + zone.getCapturePercent() + "%"
		);
		setLinesOfAllHologramsOfZone(zone, lines);
	}

	protected void zoneCaptureCanceled(Zone zone) {
		List<String> lines = Arrays.asList(
				zone.getKingdom().getName() + ": " + zone.getName(),
				"Owner: " + zone.getOwnerTeam().getName()
		);
		setLinesOfAllHologramsOfZone(zone, lines);
	}

	protected void zoneCaptured(Zone zone, Team team) {
		List<String> lines = Arrays.asList(
				zone.getKingdom().getName() + ": " + zone.getName(),
				"Owner: " + team.getName()
		);
		setLinesOfAllHologramsOfZone(zone, lines);
	}

	protected void zoneCapturePercentChanged(Zone zone, int newValue) {
		List<String> lines = Arrays.asList(
				zone.getKingdom().getName() + ": " + zone.getName(),
				"Owner: " + zone.getOwnerTeam().getName(),
				"Capturing Team: " + zone.getCapturingTeam().getName(),
				"Capture: " + newValue + "%"
		);
		setLinesOfAllHologramsOfZone(zone, lines);
	}

	private void setHologramLines(String hologramName, List<String> lines) {
		Hologram hologram = DHAPI.getHologram(hologramName);
		if (hologram != null) {
			DHAPI.setHologramLines(hologram, lines);
		}
	}

	private void setLinesOfAllHologramsOfZone(Zone zone, List<String> lines) {
		List<String> holograms = zoneHolograms.get(zone);
		if (holograms != null) {
			for (String hologramName : holograms) {
				setHologramLines(hologramName, lines);
			}
		}
	}
}
