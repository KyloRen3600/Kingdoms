package fr.kyloren3600.kingdoms.war.zone;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneCaptureCanceledEvent;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneCapturePercentChangedEvent;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneCaptureStartedEvent;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneCapturedEvent;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.WarPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Zone {

	private final Kingdom kingdom;
	private final ProtectedRegion region;
	private final String name;
	private final String id;

	private Team ownerTeam;
	private Team capturingTeam;
	private int capturePercent;

	private Set<WarPlayer> players;
	private Set<WarPlayer> playersView;

	public static String getZoneIdFromKingdomAndRegion(Kingdom kingdom, ProtectedRegion region) {
		return kingdom.getId() + "-" + region.getId();
	}

	public Zone(String name, Kingdom kingdom, ProtectedRegion region, Team team) {
		this.name = name;
		this.kingdom = kingdom;
		this.region = region;
		this.ownerTeam = team;
		this.id = getZoneIdFromKingdomAndRegion(kingdom, region);

		this.capturingTeam = null;
		this.players = new HashSet<>();
		this.playersView = Collections.unmodifiableSet(this.players);
		resetCapture();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Kingdom getKingdom() {
		return kingdom;
	}

	public ProtectedRegion getRegion() {
		return region;
	}

	public Set<WarPlayer> getPlayers() {
		return playersView;
	}

	public Team getOwnerTeam() {
		return ownerTeam;
	}

	public Team getCapturingTeam() {
		return capturingTeam;
	}

	public int getCapturePercent() {
		return capturePercent;
	}

	boolean addToCapturePercent(int i) {
		int oldValue = capturePercent;
		capturePercent += i;
		Kingdoms.callEvent(new ZoneCapturePercentChangedEvent(this, oldValue, capturePercent));
		return capturePercent >= 100;
	}

	boolean removeFromCapturePercent(int i) {
		int oldValue = capturePercent;
		capturePercent -= i;
		Kingdoms.callEvent(new ZoneCapturePercentChangedEvent(this, oldValue, capturePercent));
		return capturePercent >= 100;
	}

	public void startCapture(Team team) {
		resetCapture();
		capturingTeam = team;
		Kingdoms.callEvent(new ZoneCaptureStartedEvent(this, team));
	}

	public void capture(Team team) {
		resetCapture();
		ownerTeam = team;
		Kingdoms.callEvent(new ZoneCapturedEvent(this, team));
	}

	public void cancelCapture() {
		resetCapture();
		Kingdoms.callEvent(new ZoneCaptureCanceledEvent(this));
	}

	void resetCapture() {
		capturePercent = 0;
		capturingTeam = null;
	}

	void registerPlayer(WarPlayer player) {
		players.add(player);
	}

	void unregisterPlayer(WarPlayer player) {
		players.remove(player);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof Zone other) {
			return this.id.equals(other.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
