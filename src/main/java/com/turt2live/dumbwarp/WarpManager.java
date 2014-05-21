/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
     *
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
     *
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
