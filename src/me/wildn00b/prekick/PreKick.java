package me.wildn00b.prekick;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class PreKick extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public Permissions permissions;
	public Config config;
	public Whitelist whitelist;

	@Override
	public void onEnable() {
		permissions = new Permissions(this);
		config = new Config(new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
		whitelist = new Whitelist(this);

		getServer().getPluginManager().registerEvents(new Listener(this), this);

		log.log(Level.INFO, "[PreKick] Initialize successfully.");
	}

	@Override
	public void onDisable() {
		whitelist = null;
		config.Close();
		config = null;
		permissions = null;
		log.log(Level.INFO, "[PreKick] Disabling successfully.");
	}

}
