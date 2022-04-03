package com.sevensixfour.ee.util;

import com.sevensixfour.ee.EightyEighty;

import java.awt.*;

public class StartupMsg {

    private static Color P = ColourEffects.getColour(247, 253, 237);
    private static Color S = ColourEffects.getColour(246, 174, 177);

    public static String[] STARTUP = new String[]
            {
                    "",
                    ColourEffects.gradientEffect(" _____ _       _     _         _____ _       _     _     ", P, S),
                    ColourEffects.gradientEffect("| ____(_) __ _| |__ | |_ _   _| ____(_) __ _| |__ | |_ _   _ ", P, S),
                    ColourEffects.gradientEffect("|  _| | |/ _` | '_ \\| __| | | |  _| | |/ _` | '_ \\| __| | | |", P, S),
                    ColourEffects.gradientEffect("| |___| | (_| | | | | |_| |_| | |___| | (_| | | | | |_| |_| |", P, S),
                    ColourEffects.gradientEffect("|_____|_|\\__, |_| |_|\\__|\\__, |_____|_|\\__, |_| |_|\\__|\\__, |", P, S),
                    ColourEffects.gradientEffect("         |___/           |___/         |___/           |___/ ", P, S),
                    ColourEffects.gradientEffect("Running plugin:", P, S) + ColourEffects.reset(" EightyEighty v0.0.1"),
                    ColourEffects.gradientEffect("Server running: ", P, S) + ColourEffects.reset(EightyEighty.instance.getServer().getVersion()) + ColourEffects.gradientEffect(" Version: ", P, S) + ColourEffects.reset(EightyEighty.instance.getServer().getBukkitVersion()),
                    ColourEffects.gradientEffect("API version: ", P, S) + ColourEffects.reset(EightyEighty.instance.getDescription().getAPIVersion()),
                    ColourEffects.gradientEffect("Developed by: ", P, S) + ColourEffects.reset(EightyEighty.instance.getDescription().getAuthors().toString()),
                    ColourEffects.gradientEffect("If you liked this plugin please consider leaving feedback", P, S),
            };

}
