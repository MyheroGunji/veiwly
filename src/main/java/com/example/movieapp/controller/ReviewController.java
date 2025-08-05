package com.example.movieapp.controller;

import com.example.movieapp.model.MovieDTO;
import com.example.movieapp.model.Review;
import com.example.movieapp.repository.ReviewRepository;
import com.example.movieapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieService movieService;

    // GET: review投稿用のフォーム表示
    @GetMapping("/write")
    public String showReviewForm(@RequestParam String title,
                                 @RequestParam String posterPath,
                                 @RequestParam(required = false) String genre,
                                 @RequestParam("tmdbId") String tmdbId,
                                 @RequestParam(required = false) Integer duration,
                                 @RequestParam(required = false) String type,
                                 Model model) {

        System.out.println("=== showReviewForm called ===");
        System.out.println("title: " + title);
        System.out.println("posterPath: " + posterPath);
        System.out.println("genre param: " + genre);
        System.out.println("tmdbId param: " + tmdbId);
        System.out.println("duration param: " + duration);
        System.out.println("type param: " + type);

        Review review = new Review();
        review.setTitle(title);
        review.setTmdbId(tmdbId);
        review.setPosterPath(posterPath);
        review.setRating(5.0); // 仮の初期値

        if (tmdbId != null && type != null) {
            MovieDTO dto = new MovieDTO();
            try {
                dto.setId(Integer.parseInt(tmdbId));
                movieService.enrichMovieDetails(dto, type);
                review.setGenre(dto.getGenre());
                review.setDuration(dto.getDuration());
                review.setType(type);
            } catch (NumberFormatException e) {
                System.err.println("tmdbId is not a number: " + tmdbId);
                review.setGenre(genre != null ? genre : "Unknown");
                review.setDuration(duration != null ? duration : 0);
            } catch (Exception e) {
                System.err.println("Error in enrichMovieDetails: " + e.getMessage());
                e.printStackTrace();
                review.setGenre(genre != null ? genre : "Unknown");
                review.setDuration(duration != null ? duration : 0);
            }
        } else {
            review.setGenre(genre != null ? genre : "Unknown");
            review.setDuration(duration != null ? duration : 0);
        }

        model.addAttribute("review", review);
        return "review";
    }

    // 自分のレビュー一覧を表示（マイページ）
    @GetMapping("/reviews")
    public String showReviews(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<Review> reviews = reviewRepository.findByUsername(username);
        model.addAttribute("reviews", reviews);
        return "mypage/reviews";
    }

    // POST: レビュー保存
    @PostMapping("/save")
    public String saveReview(@ModelAttribute Review review,
                             @AuthenticationPrincipal UserDetails userDetails, Model model) {

        review.setUsername(userDetails.getUsername());
        review.setCreatedAt(LocalDateTime.now());

        System.out.println("=== Saving review ===");
        System.out.println("title: " + review.getTitle());
        System.out.println("posterPath: " + review.getPosterPath());
        System.out.println("content: " + review.getContent());
        System.out.println("username: " + review.getUsername());
        System.out.println("rating: " + review.getRating());
        System.out.println("genre: " + review.getGenre());
        System.out.println("duration: " + review.getDuration());

        reviewRepository.save(review);

        model.addAttribute("message", "Saved!");
        return "saved";
    }

    // GET: 編集フォームを表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null || !review.getUsername().equals(userDetails.getUsername())) {
            return "redirect:/review/reviews";
        }
        model.addAttribute("review", review);
        return "edit-review";
    }

    // POST: レビューを更新
    @PostMapping("/update")
    public String updateReview(@ModelAttribute Review review,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Review existing = reviewRepository.findById(review.getId()).orElse(null);
        if (existing == null || !existing.getUsername().equals(userDetails.getUsername())) {
            return "redirect:/review/reviews";
        }

        existing.setContent(review.getContent());
        existing.setRating(review.getRating());
        existing.setCreatedAt(LocalDateTime.now());

        // ⚠️ duration（nullなら0にする保険）
        existing.setDuration(review.getDuration() != null ? review.getDuration() : 0);

        // genre/typeも必要なら更新（任意）
        existing.setGenre(review.getGenre());
        existing.setType(review.getType());

        reviewRepository.save(existing);
        return "redirect:/review/reviews";
    }


    // GET: レビューを削除
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null || !review.getUsername().equals(userDetails.getUsername())) {
            return "redirect:/review/reviews";
        }
        reviewRepository.deleteById(id);
        return "redirect:/review/reviews";
    }
}
