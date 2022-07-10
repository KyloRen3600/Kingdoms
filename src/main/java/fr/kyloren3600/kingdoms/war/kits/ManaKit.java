package fr.kyloren3600.kingdoms.war.kits;

import fr.kyloren3600.kingdoms.Kingdoms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public abstract class ManaKit extends Kit {

	private final float maxMana;
	private final int manaUpdateInterval;

	private float mana;
	private BukkitTask bukkitTask;

	public ManaKit(Player bukkitPlayer, float maxMana, int manaUpdateInterval) {
		super(bukkitPlayer);
		mana = maxMana;
		this.maxMana = maxMana;
		this.manaUpdateInterval = manaUpdateInterval;
		registerManaUpdater();
	}

	public float getMana() {
		return mana;
	}

	protected void displayMana() {
		Player bukkitPlayer = getBukkitPlayer();
		int level = (int) Math.floor(mana);
		float exp = mana - level;
		bukkitPlayer.setLevel(level);
		bukkitPlayer.setExp(exp);
	}

	protected float updateMana() {
		return mana + 1;
	};

	private void registerManaUpdater() {
		bukkitTask = Bukkit.getScheduler().runTaskLater(
				Kingdoms.getInstance(), () -> {
					mana = updateMana();
					if (maxMana < mana) mana = maxMana;
					if (mana < 0) mana = 0;
					displayMana();
					registerManaUpdater();
				},
				manaUpdateInterval
		);
	}

	@Override
	public void unload() {
		super.unload();
		if (bukkitTask != null) {
			bukkitTask.cancel();
		}
	}
}
