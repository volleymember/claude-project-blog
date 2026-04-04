package com.blog.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.blog.application.service.ArticleService;
import com.blog.domain.common.BusinessException;
import com.blog.domain.common.Constants;
import com.blog.domain.common.PageResult;
import com.blog.domain.entity.Article;
import com.blog.domain.entity.Tag;
import com.blog.domain.repository.ArticleRepository;
import com.blog.domain.repository.TagRepository;
import com.blog.infrastructure.persistence.mapper.ArticleTagMapper;
import com.blog.interfaces.dto.request.ArticleRequest;
import com.blog.interfaces.dto.response.ArchiveVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagMapper articleTagMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult<Article> getArticleList(Long categoryId, Integer status, String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Article> articles = articleRepository.findList(categoryId, status, keyword, offset, pageSize);
        Long total = articleRepository.count(categoryId, status, keyword);

        // 填充标签信息
        articles.forEach(this::fillTags);

        return PageResult.of(articles, total, pageNum, pageSize);
    }

    @Override
    public Article getArticleById(Long id) {
        Article article = articleRepository.findById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        fillTags(article);
        return article;
    }

    @Override
    public Article getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        fillTags(article);
        return article;
    }

    @Override
    public List<Article> getHotArticles(Integer limit) {
        // 先从缓存获取
        String key = Constants.REDIS_HOT_ARTICLES;
        @SuppressWarnings("unchecked")
        List<Article> articles = (List<Article>) redisTemplate.opsForValue().get(key);

        if (articles == null) {
            articles = articleRepository.findHotArticles(limit);
            articles.forEach(this::fillTags);
            redisTemplate.opsForValue().set(key, articles, 1, TimeUnit.HOURS);
        }

        return articles;
    }

    @Override
    @Transactional
    public void createArticle(ArticleRequest request, Long authorId) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(StrUtil.isBlank(request.getSlug()) ?
                cn.hutool.core.util.IdUtil.simpleUUID().substring(0, 8) : request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setCategoryId(request.getCategoryId());
        article.setIsTop(request.getIsTop());
        article.setStatus(request.getStatus());
        article.setSeoKeywords(request.getSeoKeywords());
        article.setSeoDescription(request.getSeoDescription());
        article.setAuthorId(authorId);
        article.setViewCount(0L);
        article.setLikeCount(0);
        article.setCommentCount(0);

        if (request.getStatus() == Constants.ARTICLE_STATUS_PUBLISHED) {
            article.setPublishTime(LocalDateTime.now());
        }

        articleRepository.save(article);

        // 保存标签关联
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            articleTagMapper.insertBatch(article.getId(), request.getTagIds());
        }
    }

    @Override
    @Transactional
    public void updateArticle(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setCategoryId(request.getCategoryId());
        article.setIsTop(request.getIsTop());
        article.setStatus(request.getStatus());
        article.setSeoKeywords(request.getSeoKeywords());
        article.setSeoDescription(request.getSeoDescription());

        // 草稿发布时设置发布时间
        if (article.getStatus() != Constants.ARTICLE_STATUS_PUBLISHED
                && request.getStatus() == Constants.ARTICLE_STATUS_PUBLISHED) {
            article.setPublishTime(LocalDateTime.now());
        }

        articleRepository.update(article);

        // 更新标签关联
        articleTagMapper.deleteByArticleId(id);
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            articleTagMapper.insertBatch(id, request.getTagIds());
        }
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        articleRepository.deleteById(id);
        articleTagMapper.deleteByArticleId(id);
    }

    @Override
    public void incrementViewCount(Long id) {
        // 使用 Redis 计数，定时任务同步到数据库
        String key = Constants.REDIS_ARTICLE_VIEW + id;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
    }

    @Override
    public List<ArchiveVO> getArchiveList() {
        List<Article> articles = articleRepository.findArchiveList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        return articles.stream().map(article -> {
            ArchiveVO vo = new ArchiveVO();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setSlug(article.getSlug());
            vo.setCreateTime(article.getCreateTime());
            vo.setYearMonth(article.getCreateTime().format(formatter));
            return vo;
        }).collect(Collectors.toList());
    }

    private void fillTags(Article article) {
        if (article != null) {
            List<Tag> tags = tagRepository.findByArticleId(article.getId());
            article.setTags(tags);
        }
    }
}
