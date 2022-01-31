package tk.stonkdragon.mcf.using;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.stonkdragon.mcf.App;
import tk.stonkdragon.mcf.utils.JSONConv;

public class PackageLoader extends Thread {

    @Override
    public void run() {
        try {
            this.downloadExtentions();
            this.downloadPackages();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadPackages() throws JSONException, IOException {
        
        // get all repos and loop over them
        JSONArray repos = JSONConv.getData(App.SETTINGS).getJSONArray("repos");
        for (int i = 0; i < repos.length(); i++) {
        
            // specify the current repo
            JSONObject repo = repos.getJSONObject(i);
            
            // check if current repo is example entry
            if (!repo.getString("identifier").contains("org.example")) {
            
                // download package
                // File downloader from https://stackoverflow.com/questions/18872611/download-file-from-server-in-java
                try {
                    FileUtils.copyURLToFile(
                        new URL(
                            repo.getString("url")
                        ),
                        new File(App.PACKAGES + File.pathSeparator + repo.getString("identifier") + ".zip")
                    );
                } catch (JSONException | IOException e) {
                    System.err.println("Unable to download Package: " + repo.getString("identifier"));
                    return;
                }
                // decompress the downloaded zip archive
                try {
                    decompress(new File(App.PACKAGES + File.pathSeparator + repo.getString("identifier") + ".zip").getAbsolutePath(), new File(App.PACKAGES + File.pathSeparator + repo.getString("identifier") + ".pkg"));
                } catch (JSONException | IOException e) {
                    System.err.println("Unable to extract Package: " + repo.getString("identifier"));
                    return;
                }
            
                // delete the zip file
                new File(App.PACKAGES + File.pathSeparator + repo.getString("identifier") + ".zip").delete();
            }
        }
    }

    private void downloadExtentions() throws JSONException, IOException {
        
        // get all extentions and loop over them
        JSONArray exts = JSONConv.getData(App.SETTINGS).getJSONArray("extentions");
        for (int i = 0; i < exts.length(); i++) {
        
            // specify the current extention
            JSONObject ext = exts.getJSONObject(i);
            
            // check if current extention is example entry
            if (!ext.getString("file").contains("example_extention")) {
            
                // download extention
                // File downloader from https://stackoverflow.com/questions/18872611/download-file-from-server-in-java
                try {
                    FileUtils.copyURLToFile(
                        new URL(
                            ext.getString("url")
                        ),
                        new File(App.PACKAGES + File.pathSeparator + ext.getString("file"))
                    );
                } catch (JSONException | IOException e) {
                    System.err.println("Unable to download Extention: " + ext.getString("file"));
                    return;
                }
            }
        }
    }

    /**
     * Zip decompression
     * Source: https://www.baeldung.com/java-compress-and-uncompress
     */
    private void decompress(String file, File dest) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                while (zipEntry != null) {
                    File newFile = newFile(dest, zipEntry);
                    if (zipEntry.isDirectory()) {
                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
                            throw new IOException("Failed to create directory " + newFile);
                        }
                    } else {
                        // fix for Windows-created archives
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory() && !parent.mkdirs()) {
                            throw new IOException("Failed to create directory " + parent);
                        }
                        
                        // write file content
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                zipEntry = zis.getNextEntry();
                }
            }
            zis.closeEntry();
            zis.close();
        }
    } 

    /**
     * Return file from zip arcive
     * Source: https://www.baeldung.com/java-compress-and-uncompress
     */
    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
    
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
    
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
    
        return destFile;
    }
}
