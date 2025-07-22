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
@TableName("pay_board3")
@Schema(description = "支付框3相关信息")
public class PayBoard3 {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    @Schema(description = "唯一标识")
    private Integer id;

    @NotBlank(message = "brand不能为空")
    @Size(max = 45, message = "brand长度不能超过45个字符")
    @TableField("brand")
    @Schema(description = "品牌标识")
    private String brand;

    @TableField("text_bg")
    @Schema(description = "文本背景")
    private String textBg;

    @TableField("text_color")
    @Schema(description = "文本颜色")
    private String textColor;

    @TableField("text_size")
    @Schema(description = "文本大小")
    private Integer textSize;

    @TableField("price_color")
    @Schema(description = "价格颜色")
    private String priceColor;

    @TableField("price_size")
    @Schema(description = "价格大小")
    private Integer priceSize;

    @TableField("v_color")
    @Schema(description = "V 相关颜色")
    private String vColor;

    @TableField("v_size")
    @Schema(description = "V 相关大小")
    private Integer vSize;

    @TableField("check_text_color")
    @Schema(description = "复选框文本颜色")
    private String checkTextColor;

    @TableField("check_box_color")
    @Schema(description = "复选框颜色")
    private String checkBoxColor;

    /**
     * 将 PayBoard3 对象的字段转换为 CSS 样式并附加到 lines 列表中。
     *
     * @param lines 存储 CSS 样式的列表
     */
    public void appendCssOn(List<String> lines) {
        String sValue;
        Integer value;
        String fullPrefix = "pb3-";

        // 文本背景
        sValue = this.getTextBg();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "text-bg: " + sValue + ";");
        }

        // 文本颜色
        sValue = this.getTextColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "text-color: " + sValue + ";");
        }

        // 文本大小
        value = this.getTextSize();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "text-size: " + sValue + "rpx;");
        }

        // 价格颜色
        sValue = this.getPriceColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "price-color: " + sValue + ";");
        }

        // 价格大小
        value = this.getPriceSize();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "price-size: " + sValue + "rpx;");
        }

        // V 相关颜色
        sValue = this.getVColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "v-color: " + sValue + ";");
        }

        // V 相关大小
        value = this.getVSize();
        sValue = value == null ? "" : String.valueOf(value);
        if (!sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "v-size: " + sValue + "rpx;");
        }

        // 复选框文本颜色
        sValue = this.getCheckTextColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "check-text-color: " + sValue + ";");
        }

        // 复选框颜色
        sValue = this.getCheckBoxColor();
        if (sValue != null && !sValue.isEmpty()) {
            lines.add("@" + fullPrefix + "check-box-color: " + sValue + ";");
        }
    }
}