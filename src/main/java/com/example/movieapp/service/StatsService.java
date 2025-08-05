package com.example.movieapp.service;

import com.example.movieapp.model.Review;
import com.example.movieapp.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final ReviewRepository reviewRepository;

    public StatsService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public long getTotalWatchTime(String username) {
        return reviewRepository.findByUsername(username)
                .stream()
                .mapToLong(Review::getDuration)
                .sum();
    }

    public double getAverageRating(String username) {
        return Optional.ofNullable(reviewRepository.findAverageRatingByUsername(username)).orElse(0.0);
    }

    public Map<String, Long> getGenreCounts(String username) {
        return reviewRepository.findByUsername(username)
                .stream()
                .collect(Collectors.groupingBy(
                        review -> (review.getGenre() == null || review.getGenre().isBlank()) ? "Unknown" : review.getGenre(),
                        Collectors.counting()
                ));
    }


    public String getFavoriteGenre(String username) {
        return getGenreCounts(username).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    public long getTotalReviews(String username) {
        return reviewRepository.findByUsername(username).size();
    }

    public Map<String, Double> getGenreAverageRatings(String username) {
        List<Review> reviews = reviewRepository.findByUsername(username);

        return reviews.stream()
                .collect(Collectors.groupingBy(
                        r -> (r.getGenre() == null || r.getGenre().isBlank()) ? "Unknown" : r.getGenre(),
                        Collectors.averagingDouble(Review::getRating)
                ));
    }

    public Map<YearMonth, Long> getMonthlyReviewCount(String username) {
        return reviewRepository.findByUsername(username).stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getCreatedAt()),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    public Map<YearMonth, Integer> getMonthlyWatchTime(String username) {
        return reviewRepository.findByUsername(username).stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getCreatedAt()),
                        TreeMap::new,
                        Collectors.summingInt(r -> Optional.ofNullable(r.getDuration()).orElse(0))
                ));
    }

    public Map<String, Long> getTypeRatio(String username) {
        return reviewRepository.findByUsername(username).stream()
                .collect(Collectors.groupingBy(
                        r -> r.getType() == null ? "unknown" : r.getType().toLowerCase(),
                        Collectors.counting()
                ));
    }

}
