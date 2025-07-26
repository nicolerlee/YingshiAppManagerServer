package com.fun.novel.service.theme;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.dto.ThemeNodeConfigDTO;
import com.fun.novel.entity.*;
import com.fun.novel.mapper.Pay66Mapper;
import com.fun.novel.mapper.Pay6Mapper;
import com.fun.novel.utils.CreateNovelTaskLogger;
import com.fun.novel.dto.CreateNovelLogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
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
    HashMap<String, String> buildEnvironment() {
        HashMap<String, String> environment = new HashMap<>();
        environment.put("web-path", miniConfigWebPath);
        environment.put("theme-path", miniConfigThemePath);
        environment.put("work-path", buildWorkPath);
        return environment;
    }

    void overwriteConfigJson(String logTag, String taskId, String brand, ComponentStyle owner, List<ComponentStyle> css) {
        String destDir = buildWorkPath + File.separator + miniConfigThemePath + File.separator + owner.getNode() + File.separator + brand + File.separator + "config.json";
        String debugDir = owner.getNode() + File.separator + brand + File.separator + "config.json";
        Path destPath = java.nio.file.Paths.get(destDir);
        if (java.nio.file.Files.exists(destPath)) {
            // taskLogger.log(taskId, logTag + "配置文件 " + destPath + " 已存在，不用生成", CreateNovelLogType.INFO); return;
        }
        taskLogger.log(taskId, logTag + "即将生成配置文件 " + debugDir, CreateNovelLogType.INFO);


        // 构建符合格式的 Map
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> components = new HashMap<>();
        for (ComponentStyle cs : css) {
            // 假设 ComponentStyle 有 getName 和 getId 方法
            Map<String, String> component = new HashMap<>();
            component.put("id", String.valueOf(cs.getId()));
            component.put("style", String.valueOf(cs.getStyle()));
            components.put(cs.getName(), component);
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

    // 本函描述将theme目录下根组件（如Pay6/Pay66）对应的小程序(brand区分)的配置文件（config.json）指定的style， 从预制的配置目录拷贝到小程序目录下
    void overwriteConfigStyle(String logTag, String taskId, String brand, ComponentStyle owner, List<ComponentStyle> css) {
        String destDir = buildWorkPath + File.separator + miniConfigThemePath + File.separator + owner.getNode() + File.separator + brand + File.separator;
        taskLogger.log(taskId, logTag + "即将覆盖less文件 " + destDir, CreateNovelLogType.INFO);


        // 构建符合格式的 Map
        for (ComponentStyle cs : css) {
            if (cs.getStyle() < 1) {
                taskLogger.warn(taskId, logTag + "style=" + cs.getStyle() + "，不用预制style，跳过", CreateNovelLogType.INFO);
                continue;
            }
            String sourceDir = buildWorkPath + File.separator + miniConfigThemePath + File.separator + owner.getNode() + File.separator;
            sourceDir +=  "astyle" + File.separator + cs.getName() + File.separator + cs.getId() + File.separator + cs.getStyle() + ".less";
            Path sourcePath = java.nio.file.Paths.get(sourceDir);
            if (!java.nio.file.Files.exists(sourcePath)) {
                taskLogger.warn(taskId, logTag + "less文件 " + sourcePath + " 不存在，啥也不做", CreateNovelLogType.ERROR); continue;
            }
            taskLogger.log(taskId, logTag + "从less文件 " + sourcePath + " 复制样式", CreateNovelLogType.INFO);
            String filename = cs.getName() + "-v" + cs.getId() + ".less";
            if (cs.getId() == 1) {
                filename = cs.getName() + ".less";
            }
            Path destPath = java.nio.file.Paths.get(destDir + filename);
            try {
                java.nio.file.Files.copy(sourcePath, destPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                taskLogger.warn(taskId, logTag + "less文件 " + sourcePath + " 复制失败: " + e.getMessage(), CreateNovelLogType.INFO); continue;
            }
        }
        taskLogger.log(taskId, logTag + "完成覆盖less文件 " + destDir, CreateNovelLogType.INFO);
    }

    void overwriteThemeConfig(String logTag, String taskId, String brand, ThemeEntity themeEntity) {
        if (themeEntity == null) {
            taskLogger.log(taskId, logTag + "themeEntity为空，啥也不做, brand=" + brand, CreateNovelLogType.INFO);
            return;
        }
        taskLogger.log(taskId, logTag + "process 模块" + themeEntity.getComponentStyle().getName() +  " brand=" + brand, CreateNovelLogType.INFO);
        List<ComponentStyle> css = themeEntity.buildComponentStyles();
        overwriteConfigJson(logTag, taskId, brand, themeEntity.getComponentStyle(), css);
        overwriteConfigStyle(logTag, taskId, brand, themeEntity.getComponentStyle(), css);
    }

    @Override
    public void processThemeFile(String taskId, String brand, CreateNovelAppRequest.BaseConfig baseConfig, List<Runnable> rollbackActions, boolean withLogAndDelay) {
        Data data = new Data(buildEnvironment(), pay6Mapper, pay66Mapper);
        overwriteThemeConfig("[2-2-1]", taskId, brand, data.getThemeEntity(brand, ComponentStyle.Type.Pay6));
        overwriteThemeConfig("[2-2-2]", taskId, brand, data.getThemeEntity(brand, ComponentStyle.Type.Pay66));
    }


    @Override
    public List<ThemeNodeConfigDTO> getPreComponentConfigs(String brand, String name) {
        Data data = new Data(buildEnvironment(), pay6Mapper, pay66Mapper);
        data.bind(taskLogger);
        return new ThemeFileRetriever(data).getPreComponentConfigs(brand, name);
    }
    @Override
    public ThemeNodeConfigDTO getComponentConfig(String brand, String name) {
        Data data = new Data(buildEnvironment(), pay6Mapper, pay66Mapper);
        data.bind(taskLogger);
        return new ThemeFileRetriever(data).getAppliedComponentConfig(brand, name);
    }
    @Override
    public String getComponentLess(String brand, String name, int style) {
        Data data = new Data(buildEnvironment(),pay6Mapper, pay66Mapper);
        data.bind(taskLogger);
        return new ThemeFileRetriever(data).getComponentLess(brand, name, style);
    }
    @Override
    public String getComponentSubLess(String brand, String root, String name, int id, int style) {
        Data data = new Data(buildEnvironment(), pay6Mapper, pay66Mapper);
        data.bind(taskLogger);
        return new ThemeFileRetriever(data).getComponentSubLess(brand, root, name, id, style);
    }
} 