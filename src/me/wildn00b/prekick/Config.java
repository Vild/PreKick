package me.wildn00b.prekick;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private YamlConfiguration config;
	private File file;

	public Config(File file) {
		this.file = file;
		config = new YamlConfiguration();
		if (!file.exists()) {
			try {
				config.load(file);
			} catch (Exception e) {
				e.printStackTrace();
				PreKick.log.log(Level.SEVERE, "[PreKick] Couldn't load config, reseting to standard config");
				CreateStandardConfig();
			}
		} else {
			CreateStandardConfig();
		}
	}

	private void CreateStandardConfig() {
		//TODO: add standard config
	}

	public void Close() {
		Save();
		config = null;
	}

	public void Save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
