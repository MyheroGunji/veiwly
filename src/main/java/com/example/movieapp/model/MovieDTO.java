package com.example.movieapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MovieDTO {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private double voteAverage;
    private String genre;
    private Integer duration;

}
