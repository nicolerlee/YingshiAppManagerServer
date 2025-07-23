package com.fun.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.entity.*;
import com.fun.novel.mapper.AppThemeMapper;
import com.fun.novel.mapper.Pay66Mapper;
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
    @Autowired
    private final Pay66Mapper pay66Mapper;

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

    public Pay66 getPay66(String brand) {
        LambdaQueryWrapper<Pay66> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pay66::getBrand, brand);
        return pay66Mapper.selectOne(wrapper);
    }

    public ThemeEntity getThemeEntity(String brand, ComponentStyle.Type type) {
        switch (type) {
            case Pay6:
                return getPay6(brand);
            case Pay66:
                return getPay66(brand);
            default:
                return null;
        }
    }

    void overwriteConfigJson(String logTag, String taskId, String brand, ComponentStyle owner, List<ComponentStyle> css) {
        String destDir = buildWorkPath + File.separator + miniConfigThemePath + File.separator + owner.getName() + File.separator + brand + File.separator + "config.json";
        String debugDir = owner.getName() + File.separator + brand + File.separator + "config.json";
        Path destPath = java.nio.file.Paths.get(destDir);
        if (java.nio.file.Files.exists(destPath)) {
            // taskLogger.log(taskId, logTag + "配置文件 " + destPath + " 已存在，不用生成", CreateNovelLogType.INFO); return;
        }
        taskLogger.log(taskId, logTag + "即将生成配置文件 " + debugDir, CreateNovelLogType.INFO);


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

            taskLogger.log(taskId, logTag + "配置文件 " + debugDir + " 生成成功", CreateNovelLogType.INFO);
        } catch (Exception e) {
            taskLogger.log(taskId, logTag + "配置文件 " + debugDir + " 生成失败: " + e.getMessage(), CreateNovelLogType.ERROR);
        }
    }

    void overwritePayJson(String logTag, String taskId, String brand, ThemeEntity themeEntity) {
        if (themeEntity == null) {
            taskLogger.log(taskId, logTag + "themeEntity为空，啥也不做, brand=" + brand, CreateNovelLogType.INFO);
            return;
        }
        taskLogger.log(taskId, logTag + "process pay" + themeEntity.getComponentStyle().getName() +  " brand=" + brand, CreateNovelLogType.INFO);
        List<ComponentStyle> css = themeEntity.buildComponentStyles();
        overwriteConfigJson(logTag, taskId, brand, themeEntity.getComponentStyle(), css);
    }

    @Override
    public void processThemeFile(String taskId, String brand, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay) {
        overwritePayJson("[2-2-1]", taskId, brand, getThemeEntity(brand, ComponentStyle.Type.Pay6));
        overwritePayJson("[2-2-2]", taskId, brand, getThemeEntity(brand, ComponentStyle.Type.Pay66));
    }
} 