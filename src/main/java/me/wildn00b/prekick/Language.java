package me.wildn00b.prekick;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {

	public YamlConfiguration file;

	public Language(PreKick prekick) {
		file = new YamlConfiguration();
		try {
			File f = new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + prekick.config.GetString("PreKick.Language") + ".yml");
			if (f.exists())
					file.load(f);
			else {
				f = new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + "en-US.yml");
				if (f.exists())
					file.load(new File(prekick.getDataFolder().getAbsolutePath() + File.separator + "lang" + File.separator + "en-US.yml"));
				else
					createFile(f);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Close() {
		file = null;
	}
	
	private void createFile(File f) {
		//TODO: Add all strings.
		file.set("test", "test");
		
		try {
			file.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public String GetText(String path) {
		return file.getString(path, path);
	}
	
}
