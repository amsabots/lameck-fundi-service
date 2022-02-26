package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.CategoryTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagCategoryRepo extends JpaRepository<CategoryTags, Long> {

    CategoryTags findByAccountIdAndTagId(long accountId, long tagId);
    List<CategoryTags> findAllByAccountId(long id);
    @Query("delete from CategoryTags u where u.accountId=?2 and u.tagId=?1")
    void deleteByTagIdAndAccountId(long tagId, long accountId);
}
