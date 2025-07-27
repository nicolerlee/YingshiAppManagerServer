package com.fun.novel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public abstract class ThemeEntity implements ComponentStyleIF {

    @TableField(exist = false)
    ComponentStyle componentStyle;

    public void updateTable(String brand, java.util.List<ComponentStyle> styles) {
    }

    public static ThemeEntity create(String brand, ComponentStyle.Type type, java.util.List<ComponentStyle> styles) {
        ThemeEntity themeEntity;
        switch (type) {
            case Pay6:
                themeEntity = new Pay6(); break;
            case Pay66:
                themeEntity = new Pay66(); break;
            default:
                return null;
        }
        themeEntity.updateTable(brand, styles);
        return themeEntity;
    }
}
