package com.beval.server.config;

import java.util.List;

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
}
