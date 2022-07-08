package fr.kyloren3600.kingdoms.admin;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.TextLine;
import fr.kyloren3600.kingdoms.Kingdoms;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdminCommand {

	public static void registerAdminCommand(JavaPlugin plugin) {
		plugin.getCommand("kadmin").setExecutor((sender, command, label, args) -> {
			if (args.length == 0) {
				return true;
			}
			if (args[0].equals("status")) {
				Kingdoms.getDisplayManager().sendKingdomStatus(sender);
			}
			if (args[0] .equals("war")) {
				sender.sendMessage("guerre en cours: " + Kingdoms.getWarManager().isWarGoingOn());
			}
			if (args[0].equals("reload")) {
				Kingdoms.getInstance().loadConfig();
				sender.sendMessage("config rechargée !");
			}
			if (args[0].equals("forcecapture")) {
				Kingdoms.getZoneManager().getZone("ocenor-test").capture(Kingdoms.getTeamManager().getTeamFromId("nothingness"));
			}
			if (args[0].equals("forcewar")) {
				Kingdoms.getWarManager().forceWar();
			}
			if (sender instanceof Player player) {
				if (args[0].equals("joinwar")) {
					Kingdoms.getKingdomManager().getKingdomPlayer(player).enterKingdom(Kingdoms.getKingdomManager().getKingdomFromId("ocenor"));
					Kingdoms.getWarManager().joinWar(player);
					sender.sendMessage("guerre rejointe");
				}
				if (args[0].equals("leavewar")) {
					Kingdoms.getWarManager().leaveWar(player);
					sender.sendMessage("guerre quittée");
				}
				if(args[0].equals("holo")) {
					HologramManager hologramManager = JavaPlugin.getPlugin(HologramPlugin.class).getHologramManager();
					Hologram hologram = new Hologram("test", player.getLocation());
					hologram.addLine(new TextLine(hologram, "hey !"));
					hologramManager.addActiveHologram(hologram);
					hologram.spawn();

				}
			}
			return true;
		});
	}

}
