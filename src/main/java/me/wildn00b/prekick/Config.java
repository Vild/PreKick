package me.wildn00b.prekick;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private YamlConfiguration config;

	public YamlConfiguration getConfig() {
		return config;
	}

	private File file;
	public boolean reloadWhenRead;

	public Config(File file) {
		this.file = file;
		config = new YamlConfiguration();
		Reload();
		UpgradeConfig();
	}

	private void CreateDefaultConfig() {
		config.set("PreKick.Enabled", true);
		config.set("PreKick.Easter-egg", true); // Broadcast when I (WildN00b) join the server
		config.set("PreKick.ReloadWhenRead", false);
		config.set("PreKick.Language", "en-US");

		config.set("Whitelist.Enabled", true);
		config.set("Whitelist.KickMessage", "Connection refuse: connect");
		config.set("Whitelist.Players", Arrays.asList(new String[] { "WildN00b", "ThePf" }));

		config.set("IP.Enabled", false);
		config.set("IP.KickMessage", "&eIP doesn't match");
		config.set("IP.Players.WildN00b", Arrays.asList(new String[] { "127.0.0.1", "10.10.10.10" }));
		config.set("IP.Players.ThePf", Arrays.asList(new String[] { "1.1.1.1" }));

		config.set("Blacklist.Enabled", true);
		config.set("Blacklist.Players.Cheater1", "&a&lYou're not allowed on this server!");
		config.set("Blacklist.Players.Cheater2", "&a&nSorry, but you're not allowed to play on this server!");
		config.set("Blacklist.Groups.group1.KickMessage", "&2&lYou are banned for being in the group!");
		config.set("Blacklist.Groups.group1.Players", Arrays.asList(new String[] { "ImAGroup" }));
		config.set("Blacklist.Groups.Hax.KickMessage", "&c&nYou are banned for hacking!");
		config.set("Blacklist.Groups.Hax.Players", Arrays.asList(new String[] { "Hacker1337", "Hax" }));
		Save();
	}

	private void UpgradeConfig() {
		fix("PreKick.Enabled", true);
		fix("PreKick.Easter-egg", true);
		fix("PreKick.ReloadWhenRead", false);
		fix("PreKick.Language", "en-US");

		fix("Whitelist.Enabled", true);
		fix("Whitelist.KickMessage", "Connection refuse: connect");
		fix("Whitelist.Players", Arrays.asList(new String[] { "WildN00b", "ThePf" }));

		fix("IP.Enabled", false);
		fix("IP.KickMessage", "&eIP doesn't match");
		if (!config.contains(getPath("IP.Players"))) {
			config.set("IP.Players.WildN00b", Arrays.asList(new String[] { "127.0.0.1", "10.10.10.10" }));
			config.set("IP.Players.ThePf", Arrays.asList(new String[] { "1.1.1.1" }));
		}

		if (!config.contains(getPath("Blacklist.Groups"))) {
			Object blacklist = config.get(getPath("Blacklist"));
			config.set(getPath("Blacklist"), null);
			config.set(getPath("Blacklist.Groups"), blacklist);

			if (config.getConfigurationSection(getPath("Blacklist.Groups")).getKeys(false).size() <= 1) {
				config.set("Blacklist.Groups.group1.KickMessage", "&2&lYou are banned for being in the group!");
				config.set("Blacklist.Groups.group1.Players", Arrays.asList(new String[] { "ImAGroup" }));
				config.set("Blacklist.Groups.Hax.KickMessage", "&c&nYou are banned for hacking!");
				config.set("Blacklist.Groups.Hax.Players", Arrays.asList(new String[] { "Hacker1337", "Hax" }));
			}
		}

		fix("Blacklist.Enabled", true);
		if (!config.contains(getPath("Blacklist.Players"))) {
			config.set("Blacklist.Players.Cheater1", "&a&lYou're not allowed on this server!");
			config.set("Blacklist.Players.Cheater2", "&a&nSorry, but you're not allowed to play on this server!");
		}

		Save();
	}

	private void fix(String path, Object data) {
		if (!config.contains(getPath(path)))
			config.set(path, data);
	}

	public void Close() {
		// Save();
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
		if (file.exists()) {
			try {
				config.load(file);
			} catch (Exception e) {
				if (file.exists())
					file.renameTo(new File(file.getPath() + ".bak"));
				PreKick.log.log(Level.SEVERE, "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] Couldn't load config, moving broken config to '" + file.getAbsoluteFile() + ".bak' and making a new default config file.");
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
		return config.getBoolean(getPath(path));
	}

	public String GetString(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getString(getPath(path));
	}

	public List<String> GetStringList(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getStringList(getPath(path));
	}

	public Set<String> GetKeys(String path) {
		if (reloadWhenRead)
			Reload();
		return config.getConfigurationSection(getPath(path)).getKeys(false);
	}

	public void Set(String path, Object value) {
		config.set(getPath(path), value);
		Save();
	}

	public String getPath(String path) {
		String split[] = path.split("\\.");
		if (split.length <= 0)
			return path;

		String newPath = "";
		boolean set = true;

		ConfigurationSection section = config.getRoot();

		for (int i = 0; i < split.length; i++) {
			if (!set) {
				newPath += "." + split[i];
				continue;
			}

			set = false;
			try {
				Set<String> entries = section.getKeys(false);

				for (String x : entries) {
					if (x.equalsIgnoreCase(split[i])) {
						newPath += "." + x;
						set = true;
						section = section.getConfigurationSection(x);
					}
				}
			} catch (Exception e) {
				set = false;
			}
			if (!set) {
				newPath += "." + split[i];
			}
		}

		return newPath.substring(1);
	}

	public Object Get(String path, Object def) {
		return config.get(getPath(path));
	}
}
