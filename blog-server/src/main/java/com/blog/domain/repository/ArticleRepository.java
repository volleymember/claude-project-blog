package com.blog.domain.repository;

import com.blog.domain.entity.Article;

import java.util.List;

/**
 * 文章仓储接口
 */
public interface ArticleRepository {

    Article findById(Long id);

    Article findBySlug(String slug);

    List<Article> findList(Long categoryId, Integer status, String keyword, Integer offset, Integer limit);

    Long count(Long categoryId, Integer status, String keyword);

    List<Article> findHotArticles(Integer limit);

    void save(Article article);

    void update(Article article);

    void deleteById(Long id);

    void incrementViewCount(Long id, Long count);

    Long countTotal(Integer status);

    List<Article> findArchiveList();
}
