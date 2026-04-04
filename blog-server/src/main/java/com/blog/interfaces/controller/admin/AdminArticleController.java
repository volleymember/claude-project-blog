package com.blog.interfaces.controller.admin;

import com.blog.application.service.ArticleService;
import com.blog.domain.common.PageResult;
import com.blog.domain.common.Result;
import com.blog.domain.entity.Article;
import com.blog.infrastructure.security.UserPrincipal;
import com.blog.interfaces.dto.request.ArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 后台文章管理控制器
 */
@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    /**
     * 文章列表
     */
    @GetMapping
    public Result<PageResult<Article>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Article> result = articleService.getArticleList(categoryId, status, keyword, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Long id) {
        return Result.success(articleService.getArticleById(id));
    }

    /**
     * 创建文章
     */
    @PostMapping
    public Result<Void> create(@RequestBody ArticleRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        articleService.createArticle(request, principal.getUserId());
        return Result.success();
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ArticleRequest request) {
        articleService.updateArticle(id, request);
        return Result.success();
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }
}
