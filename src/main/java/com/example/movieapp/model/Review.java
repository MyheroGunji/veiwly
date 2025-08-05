package com.example.movieapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 255)
    private String posterPath;
    @Column(nullable = false)
    private Double rating;
    private String tmdbId;
    private String title;
    private String username;
    private String content; // review content
    private LocalDateTime createdAt; //　created date
    private String type; // "movie" or "tv"

    @Column(length = 50)
    private String genre;// 映画のジャンル（複数ジャンルある場合はメイン1つだけでもOK）
    private Integer duration; // 映画の長さ（時間または分数）

}
