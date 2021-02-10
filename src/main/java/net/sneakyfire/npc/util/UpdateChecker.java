package net.sneakyfire.npc.util;

import net.sneakyfire.npc.SimpleCloudNPC;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;

public class UpdateChecker {

    private String url = "https://imposdev.eu/repo/org/spigotmc/simplecloudnpc/1.0.0/";
    private String id = "version";

    private boolean isAvailable;

    public UpdateChecker() {

    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void check() {
        isAvailable = checkUpdate();
    }

    private boolean checkUpdate() {
        Log.info("Check for updates...");
        try {
            String localVersion = SimpleCloudNPC.getInstance().getVersion();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            String remoteVersion;
            if(raw.contains("-")) {
                remoteVersion = raw.split("-")[0].trim();
            } else {
                remoteVersion = raw;
            }

            if(!localVersion.equalsIgnoreCase(remoteVersion))
                return true;

        } catch (IOException e) {
            return false;
        }
        return false;
    }

}
