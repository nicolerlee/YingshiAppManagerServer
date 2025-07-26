package com.fun.novel.service.theme;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fun.novel.entity.ComponentStyle;
import com.fun.novel.entity.Pay6;
import com.fun.novel.entity.Pay66;
import com.fun.novel.entity.ThemeEntity;
import com.fun.novel.mapper.Pay66Mapper;
import com.fun.novel.mapper.Pay6Mapper;
import com.fun.novel.utils.CreateNovelTaskLogger;

import java.io.File;
import java.util.Map;

public class Data {
    public final Pay6Mapper pay6Mapper;
    public final Pay66Mapper pay66Mapper;
    public final Map<String, String> environment;
    public CreateNovelTaskLogger taskLogger;

    public Data(Map<String, String> environment, Pay6Mapper pay6Mapper, Pay66Mapper pay66Mapper) {
        this.environment = environment;
        this.pay6Mapper = pay6Mapper;
        this.pay66Mapper = pay66Mapper;
    }
    Data bind(CreateNovelTaskLogger taskLogger) {
        this.taskLogger = taskLogger; return this;
    }

    public String getWorkPath() {
        return environment.get("work-path");
    }

    public String getThemePath(boolean fullPath) {
        String workPath = environment.get("work-path");
        String themePath = environment.get("theme-path");
        return fullPath ? workPath + File.separator + themePath : themePath;
    }

    public String getWebPath(boolean fullPath) {
        String workPath = environment.get("work-path");
        String webPath = environment.get("web-path");
        return fullPath ? workPath + File.separator + webPath : webPath;
    }


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
}
