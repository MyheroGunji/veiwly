package com.example.movieapp.controller;

import com.example.movieapp.model.MovieDTO;
import com.example.movieapp.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tv")
public class TVViewController {

    private final MovieService movieService;

    public TVViewController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String tvHome(Model model) {
        List<MovieDTO> popularTVShows = movieService.fetchPopularTVShows();  // TV用の人気取得メソッド
        model.addAttribute("popularMovies", popularTVShows);
        model.addAttribute("type", "tv"); // ← 追加
        return "tv"; // tv.htmlで表示
    }

    @GetMapping("/search")
    public String searchTV(@RequestParam("query") String query, Model model) {
        List<MovieDTO> tvShows = movieService.searchTVShows(query);
        model.addAttribute("movies", tvShows); // MovieDTOのままでもOK
        model.addAttribute("type", "tv"); // ← 追加
        return "tv"; // tv.html に表示
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("type", "tv"); // ← 追加
        return "tv";
    }
}
