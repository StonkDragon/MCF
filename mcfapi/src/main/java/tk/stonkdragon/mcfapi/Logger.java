package tk.stonkdragon.mcfapi;

import java.io.File;
import java.net.URISyntaxException;

public class Logger {
    public static void logMsg(String msg) {
        StackTraceElement st = new Exception().getStackTrace()[1];
        String jarPath = "";
        try {
            jarPath = Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            if (jarPath.length() != 0) {
                jarPath = new File(jarPath).getName();
            }
            
            System.out.println("["+ jarPath + "@" + st.getFileName() + ":" + st.getLineNumber() + "] INFO: " + msg);
        } catch (URISyntaxException e) {}
    }

    public static void logErr(String msg) {
        StackTraceElement st = new Exception().getStackTrace()[1];
        String jarPath = "";
        try {
            jarPath = Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            if (jarPath.length() != 0) {
                jarPath = new File(jarPath).getName();
            }
            
            System.err.println("["+ jarPath + "@" + st.getFileName() + ":" + st.getLineNumber() + "] ERROR: " + msg);
        } catch (URISyntaxException e) {}
    }

    public static void logFatal(String msg) {
        StackTraceElement st = new Exception().getStackTrace()[1];
        String jarPath = "";
        try {
            jarPath = Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            if (jarPath.length() != 0) {
                jarPath = new File(jarPath).getName();
            }
            
            System.err.println("["+ jarPath + "@" + st.getFileName() + ":" + st.getLineNumber() + "] FATAL: " + msg);
            System.exit(-1);
        } catch (URISyntaxException e) {}
    }

    public static void logFatal(String msg, int exitCode) {
        StackTraceElement st = new Exception().getStackTrace()[1];
        String jarPath = "";
        try {
            jarPath = Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            if (jarPath.length() != 0) {
                jarPath = new File(jarPath).getName();
            }
            
            System.err.println("["+ jarPath + "@" + st.getFileName() + ":" + st.getLineNumber() + "] FATAL: " + msg);
            System.exit(exitCode);
        } catch (URISyntaxException e) {}
    }
}
