package tk.stonkdragon.mcfapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    public static File CONFIG;

    /**
     * {@code generateConfig} generates a new Config file for your Extention
     */
    public static void generateConfig() {
        String jarPath = "";
        try {
            jarPath = Config.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        CONFIG = new File(jarPath.substring(0, jarPath.lastIndexOf(".")) + ".json");
    }

    /**
     * {@code setConfig} writes {@code JSONObject j} to your Extention's Config file. 
     * {@code j} must be of Type {@code org.json.JSONObject}
     */
    public static void setConfig(JSONObject j) {
        try {
            new BufferedWriter(new FileWriter(CONFIG)).append(j.toString(4)).close();;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@code getConfig} returns a {@code JSONObject} that contains the Contents of your Extention's Config file
     * @return JSONObject
     */
    public static JSONObject getConfig() {
        return getData(CONFIG);
    }

    private static JSONObject getData(File f) {

        if (!f.exists()) {
            return new JSONObject();
        }
        
        // create file reader for given file f
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // create strings for usage
        String l;
        String d = "";
        
        // read the file
        try {
            while ((l = br.readLine()) != null) {
            
                // and append the current line to d after trimming
                d += l.trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // close reader
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return new jsonobject with data d
        return new JSONObject(d);
    }
}
