package tk.stonkdragon.mcf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class Ignores {
    public static boolean fileContainsString(File f, String s) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String l;
        while ((l = br.readLine()) != null) {
            if (l.toLowerCase(Locale.ENGLISH).contains(s.toLowerCase(Locale.ENGLISH))) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }
}
