package com.example.movieapp.model;

import jakarta.persistence.ManyToOne;

public class User {

    @ManyToOne
    private User user;

}

