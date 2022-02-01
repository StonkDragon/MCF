package tk.stonkdragon.mcf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import tk.stonkdragon.mcf.exeptions.PreProcessorException;
import tk.stonkdragon.mcf.exeptions.UsingExeption;
import tk.stonkdragon.mcf.pre.PreProcessor;
import tk.stonkdragon.mcf.using.PackageLoader;
import tk.stonkdragon.mcf.utils.Converter;
import tk.stonkdragon.mcf.utils.JSONConv;

/**
 * MCFunction Compiler
 */
public class App extends Thread
{
    public static final boolean ISWINDOWS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("windows");
    public static final File CONFIG = new File(System.getProperty("user.home") + "/MCF");
    public static final File SETTINGS = new File(CONFIG.getAbsolutePath()  + "/settings.json");
    public static final File EXTENSIONS = new File(CONFIG.getAbsolutePath()  + "/extensions");
    public static final File PACKAGES = new File(CONFIG.getAbsolutePath()  + "/packages");
    public static final File IGNORED = new File(CONFIG.getAbsolutePath() + "/ignored.txt");

    public static String version;
    public static int max_score_lenght;
    public static File src;
    public static File target;
    public static boolean obfuscate;
    public static String[] args;
    public static boolean obfuscateFuncs;
    public static JSONArray ignoredScores;
    public static PreProcessor preproc;
    public static Converter conv;

    public App() 
    {
        // prepare config folders
        try {
            configFolder();
        } catch (IOException e) {}
        
        // initialize constants
        version = JSONConv.getData(SETTINGS).optString("version");
        max_score_lenght = JSONConv.getData(SETTINGS).optInt("max_scoreboard_lenght");
        src = new File(JSONConv.getData(SETTINGS).getJSONObject("defaults").getString("source_dir"));
        target = new File(JSONConv.getData(SETTINGS).getJSONObject("defaults").getString("target_dir"));
        obfuscate = JSONConv.getData(SETTINGS).getJSONObject("defaults").getBoolean("obfuscate_scores");
        obfuscateFuncs = JSONConv.getData(SETTINGS).getJSONObject("defaults").getBoolean("obfuscate_functions");

        // create the PreProcessor and Converter
        preproc = new PreProcessor();
        conv = new Converter();
    }

    public static void main( String[] args )
    {
        // save arguments
        App.args = args;
        // start main thread
        App app = new App();
        app.start();
    }

    @Override
    public void run() {
        // get operation
        String op = "-c";
        if (args.length != 0) {
            op = args[0];
        }

        // check, if mcf.json exists
        if (new File("mcf.json").exists()) {
            // if so, get its data
            JSONObject jsonObject = JSONConv.getData(new File("mcf.json"));
            
            // and define source and target dirs
            src = (jsonObject.optString("source_dir") != null ? new File(jsonObject.optString("source_dir")) : src);
            target = (jsonObject.optString("target_dir") != null ? new File(jsonObject.optString("target_dir")) : target);

            // check obfuscation
            obfuscate = jsonObject.optBoolean("obfuscate_scores");
            obfuscateFuncs = jsonObject.optBoolean("obfuscate_functions");

            // get scores to ignore in obfuscation
            ignoredScores = jsonObject.optJSONArray("ignored_scores");
        }
        
        // if src and target dont exist, create them
        if (!target.exists()) {
            target.mkdirs();
        }
        if (!src.exists()) {
            src.mkdirs();
        }

        // trim the operator, just in case
        op = op.trim();

        // check, what to do
        if (op.contains("-c") || op.contains("compile")) {
            
            // compile
            try {
                FileUtils.deleteDirectory(target);
                target.mkdirs();
                preproc.startRead(src);
                System.out.println("Build Successful!");
            } catch (PreProcessorException e) {
                e.printStackTrace();
                System.err.println("Build failed.");
            } catch (UsingExeption e) {
                e.printStackTrace();
                System.err.println("Build failed.");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Build failed.");
            }

        } else if (op.contains("-d") || op.contains("convert")) {
            
            // convert existing pack
            try {
                conv.convertAll(src);
                System.out.println("Conversion Successful!");
            } finally {}
 
        } else if (op.contains("-f") || op.contains("convertsingle")) {
 
            // convert singe file
            try {
                conv.convert(src);
                System.out.println("Conversion Successful!");
            } finally {}
        } else if (op.contains("-v") || op.contains("version")) {
 
            // display version
            System.out.println("MCF Version: " + version );
        }
    }

    private void configFolder() throws IOException {
        
        // check if config folder exist and create if it doesnt
        if (!CONFIG.exists())
            CONFIG.mkdirs();
        
        // do the same for packages
        if (!PACKAGES.exists())
            PACKAGES.mkdirs();

        // extensions
        if (!EXTENSIONS.exists())
            EXTENSIONS.mkdirs();

        // check if the ignore file exists
        if (!IGNORED.exists()) {
            IGNORED.createNewFile();
            
            // reconstruct ignore file
            BufferedWriter bw = new BufferedWriter(new FileWriter(IGNORED));
            bw.append(".DS_Store");
            bw.newLine();

            bw.close();
        }

        // settings file
        if (!SETTINGS.exists()) {
            try {
                // create and populate settings file
                SETTINGS.createNewFile();
                generateSettingsData();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // start package loader thread so main doesn't crash when an error is encountered
        PackageLoader pl = new PackageLoader();
        pl.start();
    }
    
    // create settings file
    private void generateSettingsData() {
        // root json object
        JSONObject j = new JSONObject();
        // repos
        j.put("repos", new JSONArray());
        // add example repo
        j.getJSONArray("repos").put(
            new JSONObject(
                "{\"url\": \"https://example.org/example.zip\", \"identifier\": \"org.example\"}"
            )
        );

        // extensions
        j.put("extensions", new JSONArray());
        // add example repo
        j.getJSONArray("extensions").put(
            new JSONObject(
                "{\"url\": \"https://example.org/example.jar\", \"file\": \"example_extension.jar\"}"
            )
        );

        // max scoreboard lenght
        j.put("max_scoreboard_lenght", 16);
        // default values
        j.put("defaults", new JSONObject());
        // obfuscate scores
        j.getJSONObject("defaults").put("obfuscate_scores", false);
        // obfuscate functions
        j.getJSONObject("defaults").put("obfuscate_functions", false);
        // source directory
        j.getJSONObject("defaults").put("source_dir", "src");
        // target directory
        j.getJSONObject("defaults").put("target_dir", "target");

        // write data to file
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SETTINGS))) {
            br.append(j.toString(4));
            System.out.println("Generated Settings File at " + SETTINGS.getAbsolutePath());
            br.close();
        } catch (IOException e) {
            System.err.println("Unable to generate Settings File at ~/MCF/settings.json");
            e.printStackTrace();
        }
    }
}
