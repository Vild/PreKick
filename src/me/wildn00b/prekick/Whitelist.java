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
		if (!prekick.config.GetKeys("Whitelist.IP.Players").contains(player.getName()) && !IP.equals("NULL"))
			if (prekick.config.GetString("Whitelist.IP.Players." + player.getName()).equals(IP))
				return 2;

		if (prekick.config.GetStringList("Whitelist.Players").contains(player.getName()))
			return 1;
		else
			return 0;
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
		for (String group : prekick.config.GetKeys("Blacklist")) {
			if (prekick.config.GetStringList("Blacklist." + group + ".Players").contains(player.getName())) {
				if (reason == 0 || reason == 2)
					return prekick.config.GetString("Blacklist." + group + ".KickMessage");
				else
					break;
			}
		}

		if (reason == 0)
			return prekick.config.GetString("Whitelist.KickMessage");
		else if (reason == 2)
			return prekick.config.GetString("Whitelist.IP.KickMessage");
		else {
			PreKick.log.log(Level.WARNING, "[PreKick] Unknown kick reason. reason: " + reason);
			return "PreKick missbehaving.";
		}
	}

}
