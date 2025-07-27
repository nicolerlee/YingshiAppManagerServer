package com.fun.novel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("pay6")
@Schema(description = "pay6表实体")
public class Pay6 extends ThemeEntity {
    Pay6() {
        setComponentStyle(new ComponentStyle("6", "episode-pay", ComponentStyle.Type.Pay6));
    }

    @TableField("id")
    @Schema(description = "自增主键")
    private Integer id;

    @TableId(type = IdType.INPUT)
    @TableField("brand")
    @Schema(description = "小程序代码唯一标识")
    private String brand;

    @TableField("pay_board_id")
    @Schema(description = "支付盘类型")
    private Integer payBoardId;

    @TableField("pay_board_style")
    @Schema(description = "选取样式id")
    private Integer payBoardStyle;

    @TableField("good_item_id")
    @Schema(description = "套餐类型id")
    private Integer goodItemId;

    @TableField("good_item_style")
    @Schema(description = "选取样式id")
    private Integer goodItemStyle;

    // 将除brand/id外的字段，转换为List<ComponentStyle>的结果形式
    @Override
    public List<ComponentStyle> buildComponentStyles() {
        List<ComponentStyle> styles = new ArrayList<>();
        styles.add(new ComponentStyle("pay-board", ComponentStyle.Type.payBoard, this.payBoardId, this.payBoardStyle));
        styles.add(new ComponentStyle("good-item", ComponentStyle.Type.goodItem, this.goodItemId, this.goodItemStyle));
        return styles;
    }
} 