package com.example.movieapp.controller;

import com.example.movieapp.model.MovieDTO;
import com.example.movieapp.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieApiController {

    private final MovieService movieService;

    public MovieApiController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchJson(@RequestParam("query") String query) {
        List<MovieDTO> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }
}
