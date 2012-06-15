package me.wildn00b.prekick;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class Listener implements org.bukkit.event.Listener {

	private PreKick prekick;

	public Listener(PreKick prekick) {
		this.prekick = prekick;
	}

	@EventHandler
	public void OnLogin(PlayerLoginEvent event) {
		int reason = prekick.whitelist.IsPlayerOnWhitelist(event.getPlayer(), event.getAddress().getHostAddress());
		if (reason != 1) {
			event.setKickMessage(prekick.whitelist.GetKickMessage(event.getPlayer(), reason));
			event.setResult(Result.KICK_WHITELIST);
		}
	}

}
