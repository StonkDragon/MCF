package tk.stonkdragon.mcf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import tk.stonkdragon.mcf.conv.Converter;
import tk.stonkdragon.mcf.pre.PreProcessor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        String op = "-c";
        if (args.length != 0) {
            op = args[0];
        }

        File src;
        if (new File("mcf.json").exists()) {
            BufferedReader br = new BufferedReader(new FileReader(new File("mcf.json")));
            List<String> lines = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            String jsonString = "";
            for (String i : lines) {
                jsonString += i.trim();
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            src = new File(jsonObject.optString("source_dir"));
        } else {
            src = new File("data");
        }
        op = op.trim();

        PreProcessor preproc = new PreProcessor();
        Converter conv = new Converter();
        if (op.contains("-c") || op.contains("compile")) {
            preproc.startRead(src);
            System.out.println("Build Successful!");
        } else if (op.contains("-d") || op.contains("convert")) {
            conv.convertAll(src);
            System.out.println("Conversion Successful!");
        } else if (op.contains("-f") || op.contains("convertsingle")) {
            conv.convert(src);
            System.out.println("Conversion Successful!");
        }
    }
}
