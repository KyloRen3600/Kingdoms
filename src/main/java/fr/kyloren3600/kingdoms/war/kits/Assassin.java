package fr.kyloren3600.kingdoms.war.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin extends ManaKit {

	private static final float maxMana = 10f;
	private static final int manaUpdateInterval = 20;

	private static final PotionEffect defaultSpeed = new PotionEffect(PotionEffectType.SPEED, manaUpdateInterval + 1, 2);
	private static final PotionEffect sneakSpeed = new PotionEffect(PotionEffectType.SPEED, manaUpdateInterval + 1, 5);
	private static final PotionEffect sneakInvisibility = new PotionEffect(PotionEffectType.INVISIBILITY, manaUpdateInterval + 1, 1);

	public Assassin(Player bukkitPlayer) {
		super(bukkitPlayer, maxMana, manaUpdateInterval);
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if (!event.getPlayer().equals(getBukkitPlayer())) return;
		if (event.isSneaking()) {
			if (getMana() > 0) {
				giveSneakEffects();
			}
		} else {
			giveDefaultEffects();
		}
	}

	@Override
	protected float updateMana() {
		Player player = getBukkitPlayer();
		if (player.isSneaking()) {
			player.addPotionEffect(sneakSpeed);
			if (getMana() > 0) {
				player.addPotionEffect(sneakInvisibility);
			}
			return getMana() - 1;
		}
		player.addPotionEffect(defaultSpeed);
		return getMana() + 1;
	}

	@Override
	public void load() {
		giveDefaultEffects();
		super.load();
	}

	@Override
	public void unload() {
		getBukkitPlayer().setTotalExperience(0);
		getBukkitPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
		getBukkitPlayer().removePotionEffect(PotionEffectType.SPEED);
		super.unload();
	}

	private void giveDefaultEffects() {
		Player player = getBukkitPlayer();
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.addPotionEffect(defaultSpeed);
	}

	private void giveSneakEffects() {
		Player player = getBukkitPlayer();
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.addPotionEffect(sneakInvisibility);
		player.addPotionEffect(sneakSpeed);
	}
}
