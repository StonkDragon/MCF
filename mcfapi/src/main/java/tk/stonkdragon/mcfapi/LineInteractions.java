package tk.stonkdragon.mcfapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class LineInteractions {
    public LineInteractions() {}

    /**
     * {@code setLineContents} sets the value of the given Line {@code l} in File {@code f} to {@code c}
     * @throws IOException
     */
    public void setLineContents(String c, int l, File f) throws IOException {
        BufferedWriter bw = null;
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(f));
        
        List<String> buf = new ArrayList<>();
        String line;
        
        int lindex = 0;
        while ((line = br.readLine()) != null) {
            if (lindex == l) {
                line = c;
            }

            buf.add(lindex, line);
            lindex++;
        }

        bw = new BufferedWriter(new FileWriter(f));

        for (int i = 0; i < buf.size(); i++) {
            bw.append(buf.get(i));
            bw.newLine();
        }

        br.close();
        bw.close();
    }

    /**
     * {@code getLineContents} returns the value of the given Line {@code l} in File {@code f}
     * @return String
     * @throws IOException
     */
    public String getLineContents(int l, File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        int lindex = 0;
        while ((line = br.readLine()) != null) {
            if (lindex == l) {
                br.close();
                return line;
            }
            lindex++;
        }
        br.close();
        return null;
    }

    /**
     * {@code getLines} returns all lines of File {@code f} in an Array of type {@code java.util.ArrayList}
     * @param File
     * @return ArrayList
     * @throws IOException
     */
    public List<String> getLines(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<String> out = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            out.add(line);
        }
        br.close();
        return out;
    }
}
