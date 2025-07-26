package com.fun.novel.service.theme;

import com.fun.novel.dto.CreateNovelLogType;
import com.fun.novel.entity.ComponentStyle;
import com.fun.novel.entity.ThemeEntity;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class ThemeFileRetriever {
    private final Data data;

    ThemeFileRetriever(Data data) {
        this.data = data;
    }
    public String getComponentConfig(String brand, String name) {
        String themePath = data.getThemePath(true);
        ThemeEntity themeEntity = data.getThemeEntity(brand, ComponentStyle.name2Type(name));
        ComponentStyle componentStyle = themeEntity.getComponentStyle();
        String componentName = componentStyle.getName();
        String componentNode = componentStyle.getNode();
        String componentDir = themePath + File.separator + componentNode + File.separator + brand + File.separator + "config.json";
        Path componentPath = java.nio.file.Paths.get(componentDir);
        if (!java.nio.file.Files.exists(componentPath)) {
            return componentPath + " is missing";
        }
        //ComponentStyle{name=66, type=Pay66, id=1, style=1}
        try {
            return new String(java.nio.file.Files.readAllBytes(componentPath), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return componentPath + " is not readable";
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
