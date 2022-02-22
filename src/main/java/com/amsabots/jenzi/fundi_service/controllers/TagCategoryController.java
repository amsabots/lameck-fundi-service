package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.entities.CategoryTags;
import com.amsabots.jenzi.fundi_service.repos.TagCategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import java.util.List;

@RestController
@RequestMapping("/tag-category")

public class TagCategoryController {
    @Autowired
    private TagCategoryRepo repo;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryTags> createTagRelationship(@RequestBody CategoryTags categoryTags) {
        CategoryTags c;
        c = repo.findByAccountIdAndTagId(categoryTags.getAccountId(), categoryTags.getTagId());
        if (null == c)
            c = repo.save(categoryTags);
        return ResponseEntity.ok(c);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{userId}")
    public ResponseEntity<List<CategoryTags>> getTagsForUser(@PathVariable long userId) {
        List<CategoryTags> categoryTags = repo.findAllByAccountId(userId);
        return ResponseEntity.ok(categoryTags);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{tagId}/{accountId}")
    public ResponseEntity<String> deleteEntryById(@PathVariable long tagId, @PathVariable long accountId) {
        repo.deleteByTagIdAAndAccountId(tagId, accountId);
        return ResponseEntity.ok("removed");
    }
}
