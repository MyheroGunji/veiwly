package com.example.movieapp.controller;

import com.example.movieapp.model.BadgeViewModel;
import com.example.movieapp.model.Badges;
import com.example.movieapp.model.Review;
import com.example.movieapp.model.WatchlistItem;
import com.example.movieapp.repository.ReviewRepository;
import com.example.movieapp.repository.WatchlistRepository;
import com.example.movieapp.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final ReviewRepository reviewRepository;
    private final WatchlistRepository watchlistRepository;

    public MypageController(ReviewRepository reviewRepository, WatchlistRepository watchlistRepository) {
        this.reviewRepository = reviewRepository;
        this.watchlistRepository = watchlistRepository;
    }

    @GetMapping({"", "/"})
    public String showMypage() {
        return "mypage";
    }


    @GetMapping("/reviews")
    public String viewReviews(Model model, Principal principal) {
        String username = principal.getName(); // ログイン中のユーザー
        List<Review> reviews = reviewRepository.findByUsername(username);
        model.addAttribute("reviews", reviews);
        return "mypage/reviews";
    }

    @GetMapping("/watchlist")
    public String viewWatchlist(Model model, Principal principal) {
        String username = principal.getName();
        List<WatchlistItem> watchlist = watchlistRepository.findByUsername(username);
        model.addAttribute("watchlist", watchlist);
        return "mypage/watchlist";
    }

    @Autowired
    private BadgeService badgeService;

    @GetMapping("/badges")
    public String showBadges(@AuthenticationPrincipal UserDetails user, Model model) {
        List<BadgeViewModel> badgeList = badgeService.getAllBadgesWithStatus(user.getUsername());
        model.addAttribute("badgeList", badgeList);
        model.addAttribute("hasEarnedBadges", badgeList.stream().anyMatch(BadgeViewModel::isEarned));

        return "mypage/badges";
    }


}

