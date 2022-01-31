package tk.stonkdragon.mcf.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import tk.stonkdragon.mcf.App;

public class Converter {

    // convert all files
    public void convertAll(File rootFolder) {
        try {
            // start checking
            check(rootFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // convert single file
    public void convert(File file) {
        try {
            convertFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void check(File folder) throws FileNotFoundException, IOException {
        
        // get all files in current dir
        File[] files = folder.listFiles();

        // loop over every file
        for (File file : files) {
            
            // if it is a directory loop over every file in it
            if (file.isDirectory()) {
                check(file);

            // else convert the file
            } else {
                if (file.getName().endsWith(".mcfunction")) {
                    convertFile(file);
                }
            }
        }
    }

    private void convertFile(File file) throws FileNotFoundException, IOException {
        // re-create the file if it exists
        if (new File(file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).substring(0, file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).lastIndexOf(".mcfunction")) + ".mcf").exists()) {
            new File(file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).substring(0, file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).lastIndexOf(".mcfunction")) + ".mcf").delete();
            new File(file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).substring(0, file.getPath().replace(App.target.getAbsolutePath(), App.src.getAbsolutePath()).lastIndexOf(".mcfunction")) + ".mcf").createNewFile();
        }

        // define reader, writer and line variables
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file.getPath().substring(0, file.getPath().lastIndexOf(".mcfunction")) + ".mcf")));

        // loop over every line and write it to the file
        while ((line = br.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }

        // close reader and writer
        br.close();
        bw.close();
    }
}
