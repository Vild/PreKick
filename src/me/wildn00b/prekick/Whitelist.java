package me.wildn00b.prekick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;

public class Whitelist {

	private PreKick prekick;
	private boolean whitelistInConfig;
	private File whitelist;
	private List<String> players;

	public Whitelist(PreKick prekick) {
		this.prekick = prekick;
		whitelistInConfig = prekick.config.GetBoolean("PreKick.WhitelistInConfig");
		whitelist = new File("whitelist.txt");
		ReloadWhitelist();
	}

	public void ReloadWhitelist() {
		if (!whitelistInConfig && !whitelist.exists()) {
			try {
				whitelist.createNewFile();
				players = new ArrayList<String>();
				BufferedReader r = new BufferedReader(new FileReader(whitelist));
				String line;
				while ((line = r.readLine()) != null)
					if (!line.equals("") && !line.equals("#"))
						players.add(line.toLowerCase());
			} catch (IOException e) {
				PreKick.log.log(Level.SEVERE, "[PreKick] Couldn't make 'whitelist.txt', reverting to 'config.yml' for whitelist.");
				whitelistInConfig = true;
			}
		}
	}

	public boolean IsPlayerOnWhitelist(Player player) {
		return IsPlayerOnWhitelist(player, false);
	}
	
	public boolean IsPlayerOnWhitelist(Player player, boolean noReload) {
		if (whitelistInConfig)
			return prekick.config.GetStringList("Whitelist.Players", true).contains(player.getName().toLowerCase());
		else {
			if (prekick.config.reloadWhenRead && !noReload)
				ReloadWhitelist();
			return players.contains(player.getName().toLowerCase());
		}
	}
	
	public String AddPlayerToWhitelist(Player player) {
		if (IsPlayerOnWhitelist(player, true))
			return "[PreKick] Player already on the whitelist";
		
		if (whitelistInConfig) {
			List<String> list = prekick.config.GetStringList("Whitelist.Players");
			list.add(player.getName());
			prekick.config.Set("Whitelist.Players", list);
			prekick.config.Save();
		} else {
			try {
				players.add(player.getName());
				BufferedWriter w = new BufferedWriter(new FileWriter(whitelist, true));
				w.newLine();
				w.write(player.getName());
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "[PreKick] Error while adding user, check console for the error message";
			}
		}
		return "[PreKick] Added user succsessfully";
	}
	
	public String RemovePlayerToWhitelist(Player player) {
		if (!IsPlayerOnWhitelist(player, true))
			return "[PreKick] Player are not on the whitelist";
		
		if (whitelistInConfig) {
			List<String> list = prekick.config.GetStringList("Whitelist.Players");
			list.remove(player.getName());
			prekick.config.Set("Whitelist.Players", list);
			prekick.config.Save();
		} else {
			try {
				players.remove(player.getName());
				BufferedReader r = new BufferedReader(new FileReader(whitelist));
				String all = "";
				String line;
				while ((line = r.readLine()) != null)
					if (!line.equalsIgnoreCase(player.getName()))
						all += line;
				r.close();
				BufferedWriter w = new BufferedWriter(new FileWriter(whitelist));
				w.write(all);
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
				return "[PreKick] Error while adding user, check console for the error message";
			}
		}
		return "[PreKick] Removed user succsessfully";
	}
	
}
