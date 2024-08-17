package com.coresaken.mcserverlist.database.repository;

import com.coresaken.mcserverlist.database.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByOwnerId(Long userId);
    List<Banner> findByStatus(Banner.Status status);

    List<Banner> findByStatusAndSize(Banner.Status status, Banner.Size size);
}
