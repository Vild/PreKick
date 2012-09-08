package me.wildn00b.prekick;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	public YamlConfiguration file;

	public Language(PreKick prekick) {
		file = new YamlConfiguration();
		try {
			File f = new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + prekick.config.GetString("PreKick.Language") + ".yml");
			if (f.exists())
				file.load(f);
			else {
				PreKick.log.log(Level.WARNING, "[PreKick] Couldn't find language file, reverting to en-US");
				f = new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + "en-US.yml");
				if (f.exists())
					file.load(new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + "en-US.yml"));
				else
					createFile(f);
			}
			Upgrade(f);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void Close() {
		file = null;
	}

	private void createFile(File f) throws IOException {
		file.set("Listener.Message.Base", "%PLAYER% from IP %IP% was kicked");
		file.set("Listener.Message.WhiteList", "for not being on the whitelist.");
		file.set("Listener.Message.IP", "for the IP not being on the IP whitelist.");
		file.set("Listener.Message.BlackList", "for being on the blacklist.");
		file.set("Listener.Message.Missbehaving", "for PreKick missbehaving!");
		file.set("Listener.Message.Console.Missbehaving", "Unknown kick reason. reason: %REASON%");
		file.set("Listener.EasterEgg", "&6WildN00b, the developer for PreKick is connecting. Say Hi!");
		file.set("Listener.Console.EasterEgg", "You can disable this message, if you want ;(, just turn off Easter-egg in the config");

		file.set("Permissions.NotFound", "Vault not found, fall back on Bukkit Permissions.");

		file.set("PreKick.Initialized", "Initialized successfully.");
		file.set("PreKick.Disabling", "Disabling successfully.");

		file.set("PreKickCommand.Status.PreKick", "PreKick V%VERSION% status page");
		file.set("PreKickCommand.Status.Status.PreKick", "PreKick status:");
		file.set("PreKickCommand.Status.Status.Whitelist", "Whitelist status:");
		file.set("PreKickCommand.Status.Status.IP", "IP status:");
		file.set("PreKickCommand.Status.Status.Blacklist", "Blacklist status:");
		file.set("PreKickCommand.Status.On", "&aon");
		file.set("PreKickCommand.Status.Off", "&coff");
		file.set("PreKickCommand.Toggle.Enable", "%NAME% is now &aenabled.");
		file.set("PreKickCommand.Toggle.AlreadyEnable", "%NAME% is already enabled.");
		file.set("PreKickCommand.Toggle.Disable", "%NAME% is now &cdisabled.");
		file.set("PreKickCommand.Toggle.AlreadyEnable", "%NAME% is already disabled.");
		file.set("PreKickCommand.Help.Help", "<Number> - Shows <Number> page of help.");
		file.set("PreKickCommand.Help.Status", "Show some status about PreKick.");
		file.set("PreKickCommand.Help.Page", "PreKick V%VERSION% Page %PAGE%/%MAXPAGE%");
		file.set("PreKickCommand.Help.Whitelist", "Shows help for the whitelist.");
		file.set("PreKickCommand.Help.Blacklist", "Shows help for the blacklist.");

		file.set("WhitelistCommand.Help.Add", "<Player> - Adds player to the whitelist.");
		file.set("WhitelistCommand.Help.Remove", "<Player> - Removes player from the whitelist.");
		file.set("WhitelistCommand.Help.IP.Add", "<Player> <IP> - Adds player to the ip whitelist with the ip or adds ip the player.");
		file.set("WhitelistCommand.Help.IP.Remove", "<Player> - Removes player from the ip whitelist.");
		file.set("WhitelistCommand.Help.IP.RemoveIP", "<Player> <IP> - Removes IP from the player from the ip whitelist.");

		file.set("BlacklistCommand.Help.Add", "<Group> <Player> - Adds player to the group in the blacklist.");
		file.set("BlacklistCommand.Help.Remove", "<Group> <Player> - Removes player from the group in the blacklist.");

		file.set("Whitelist.KickMessage.CantFindGroup", "Can't find the blacklist group for %PLAYER%");
		file.set("Whitelist.KickMessage.UnknownReason", "Unknown kick reason. reason: %REASON%");
		file.set("Whitelist.KickMessage.Missbehaving", "PreKick missbehaving.");
		file.set("Whitelist.Whitelist.AlreadyAdded", "%PLAYER% already on the whitelist.");
		file.set("Whitelist.Whitelist.Add", "Added %PLAYER% to the whitelist successfully.");
		file.set("Whitelist.Whitelist.NotOnList", "%PLAYER% is not on the whitelist.");
		file.set("Whitelist.Whitelist.Removed", "Removed %PLAYER% from the whitelist successfully.");
		file.set("Whitelist.IP.AddIP", "Added IP to %PLAYER% in the IP whitelist successfully.");
		file.set("Whitelist.IP.Add", "Added %PLAYER% in the IP whitelist successfully.");
		file.set("Whitelist.IP.RemovedIP", "Removed IP from the %PLAYER% from the IP whitelist successfully.");
		file.set("Whitelist.IP.Removed", "Removed %PLAYER% from the IP whitelist successfully.");
		file.set("Whitelist.IP.NotOnList", "Can't find %PLAYER% on the IP whitelist.");
		file.set("Whitelist.Blacklist.AlreadyAdded", "%PLAYER% already on the blacklist.");
		file.set("Whitelist.Blacklist.Add", "Added %PLAYER% to the blacklist successfully.");
		file.set("Whitelist.Blacklist.NotOnList", "%PLAYER% is not on the blacklist.");
		file.set("Whitelist.Blacklist.Removed", "Removed %PLAYER% from the blacklist successfully.");

		file.save(f);
	}

	private void Upgrade(File path) throws IOException {
		fix("Listener.Message.Base", "%PLAYER% from IP %IP% was kicked");
		fix("Listener.Message.WhiteList", "for not being on the whitelist.");
		fix("Listener.Message.IP", "for the IP not being on the IP whitelist.");
		fix("Listener.Message.BlackList", "for being on the blacklist.");
		fix("Listener.Message.Missbehaving", "for PreKick missbehaving!");
		fix("Listener.Message.Console.Missbehaving", "Unknown kick reason. reason: %REASON%");
		fix("Listener.EasterEgg", "&6WildN00b, the developer for PreKick is connecting. Say Hi!");
		fix("Listener.Console.EasterEgg", "You can disable this message, if you want ;(, just turn off Easter-egg in the config");

		fix("Permissions.NotFound", "Vault not found, fall back on Bukkit Permissions.");

		fix("PreKick.Initialized", "Initialized successfully.");
		fix("PreKick.Disabling", "Disabling successfully.");

		fix("PreKickCommand.Status.PreKick", "PreKick V%VERSION% status page");
		fix("PreKickCommand.Status.Status.PreKick", "PreKick status:");
		fix("PreKickCommand.Status.Status.Whitelist", "Whitelist status:");
		fix("PreKickCommand.Status.Status.IP", "IP status:");
		fix("PreKickCommand.Status.Status.Blacklist", "Blacklist status:");
		fix("PreKickCommand.Status.On", "&aon");
		fix("PreKickCommand.Status.Off", "&coff");
		fix("PreKickCommand.Toggle.Enable", "%NAME% is now &aenabled.");
		fix("PreKickCommand.Toggle.AlreadyEnable", "%NAME% is already enabled.");
		fix("PreKickCommand.Toggle.Disable", "%NAME% is now &cdisabled.");
		fix("PreKickCommand.Toggle.AlreadyEnable", "%NAME% is already disabled.");
		fix("PreKickCommand.Help.Help", "<Number> - Shows <Number> page of help.");
		fix("PreKickCommand.Help.Status", "Show some status about PreKick.");
		fix("PreKickCommand.Help.Switch.On", "Turns %CMD% on.");
		fix("PreKickCommand.Help.Switch.Off", "Turns %CMD% off.");
		fix("PreKickCommand.Help.Page", "PreKick V%VERSION% Page %PAGE%/%MAXPAGE%");

		if (file.contains(getPath("PreKickCommand.Help.Whitelist"))) {
			Object blacklist = file.get(getPath("PreKickCommand.Help.Whitelist"));
			file.set(getPath("PreKickCommand.Help.Whitelist.Add"), null);
			file.set(getPath("PreKickCommand.Help.Whitelist.Remove"), null);
			file.set(getPath("PreKickCommand.Help.Whitelist"), null);
			file.set(getPath("WhitelistCommand.Help"), blacklist);
		}
		if (file.contains(getPath("PreKickCommand.Help.IP"))) {
			Object blacklist = file.get(getPath("PreKickCommand.Help.IP"));
			file.set(getPath("PreKickCommand.Help.IP.Add"), null);
			file.set(getPath("PreKickCommand.Help.IP.Remove"), null);
			file.set(getPath("PreKickCommand.Help.IP.RemoveIP"), null);
			file.set(getPath("PreKickCommand.Help.IP"), null);
			file.set(getPath("WhitelistCommand.Help.IP"), blacklist);
		}
		fix("WhitelistCommand.Help.Add", "<Player> - Adds player to the whitelist.");
		fix("WhitelistCommand.Help.Remove", "<Player> - Removes player from the whitelist.");
		fix("WhitelistCommand.Help.IP.Add", "<Player> <IP> - Adds player to the ip whitelist with the ip or adds ip the player.");
		fix("WhitelistCommand.Help.IP.Remove", "<Player> - Removes player from the ip whitelist.");
		fix("WhitelistCommand.Help.IP.RemoveIP", "<Player> <IP> - Removes IP from the player from the ip whitelist.");

		if (file.contains(getPath("PreKickCommand.Help.Blacklist"))) {
			Object blacklist = file.get(getPath("PreKickCommand.Help.Blacklist"));
			file.set(getPath("PreKickCommand.Help.Blacklist.Add"), null);
			file.set(getPath("PreKickCommand.Help.Blacklist.Remove"), null);
			file.set(getPath("PreKickCommand.Help.Blacklist"), null);
			file.set(getPath("BlacklistCommand.Help"), blacklist);
		}
		fix("BlacklistCommand.Help.Add", "<Group> <Player> - Adds player to the group in the blacklist.");
		fix("BlacklistCommand.Help.Remove", "<Group> <Player> - Removes player from the group in the blacklist.");

		fix("PreKickCommand.Help.Whitelist", "Shows help for the whitelist.");
		fix("PreKickCommand.Help.Blacklist", "Shows help for the blacklist.");
		
		fix("Whitelist.KickMessage.CantFindGroup", "Can't find the blacklist group for %PLAYER%");
		fix("Whitelist.KickMessage.UnknownReason", "Unknown kick reason. reason: %REASON%");
		fix("Whitelist.KickMessage.Missbehaving", "PreKick missbehaving.");
		fix("Whitelist.Whitelist.AlreadyAdded", "%PLAYER% already on the whitelist.");
		fix("Whitelist.Whitelist.Add", "Added %PLAYER% to the whitelist successfully.");
		fix("Whitelist.Whitelist.NotOnList", "%PLAYER% is not on the whitelist.");
		fix("Whitelist.Whitelist.Removed", "Removed %PLAYER% from the whitelist successfully.");
		fix("Whitelist.IP.AddIP", "Added IP to %PLAYER% in the IP whitelist successfully.");
		fix("Whitelist.IP.Add", "Added %PLAYER% in the IP whitelist successfully.");
		fix("Whitelist.IP.RemovedIP", "Removed IP from the %PLAYER% from the IP whitelist successfully.");
		fix("Whitelist.IP.Removed", "Removed %PLAYER% from the IP whitelist successfully.");
		fix("Whitelist.IP.NotOnList", "Can't find %PLAYER% on the IP whitelist.");
		fix("Whitelist.Blacklist.AlreadyAdded", "%PLAYER% already on the blacklist.");
		fix("Whitelist.Blacklist.Add", "Added %PLAYER% to the blacklist successfully.");
		fix("Whitelist.Blacklist.NotOnList", "%PLAYER% is not on the blacklist.");
		fix("Whitelist.Blacklist.Removed", "Removed %PLAYER% from the blacklist successfully.");

		file.save(path);
	}

	private void fix(String path, Object data) {
		if (!file.contains(getPath(path)))
			file.set(path, data);
	}

	public String processText(String text) {
		return ChatColor.translateAlternateColorCodes('&', text).replace("\\n", "\n");
	}

	public String GetText(String path) {
		return processText(file.getString(getPath(path), path));
	}

	public String getPath(String path) {
		String split[] = path.split("\\.");
		if (split.length <= 0)
			return path;

		String newPath = "";
		boolean set = true;

		ConfigurationSection section = file.getRoot();

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

}
