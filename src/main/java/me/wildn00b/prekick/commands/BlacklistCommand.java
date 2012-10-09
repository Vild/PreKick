package me.wildn00b.prekick.commands;

import java.util.ArrayList;
import java.util.Arrays;

import me.wildn00b.prekick.PreKick;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlacklistCommand implements CommandExecutor {

	private PreKick prekick;

	public BlacklistCommand(PreKick prekick) {
		this.prekick = prekick;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player)
			if (!p(((Player) sender).getPlayer(), "prekick.show"))
				return false;

		try {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on") && p(sender, "prekick.blacklist.switch"))
					ToggleConfig(sender, "Blacklist.Enabled", true, "Blacklist");
				else if (args[0].equalsIgnoreCase("off") && p(sender, "prekick.blacklist.switch"))
					ToggleConfig(sender, "Blacklist.Enabled", false, "Blacklist");
				else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("help"))
					ShowHelp(sender, commandLabel, Integer.parseInt(args[1]));
				else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("add") && p(sender, "prekick.blacklist.add"))
					sender.sendMessage(prekick.whitelist.AddPlayerToBlacklist(args[1], args[2]));
				else if (args[0].equalsIgnoreCase("remove") && p(sender, "prekick.blacklist.add"))
					sender.sendMessage(prekick.whitelist.RemovePlayerFromBlacklist(args[1], args[2]));
				else if (args[0].equalsIgnoreCase("group")) {
					if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.blacklist.group.remove"))
						sender.sendMessage(prekick.whitelist.RemoveGroupFromBlacklist(args[2]));
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length >= 4) {
				if (args[0].equalsIgnoreCase("group")) {
					if (args[1].equalsIgnoreCase("add") && p(sender, "prekick.blacklist.group.add")) {
						String arr[] = Arrays.copyOfRange(args, 2, args.length - 1);
						sender.sendMessage(prekick.whitelist.AddGroupToBlacklist(args[2], StringUtils.join(arr, " ")));
					}else if (args[1].equalsIgnoreCase("set") && p(sender, "prekick.blacklist.group.set")) {
						String arr[] = Arrays.copyOfRange(args, 2, args.length - 1);
						sender.sendMessage(prekick.whitelist.SetMessageForGroupOnBlacklist(args[2], StringUtils.join(arr, " ")));
					}
				} else
					ShowHelp(sender, commandLabel, 1);
			} else
				ShowHelp(sender, commandLabel, 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			ShowHelp(sender, commandLabel, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
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

		if (p(sender, "prekick.blacklist.switch"))
			cmds.add("/" + cmdLabel + " on - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "blacklist"));
		if (p(sender, "prekick.blacklist.switch"))
			cmds.add("/" + cmdLabel + " off - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "blacklist"));
		if (p(sender, "prekick.blacklist.add"))
			cmds.add("/" + cmdLabel + " add " + prekick.language.GetText("BlacklistCommand.Help.Add"));
		if (p(sender, "prekick.blacklist.remove"))
			cmds.add("/" + cmdLabel + " remove " + prekick.language.GetText("BlacklistCommand.Help.Remove"));
		if (p(sender, "prekick.blacklist.group.add"))
			cmds.add("/" + cmdLabel + " group add " + prekick.language.GetText("BlacklistCommand.Help.Grope.Add"));
		if (p(sender, "prekick.blacklist.group.remove"))
			cmds.add("/" + cmdLabel + " group remove " + prekick.language.GetText("BlacklistCommand.Help.Grope.Remove"));
		if (p(sender, "prekick.blacklist.group.set"))
			cmds.add("/" + cmdLabel + " group set " + prekick.language.GetText("BlacklistCommand.Help.Grope.Set"));
		
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
