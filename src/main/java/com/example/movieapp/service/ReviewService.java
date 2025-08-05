package com.example.movieapp.service;

import com.example.movieapp.model.Review;
import com.example.movieapp.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public void saveReview(Review review) {
        repository.save(review);
    }

    public List<Review> getReviewsByUser(String userId) {
        return repository.findByUsername(userId);
    }
}
