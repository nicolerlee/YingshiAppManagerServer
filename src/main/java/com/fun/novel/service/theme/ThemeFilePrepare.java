package com.fun.novel.service.theme;

import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.dto.CreateNovelLogType;
import com.fun.novel.entity.ComponentStyle;
import com.fun.novel.entity.ThemeEntity;

import java.util.ArrayList;

public class ThemeFilePrepare {
    private final Data data;

    ThemeFilePrepare(Data data) {
        this.data = data;
    }

    void prepareThemeFile(String taskId, CreateNovelAppRequest.ThemeConfig themeConfig, String brand) {
        ComponentStyle.Type type = ComponentStyle.name2Type(themeConfig.getName());
        switch (type) {
            case Pay66:
                preaparePay66(taskId, themeConfig, type, brand);
                break;
            case Pay6:
                preaparePay6(taskId, themeConfig, type, brand);
                break;
        }
    }

    void preaparePay66(String taskId, CreateNovelAppRequest.ThemeConfig themeConfig, ComponentStyle.Type type, String brand) {
        data.taskLogger.log(taskId, "preaparePay66", CreateNovelLogType.INFO);
        java.util.List<CreateNovelAppRequest.ComponentThemeConfig> components = themeConfig.getComponents();
        java.util.List<ComponentStyle> styles = new ArrayList<>();
        for (CreateNovelAppRequest.ComponentThemeConfig component : components) {
            styles.add(new ComponentStyle(component.getName(), component.getId(), component.getStyle()));
        }
        data.updateTable(brand, type, styles);
        data.taskLogger.log(taskId, "preaparePay66 finished", CreateNovelLogType.INFO);
    }
    void preaparePay6(String taskId, CreateNovelAppRequest.ThemeConfig themeConfig, ComponentStyle.Type type, String brand) {
        data.taskLogger.log(taskId, "preaparePay6", CreateNovelLogType.INFO);
        java.util.List<CreateNovelAppRequest.ComponentThemeConfig> components = themeConfig.getComponents();
        java.util.List<ComponentStyle> styles = new ArrayList<>();
        for (CreateNovelAppRequest.ComponentThemeConfig component : components) {
            styles.add(new ComponentStyle(component.getName(), component.getId(), component.getStyle()));
        }
        data.updateTable(brand, type, styles);
        data.taskLogger.log(taskId, "preaparePay6 finished", CreateNovelLogType.INFO);
    }
}
