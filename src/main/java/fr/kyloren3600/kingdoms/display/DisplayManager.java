package fr.kyloren3600.kingdoms.display;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.kingdom.KingdomPlayer;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.WarPlayer;
import fr.kyloren3600.kingdoms.war.zone.Zone;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayManager {

	private final String prefix;
	private final Logger logger;


	public DisplayManager(String lang) {
		prefix = "[" + Kingdoms.getInstance().getName() + "] ";
		logger = Bukkit.getServer().getLogger();
	}

	//Config
	public void loadingConfig() {
		sendConfigLog("Loading config...");
	}

	public void configLoaded() {
		sendConfigLog("Config loaded !");
	}

	//War config
	public void configNoWarTimeSpecified() {
		sendConfigLog("No war time specified");
	}

	public void configWrongWarTime(String key) {
		sendConfigLog("Wrong war time at key " + key);
	}

	//Kingdom config
	public void configNoKingdomSpecified() {
		sendConfigLog("No kingdom specified");
	}

	public void configKingdomOwnerNotSpecified(String kingdomName) {
		sendConfigLog("No team owner specified for kingdom " + kingdomName + " so using wilderness as default");
	}

	public void configKingdomOwnerNotFound(String kingdomName, String teamId) {
		sendConfigLog("Team " + teamId + " not found, so using wilderness as owner of " + kingdomName);
	}

	public void configNoZoneSpecifiedForKingdom(String kingdomName) {
		sendConfigLog("No zone specified in config of " + kingdomName);
	}

	public void configIncorrectWorldSpecifiedForKingdom(String kingdomName) {
		sendConfigLog("Incorrect world specified in config of " + kingdomName + " zones can't be loaded without a world");
	}

	//Zone config
	public void configNoWorldGuardRegionSpecified(String zoneName) {
		sendConfigLog("No WorldGuard region specified for zone " + zoneName);
	}

	public void configNoWorldGuardRegionWithSpecifiedId(String zoneName, String regionId) {
		sendConfigLog("No WorldGuard region with id " + regionId + " for zone " + zoneName);
	}

	//Wars
	public void warStarted() {
		Bukkit.getServer().broadcastMessage("DEBUT DE LA GUERRE");
	}

	public void warEnded() {
		Bukkit.getServer().broadcastMessage("FIN DE LA GUERRE");
	}

	//Zones
	public void zoneEntered(Zone zone, WarPlayer warPlayer) {
		warPlayer.getKingdomPlayer().getBukkitPlayer().sendMessage("Vous entrez dans la zone " + zone.getName());
	}

	public void zoneLeft(Zone zone, WarPlayer warPlayer) {
		warPlayer.getKingdomPlayer().getBukkitPlayer().sendMessage("Vous quittez la zone " + zone.getName());
	}

	public void zoneCaptureStarted(Zone zone, Team team) {
		Bukkit.getServer().broadcastMessage("Début de la capture de " + zone.getName() + " par " + team.getName());
	}

	public void zoneCaptureCanceled(Zone zone) {
		Bukkit.getServer().broadcastMessage("Capture de la zone " + zone.getName() + " annulée !");
	}

	public void zoneCaptured(Zone zone, Team team) {
		Bukkit.getServer().broadcastMessage("Zone " + zone.getName() + " capturée par " + team.getName());
	}

	public void zoneCapturePercentChanged(Zone zone, int newValue) {
		Bukkit.getServer().broadcastMessage("Zone " + zone.getName() + " capturée à " + newValue + "%");
	}

	//Kingdoms
	public void kingdomConquered(Kingdom kingdom, Team team) {
		Bukkit.getServer().broadcastMessage("Royaume " + kingdom.getName() + " conquis par " + team.getName());
	}

	public void kingdomEntered(Kingdom kingdom, KingdomPlayer kingdomPlayer) {
		kingdomPlayer.getBukkitPlayer().sendMessage("Vous entrez dans le royaume " + kingdom.getName());
	}

	public void kingdomLeft(Kingdom kingdom, KingdomPlayer kingdomPlayer) {
		kingdomPlayer.getBukkitPlayer().sendMessage("Vous quittez le royaume " + kingdom.getName());
	}

	//Admin command
	public void sendKingdomStatus(CommandSender sender) {
		Collection<Kingdom> kingdoms = Kingdoms.getKingdomManager().getKingdoms();
		kingdoms.forEach(kingdom -> {
			sender.sendMessage(kingdom.getId() + ": " + kingdom.getName() + " " + kingdom.getOwner().getName() + " ");
		});
	}

	public void wrongKingdomZoneError(Zone zone, KingdomPlayer kingdomPlayer) {

	}

	private void sendConfigLog(String message) {
		logger.log(Level.INFO, prefix + message);
	}
}
