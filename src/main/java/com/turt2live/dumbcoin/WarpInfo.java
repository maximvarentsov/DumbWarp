package com.turt2live.dumbcoin;

/**
 * Represents warp information
 *
 * @author turt2live
 */
public class WarpInfo {

    private String name;
    private Warp warp;

    WarpInfo(String name, Warp warp) {
        this.name = name;
        this.warp = warp;
    }

    /**
     * Gets the associated name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the associated warp
     *
     * @return the warp
     */
    public Warp getWarp() {
        return warp;
    }

}
