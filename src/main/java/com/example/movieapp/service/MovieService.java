package com.example.movieapp.service;

import com.example.movieapp.model.MovieDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tmdb.api.key}")
    private String apiKey;

    public MovieService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .build();
    }

    public List<MovieDTO> searchMovies(String query) {
        String normalizedQuery = normalizeQuery(query);
        List<MovieDTO> movies = fetchAndMapResults("/search/movie", normalizedQuery);

        for (MovieDTO dto : movies) {
            System.out.println("Fetching details for: /movie/" + dto.getId());  // ← ⭐ ここに入れる！

            enrichMovieDetails(dto, "movie");  // ジャンルと時間をここで埋める
        }

        return movies;
    }

    public List<MovieDTO> searchTVShows(String query) {
        String normalizedQuery = normalizeQuery(query);
        List<MovieDTO> tvShows = fetchAndMapResults("/search/tv", normalizedQuery);

        for (MovieDTO dto : tvShows) {
            enrichMovieDetails(dto, "tv");  // TV用ジャンル・時間
        }

        return tvShows;
    }


    public List<MovieDTO> fetchPopularMovies() {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            List<MovieDTO> list = new ArrayList<>();
            for (JsonNode node : results) {
                MovieDTO dto = new MovieDTO();
                dto.setId(node.path("id").asInt());
                dto.setTitle(node.path("title").asText());
                dto.setPosterPath("https://image.tmdb.org/t/p/w500" + node.path("poster_path").asText());
                dto.setReleaseDate(node.path("release_date").asText());
                dto.setVoteAverage(node.path("vote_average").asDouble());
                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<MovieDTO> fetchPopularTVShows() {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tv/popular")
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            List<MovieDTO> list = new ArrayList<>();
            for (JsonNode node : results) {
                MovieDTO dto = new MovieDTO();
                dto.setId(node.path("id").asInt());
                dto.setTitle(node.path("name").asText());
                dto.setOverview(node.path("overview").asText());
                dto.setPosterPath("https://image.tmdb.org/t/p/w500" + node.path("poster_path").asText());
                dto.setReleaseDate(node.path("first_air_date").asText());
                dto.setVoteAverage(node.path("vote_average").asDouble());
                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String normalizeQuery(String query) {
        if (query == null) return "";
        return query.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase();
    }

    private List<MovieDTO> fetchAndMapResults(String path, String query) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("api_key", apiKey)
                            .queryParam("query", query)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            List<MovieDTO> list = new ArrayList<>();
            for (JsonNode node : results) {
                MovieDTO dto = new MovieDTO();
                dto.setId(node.path("id").asInt());
                dto.setTitle(node.path("title").asText(node.path("name").asText()));
                dto.setOverview(node.path("overview").asText());
                dto.setPosterPath("https://image.tmdb.org/t/p/w500" + node.path("poster_path").asText());
                dto.setReleaseDate(node.path("release_date").asText(node.path("first_air_date").asText()));
                dto.setVoteAverage(node.path("vote_average").asDouble());
                list.add(dto);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 🔧 正しい位置に置く enrichMovieDetails
    public void enrichMovieDetails(MovieDTO dto, String type) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/" + type + "/" + dto.getId())
                            .queryParam("api_key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode node = objectMapper.readTree(response);

            JsonNode genres = node.path("genres");
            if (genres.isArray() && genres.size() > 0) {
                dto.setGenre(genres.get(0).path("name").asText());
            }

            if (type.equals("movie")) {
                dto.setDuration(node.path("runtime").asInt(0));
            } else {
                JsonNode episodes = node.path("episode_run_time");
                dto.setDuration(episodes.isArray() && episodes.size() > 0 ? episodes.get(0).asInt() : 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
