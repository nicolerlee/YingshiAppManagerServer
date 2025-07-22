package com.fun.novel.service;

import com.fun.novel.dto.CreateNovelAppRequest;
import java.util.List;

public interface AppThemeService {
    void processThemeFile(String taskId, String buildCode, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay);
} 