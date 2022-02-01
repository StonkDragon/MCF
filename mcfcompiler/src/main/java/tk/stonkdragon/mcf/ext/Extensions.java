package tk.stonkdragon.mcf.ext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tk.stonkdragon.mcf.App;

public class Extensions extends Thread {

    private File ext;
    
    public static List<File> getExtensions() {
        List<File> e = new ArrayList<>();
        for (File f : App.EXTENSIONS.listFiles()) {
            e.add(f);
        }
        return e;
    }

    public Extensions(File ext) {
        this.ext = ext;
    }

    public void runExtension() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("java -jar " + this.ext);
            InputStream in = p.getInputStream();
            InputStream err = p.getErrorStream();
            while (p.isAlive()) {
                in.transferTo(System.out);
                err.transferTo(System.err);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runExtension(String args) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("java -jar " + this.ext + " " + args);
            InputStream in = p.getInputStream();
            InputStream err = p.getErrorStream();
            while (p.isAlive()) {
                in.transferTo(System.out);
                err.transferTo(System.err);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
