package me.wildn00b.prekick;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Whitelist {

	private PreKick prekick;

	public Whitelist(PreKick prekick) {
		this.prekick = prekick;
	}

	public int IsPlayerOnWhitelist(String player, String IP) { // Returns 0 = not on the whitelist, 1 = on the
																// whitelist, 2 = wrong ip, 3 = on the blacklist
		// Blacklist start
		if (prekick.config.GetBoolean("Blacklist.Enabled")) {
			for (String group : prekick.config.GetKeys("Blacklist")) {
				if (group != "Enabled") {
					if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player))
						return 3;
				}
			}
		}
		// Blacklist stop

		// Whitelist start
		if (prekick.config.GetBoolean("Whitelist.Enabled"))
			if (!prekick.config.GetStringList("Whitelist.Players").contains(player))
				return 0;
		// Whitelist stop

		// IP whitelist start
		if (prekick.config.GetBoolean("IP.Enabled"))
			if (prekick.config.GetKeys("IP.Players").contains(player) && !IP.equals("NULL"))
				if (!prekick.config.GetStringList("IP.Players." + player).contains(IP))
					return 2;
		// IP whitelist stop

		return 1;
	}

	public String AddPlayerToWhitelist(String player) {
		if (prekick.config.GetStringList("Whitelist.Players").contains(player))
			return "[PreKick] Player already on the whitelist";

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		list.add(player);
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[PreKick] Added player from the whitelist successfully";
	}

	public String RemovePlayerFromWhitelist(String player) {
		if (!prekick.config.GetStringList("Whitelist.Players").contains(player))
			return "[PreKick] Player are not on the whitelist";

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		list.remove(player);
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[PreKick] Removed player from the whitelist successfully";
	}

	public String GetKickMessage(String player, int reason) {
		if (reason == 0)
			return prekick.config.GetString("Whitelist.KickMessage");
		else if (reason == 2)
			return prekick.config.GetString("IP.KickMessage");
		else if (reason == 3) {
			for (String group : prekick.config.GetKeys("Blacklist")) {
				if (group != "Enabled") {
					if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player))
						return prekick.config.GetString("Blacklist." + group + ".KickMessage");
				}
			}
			PreKick.log.log(Level.WARNING, "[PreKick] Can't find blacklist group for " + player);
			return "PreKick missbehaving.";
		} else {
			PreKick.log.log(Level.WARNING, "[PreKick] Unknown kick reason. reason: " + reason);
			return "PreKick missbehaving.";
		}
	}

	public String AddPlayerToIPWhitelist(String player, String IP) {
		if (prekick.config.GetKeys("IP.Players").contains(player)) {
			List<String> list = prekick.config.GetStringList("IP.Players." + player);
			list.add(IP);
			prekick.config.Set("IP.Players." + player, list);
			prekick.config.Save();
			return "[PreKick] Added IP to player in the IP whitelist successfully";
		} else {
			prekick.config.Set("IP.Players." + player, Arrays.asList(new String[] { IP }));
			prekick.config.Save();
			return "[PreKick] Added player to the IP whitelist successfully";
		}

	}

	public String RemovePlayerFromIPWhitelist(String player, String IP) {
		if (prekick.config.GetKeys("IP.Players").contains(player)) {
			if (IP == "NULL") {
				List<String> list = prekick.config.GetStringList("IP.Players." + player);
				list.clear();
				prekick.config.Set("IP.Players." + player, list);
				prekick.config.getConfig().getConfigurationSection("IP.Players").set(player, null);
				prekick.config.Save();
				return "[PreKick] Removed IP from the player in the IP whitelist successfully";
			} else {
				List<String> list = prekick.config.GetStringList("IP.Players." + player);
				list.remove(IP);
				if (list.size() == 0)
					prekick.config.getConfig().getConfigurationSection("IP.Players").set(player, null);
				else
					prekick.config.Set("IP.Players." + player, list);
				prekick.config.Save();
				return "[PreKick] Removed player successfully";
			}
		} else
			return "[PreKick] Can't find player";

	}

	public String AddPlayerToBlacklist(String player, String group) {
		if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player))
			return "[PreKick] Player already on the blacklist";

		List<String> list = prekick.config.GetStringList("Whitelist." + group + ".Players");
		list.add(player);
		prekick.config.Set("Whitelist." + group + ".Players", list);
		prekick.config.Save();

		return "[PreKick] Added player to the blacklist successfully";
	}

	public String RemovePlayerFromBlacklist(String player, String group) {
		if (!prekick.config.GetStringList("Whitelist.Players").contains(player))
			return "[PreKick] Player are not on the blacklist";

		List<String> list = prekick.config.GetStringList("Blacklist." + group + ".Players");
		list.remove(player);
		prekick.config.Set("Blacklist." + group + ".Players", list);
		prekick.config.Save();

		return "[PreKick] Removed player from the blacklist successfully";
	}

}
