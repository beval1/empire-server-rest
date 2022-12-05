package com.beval.server.config;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public final class AppConstants {
    private AppConstants(){}
    public static final String PROJECT_NAME = "Empire Server";
    public static final String API_BASE = "/api/v1";
    public static final List<String> DO_NOT_FILTER_PATHS = List.of(
            AppConstants.API_BASE + "/auth/signin",
            AppConstants.API_BASE + "/auth/signup"
    );
    public static final List<String> CROSS_ORIGIN_DOMAINS = List.of(
            "http://localhost:3000",
            "http://127.0.0.1:80"
    );
    public static final int MAXIMUM_USERNAME_LENGTH = 20;
    public static final int MINIMUM_USERNAME_LENGTH = 5;
    public static final int PRODUCTION_BUILDINGS_CASTLE_LIMIT = 20;
    public static final List<Integer> KEEP_COORDINATES = List.of(12, 1);
    public static final int CITIZEN_COINS_MULTIPLIER = 2;
    public static final int CASTLE_STARTING_FOOD = 1;
    public static final int CASTLE_STARTING_WOOD = 10000;
    public static final int CASTLE_STARTING_STONE = 10000;

    public static final Map<Integer, Integer> userLevelsXP = Map.ofEntries(
            entry(1, 0),
            entry(2, 5),
            entry(3, 15),
            entry(4, 25),
            entry(5, 40),
            entry(6, 60),
            entry(7, 100),
            entry(8, 150),
            entry(9, 200),
            entry(10, 250)
    );

    public static final Map<Integer, String> keepMapImages = Map.ofEntries(
            entry(1, "https://res.cloudinary.com/djog8qqis/image/upload/v1670204661/empire/map/keep/map_keep_level1-removebg-preview_w2j5vc.png"),
            entry(2, "https://res.cloudinary.com/djog8qqis/image/upload/v1670204669/empire/map/keep/map_keep_level2-removebg-preview_zjdtle.png"),
            entry(3, "https://res.cloudinary.com/djog8qqis/image/upload/v1670204677/empire/map/keep/map_keep_level3-removebg-preview_ajbut9.png"),
            entry(4, "https://res.cloudinary.com/djog8qqis/image/upload/v1670204684/empire/map/keep/map_keep_level4-removebg-preview_bc8tkj.png")
    );

    //x, y, quadrant
    public static final List<List<Integer>> mapCastleValidPositions = List.of(
            List.of(3, 5, 1),
            List.of(9, 15, 1),
            List.of(5, 7, 1),
            List.of(22, 12, 1),
            List.of(11, 5, 1),
            List.of(20, 10, 1),
            List.of(10, 10, 1),
            List.of(20, 20, 1),
            List.of(15, 3, 1),
            List.of(9, 15, 1),
            List.of(14, 12, 1),
            List.of(3, 15, 1),
            List.of(12, 22, 1),
            List.of(17, 10, 1),
            List.of(5, 2, 1),
            List.of(13, 2, 1),
            List.of(13, 4, 1),
            List.of(4, 13, 1),
            List.of(7, 8, 1),
            List.of(21, 8, 1)
    );

    public static final int[][] validGridBuildingPosition = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
}
