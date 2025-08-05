package com.example.movieapp.service;

import com.example.movieapp.model.WatchlistItem;
import com.example.movieapp.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository repository;

    public WatchlistService(WatchlistRepository repository) {
        this.repository = repository;
    }

    public void addToWatchlist(WatchlistItem item) {
        repository.save(item);
    }

    public List<WatchlistItem> getWatchlistForUser(String username) {
        return repository.findByUsername(username);
    }
}

