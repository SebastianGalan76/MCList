package com.coresaken.mcserverlist.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkChecker {
    private static final String URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    public static boolean isLink(String input) {
        if (input == null) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(input);
        return matcher.matches();
    }
}