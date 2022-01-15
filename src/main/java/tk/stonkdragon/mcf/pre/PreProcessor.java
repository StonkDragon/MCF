package tk.stonkdragon.mcf.pre;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreProcessor {

    private List<String> globalDefines = new ArrayList<String>();
    private boolean shouldCompile;

    public void startRead(File rootFolder) {
        check(rootFolder);
    }

    private void check(File folder) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                check(file);
            } else {
                if (file.getName().endsWith(".mcf")) {
                    try {
                        readFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readFile(File file) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<String> fileDefines = new ArrayList<String>();
        if (new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcf")) + ".mcfunction").exists()) {
            new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcf")) + ".mcfunction").delete();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcf")) + ".mcfunction")));

        this.shouldCompile = true;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("#!")) {
                String def = "";
                line = line.substring(2);
                String[] keys = line.split(" ");

                if (keys.length > 1) {
                    def = keys[1];
                }

                System.err.println(keys[0]);

                switch (keys[0]) {
                    case "GLOBALDEF":
                        this.globalDefines.add(def);
                        continue;
                    case "DEFINE":
                        fileDefines.add(def);
                        continue;
                    case "IFDEF":
                        this.shouldCompile = (globalDefines.contains(def) || fileDefines.contains(def));
                        continue;
                    case "IFNDEF":
                        this.shouldCompile = !(globalDefines.contains(def) || fileDefines.contains(def));
                        continue;
                    case "ENDIF":
                        this.shouldCompile = true;
                        continue;
                    case "ELSE":
                        this.shouldCompile = !this.shouldCompile;
                        continue;
                    case "UNDEF":
                        this.globalDefines.remove(def);
                        fileDefines.remove(def);
                        continue;
                }

            } else {
                if (shouldCompile) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }
        bw.close();
        br.close();
    }
}
