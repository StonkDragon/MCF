package tk.stonkdragon.mcf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class JSONConv {
    public static JSONObject getData(File f) {

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
