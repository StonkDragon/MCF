package tk.stonkdragon.mcf.using;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tk.stonkdragon.mcf.App;
import tk.stonkdragon.mcf.exeptions.UsingExeption;
import tk.stonkdragon.mcf.utils.JSONConv;

public class Using {

    private static List<File> uses = new ArrayList<>();

    public static void use(String packageString) throws UsingExeption, JSONException, IOException {
        // Check, if package exists and define it as a var
        check(packageString);
        File use = new File(App.CONFIG + "/packages/" + packageString + ".pkg");
        // add the package to our list of packages
        uses.add(use);
        
        // get the packages namespace from its package.json
        String ns = JSONConv.getData(new File(use.getAbsolutePath() + "/package.json")).getString("namespace");
        
        // define the source and destination of the package
        File usedata = new File(use.getAbsolutePath() + File.pathSeparator + ns);
        File target = new File(App.target.getAbsolutePath() + "/data/" + ns);

        // copy package to our target
        FileUtils.copyDirectory(usedata, target);
    }

    public static boolean isCommand(String s) throws IOException {
        // loop over every package used
        for (File f : uses) {
            
            // get its package.json
            File conf = new File(f.getAbsolutePath() + "/package.json");
            
            // get the data from package.json
            JSONObject confdata = JSONConv.getData(conf);

            // return if command aliases are used
            return (confdata.getJSONObject("commandAliases").has(s));
        }
        // return false if loop ended unsuccessfully
        return false;
    }

    public static String expand(String cmd, String[] args) throws IOException {
        // define output string as an empty string
        String o = "";
        
        // loop over every package used
        for (File f : uses) {
            
            // reset output
            o = "";
            
            // get package.json
            File conf = new File(f.getAbsolutePath() + "/package.json");
            
            // get its data
            JSONObject confdata = JSONConv.getData(conf);
            
            // generate output string
            for (int i = 0; i < confdata.getJSONObject("commandAliases").getJSONArray(cmd).length(); i++) {
                String s = (
                    confdata
                    .getJSONObject("commandAliases")
                    .getJSONArray(cmd)
                    .getString(i)
                );
                o += s + "\n";
            }
        }
        // loop over args
        for (int i = 0; i < args.length; i++) {
            o = o.replaceAll(("\\$" + i), ((args[i] == null) ? "" : args[i]));
        }
        // re-construct output data
        String oo = "";
        for (String s : o.split(" ")) {
            if (s.startsWith("$"))
                s = "";
            oo += s + " ";
        }

        // return output
        return oo.trim();
    }

    private static void check(String check) throws UsingExeption {
        // define new file
        File use = new File(App.CONFIG + "/packages/" + check + ".pkg");
        
        // check if file exists
        if (!use.exists())
            // if not, throw error
            throw new UsingExeption("Package " + check + " does not exist!");
    }
}
