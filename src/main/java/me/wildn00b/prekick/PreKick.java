package me.wildn00b.prekick;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PreKick extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public Config config;
	public Language language;
	public Permissions permissions;
	public Whitelist whitelist;
	public String version;

	@Override
	public void onEnable() {
		version = getDescription().getVersion();
		config = new Config(new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
		language = new Language(this);
		permissions = new Permissions(this);
		whitelist = new Whitelist(this);

		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getCommand("prekick").setExecutor(new PreKickCommand(this));
		log.log(Level.INFO, "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + language.GetText("PreKick.Initialized"));
	}

	@Override
	public void onDisable() {
		whitelist = null;
		config.Close();
		config = null;
		permissions = null;
		log.log(Level.INFO, "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + language.GetText("PreKick.Disabling"));
	}

}
