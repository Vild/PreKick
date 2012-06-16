package me.wildn00b.prekick;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;

public class Whitelist {

	private PreKick prekick;

	public Whitelist(PreKick prekick) {
		this.prekick = prekick;
	}

	public int IsPlayerOnWhitelist(Player player, String IP) { // Returns 0 = not on the whitelist, 1 = on the whitelist, 2 = wrong ip
		if (!prekick.config.GetBoolean("PreKick.Enabled"))
			return -1;

		//Blacklist start
		if (prekick.config.GetBoolean("Blacklist.Enabled")) {
			for (String group : prekick.config.GetKeys("Blacklist")) {
				if (group != "Enabled") {
					if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player.getName()))
						return 3;
				}
			}
		}
		//Blacklist stop
		
		//Whitelist start
		if (prekick.config.GetBoolean("Whitelist.Enabled"))
			if (!prekick.config.GetStringList("Whitelist.Players").contains(player.getName()))
				return 0;
		//Whitelist stop
		
		//IP whitelist start
		if (prekick.config.GetBoolean("Whitelist.IP.Enabled"))
			if (prekick.config.GetKeys("Whitelist.IP.Players").contains(player.getName()) && !IP.equals("NULL"))
				if (!prekick.config.GetStringList("Whitelist.IP.Players." + player.getName()).contains(IP))
					return 2;
		//IP whitelist stop

		return 1;
	}

	public String AddPlayerToWhitelist(Player player) {
		if (IsPlayerOnWhitelist(player, "NULL") == 1)
			return "[PreKick] Player already on the whitelist";

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		list.add(player.getName());
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[PreKick] Added user succsessfully";
	}

	public String RemovePlayerToWhitelist(Player player) {
		if (IsPlayerOnWhitelist(player, "NULL") == 0)
			return "[PreKick] Player are not on the whitelist";

		List<String> list = prekick.config.GetStringList("Whitelist.Players");
		list.remove(player.getName());
		prekick.config.Set("Whitelist.Players", list);
		prekick.config.Save();

		return "[PreKick] Removed user successfully";
	}

	public String GetKickMessage(Player player, int reason) {
		if (reason == 0)
			return prekick.config.GetString("Whitelist.KickMessage");
		else if (reason == 2)
			return prekick.config.GetString("Whitelist.IP.KickMessage");
		else if (reason == 3) {
			for (String group : prekick.config.GetKeys("Blacklist")) {
				if (group != "Enabled") {
					if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player.getName()))
							return prekick.config.GetString("Blacklist." + group + ".KickMessage");
				}
			}
			PreKick.log.log(Level.WARNING, "[PreKick] Can't find blacklist group for " + player.getName());
			return "PreKick missbehaving.";
		} else {
			PreKick.log.log(Level.WARNING, "[PreKick] Unknown kick reason. reason: " + reason);
			return "PreKick missbehaving.";
		}
	}

}
