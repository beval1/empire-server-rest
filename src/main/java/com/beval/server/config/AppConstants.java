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
            "http://127.0.0.1:80",
            "http://reddit-clone-react1.herokuapp.com",
            "https://reddit-clone-react1.herokuapp.com"
    );
    public static final int MAXIMUM_USERNAME_LENGTH = 20;
    public static final int MINIMUM_USERNAME_LENGTH = 5;
    public static final int PRODUCTION_BUILDINGS_CASTLE_LIMIT = 20;
    public static final List<Integer> KEEP_COORDINATES = List.of(12, 3);
    public static final Map<Integer, Integer> userLevelsXP = Map.ofEntries(
            entry(1, 0),
            entry(2, 5),
            entry(3, 15),
            entry(4, 15),
            entry(5, 25),
            entry(6, 40),
            entry(7, 60),
            entry(8, 100),
            entry(9, 150),
            entry(10, 200)
    );
}
