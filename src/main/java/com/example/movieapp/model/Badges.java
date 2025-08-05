package com.example.movieapp.model;

public enum Badges {
    FIRST_REVIEW("First Review!"),
    ASPIRING_CRITIC("Aspiring Critic"),
    LONG_WATCH_MASTER("Long Watch Master"),
    GENRE_COLLECTOR("Genre Collector"),
    TV_JUNKIE("TV Junkie"),
    MOVIE_MANIAC("Movie Maniac");

    private final String displayName;

    Badges(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
