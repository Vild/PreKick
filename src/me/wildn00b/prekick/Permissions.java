package me.wildn00b.prekick;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class Permissions {

	private Permission vault;
	private boolean isVaultEnabled;

	public Permissions(PreKick prekick) {
		if (prekick.getServer().getPluginManager().getPlugin("Vault") == null) {
			PreKick.log.log(Level.INFO, "[PreKick] Vault not found, fall back on Bukkit Permissions.");
			isVaultEnabled = false;
		} else {
			RegisteredServiceProvider<Permission> rsp = prekick.getServer().getServicesManager().getRegistration(Permission.class);
			vault = rsp.getProvider();
		}
	}

	public boolean HasPermissions(Player player, String permission) {
		if (isVaultEnabled)
			return vault.has(player, permission);
		else
			return player.hasPermission(permission);
	}

}
