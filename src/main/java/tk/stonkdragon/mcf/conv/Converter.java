package tk.stonkdragon.mcf.conv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Converter {
    public void convertAll(File rootFolder) {
        try {
            check(rootFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convert(File file) {
        convert(file);
    }

    private void check(File folder) throws FileNotFoundException, IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                check(file);
            } else {
                if (file.getName().endsWith(".mcfunction")) {
                    convertFile(file);
                }
            }
        }
    }

    private void convertFile(File file) throws FileNotFoundException, IOException {
        if (new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcfunction")) + ".mcf").exists()) {
            new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcfunction")) + ".mcf").delete();
            new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcfunction")) + ".mcf").createNewFile();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcfunction")) + ".mcf")));

        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }

        br.close();
        bw.close();
    }
}
