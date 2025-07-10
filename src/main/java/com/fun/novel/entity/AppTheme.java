package com.fun.novel.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@TableName("app_theme")
@Schema(description = "小说应用信息")
public class AppTheme {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    @Schema(description = "主键ID")
    private Integer id;

    @NotBlank(message = "brand不能为空")
    @Size(max = 255, message = "应用名称长度不能超过255个字符")
    @TableField("brand")
    @Schema(description = "应用id")
    private String brand;

    @TableField("theme_top_color")
    @Schema(description = "顶部色")
    private String themeTopColor;

    @TableField("theme_top_gradient")
    @Schema(description = "顶部渐变色")
    private String themeTopGradient;

    @TableField("pd_top_color")
    @Schema(description = "支付框顶部色")
    private String pdTopColor;

    @TableField("pd_bg_color")
    @Schema(description = "支付框顶部色")
    private String pdBgColor;

    @TableField("pd_item_color")
    @Schema(description = "支付框顶部色")
    private String pdItemColor;

    @TableField("pd_item_selcolor")
    @Schema(description = "支付框顶部色")
    private String pdItemSelcolor;

    @TableField("pd_item_border")
    @Schema(description = "支付框顶部色")
    private String pdItemBorder;

    @TableField("pd_item_selborder")
    @Schema(description = "支付框顶部色")
    private String pdItemSelBorder;

    @TableField("pd_item_width")
    @Schema(description = "支付框顶部色")
    private Integer pdItemWidth;

    @TableField("pd_item_selwidth")
    @Schema(description = "支付框顶部色")
    private Integer pdItemSelWidth;
}