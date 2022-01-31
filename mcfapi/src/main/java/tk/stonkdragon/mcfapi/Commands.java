package tk.stonkdragon.mcfapi;

public class Commands {

    //#region commands
    public static final String[] commands = {
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
    //#endregion

    public static boolean isCommand(String s) {
        for (String t : commands) {
            if (t.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
