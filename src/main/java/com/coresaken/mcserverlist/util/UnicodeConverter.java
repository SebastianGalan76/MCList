package com.coresaken.mcserverlist.util;

import java.util.HashMap;
import java.util.Map;

public class UnicodeConverter {

    private static final Map<Character, Character> unicodeToAsciiMap = new HashMap<>();

    static {
        unicodeToAsciiMap.put('ᴡ', 'w');
        unicodeToAsciiMap.put('ʏ', 'y');
        unicodeToAsciiMap.put('s', 's');
        unicodeToAsciiMap.put('ᴛ', 't');
        unicodeToAsciiMap.put('ᴀ', 'a');
        unicodeToAsciiMap.put('ʀ', 'r');
        unicodeToAsciiMap.put('ᴛ', 't');
        unicodeToAsciiMap.put('ᴏ', 'o');
        unicodeToAsciiMap.put('ᴡ', 'w');
        unicodeToAsciiMap.put('ᴀ', 'a');
        unicodeToAsciiMap.put('ʟ', 'l');
        unicodeToAsciiMap.put('ɴ', 'n');
        unicodeToAsciiMap.put('ᴏ', 'o');
        unicodeToAsciiMap.put('ᴡ', 'w');
        unicodeToAsciiMap.put('ʏ', 'y');
        unicodeToAsciiMap.put('ᴛ', 't');
        unicodeToAsciiMap.put('ʀ', 'r');
        unicodeToAsciiMap.put('ʏ', 'y');
        unicodeToAsciiMap.put('ʙ', 'b');
        unicodeToAsciiMap.put('ʙ', 'b');
        unicodeToAsciiMap.put('ɪ', 'i');
        unicodeToAsciiMap.put('ᴊ', 'j');
        unicodeToAsciiMap.put('ᴀ', 'a');
        unicodeToAsciiMap.put('ᴊ', 'j');
        unicodeToAsciiMap.put('ᴅ', 'd');
        unicodeToAsciiMap.put('ᴏ', 'o');
        unicodeToAsciiMap.put('ɢ', 'g');
        unicodeToAsciiMap.put('ʀ', 'r');
        unicodeToAsciiMap.put('ʏ', 'y');
    }

    public static String convertUnicodeToAscii(String unicodeText) {
        StringBuilder asciiText = new StringBuilder();
        for (char c : unicodeText.toCharArray()) {
            asciiText.append(unicodeToAsciiMap.getOrDefault(c, c));
        }
        return asciiText.toString();
    }
}