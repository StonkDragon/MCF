package tk.stonkdragon.mcf.utils;

import java.util.Random;

public class Rand {

    // generate a random string based on the hash code of s
    public static String randomString(String s) {
        return (Long.toHexString(new Random(s.hashCode()).nextLong()));
    }

    // generate a random string based on the hash code of s with max length of l
    public static String randomString(String s, int l) {
        String r;
        r = (Long.toHexString(new Random(s.hashCode()).nextLong()));
        while (r.length() > l) {
            r = (Long.toHexString(new Random(s.hashCode()).nextLong()));
        }
        return r;
    }
}
