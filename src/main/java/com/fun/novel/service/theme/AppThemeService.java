package com.fun.novel.service.theme;

import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.dto.ThemeNodeConfigDTO;

import java.util.List;

public interface AppThemeService {
    void processThemeFile(String taskId, String buildCode, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay);
    List<ThemeNodeConfigDTO> getPreComponentConfigs(String brand, String name);
    ThemeNodeConfigDTO getComponentConfig(String brand, String name);
    String getComponentLess(String brand, String name, int style);
    String getComponentSubLess(String brand, String root, String name, int id, int style);
} 