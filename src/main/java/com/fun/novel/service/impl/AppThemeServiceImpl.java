package com.fun.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.entity.*;
import com.fun.novel.mapper.AppThemeMapper;
import com.fun.novel.mapper.Pay6Mapper;
import com.fun.novel.mapper.PayBoard3Mapper;
import com.fun.novel.service.AppCommonConfigService;
import com.fun.novel.service.AppThemeService;
import com.fun.novel.utils.CreateNovelTaskLogger;
import com.fun.novel.dto.CreateNovelLogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppThemeServiceImpl implements AppThemeService {

    @Autowired
    private CreateNovelTaskLogger taskLogger;

    @Autowired
    private final Pay6Mapper pay6Mapper;

    @Value("${build.workPath}")
    private String buildWorkPath;

    private static final int FILE_STEP_DELAY_MS = 1000;
    private static final String miniConfigWebPath = "src" + File.separator + "appConfig" + File.separator + "web";
    private static final String miniConfigThemePath = "src" + File.separator + "appConfig" + File.separator + "theme";

    public Pay6 getPay6(String brand) {
        LambdaQueryWrapper<Pay6> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pay6::getBrand, brand);
        return pay6Mapper.selectOne(wrapper);
    }

    void overwriteConfigJson(String logTag, String taskId, String brand, ComponentStyle owner, List<ComponentStyle> css) {
        String destDir = buildWorkPath + File.separator + miniConfigThemePath + File.separator + owner.getName() + File.separator + brand + File.separator + "config.json";
        Path destPath = java.nio.file.Paths.get(destDir);
        if (java.nio.file.Files.exists(destPath)) {
            // taskLogger.log(taskId, logTag + "配置文件 " + destPath + " 已存在，不用生成", CreateNovelLogType.INFO); return;
        }
        taskLogger.log(taskId, logTag + "即将生成配置文件 " + destPath, CreateNovelLogType.INFO);


        // 构建符合格式的 Map
        Map<String, Object> root = new HashMap<>();
        Map<String, String> components = new HashMap<>();
        for (ComponentStyle cs : css) {
            // 假设 ComponentStyle 有 getName 和 getId 方法
            components.put(cs.getName(), String.valueOf(cs.getId()));
        }
        root.put("components", components);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 Map 转换为 JSON 字符串
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            taskLogger.log(taskId, logTag + "写到配置文件 jsonContent:" + jsonContent, CreateNovelLogType.INFO);

            // 写入文件
            java.nio.file.Files.write(destPath, jsonContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            taskLogger.log(taskId, logTag + "配置文件 " + destPath + " 生成成功", CreateNovelLogType.INFO);
        } catch (Exception e) {
            taskLogger.log(taskId, logTag + "配置文件 " + destPath + " 生成失败: " + e.getMessage(), CreateNovelLogType.ERROR);
        }
    }

    void processPay6(String logTag, String taskId, String brand, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay) {
        taskLogger.log(taskId, logTag + "processPay6 brand=" + brand, CreateNovelLogType.INFO);
        Pay6 pay6 = getPay6(brand);
        if (pay6 == null) {
            taskLogger.log(taskId, logTag + "pay6数据为空，啥也不做, brand=" + brand, CreateNovelLogType.INFO);
            return;
        }
        taskLogger.log(taskId, logTag + "获取到pay6", CreateNovelLogType.INFO);
        List<ComponentStyle> css = pay6.buildComponentStyles();
        overwriteConfigJson(logTag, taskId, brand, pay6.getComponentStyle(), css);
    }

    @Override
    public void processThemeFile(String taskId, String brand, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay) {
        processPay6("[2-2-1]", taskId, brand, baseConfig, rollbackActions, withLogAndDelay);
    }

    private void overwriteThemeFile(String taskId, String brand, List<Runnable> rollbackActions, boolean withLogAndDelay) {
        /*AppTheme appTheme = this.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AppTheme>().eq("brand", brand));
        PayDlg payDlg = appCommonConfigService.getPayDlg(brand);
        if (payDlg == null) {
            taskLogger.log(taskId, "[2-2-1-0] theme数据为空，啥也不做, brand=" + brand, CreateNovelLogType.INFO);
            return;
        }
        taskLogger.log(taskId, "[2-2-1-1] 获取到apptheme", CreateNovelLogType.INFO);
        // add theme file
        String themeFolderStr = buildWorkPath + File.separator + miniConfigPath + File.separator + "theme";
        java.nio.file.Path themeFolderPath = java.nio.file.Paths.get(themeFolderStr);
        try {
            // 判断文件夹是否存在
            if (!java.nio.file.Files.exists(themeFolderPath)) {
                // 创建多级文件夹
                java.nio.file.Files.createDirectories(themeFolderPath);
            }
        } catch (java.io.IOException e) {
            taskLogger.log(taskId, "[2-2-1] 主题文件夹 " + themeFolderStr + " 创建失败: " + e.getMessage(), CreateNovelLogType.ERROR);
            throw new RuntimeException("主题文件夹创建失败: " + e.getMessage(), e);
        }
        String themeFilePath = themeFolderStr + File.separator + brand + ".less";
        java.nio.file.Path themePath = java.nio.file.Paths.get(themeFilePath);
        String backupPath = themeFilePath + ".bak";
        try {
            if (java.nio.file.Files.exists(themePath)) {
                // 备份原文件
                java.nio.file.Files.copy(themePath, java.nio.file.Paths.get(backupPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                // 立即添加回滚动作，确保后续任何失败都能回滚主题文件
                rollbackActions.add(() -> {
                    try {
                        taskLogger.log(taskId, "回滚动作：还原主题文件",CreateNovelLogType.ERROR);
                        java.nio.file.Files.copy(java.nio.file.Paths.get(backupPath), themePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(backupPath));
                    } catch (Exception ignore) {}
                });
                taskLogger.log(taskId, "[2-2-1] 备份主题文件完成", CreateNovelLogType.INFO);
                if (withLogAndDelay) {
                    try { Thread.sleep(FILE_STEP_DELAY_MS); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }
            }
            // 读取原内容
            java.util.List<String> lines = new java.util.ArrayList<>();
            // 构造新主题色变量
            String sValue = appTheme.getThemeTopColor();
            if (sValue != null && sValue.length() > 0) lines.add("@theme-top-color: " + sValue + ";");
            sValue = appTheme.getThemeTopGradient();
            if (sValue != null && sValue.length() > 0) lines.add("@theme-top-gradient: " + sValue + ";");
            payDlg.appendCssOn(lines);
            PayBoard3 payBoard3 = appCommonConfigService.getPayBoard3(brand);
            if (payBoard3 != null) {
                payBoard3.appendCssOn(lines);
            }
            java.nio.file.Files.write(themePath, lines, java.nio.charset.StandardCharsets.UTF_8);
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(backupPath));
        } catch (Exception e) {
            // 还原自身
            try { java.nio.file.Files.copy(java.nio.file.Paths.get(backupPath), themePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING); } catch (Exception ignore) {}
            throw new RuntimeException("主题文件2处理失败: " + e.getMessage(), e);
        }*/
    }
} 