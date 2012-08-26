package me.wildn00b.prekick;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistCommand implements CommandExecutor {

	private PreKick prekick;

	public WhitelistCommand(PreKick prekick) {
		this.prekick = prekick;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player)
			if (!p(((Player) sender).getPlayer(), "prekick.show"))
				return false;

		try {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on"))
					ToggleConfig(sender, "Whitelist.Enabled", true, "Whitelist");
				else if (args[0].equalsIgnoreCase("off") && p(sender, "prekick.whitelist.switch"))
					ToggleConfig(sender, "Whitelist.Enabled", false, "Whitelist");
				else if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("on") && p(sender, "prekick.ip.switch"))
						ToggleConfig(sender, "IP.Enabled", true, "IP whitelist");
					else if (args[1].equalsIgnoreCase("off") && p(sender, "prekick.ip.switch"))
						ToggleConfig(sender, "IP.Enabled", false, "IP whitelist");
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("help"))
					ShowHelp(sender, commandLabel, Integer.parseInt(args[1]));
				else if (args[0].equalsIgnoreCase("add") && p(sender, "prekick.whitelist.add"))
					sender.sendMessage(prekick.whitelist.AddPlayerToWhitelist(args[1]));
				else if (args[0].equalsIgnoreCase("remove") && p(sender, "prekick.whitelist.remove"))
					sender.sendMessage(prekick.whitelist.RemovePlayerFromWhitelist(args[1]));
				else if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.ip.remove"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromIPWhitelist(args[2], null));
					else
						ShowHelp(sender, commandLabel, 1);
				} else
					ShowHelp(sender, commandLabel, 1);
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("ip")) {
					if (args[1].equalsIgnoreCase("add") && p(sender, "prekick.ip.add"))
						sender.sendMessage(prekick.whitelist.AddPlayerToIPWhitelist(args[2], args[3]));
					else if (args[1].equalsIgnoreCase("remove") && p(sender, "prekick.ip.remove"))
						sender.sendMessage(prekick.whitelist.RemovePlayerFromIPWhitelist(args[2], args[3]));
					else
						ShowHelp(sender, commandLabel, 1);
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

		if (p(sender, "prekick.whitelist.switch"))
			cmds.add("/" + cmdLabel + " on - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "whitelist"));
		if (p(sender, "prekick.whitelist.switch"))
			cmds.add("/" + cmdLabel + " off - " + prekick.language.GetText("PreKickCommand.Help.Switch.Off").replaceAll("%CMD%", "whitelist"));
		if (p(sender, "prekick.whitelist.add"))
			cmds.add("/" + cmdLabel + " add " + prekick.language.GetText("PreKickCommand.Help.Whitelist.Add"));
		if (p(sender, "prekick.whitelist.remove"))
			cmds.add("/" + cmdLabel + " remove " + prekick.language.GetText("PreKickCommand.Help.Whitelist.Remove"));

		if (p(sender, "prekick.ip.switch"))
			cmds.add("/" + cmdLabel + " ip on - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "IP whitelist"));
		if (p(sender, "prekick.ip.switch"))
			cmds.add("/" + cmdLabel + " ip off - " + prekick.language.GetText("PreKickCommand.Help.Switch.On").replaceAll("%CMD%", "IP whitelist"));
		if (p(sender, "prekick.ip.add"))
			cmds.add("/" + cmdLabel + " ip add " + prekick.language.GetText("PreKickCommand.Help.IP.Add"));
		if (p(sender, "prekick.ip.remove"))
			cmds.add("/" + cmdLabel + " ip remove " + prekick.language.GetText("PreKickCommand.Help.IP.Remove"));
		if (p(sender, "prekick.ip.remove"))
			cmds.add("/" + cmdLabel + " ip remove " + prekick.language.GetText("PreKickCommand.Help.IP.RemoveIP"));

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
