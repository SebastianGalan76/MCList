package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.server.ratings.RatingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingCategoryRepository extends JpaRepository<RatingCategory, Long> {
}
