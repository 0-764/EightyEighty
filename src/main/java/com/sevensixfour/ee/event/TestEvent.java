package com.sevensixfour.ee.event;

import com.sevensixfour.ee.EightyEighty;
import org.bukkit.Chunk;
import org.bukkit.event.Listener;

public class TestEvent implements Listener {
    public void onChunkLoad() {
        Chunk[] c = EightyEighty.instance.getServer().getWorlds().get(0).getLoadedChunks();
        c[0].getInhabitedTime()
    }
}
