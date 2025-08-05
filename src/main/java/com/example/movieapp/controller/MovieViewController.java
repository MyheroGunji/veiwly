package com.example.movieapp.controller;

import com.example.movieapp.model.MovieDTO;
import com.example.movieapp.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieViewController {

    private final MovieService movieService;

    public MovieViewController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<MovieDTO> movies = movieService.searchMovies(query);
        model.addAttribute("movies", movies);
        model.addAttribute("type", "movie"); // ← 追加
        return "index"; // Thymeleafテンプレートへ渡す
    }

    @GetMapping
    public String movieHome(Model model) {
        List<MovieDTO> popularMovies = movieService.fetchPopularMovies();
        model.addAttribute("popularMovies", popularMovies);
        model.addAttribute("type", "movie"); // ← 追加（念のためTop Picksでも渡す）
        return "index"; // Top Picks 表示ありのトップページ
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("type", "movie"); // ← 追加
        return "index";
    }
}
