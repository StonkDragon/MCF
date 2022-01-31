package tk.stonkdragon.mcf.pre;

import java.util.ArrayList;
import java.util.List;

public class Commands {

    // define lists
    private List<String> cmds = new ArrayList<>();
    private static final String[] commands = {
        "advancement",
        "attribute",
        "bossbar",
        "clear",
        "clone",
        "data",
        "datapack",
        "defaultgamemode",
        "difficulty",
        "effect",
        "enchant",
        "execute",
        "experience",
        "fill",
        "forceload",
        "function",
        "gamemode",
        "gamerule",
        "give",
        "help",
        "item",
        "kill",
        "list",
        "locate",
        "locatebiome",
        "loot",
        "me",
        "msg",
        "particle",
        "playsound",
        "recipe",
        "say",
        "schedule",
        "scoreboard",
        "seed",
        "setblock",
        "setworldspawn",
        "spawnpoint",
        "spectate",
        "spreadplayers",
        "stopsound",
        "summon",
        "tag",
        "team",
        "teammsg",
        "teleport",
        "tellraw",
        "time",
        "trigger",
        "weather",
        "worldborder"
    };

    public Commands() {
        
        // initialise command list
        for (String s : commands) {
            this.cmds.add(s);
        }
    }

    // check if command exists
    public boolean exists(String s) {
        return cmds.contains(s);
    }
}
