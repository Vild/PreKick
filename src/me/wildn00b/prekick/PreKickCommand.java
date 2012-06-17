package me.wildn00b.prekick;

import java.util.ArrayList;

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
			if (args.length == 1) {
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
				else if (args[0].equalsIgnoreCase("whitelist") && p(sender, "prekick.whitelist.switch")) {
					if (args[1].equalsIgnoreCase("on"))
						ToggleConfig(sender, "Whitelist.Enabled", true, "Whitelist");
					else if (args[1].equalsIgnoreCase("off") && p(sender, "prekick.whitelist.switch"))
						ToggleConfig(sender, "Whitelist.Enabled", false, "Whitelist");
					else
						ShowHelp(sender, commandLabel, 1);
				} else if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("on") && p(sender, "prekick.ip.switch"))
						ToggleConfig(sender, "IP.Enabled", true, "IP whitelist");
					else if (args[1].equalsIgnoreCase("off") && p(sender, "prekick.ip.switch"))
						ToggleConfig(sender, "IP.Enabled", false, "IP whitelist");
					else
						ShowHelp(sender, commandLabel, 1);
				} else if (args[0].equalsIgnoreCase("blacklist")) {
					if (args[1].equalsIgnoreCase("on") && p(sender, "prekick.blacklist.switch"))
						ToggleConfig(sender, "Blacklist.Enabled", true, "Blacklist");
					else if (args[1].equalsIgnoreCase("off") && p(sender, "prekick.blacklist.switch"))
						ToggleConfig(sender, "Blacklist.Enabled", false, "Blacklist");
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("whitelist")) {
					if (args[1].equalsIgnoreCase("add") && p(sender, "prekick.whitelist.add"))
						sender.sendMessage(prekick.whitelist.AddPlayerToWhitelist(args[2]));
					else if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.whitelist.remove"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromWhitelist(args[2]));
					else
						ShowHelp(sender, commandLabel, 1);
				} else if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.ip.remove"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromIPWhitelist(args[2], null));
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("add") && p(sender, "prekick.ip.add"))
						sender.sendMessage(prekick.whitelist.AddPlayerToIPWhitelist(args[2], args[3]));
					else if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.ip.remove"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromIPWhitelist(args[2], args[3]));
					else
						ShowHelp(sender, commandLabel, 1);
				} else if (args[0].equalsIgnoreCase("blacklist")) {
					if (args[1].equalsIgnoreCase("add") && p(sender, "prekick.blacklist.add"))
						sender.sendMessage(prekick.whitelist.AddPlayerToBlacklist(args[2], args[3]));
					else if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.blacklist.add"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromBlacklist(args[2], args[3]));
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else
				ShowHelp(sender, commandLabel, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void ShowStatus(CommandSender sender) {
		sender.sendMessage("PreKick V2.0 status page");
		sender.sendMessage("PreKick status: " + (prekick.config.GetBoolean("PreKick.Enabled") ? "true" : "false"));
		sender.sendMessage("Whitelist status: " + (prekick.config.GetBoolean("Whitelist.Enabled") ? "true" : "false"));
		sender.sendMessage("IP status: " + (prekick.config.GetBoolean("IP.Enabled") ? "true" : "false"));
		sender.sendMessage("Blacklist status: " + (prekick.config.GetBoolean("Blacklist.Enabled") ? "true" : "false"));
	}

	private void ToggleConfig(CommandSender sender, String path, boolean enabling, String name) {
		if (enabling) {
			if (!prekick.config.GetBoolean(path)) {
				prekick.config.Set(path, true);
				sender.sendMessage("[PreKick] " + name + " is now enabled.");
			} else
				sender.sendMessage("[PreKick] " + name + " is already enabled.");
		} else {
			if (prekick.config.GetBoolean(path)) {
				prekick.config.Set(name, false);
				sender.sendMessage("[PreKick] " + name + " is now disabled.");
			} else
				sender.sendMessage("[PreKick] " + name + " is already disabled.");
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

		cmds.add("/" + cmdLabel + " help <Number> - Shows <Number> page of help.");

		if (p(sender, "prekick.status"))
			cmds.add("/" + cmdLabel + " on - Show some status about PreKick.");

		if (p(sender, "prekick.switch"))
			cmds.add("/" + cmdLabel + " on - Turns PreKick on.");
		if (p(sender, "prekick.switch"))
			cmds.add("/" + cmdLabel + " off - Turns PreKick off.");

		if (p(sender, "prekick.whitelist.switch"))
			cmds.add("/" + cmdLabel + " whitelist on - Turns whitelist on.");
		if (p(sender, "prekick.whitelist.switch"))
			cmds.add("/" + cmdLabel + " whitelist off - Turns whitelist off.");
		if (p(sender, "prekick.whitelist.add"))
			cmds.add("/" + cmdLabel + " whitelist add <Player> - Adds player to the whitelist.");
		if (p(sender, "prekick.whitelist.remove"))
			cmds.add("/" + cmdLabel + " whitelist remove <Player> - Removes player from the whitelist.");

		if (p(sender, "prekick.ip.switch"))
			cmds.add("/" + cmdLabel + " ip on - Turns IP whitelist on.");
		if (p(sender, "prekick.ip.switch"))
			cmds.add("/" + cmdLabel + " ip off - Turns IP whitelist off.");
		if (p(sender, "prekick.ip.add"))
			cmds.add("/" + cmdLabel + " ip add <Player> <IP> - Adds player to the ip whitelist with the ip or adds ip the player.");
		if (p(sender, "prekick.ip.remove"))
			cmds.add("/" + cmdLabel + " ip remove <Player> - Removes player from the ip whitelist.");
		if (p(sender, "prekick.ip.remove"))
			cmds.add("/" + cmdLabel + " ip remove <Player> <IP> - Removes IP from the player from the ip whitelist.");

		if (p(sender, "prekick.blacklist.switch"))
			cmds.add("/" + cmdLabel + " blacklist on - Turns blacklist on.");
		if (p(sender, "prekick.blacklist.switch"))
			cmds.add("/" + cmdLabel + " blacklist off - Turns blacklist off.");
		if (p(sender, "prekick.blacklist.add"))
			cmds.add("/" + cmdLabel + " blacklist add <Group> <Player> - Adds player to the group in the blacklist.");
		if (p(sender, "prekick.blacklist.remove"))
			cmds.add("/" + cmdLabel + " blacklist remove <Group> <Player> - Removes player from the group in the blacklist.");

		int maxpage = 1 + cmds.size() / 6;
		if (page < 1)
			page = 1;
		else if (page > maxpage)
			page = maxpage;
		sender.sendMessage("PreKick V2.0 Page " + page + "/" + maxpage);
		try {
			for (int i = (page - 1) * 6; i < ((page - 1) * 6) + 6; i++) {
				sender.sendMessage(cmds.get(i));
			}

		} catch (Exception e) {
		}
	}

}
