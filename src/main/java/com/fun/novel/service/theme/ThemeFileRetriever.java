package com.fun.novel.service.theme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.novel.dto.CreateNovelLogType;
import com.fun.novel.dto.ThemeNodeConfigDTO;
import com.fun.novel.entity.ComponentStyle;
import com.fun.novel.entity.ThemeEntity;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ThemeFileRetriever {
    private final Data data;
    private final String taskId = "--";
    private final String logTag = "ThemeFileRetriever";

    ThemeFileRetriever(Data data) {
        this.data = data;
    }

    public int countFilesInDirectory(String componentDir) {
        Path dirPath = java.nio.file.Paths.get(componentDir);

        if (!java.nio.file.Files.exists(dirPath)) {
            data.taskLogger.log(taskId, logTag + componentDir + " does not exist", CreateNovelLogType.ERROR);
            return 0;
        }

        if (!java.nio.file.Files.isDirectory(dirPath)) {
            data.taskLogger.log(taskId, logTag + componentDir + " is not a directory", CreateNovelLogType.ERROR);
            return 0;
        }

        try {
            // 方法1: 使用Files.list()计算文件数量
            long fileCount = java.nio.file.Files.list(dirPath)
                    .filter(path -> java.nio.file.Files.isRegularFile(path))
                    .count();
            return (int) fileCount;

        } catch (Exception e) {
            data.taskLogger.log(taskId, logTag + "Failed to count files in " + componentDir + ": " + e.getMessage(), CreateNovelLogType.ERROR);
            return 0;
        }
    }

    public ThemeNodeConfigDTO parseConfigContent(String content) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, ThemeNodeConfigDTO.class);
        } catch (Exception e) {
            // 记录日志或处理异常
            return null;
        }
    }

    public List<ThemeNodeConfigDTO> getPreComponentConfigs(String brand, String name) {
        String themePath = data.getThemePath(true);
        ThemeEntity themeEntity = data.getThemeEntity(brand, ComponentStyle.name2Type(name));
        ComponentStyle componentStyle = themeEntity.getComponentStyle();
        String componentName = componentStyle.getName();
        String componentNode = componentStyle.getNode();
        String componentDir = themePath + File.separator + componentNode + File.separator + "astyle" + File.separator + "config" + File.separator;
        int count = countFilesInDirectory(componentDir);
        List<ThemeNodeConfigDTO> result = new ArrayList<>();
        if (count <= 0) {
            data.taskLogger.log(taskId, logTag + "No config files found in " + componentDir, CreateNovelLogType.ERROR);
            return result;
        }
        data.taskLogger.log(taskId, logTag + " files: " + count, CreateNovelLogType.ERROR);
        for (int i = 1; i <= count; i++) {
            ThemeNodeConfigDTO config = getComponentConfig(componentDir + i + ".json");
            if (config != null) {
                result.add(config);
            }
        }
        return result;
    }

    public ThemeNodeConfigDTO getAppliedComponentConfig(String brand, String name) {
        String themePath = data.getThemePath(true);
        ThemeEntity themeEntity = data.getThemeEntity(brand, ComponentStyle.name2Type(name));
        ComponentStyle componentStyle = themeEntity.getComponentStyle();
        String componentName = componentStyle.getName();
        String componentNode = componentStyle.getNode();
        String componentDir = themePath + File.separator + componentNode + File.separator + brand + File.separator + "config.json";
        return getComponentConfig(componentDir);
    }

    public ThemeNodeConfigDTO getComponentConfig(String sourceDir) {
        Path componentPath = java.nio.file.Paths.get(sourceDir);
        if (!java.nio.file.Files.exists(componentPath)) {
            data.taskLogger.log(taskId, logTag + componentPath + " is missing", CreateNovelLogType.ERROR);
            return null;
        }
        //ComponentStyle{name=66, type=Pay66, id=1, style=1}
        try {
            String content = new String(java.nio.file.Files.readAllBytes(componentPath), java.nio.charset.StandardCharsets.UTF_8);
            // 转换为DTO对象
            ThemeNodeConfigDTO configDTO = parseConfigContent(content);
            return configDTO;
        } catch (Exception e) {
            data.taskLogger.log(taskId, logTag + componentPath + " is not readable", CreateNovelLogType.ERROR);
            return null;
        }
    }
    public String getComponentLess(String brand, String name, int style) {
        String themePath = data.getThemePath(true);
        ThemeEntity themeEntity = data.getThemeEntity(brand, ComponentStyle.name2Type(name));
        ComponentStyle componentStyle = themeEntity.getComponentStyle();
        String componentName = componentStyle.getName();
        String componentNode = componentStyle.getNode();
        String lessDir = themePath + File.separator + componentNode + File.separator + "astyle" + File.separator + componentName + File.separator + "1" + File.separator + style + ".less";
        if (style < 1) { // 样式版本小于1就用自身的 less
            lessDir = themePath + File.separator + componentNode + File.separator + brand + File.separator + componentName + ".less";
        }
        Path lessPath = java.nio.file.Paths.get(lessDir);
        if (!java.nio.file.Files.exists(lessPath)) {
            return lessPath + " is missing";
        }
        //ComponentStyle{name=66, type=Pay66, id=1, style=1}
        try {
            return new String(java.nio.file.Files.readAllBytes(lessPath), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return lessPath + " is not readable";
        }
    }
    public String getComponentSubLess(String brand, String root, String name, int id, int style) {
        String themePath = data.getThemePath(true);
        ThemeEntity themeEntity = data.getThemeEntity(brand, ComponentStyle.name2Type(root));
        ComponentStyle rootStyle = themeEntity.getComponentStyle();
        String rootName = rootStyle.getName();
        String rootNode = rootStyle.getNode();

        String lessDir = themePath + File.separator + rootNode + File.separator + "astyle" + File.separator + name + File.separator + id + File.separator + style + ".less";
        if (style < 1) { // 样式版本小于1就用自身的 less
            lessDir = themePath + File.separator + rootNode + File.separator + brand + File.separator + name + "-v" + id + ".less";
            if (id < 2) {
                lessDir = themePath + File.separator + rootNode + File.separator + brand + File.separator + name + ".less";
            }
        }
        Path lessPath = java.nio.file.Paths.get(lessDir);
        if (!java.nio.file.Files.exists(lessPath)) {
            return lessPath + " is missing";
        }
        //ComponentStyle{name=66, type=Pay66, id=1, style=1}
        try {
            return new String(java.nio.file.Files.readAllBytes(lessPath), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return lessPath + " is not readable";
        }
    }
}
