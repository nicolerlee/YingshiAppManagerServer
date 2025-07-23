package com.fun.novel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public abstract class ThemeEntity implements ComponentStyleIF {

    @TableField(exist = false)
    ComponentStyle componentStyle;
}
