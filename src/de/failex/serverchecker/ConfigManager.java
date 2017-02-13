package de.failex.serverchecker;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/*
* Yes i was used to YAML Configs from bukkit, so much so that i actually
* used the same Classes...
*/
public class ConfigManager {
	public File config = new File("tm2config.yml");
	public FileConfiguration con = YamlConfiguration.loadConfiguration(config);

	public void saveToConfig(String title, String data) throws IOException {
		con.set(title, data);
		con.save(config);
	}
}
