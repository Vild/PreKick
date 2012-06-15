package me.wildn00b.prekick;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private YamlConfiguration config;
	private File file;
	public boolean reloadWhenRead;

	public Config(File file) {
		this.file = file;
		config = new YamlConfiguration();
		Reload();
	}

	private void CreateDefaultConfig() {
		config.set("PreKick.Enabled", true);
		config.set("PreKick.Easter-egg", true); // Broadcast when I (WildN00b) join the server
		config.set("PreKick.ReloadWhenRead", false);
//		config.set("PreKick.WhitelistInConfig", true);

		config.set("Whitelist.Enabled", true);
		config.set("Whitelist.KickMessage", "Connection refuse: connect");
		config.set("Whitelist.Players", Arrays.asList(new String[] { "WildN00b", "ThePf" }));

		config.set("Whitelist.IP.Enabled", false);
		config.set("Whitelist.IP.AutoAddIP", false);
		config.set("Whitelist.IP.KickMessage", "IP doesn't match");
		config.set("Whitelist.IP.Players.WildN00b", Arrays.asList(new String[] { "127.0.0.1", "10.10.10.10" }));
		config.set("Whitelist.IP.Players.ThePf", Arrays.asList(new String[] { "1.1.1.1" }));

		config.set("Blacklist.Enabled", true);
		config.set("Blacklist.group1.KickMessage", "You are banned for being in the group!");
		config.set("Blacklist.group1.Players", Arrays.asList(new String[] { "ImAGroup" }));
		config.set("Blacklist.Hax.KickMessage", "You are banned for hacking!");
		config.set("Blacklist.Hax.Players", Arrays.asList(new String[] { "Hacker1337", "Haxz" }));

		Save();
	}

	public void Close() {
		Save();
		config = null;
	}

	public void Save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Reload() {
		if (!file.exists()) {
			try {
				config.load(file);
			} catch (Exception e) {
				file.renameTo(new File(file.getPath() + ".bak"));
				PreKick.log.log(Level.SEVERE, "[PreKick] Couldn't load config, moving broken config to '" + file.getAbsoluteFile()
						+ ".bak' and making a new default config file.");
				CreateDefaultConfig();
			}
		} else {
			CreateDefaultConfig();
		}
		reloadWhenRead = config.getBoolean("PreKick.ReloadWhenRead");
	}

	public boolean GetBoolean(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getBoolean(path);
	}

	public String GetString(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getString(path);
	}

	public List<String> GetStringList(String path) {
		return GetStringList(path, false);
	}
	
	public List<String> GetStringList(String path, boolean toLower) {
		if (reloadWhenRead)
			Reload();
		if (toLower) {
			ArrayList<String> tmp = new ArrayList<String>();
			for (String s : config.getStringList(path))
				tmp.add(s.toLowerCase());
			return tmp;
		} else
			return config.getStringList(path);
	}

	public Set<String> GetKeys(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getConfigurationSection(path).getKeys(false);
	}

	public void Set(String path, Object value) {
		config.set(path, value);
		Save();
	}
	
}
