package me.wildn00b.prekick;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listener implements org.bukkit.event.Listener {

	private PreKick prekick;

	public Listener(PreKick prekick) {
		this.prekick = prekick;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnLogin(PlayerLoginEvent event) {
		if (!prekick.config.GetBoolean("PreKick.Enabled"))
			return;

		int reason = prekick.whitelist.IsPlayerOnWhitelist(event.getPlayer().getName(), event.getAddress().getHostAddress());

		if (reason != 1) {
			event.disallow(Result.KICK_WHITELIST, prekick.whitelist.GetKickMessage(event.getPlayer().getName(), reason));

			String message = "[PreKick] '" + event.getPlayer().getName() + "' from IP '" + event.getAddress().getHostAddress() + "' was kicked ";
			if (reason == 0)
				message += "for not being on the whitelist.";
			else if (reason == 2)
				message += "for the IP not being on the IP whitelist.";
			else if (reason == 3)
				message += "for being on the blacklist.";
			else {
				PreKick.log.log(Level.WARNING, "[PreKick] Unknown kick reason. reason: " + reason);
				message += " for PreKick missbehaving!";
			}
			for (Player player : prekick.getServer().getOnlinePlayers()) {
				if (prekick.permissions.HasPermissions(player, "prekick.seekick"))
					player.sendMessage(message);
			}

			PreKick.log.log(Level.INFO, message);
		}
	}
}
