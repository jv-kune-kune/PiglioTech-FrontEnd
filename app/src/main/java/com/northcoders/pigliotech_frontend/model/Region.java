package com.northcoders.pigliotech_frontend.model;

import com.northcoders.pigliotech_frontend.R;

public enum Region {
    NORTH_WEST(R.string.north_west),
    NORTH_EAST(R.string.north_east),
    YORKSHIRE_HUMBER(R.string.yorkshire_humber),
    WEST_MIDLANDS(R.string.west_midlands),
    EAST_MIDLANDS(R.string.east_midlands),
    EAST_ANGLIA(R.string.east_anglia),
    LONDON(R.string.london),
    SOUTH_EAST(R.string.south_east),
    SOUTH_WEST(R.string.south_west);

    public final int region;

    private Region(int region) {
        this.region = region;
    }
}