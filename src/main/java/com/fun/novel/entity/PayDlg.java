package com.fun.novel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@TableName("pay_dlg")
@Schema(description = "支付框样式表")
public class PayDlg {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    @Schema(description = "唯一标识")
    private Integer id;

    @NotBlank(message = "brand不能为空")
    @Size(max = 45, message = "brand长度不能超过45个字符")
    @TableField("brand")
    @Schema(description = "品牌标识")
    private String brand;

    @TableField("top_color")
    @Schema(description = "顶部颜色")
    private String topColor;

    @TableField("bg_color")
    @Schema(description = "背景颜色")
    private String bgColor;

    @TableField("item_color")
    @Schema(description = "条目颜色")
    private String itemColor;

    @TableField("item_selcolor")
    @Schema(description = "选中条目颜色")
    private String itemSelcolor;

    @TableField("item_border")
    @Schema(description = "条目边框")
    private String itemBorder;

    @TableField("item_selborder")
    @Schema(description = "选中条目边框")
    private String itemSelborder;

    @TableField("item_width")
    @Schema(description = "条目宽度")
    private Integer itemWidth;

    @TableField("item_selwidth")
    @Schema(description = "选中条目宽度")
    private Integer itemSelwidth;

    @TableField("cap_text_color")
    @Schema(description = "标题文本颜色")
    private String capTextColor;

    @TableField("cap_text_size")
    @Schema(description = "标题文本大小")
    private Integer capTextSize;

    @TableField("cap_title_color")
    @Schema(description = "标题颜色")
    private String capTitleColor;

    @TableField("cap_title_size")
    @Schema(description = "标题大小")
    private Integer capTitleSize;

    @TableField("notice_t1_color")
    @Schema(description = "提示文本1颜色")
    private String noticeT1Color;

    @TableField("notice_t1_size")
    @Schema(description = "提示文本1大小")
    private Integer noticeT1Size;

    @TableField("notice_t2_color")
    @Schema(description = "提示文本2颜色")
    private String noticeT2Color;

    @TableField("notice_t2_size")
    @Schema(description = "提示文本2大小")
    private Integer noticeT2Size;

    @TableField("notice_t3_color")
    @Schema(description = "提示文本3颜色")
    private String noticeT3Color;

    @TableField("notice_t3_size")
    @Schema(description = "提示文本3大小")
    private Integer noticeT3Size;

    @TableField("notice_service_color")
    @Schema(description = "notice_service_color")
    private String noticeServiceColor;

    @TableField("notice_color")
    @Schema(description = "notice_color")
    private String noticeColor;

    @TableField("pay_board")
    @Schema(description = "支付框信息")
    private String payBoard;

    @TableField("pay_board_color")
    @Schema(description = "支付框颜色")
    private String payBoardColor;

    @TableField("pay_board_shadow")
    @Schema(description = "支付框阴影")
    private String payBoardShadow;

    @TableField(exist = false)
    private PayBoard3 payBoard3; // 弃用

    public void appendCssOn(List<String> lines) {
        String sValue;
        Integer value;

        // 顶部颜色
        sValue = this.getTopColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-top-color: " + sValue + ";");
        }

        // 背景颜色
        sValue = this.getBgColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-bg-color: " + sValue + ";");
        }

        // 条目颜色
        sValue = this.getItemColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-item-color: " + sValue + ";");
        }

        // 选中条目颜色
        sValue = this.getItemSelcolor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-item-selcolor: " + sValue + ";");
        }

        // 条目边框
        sValue = this.getItemBorder();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-item-border: " + sValue + ";");
        }

        // 选中条目边框
        sValue = this.getItemSelborder();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-item-selborder: " + sValue + ";");
        }

        // 条目宽度
        value = this.getItemWidth();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-item-width: " + sValue + "rpx;");
        }

        // 选中条目宽度
        value = this.getItemSelwidth();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-item-selwidth: " + sValue + "rpx;");
        }

        // 标题文本颜色
        sValue = this.getCapTextColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-cap-text-color: " + sValue + ";");
        }

        // 标题文本大小
        value = this.getCapTextSize();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-cap-text-size: " + sValue + "rpx;");
        }

        // 标题颜色
        sValue = this.getCapTitleColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-cap-title-color: " + sValue + ";");
        }

        // 标题大小
        value = this.getCapTitleSize();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-cap-title-size: " + sValue + "rpx;");
        }

        // 提示文本1颜色
        sValue = this.getNoticeT1Color();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-notice-t1-color: " + sValue + ";");
        }

        // 提示文本1大小
        value = this.getNoticeT1Size();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-notice-t1-size: " + sValue + "rpx;");
        }

        // 提示文本2颜色
        sValue = this.getNoticeT2Color();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-notice-t2-color: " + sValue + ";");
        }

        // 提示文本2大小
        value = this.getNoticeT2Size();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-notice-t2-size: " + sValue + "rpx;");
        }

        // 提示文本3颜色
        sValue = this.getNoticeT3Color();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-notice-t3-color: " + sValue + ";");
        }

        // 提示文本3大小
        value = this.getNoticeT3Size();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@pd-notice-t3-size: " + sValue + "rpx;");
        }

        // notice_service_color
        sValue = this.getNoticeServiceColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-notice-service-color: " + sValue + ";");
        }

        // notice_color
        sValue = this.getNoticeColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-notice-color: " + sValue + ";");
        }

        // 支付框颜色
        sValue = this.getPayBoardColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-pay-board-color: " + sValue + ";");
        }

        // 支付框阴影
        sValue = this.getPayBoardShadow();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@pd-pay-board-shadow: " + sValue + ";");
        }
    }
}