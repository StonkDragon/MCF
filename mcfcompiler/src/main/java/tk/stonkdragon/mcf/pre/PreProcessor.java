package tk.stonkdragon.mcf.pre;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tk.stonkdragon.mcf.App;
import tk.stonkdragon.mcf.exeptions.PreProcessorException;
import tk.stonkdragon.mcf.exeptions.UsingExeption;
import tk.stonkdragon.mcf.ext.Extensions;
import tk.stonkdragon.mcf.using.Using;
import tk.stonkdragon.mcf.utils.Ignores;
import tk.stonkdragon.mcf.utils.Rand;

public class PreProcessor {

    // define vars
    private List<String> globalDefines = new ArrayList<String>();
    private boolean shouldCompile;
    private List<String> obfc = new ArrayList<>();
    private List<String> deobfc = new ArrayList<>();
    private List<String> obfcfn = new ArrayList<>();
    private List<String> deobfcfn = new ArrayList<>();
    private File outfile;

    // start the compilation process
    public void startRead(File rootFolder) throws PreProcessorException, UsingExeption {
        if (App.obfuscate) {
            checkObf(rootFolder);
        }
        check(rootFolder);
    }

    // check if is file
    private void check(File folder) throws PreProcessorException, UsingExeption {
        
        // create array containing all files in current dir
        File[] files = folder.listFiles();

        // loop over all of them
        for (File file : files) {
            
            // if is dir check that
            if (file.isDirectory())
                check(file);

            // else check what kind of file it is
            else {

                // if it is a .mcf file, read it
                if (file.getName().endsWith(".mcf")) {
                    try {
                        readFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
                // else copy it to target dir
                } else {
                    try {
                        copyFileUsingStream(file, new File(file.getAbsolutePath().replace(App.src.getPath(), App.target.getPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 
            }
        }
    }

    private void checkObf(File folder) throws PreProcessorException, UsingExeption {
        
        // create array containing all files in current dir
        File[] files = folder.listFiles();

        // loop over all of them
        for (File file : files) {

            // if is dir check that
            if (file.isDirectory())
                checkObf(file);

            // else check what kind of file it is
            else {

                // if it is a .mcf file, read it
                if (file.getName().endsWith(".mcf")) {
                    try {
                        obfuscateFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 
            }
        }
    }

    /**
     * Source: https://stackoverflow.com/questions/16433915/how-to-copy-file-from-one-location-to-another-location
     */
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        if (Ignores.fileContainsString(App.IGNORED, source.getName()))
            return;
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    // read through file
    private void readFile(File file) throws IOException, FileNotFoundException, PreProcessorException, UsingExeption {
        
        // create all Objects used
        // line
        String line;
        String repl;
        // defines
        List<String> fileDefines = new ArrayList<String>();
        List<String> macroDefines = new ArrayList<String>();
        
        // output file
        String filepath = file
            .getPath()
            .replaceFirst(
                App.src.getPath(),
                App.target.getPath()
            );
        outfile = new File(
            filepath
                .substring(
                    0,
                    filepath
                    .lastIndexOf(".mcf")
            )+ ".mcfunction"
        );
        
        String outpath = outfile.getPath();
        String outname = outpath.substring(outpath.lastIndexOf("functions/") + "functions/".length(), outpath.lastIndexOf("."));
        if (App.obfuscateFuncs) {
            for (int i = 0; i < deobfcfn.size(); i++) {
                String s = deobfcfn.get(i);
                String t = obfcfn.get(i);
                s = s.trim();
                outname = outname.trim();

                if (s.equals(outname)) {
                    outfile = new File(outfile.getPath().replace(s, t));
                }
            }
        }

        // create outfile if it doesnt exist
        if (outfile.exists())
            outfile.delete();
        new File(outfile.getParent()).mkdirs();


        // extensions run before anything else
        // this loops over every extension and runs it
        for (File f : Extensions.getExtensions()) {
            if (f.getName().endsWith(".jar")) {
                // run extension with source file, target file and current line index
                new Extensions(f).runExtension(file.getAbsolutePath() + " " + outfile.getAbsolutePath());
            }
        }

        // create filereader and filewriter
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));

        // set compilation to true
        this.shouldCompile = true;

        // loop over every line in the file
        while ((line = br.readLine()) != null) {
            
            // trim the line
            line = line.trim();

            // check if the line starts with the mcf prefix "#!"
            if (line.startsWith("#!")) {
                
                // new empty string
                String def = "";
                
                // remove prefix
                line = line.substring(2);
                
                // split line at " "
                String[] keys = line.split(" ");

                // set def to keys[1]
                // need to check what this does exactly
                if (keys.length > 1) {
                    def = keys[1];
                }

                // make the operator Uppercase
                keys[0] = keys[0].toUpperCase(Locale.ENGLISH);

                // check, what operator is used and execute its logic
                switch (keys[0]) {
                    case "INFO": 
                        if (this.shouldCompile) {
                            if (!App.ISWINDOWS) {
                                System.err.print("\u001B[34mINFO: ");
                            } else {
                                System.err.print("INFO: ");
                            }

                            for (int i = 1; i < keys.length; i++) {
                                System.err.print(keys[i] + " ");
                            }

                            if (!App.ISWINDOWS) {
                                System.err.println("\u001B[0m");
                            }
                        }
                        continue;
                    case "ERROR":
                        if (this.shouldCompile) {
                            if (!App.ISWINDOWS) {
                                System.err.print("\u001B[31mERROR: ");
                            } else {
                                System.err.println("ERROR: ");
                            }

                            for (int i = 1; i < keys.length; i++) {
                                System.err.print(keys[i] + " ");
                            }

                            if (!App.ISWINDOWS) {
                                System.err.println("\u001B[0m");
                            }
                        }
                        continue;
                    case "GLOBALDEF":
                        if (def == null || def.length() == 0) 
                            throw new PreProcessorException("<label> is null");
                        if (this.shouldCompile && !this.globalDefines.contains(def))
                            this.globalDefines.add(def);
                        continue;
                    case "DEFINE":
                        if (def == null || def.length() == 0) 
                            throw new PreProcessorException("<label> is null");
                        if (this.shouldCompile && !fileDefines.contains(def))
                            fileDefines.add(def);
                        continue;
                    case "IFDEF":
                        if (def == null || def.length() == 0) 
                            throw new PreProcessorException("<label> is null");
                        this.shouldCompile = (globalDefines.contains(def) || fileDefines.contains(def));
                        continue;
                    case "IFNDEF":
                        if (def == null || def.length() == 0) 
                            throw new PreProcessorException("<label> is null");
                        this.shouldCompile = !(globalDefines.contains(def) || fileDefines.contains(def));
                        continue;
                    case "ENDIF":
                        this.shouldCompile = true;
                        continue;
                    case "ELSE":
                        this.shouldCompile = !this.shouldCompile;
                        continue;
                    case "UNDEF":
                        if (def == null || def.length() == 0) 
                            throw new PreProcessorException("<label> is null");
                        if (this.shouldCompile) {
                            this.globalDefines.remove(def);
                            fileDefines.remove(def);
                        }

                        continue;
                    case "MACRO":
                        if (this.shouldCompile) {
                            if (keys.length >= 3){
                                for (int i = 3; i < keys.length; i++) {
                                    keys[2] += " " + keys[i];
                                    keys[i] = "";
                                }
                            }
                            repl = keys[2];
                            System.err.println(def + "->" + repl);
                            macroDefines.add(def + "->" + repl);
                            if (!fileDefines.contains(def))
                                fileDefines.add(def);
                        }
                        continue;
                    case "USING":
                        Using.use(def);
                        continue;
                }
            
            // if not
            } else {

                // string to store the current lines command in
                String cmd;
                
                // actually store the command
                if (line.contains(" ")) {
                    cmd = line.substring(0, line.indexOf(" "));
                } else if (line.length() == 0) {
                    cmd = null;
                } else {
                    cmd = line;
                }

                // replace score obfuscations
                for (int i = 0; i < obfc.size(); i++) {
                    if (line.contains("score"))
                        line = line.replaceAll(deobfc.get(i), obfc.get(i));
                }

                // replace function obfuscations
                for (int i = 0; i < obfcfn.size(); i++) {
                    if (line.contains("function"))
                        line = line.replaceAll(deobfcfn.get(i), obfcfn.get(i));
                }

                // useful for arguments with command aliases
                // string to store args
                String arg;

                // store arguments in string
                if (line.contains(" ")) {
                    arg = line.substring(line.indexOf(" "), line.length());
                } else {
                    arg = null;
                }
                
                // create list of arguments
                String[] args = ((arg == null) ? new String[1] : arg.split(" "));

                // check if the command is a vanilla command
                if (cmd != null && new Commands().exists(cmd)) {
                    
                    // replace macros
                    for (String i : macroDefines) {
                        line = line.replaceAll("%" + i.substring(0, i.indexOf("->")) + "%", i.substring(i.indexOf("->") + 2, i.length()));
                    }

                    // if the current line should compile, compile
                    if (shouldCompile && (line.length() != 0)) {
                        bw.write(line);
                        bw.newLine();
                    }
                
                // if it isnt
                } else if (cmd != null && !new Commands().exists(cmd)) {
                    
                    // check if one of the used packages has the command
                    if (Using.isCommand(cmd) && shouldCompile) {
                        // get the replacement for cmd and write it to the file
                        bw.write(Using.expand(cmd, args));
                        // insert newline
                        bw.newLine();
                    }
                }
            }
        }

        // close the reader and writer
        bw.close();
        br.close();
    }

    private void obfuscateFile(File file) throws IOException, FileNotFoundException, PreProcessorException, UsingExeption {

        // file reader
        BufferedReader br = new BufferedReader(new FileReader(file));

        // line
        String line;

        // loop over every line in the file
        while ((line = br.readLine()) != null) {
            
            // trim the line
            line = line.trim();

            // if line is scoreboard definition
            if (line.startsWith("scoreboard objectives add")) {
                String obf;
                
                // and doesnt define a trigger
                if (line.startsWith("scoreboard objectives add") && !line.endsWith("trigger")) {

                    // set obf to the score name
                    obf = line.substring("scoreboard objectives add ".length(), line.lastIndexOf(" "));

                    // generate a random string based on the score name
                    String r = Rand.randomString(obf.trim(), App.max_score_lenght);

                    // just in case r already exists, generate a new random string
                    while (obfc.contains(r)) {
                        r = Rand.randomString(obf.trim(), App.max_score_lenght);
                    }

                    // add r and the score name to our obfuscation mapping if it shouldnt be ignored
                    if (!App.ignoredScores.toList().contains(obf.trim())) {
                        obfc.add(r);
                        deobfc.add(obf.trim());
                    } else {
                        obfc.add(obf.trim());
                        deobfc.add(obf.trim());
                    }

                    // notify the user of the mapping
                    System.out.println("Score " + obf.trim() + " becomes " + obfc.get(deobfc.indexOf(obf.trim())));
                }
            }
        }

        // get the file path
        String filepath = file.getPath().replaceFirst(App.src.getPath(), App.target.getPath());
        
        // define outfile 
        outfile = new File(filepath.substring(0, filepath.lastIndexOf(".mcf"))+ ".mcfunction");
        
        // get outfile's path
        String outpath = outfile.getPath();
        
        // check if functions should be obfuscated
        if (App.obfuscateFuncs) {
            // get the function name for minecraft
            String outf = outfile.getPath().substring(outfile.getPath().lastIndexOf("functions/") + "functions/".length() ,outfile.getPath().lastIndexOf("."));

            // get the index of the function name in the path
            int i = outpath.lastIndexOf(outf);

            // generate a random string based on the function name
            String r = Rand.randomString(outf);

            // new stringbuilder
            // used to replace the last occurence of the original name
            // source: https://stackoverflow.com/questions/23325800/replace-the-last-occurrence-of-a-string-in-another-string/23325828
            StringBuilder builder = new StringBuilder();
            builder.append(outpath.substring(0, i));
            builder.append(r);
            builder.append(".mcfunction");
            // store the new function name in a variable
            String build = builder.toString().substring(builder.lastIndexOf("functions/") + "functions/".length(), builder.lastIndexOf("."));
            
            // add the names to our obfuscation mapping
            obfcfn.add(build);
            deobfcfn.add(outf);

            // inform the user about the mapping
            System.out.print("File ");
            System.out.print(outfile.getName().substring(0, outfile.getName().lastIndexOf(".")));
            System.out.print(" becomes ");
            System.out.println(builder.toString().substring(builder.lastIndexOf("functions/") + "functions/".length(), builder.lastIndexOf(".")));
        }

        // close the reader
        br.close();
    }
}
