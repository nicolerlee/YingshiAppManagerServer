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
@TableName("pay66")
@Schema(description = "pay66表实体")
public class Pay66 extends ThemeEntity {
    Pay66() {
        setComponentStyle(new ComponentStyle("66", "plane-payment", ComponentStyle.Type.Pay66));
    }

    @TableField("id")
    @Schema(description = "自增主键")
    private Integer id;

    @TableId(type = IdType.INPUT)
    @TableField("brand")
    @Schema(description = "小程序唯一标识")
    private String brand;

    @TableField("pay_board_id")
    @Schema(description = "支付盘id，表征应用哪个支付盘")
    private Integer payBoardId;

    @TableField("pay_board_style")
    @Schema(description = "支付盘样式，表征应用哪个支付盘样式")
    private Integer payBoardStyle;

    // 将除brand/id外的字段，转换为List<ComponentStyle>的结果形式
    @Override
    public List<ComponentStyle> buildComponentStyles() {
        List<ComponentStyle> styles = new ArrayList<>();
        styles.add(new ComponentStyle("pay-board", ComponentStyle.Type.payBoard, this.payBoardId, this.payBoardStyle));
        return styles;
    }
}