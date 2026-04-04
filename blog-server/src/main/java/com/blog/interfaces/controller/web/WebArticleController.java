package com.blog.interfaces.controller.web;

import com.blog.application.service.ArticleService;
import com.blog.domain.common.PageResult;
import com.blog.domain.common.Result;
import com.blog.domain.entity.Article;
import com.blog.interfaces.dto.response.ArchiveVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台文章控制器
 */
@RestController
@RequestMapping("/api/web/articles")
@RequiredArgsConstructor
public class WebArticleController {

    private final ArticleService articleService;

    /**
     * 文章列表
     */
    @GetMapping
    public Result<PageResult<Article>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Article> result = articleService.getArticleList(categoryId, 1, null, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        articleService.incrementViewCount(id);
        return Result.success(article);
    }

    /**
     * 热门文章
     */
    @GetMapping("/hot")
    public Result<List<Article>> hotArticles(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(articleService.getHotArticles(limit));
    }

    /**
     * 归档
     */
    @GetMapping("/archive")
    public Result<List<ArchiveVO>> archive() {
        return Result.success(articleService.getArchiveList());
    }
}
