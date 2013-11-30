package com.eyeofender.enderpearl;

public class Util {

    public static String createString(String[] args, int start) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; x++) {
            string.append(args[x]);
            if (x != args.length - 1) {
                string.append(" ");
            }
        }

        return string.toString();
    }

}
