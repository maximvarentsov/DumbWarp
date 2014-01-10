package com.turt2live.dumbwarp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the warp manager. This class manages all warps in regards to DumbWarp
 *
 * @author turt2live
 */
public class WarpManager {

    private Map<String, Warp> warps = new HashMap<String, Warp>();

    /**
     * Adds a warp to the manager
     *
     * @param warp the warp to add
     */
    public void addWarp(Warp warp) {
        if (warp != null) {
            warps.put(warp.getName().toLowerCase(), warp);
        }
    }

    /**
     * Determines if a warp exists
     *
     * @param name the name to look for
     * @return true if the warp exists, false otherwise
     */
    public boolean hasWarp(String name) {
        if (name != null) {
            return warps.containsKey(name.toLowerCase());
        }
        return false;
    }

    /**
     * Removes a warp from the manager
     *
     * @param warp the warp to remove
     */
    public void removeWarp(Warp warp) {
        if (warp != null) {
            warps.remove(warp.getName().toLowerCase());
        }
    }

    /**
     * Gets a warp by name
     *
     * @param name the name to look for
     * @return the warp, or null if not found
     */
    public Warp getWarp(String name) {
        if (name != null) {
            return warps.get(name.toLowerCase());
        }
        return null;
    }

    /**
     * Gets a list of all warps. Editing this has no effect
     *
     * @return the list of all warps
     */
    public List<Warp> getWarps() {
        List<Warp> warps = new ArrayList<Warp>();
        warps.addAll(this.warps.values());
        return warps;
    }

}
