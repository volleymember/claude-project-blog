package com.blog.interfaces.controller.web;

import com.blog.application.service.TagService;
import com.blog.domain.common.Result;
import com.blog.domain.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台标签控制器
 */
@RestController
@RequestMapping("/api/web/tags")
@RequiredArgsConstructor
public class WebTagController {

    private final TagService tagService;

    /**
     * 标签列表
     */
    @GetMapping
    public Result<List<Tag>> list() {
        return Result.success(tagService.getAllTags());
    }
}
