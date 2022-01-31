package tk.stonkdragon.mcfapi;

import java.io.IOException;

public class Info {
    /**
     * @throws IOException
     */

    public static void main(String[] args) {
        String help = 
            "\n" +
            "Maven: \n" +
            "<dependency>\n" +
            "  <groupId>tk.stonkdragon.mcfapi</groupId>\n" +
            "  <artifactId>mcfapi</artifactId>\n" +
            "  <version>1</version>\n" +
            "  <scope>system</scope>\n" +
            "  <systemPath>${project.basedir}/path/to/mcf.jar</systemPath>\n" +
            "</dependency>\n" +
            "\n" +

            // TODO: check if this is correct:
            "Gradle:\n" +
            "dependencies {\n" +
            "    compile files('path/to/mcf.jar')\n" +
            "}\n";
        System.out.println(help);

        Logger.logMsg("This is an Info");
        Logger.logErr("This is an Error");
        Logger.logFatal("This is a Crash", 0);
    }
}
