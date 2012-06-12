package me.wildn00b.prekick;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class Permissions {

	private Permission vault;
	private boolean isVaultEnabled;

	public Permissions(Plugin PreKick, boolean isVaultEnabled) {
		this.isVaultEnabled = isVaultEnabled;
		if (isVaultEnabled) {
			RegisteredServiceProvider<Permission> rsp = PreKick.getServer().getServicesManager().getRegistration(Permission.class);
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
