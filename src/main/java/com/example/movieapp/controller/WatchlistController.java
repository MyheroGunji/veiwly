package com.example.movieapp.controller;

import com.example.movieapp.model.WatchlistItem;
import com.example.movieapp.repository.WatchlistRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistRepository watchlistRepository;

    public WatchlistController(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @PostMapping("/add")
    public String addToWatchlist(@RequestParam String title,
                                 @RequestParam String posterPath,
                                 @RequestParam double voteAverage,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();


        WatchlistItem item = new WatchlistItem();
        item.setTitle(title);
        item.setPosterPath(posterPath);
        item.setVoteAverage(voteAverage);
        item.setUsername(username);

        watchlistRepository.save(item);
        return "redirect:/mypage/watchlist";  // mypageにある一覧ページへリダイレクト
    }

    @GetMapping("/mylist")
    public String viewMyWatchlist(Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        List<WatchlistItem> items = watchlistRepository.findByUsername(userDetails.getUsername());
        model.addAttribute("watchlist", items);
        return "mypage/watchlist";  // このHTMLに表示
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public String removeFromWatchlistAjax(@PathVariable Long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        return watchlistRepository.findById(id)
                .filter(item -> item.getUsername().equals(username))
                .map(item -> {
                    watchlistRepository.delete(item);
                    return "success";
                })
                .orElse("not_found");
    }

}
