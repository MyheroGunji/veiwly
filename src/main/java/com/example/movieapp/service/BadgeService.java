package com.example.movieapp.service;

import com.example.movieapp.model.BadgeViewModel;
import com.example.movieapp.model.Badges;
import com.example.movieapp.model.Review;
import com.example.movieapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BadgeService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Set<Badges> evaluateBadges(String username) {
        List<Review> reviews = reviewRepository.findByUsername(username);
        Set<Badges> earned = new HashSet<>();

        if (reviews.size() >= 1) earned.add( Badges.FIRST_REVIEW);
        if (reviews.size() >= 10) earned.add( Badges.ASPIRING_CRITIC);
        if (reviews.stream().mapToInt(r -> r.getDuration() != null ? r.getDuration() : 0).sum() >= 500)
            earned.add( Badges.LONG_WATCH_MASTER);

        long genreCount = reviews.stream()
                .map(Review::getGenre)
                .filter(g -> g != null && !g.isBlank())
                .distinct().count();
        if (genreCount >= 5) earned.add( Badges.GENRE_COLLECTOR);

        long tvCount = reviews.stream()
                .filter(r -> "tv".equalsIgnoreCase(r.getType()))
                .count();
        if (tvCount >= 10) earned.add( Badges.TV_JUNKIE);

        long movieCount = reviews.stream()
                .filter(r -> "movie".equalsIgnoreCase(r.getType()))
                .count();
        if (movieCount >= 20) earned.add( Badges.MOVIE_MANIAC);

        return earned;
    }

    public List<BadgeViewModel> getAllBadgesWithStatus(String username) {
        Set<Badges> earned = evaluateBadges(username);
        List<BadgeViewModel> result = new ArrayList<>();
        for (Badges badge : Badges.values()) {
            result.add(new BadgeViewModel(badge, earned.contains(badge)));
        }
        return result;
    }

}
