package me.wildn00b.prekick;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class PreKick extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public Permissions permissions;
	public Config config;

	@Override
	public void onEnable() {
		config = new Config(new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
		
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			log.log(Level.INFO, "[PreKick] Vault not found, fall back on Bukkit Permissions.");
			permissions = new Permissions(this, true);
		} else
			permissions = new Permissions(this, false);
		
		log.log(Level.INFO, "[PreKick] Initialize successfully.");
	}

	@Override
	public void onDisable() {
		config.Close();
		config = null;
		permissions = null;
	}

}
