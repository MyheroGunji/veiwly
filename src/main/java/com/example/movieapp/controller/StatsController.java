package com.example.movieapp.controller;

import com.example.movieapp.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public String showStats(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();

        model.addAttribute("totalWatchTime", statsService.getTotalWatchTime(username));
        model.addAttribute("averageRating", statsService.getAverageRating(username));
        model.addAttribute("favoriteGenre", statsService.getFavoriteGenre(username));
        model.addAttribute("totalReviews", statsService.getTotalReviews(username));

        // ジャンルごとの本数
        Map<String, Long> genreCounts = statsService.getGenreCounts(username);
        model.addAttribute("genreLabels", genreCounts.keySet());
        model.addAttribute("genreData", genreCounts.values());

        // ジャンルごとの平均評価
        Map<String, Double> genreAvgRatings = statsService.getGenreAverageRatings(username);
        List<Double> avgRatings = genreCounts.keySet().stream()
                .map(genre -> genreAvgRatings.getOrDefault(genre, 0.0))
                .collect(Collectors.toList());
        model.addAttribute("genreAverageRatings", avgRatings);

        Map<YearMonth, Long> monthlyCount = statsService.getMonthlyReviewCount(username);
        Map<YearMonth, Integer> monthlyTime = statsService.getMonthlyWatchTime(username);
        Map<String, Long> typeRatio = statsService.getTypeRatio(username);

// グラフ用のデータ形式に整形（例: ["2025-06", "2025-07"]）
        List<String> monthLabels = monthlyCount.keySet().stream()
                .map(ym -> ym.toString()) // "2025-08"
                .collect(Collectors.toList());

        List<Long> monthlyCounts = new ArrayList<>(monthlyCount.values());
        List<Integer> monthlyTimes = new ArrayList<>(monthlyTime.values());

        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthlyCounts", monthlyCounts);
        model.addAttribute("monthlyTimes", monthlyTimes);

        model.addAttribute("typeLabels", new ArrayList<>(typeRatio.keySet())); // ["movie", "tv"]
        model.addAttribute("typeData", new ArrayList<>(typeRatio.values()));   // [12, 8]


        return "mypage/stats";
    }
}
