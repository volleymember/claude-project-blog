package com.blog.infrastructure.persistence.mapper;

import com.blog.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章Mapper
 */
@Mapper
public interface ArticleMapper {

    Article selectById(@Param("id") Long id);

    Article selectBySlug(@Param("slug") String slug);

    List<Article> selectList(@Param("categoryId") Long categoryId,
                             @Param("status") Integer status,
                             @Param("keyword") String keyword,
                             @Param("offset") Integer offset,
                             @Param("limit") Integer limit);

    Long selectCount(@Param("categoryId") Long categoryId,
                     @Param("status") Integer status,
                     @Param("keyword") String keyword);

    List<Article> selectHotArticles(@Param("limit") Integer limit);

    List<Article> selectByCategoryId(@Param("categoryId") Long categoryId,
                                     @Param("offset") Integer offset,
                                     @Param("limit") Integer limit);

    int insert(Article article);

    int update(Article article);

    int deleteById(@Param("id") Long id);

    int updateViewCount(@Param("id") Long id, @Param("count") Long count);

    int updateLikeCount(@Param("id") Long id, @Param("count") Integer count);

    int updateCommentCount(@Param("id") Long id, @Param("count") Integer count);

    Long selectTotalCount(@Param("status") Integer status);

    List<Article> selectArchiveList();
}
