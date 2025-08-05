package com.example.movieapp.repository;

import com.example.movieapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUsername(String username);

    // ユーザーごとのレビュー数をカウント
    long countByUsername(String username);

    // ユーザーごとの評価の平均を取得
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.username = :username")
    Double findAverageRatingByUsername(String username);

    // ジャンル別の平均評価
    @Query("SELECT r.genre, AVG(r.rating) FROM Review r WHERE r.username = :username GROUP BY r.genre")
    List<Object[]> averageRatingByGenre(String username);

    // ジャンル別の視聴時間（ここでは仮に1本 = 2時間固定と仮定）
    @Query("SELECT r.genre, COUNT(r) * 2 FROM Review r WHERE r.username = :username GROUP BY r.genre")
    List<Object[]> totalHoursByGenre(String username);

}
