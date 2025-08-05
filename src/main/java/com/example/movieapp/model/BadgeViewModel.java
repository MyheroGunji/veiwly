package com.example.movieapp.model;

public class BadgeViewModel {
    private Badges badge;
    private boolean earned;

    public BadgeViewModel(Badges badge, boolean earned) {
        this.badge = badge;
        this.earned = earned;
    }

    public Badges getBadge() {
        return badge;
    }

    public boolean isEarned() {
        return earned;
    }
}

