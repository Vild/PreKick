package me.wildn00b.prekick;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;

public class Whitelist {

	private PreKick prekick;

	public Whitelist(PreKick prekick) {
		this.prekick = prekick;
	}

	public String processColors(String text) {
		return text.replaceAll("&", "\u00A7");
	}

	private boolean containsIgnoreCase(List<String> list, String text) {
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			if (it.next().equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
	
	private boolean containsIgnoreCase(Set<String> list, String text) {
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			if (it.next().equalsIgnoreCase(text))
				return true;
		}
		return false;
	}
	
	private String getCase(List<String> list, String text) {
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String tmp = it.next();
			if (tmp.equalsIgnoreCase(text))
				return tmp;
		}
		return "";
	}

	public int IsPlayerOnWhitelist(String player, String IP) { // Returns 0 = not on the whitelist, 1 = on the
																// whitelist, 2 = wrong ip, 3 = on the blacklist
		if (prekick.config.GetBoolean("Blacklist.Enabled")) {
			// Blacklist group start
			if (prekick.config.GetBoolean("Blacklist.Groups.Enabled")) {
				for (String group : prekick.config.GetKeys("Blacklist.Groups")) {
					if (group != "Enabled") {
						if (containsIgnoreCase(prekick.config.GetStringList("Blacklist.Groups." + group + ".Players"), player))
							return 4;
					}
				}
			}
			// Blacklist group stop

			// Blacklist player start
			if (containsIgnoreCase(prekick.config.GetKeys("Blacklist.Players"), player))
				return 3;
			// Blacklist player stop
		}

		// Whitelist start
		if (prekick.config.GetBoolean("Whitelist.Enabled"))
			if (!containsIgnoreCase(prekick.config.GetStringList("Whitelist.Players"), player))
				return 0;
		// Whitelist stop

		// IP whitelist start
		if (prekick.config.GetBoolean("IP.Enabled"))
			if (prekick.config.GetKeys("IP.Players").contains(player) && !IP.equals("NULL"))
				if (!containsIgnoreCase(prekick.config.GetStringList("IP.Players." + player), IP))
					return 2;
		// IP whitelist stop

		return 1;
	}

	public String GetKickMessage(String player, int reason) {
		if (reason == 0)
			return prekick.config.GetString("Whitelist.KickMessage");
		else if (reason == 2)
			return prekick.config.GetString("IP.KickMessage");
		else if (reason == 3)
			return prekick.config.GetString("Blacklist.Players." + player);
		else if (reason == 4) {
			for (String group : prekick.config.GetKeys("Blacklist.Groups")) {
				if (group != "Enabled") {
					if (containsIgnoreCase(prekick.config.GetStringList("Blacklist.Groups." + group + ".Players"), player))
						return prekick.config.GetString("Blacklist.Groups." + group + ".KickMessage");
				}
			}
			PreKick.log.log(Level.WARNING, "[PreKick] " + prekick.language.GetText("Whitelist.KickMessage.CantFindGroup").replaceAll("%PLAYER%", player));
			return prekick.language.GetText("Whitelist.KickMessage.Missbehaving");
		} else {
			PreKick.log.log(Level.WARNING, "[PreKick] " + prekick.language.GetText("Whitelist.KickMessage.UnknownReason").replaceAll("%REASON%", reason + ""));
			return prekick.language.GetText("Whitelist.KickMessage.Missbehaving");
		}
	}

	public String AddPlayerToWhitelist(String player) {
		if (containsIgnoreCase(prekick.config.GetStringList("Whitelist.Players"), player))
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Whitelist.AlreadyAdded").replaceAll("%PLAYER%", player);

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		list.add(player);
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Whitelist.Add").replaceAll("%PLAYER%", player);
	}

	public String RemovePlayerFromWhitelist(String player) {
		if (!containsIgnoreCase(prekick.config.GetStringList("Whitelist.Players"), player))
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Whitelist.NotOnList").replaceAll("%PLAYER%", player);

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		
		list.remove(getCase(list, player));
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Whitelist.Removed").replaceAll("%PLAYER%", player);
	}

	public String AddPlayerToIPWhitelist(String player, String IP) {
		if (containsIgnoreCase(prekick.config.GetKeys("IP.Players"), player)) {
			List<String> list = prekick.config.GetStringList("IP.Players." + player);
			list.add(IP);
			prekick.config.Set("IP.Players." + player, list);
			prekick.config.Save();
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.IP.AddIP").replaceAll("%PLAYER%", player);
		} else {
			prekick.config.Set("IP.Players." + player, Arrays.asList(new String[] { IP }));
			prekick.config.Save();
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.IP.Add").replaceAll("%PLAYER%", player);
		}

	}

	public String RemovePlayerFromIPWhitelist(String player, String IP) {
		if (containsIgnoreCase(prekick.config.GetKeys("IP.Players"), player)) {
			if (IP == "NULL") {
				List<String> list = prekick.config.GetStringList("IP.Players." + player);
				list.clear();
				prekick.config.Set("IP.Players." + player, list);
				prekick.config.getConfig().getConfigurationSection("IP.Players").set(player, null);
				prekick.config.Save();
				return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.IP.RemovedIP").replaceAll("%PLAYER%", player);
			} else {
				List<String> list = prekick.config.GetStringList("IP.Players." + player);
				list.remove(getCase(list, IP));
				if (list.size() == 0)
					prekick.config.getConfig().getConfigurationSection("IP.Players").set(player, null);
				else
					prekick.config.Set("IP.Players." + player, list);
				prekick.config.Save();
				return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.IP.Removed").replaceAll("%PLAYER%", player);
			}
		} else
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.IP.NotOnList").replaceAll("%PLAYER%", player);

	}

	public String AddPlayerToBlacklist(String player, String group) {
		if (containsIgnoreCase(prekick.config.GetStringList("Blacklist.Groups." + group + ".Players"), player))
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Blacklist.AlreadyAdded").replaceAll("%PLAYER%", player);

		List<String> list = prekick.config.GetStringList("Blacklist.Groups." + group + ".Players");
		list.add(player);
		prekick.config.Set("Blacklist.Groups." + group + ".Players", list);
		prekick.config.Save();

		return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Blacklist.Add").replaceAll("%PLAYER%", player);
	}

	public String RemovePlayerFromBlacklist(String player, String group) {
		if (!containsIgnoreCase(prekick.config.GetStringList("Blacklist.Groups.Players"), player))
			return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Blacklist.NotOnList").replaceAll("%PLAYER%", player);

		List<String> list = prekick.config.GetStringList("Blacklist.Groups." + group + ".Players");
		list.remove(getCase(list, player));
		prekick.config.Set("Blacklist.Groups." + group + ".Players", list);
		prekick.config.Save();

		return "[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("Whitelist.Blacklist.Removed").replaceAll("%PLAYER%", player);
	}

}
