package com.blog.application.service;

import com.blog.domain.common.PageResult;
import com.blog.domain.entity.Article;
import com.blog.interfaces.dto.request.ArticleRequest;
import com.blog.interfaces.dto.response.ArchiveVO;

import java.util.List;

/**
 * 文章服务接口
 */
public interface ArticleService {

    PageResult<Article> getArticleList(Long categoryId, Integer status, String keyword, Integer pageNum, Integer pageSize);

    Article getArticleById(Long id);

    Article getArticleBySlug(String slug);

    List<Article> getHotArticles(Integer limit);

    void createArticle(ArticleRequest request, Long authorId);

    void updateArticle(Long id, ArticleRequest request);

    void deleteArticle(Long id);

    void incrementViewCount(Long id);

    List<ArchiveVO> getArchiveList();
}
