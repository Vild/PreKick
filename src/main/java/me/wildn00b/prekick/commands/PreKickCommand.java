package me.wildn00b.prekick.commands;

import java.util.ArrayList;
import java.util.Arrays;

import me.wildn00b.prekick.PreKick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PreKickCommand implements CommandExecutor {

	private PreKick prekick;

	public PreKickCommand(PreKick prekick) {
		this.prekick = prekick;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player)
			if (!p(((Player) sender).getPlayer(), "prekick.show"))
				return false;

		try {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("whitelist"))
					return prekick.getCommand("whitelist").getExecutor().onCommand(sender, cmd, commandLabel + " whitelist", (args.length == 1) ? new String[0] : Arrays.copyOfRange(args, 1, args.length - 1));
				else if (args[0].equalsIgnoreCase("blacklist"))
					return prekick.getCommand("blacklist").getExecutor().onCommand(sender, cmd, commandLabel + " blacklist", (args.length == 1) ? new String[0] : Arrays.copyOfRange(args, 1, args.length - 1));
				else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("status") && p(sender, "prekick.status"))
						ShowStatus(sender);
					else if (args[0].equalsIgnoreCase("on") && p(sender, "prekick.switch"))
						ToggleConfig(sender, "PreKick.Enabled", true, "PreKick");
					else if (args[0].equalsIgnoreCase("off") && p(sender, "prekick.switch"))
						ToggleConfig(sender, "PreKick.Enabled", false, "PreKick");
					else
						ShowHelp(sender, commandLabel, 1);
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("help"))
						ShowHelp(sender, commandLabel, Integer.parseInt(args[1]));
				}
			} else
				ShowHelp(sender, commandLabel, 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			ShowHelp(sender, commandLabel, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void ShowStatus(CommandSender sender) {
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Status.PreKick").replaceAll("%VERSION%", prekick.version));
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Status.Status.PreKick") + " " + prekick.language.GetText("PreKickCommand.Status." + (prekick.config.GetBoolean("PreKick.Enabled") ? "On" : "Off")));
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Status.Status.Whitelist") + " " + prekick.language.GetText("PreKickCommand.Status." + (prekick.config.GetBoolean("Whitelist.Enabled") ? "On" : "Off")));
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Status.Status.IP") + " " + prekick.language.GetText("PreKickCommand.Status." + (prekick.config.GetBoolean("IP.Enabled") ? "On" : "Off")));
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Status.Status.Blacklist") + " " + prekick.language.GetText("PreKickCommand.Status." + (prekick.config.GetBoolean("Blacklist.Enabled") ? "On" : "Off")));
	}

	private void ToggleConfig(CommandSender sender, String path, boolean enabling, String name) {
		if (enabling) {
			if (!prekick.config.GetBoolean(path)) {
				prekick.config.Set(path, true);
				sender.sendMessage("[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("PreKickCommand.Toggle.Enable").replaceAll("%NAME%", name));
			} else
				sender.sendMessage("[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("PreKickCommand.Toggle.AlreadyEnable").replaceAll("%NAME%", name));
		} else {
			if (prekick.config.GetBoolean(path)) {
				prekick.config.Set(name, false);
				sender.sendMessage("[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("PreKickCommand.Toggle.Disable").replaceAll("%NAME%", name));
			} else
				sender.sendMessage("[" + ChatColor.RED + "PreKick" + ChatColor.RESET + "] " + prekick.language.GetText("PreKickCommand.Toggle.AlreadyDisable").replaceAll("%NAME%", name));
		}
	}

	private boolean p(CommandSender sender, String permission) {
		if (sender instanceof Player)
			return p((Player) sender, permission);
		else
			return true;
	}

	private boolean p(Player player, String permission) {
		return prekick.permissions.HasPermissions(player, permission);
	}

	public void ShowHelp(CommandSender sender, String cmdLabel, int page) {
		ArrayList<String> cmds = new ArrayList<String>();

		cmds.add("/" + cmdLabel + " help " + prekick.language.GetText("PreKickCommand.Help.Help").replaceAll("%CMDLABEL%", cmdLabel));

		if (p(sender, "prekick.status"))
			cmds.add("/" + cmdLabel + " status - " + prekick.language.GetText("PreKickCommand.Help.Status"));

		if (p(sender, "prekick.switch"))
			cmds.add("/" + cmdLabel + " on - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "PreKicks"));
		if (p(sender, "prekick.switch"))
			cmds.add("/" + cmdLabel + " off - " + prekick.language.GetText("PreKickCommand.Help.Switch.Off").replaceAll("%CMD%", "PreKicks"));

		if (p(sender, "prekick.whitelist.switch") || p(sender, "prekick.whitelist.add") || p(sender, "prekick.whitelist.remove") ||
				p(sender, "prekick.ip.switch") || p(sender, "prekick.ip.add") || p(sender, "prekick.ip.remove"))
			cmds.add("/" + cmdLabel + " whitelist - " + prekick.language.GetText("PreKickCommand.Help.Whitelist"));

		if (p(sender, "prekick.blacklist.switch") || p(sender, "prekick.blacklist.add") || p(sender, "prekick.blacklist.remove"))
			cmds.add("/" + cmdLabel + " blacklist - " + prekick.language.GetText("PreKickCommand.Help.Blacklist"));


		int maxpage = 1 + cmds.size() / 6;
		if (page < 1)
			page = 1;
		else if (page > maxpage)
			page = maxpage;
		sender.sendMessage(prekick.language.GetText("PreKickCommand.Help.Page").replaceAll("%VERSION%", prekick.version).replaceAll("%PAGE%", page + "").replaceAll("%MAXPAGE%", maxpage + ""));
		try {
			for (int i = (page - 1) * 6; i < ((page - 1) * 6) + 6; i++) {
				sender.sendMessage(cmds.get(i));
			}

		} catch (Exception e) {
		}
	}

}
