package com.sevensixfour.ee.logger;

import com.sevensixfour.ee.EightyEighty;
import com.sevensixfour.ee.file.message.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LoggerHelper {

    private static String CONSOLE_PREFIX = ChatColor.GOLD + "[EE]" + ChatColor.RESET;

    public static void raw(String... rawMsg) {
        StringBuilder str = new StringBuilder();
        for (String raw : rawMsg) {
            str.append(raw).append("\n");
        }
        Bukkit.getConsoleSender().sendMessage(parseLoggerMsg(str.toString()));
    }

    public static void debug(String msg) {
        if (EightyEighty.DEBUG_MODE)
            Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + (translate(String.format("&3[Debug]: &r%s", parseLoggerMsg(msg)))));
    }

    public static void success(String msg) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + (translate(String.format("&2[Info]: %s", parseLoggerMsg(msg)))));
    }

    public static void info(String msg) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + (translate(String.format("&r[Info]: %s", parseLoggerMsg(msg)))));
    }

    public static void warning(String msg) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + (translate(String.format("&e[Warning]:&r %s", parseLoggerMsg(msg)))));
    }

    public static void error(String msg) {
        Bukkit.getConsoleSender().sendMessage(CONSOLE_PREFIX + (translate(String.format("&4[E&cr&4r&co&4r]:&r %s", parseLoggerMsg(msg)))));
    }

    private static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', parseLoggerMsg(msg));
    }

    private static String parseLoggerMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', MessageHelper.parsePREFIXESInString(msg));
    }

}
