package com.searchteam.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtils {

    public static String aliasUsername(String username) {
        if (username.startsWith("@")) return username;
        return "@" + username;
    }

}
