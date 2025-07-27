package com.fun.novel.entity;

import lombok.Data;

@Data
public class ComponentStyle {
    public enum Type {
        Pay6, Pay66,
        goodItem, payBoard, planePayment,
    }

    final Type type;
    final int style;
    final int id;
    final String name;
    final String node;

    public ComponentStyle(String name, Type type, int id, int style) {
        this.type = type;
        this.id = id;
        this.style = style;
        this.name = name;
        node = "";
    }

    public ComponentStyle(String name, int id, int style) {
        this.type = ComponentStyle.name2Type(name);
        this.id = id;
        this.style = style;
        this.name = name;
        node = "";
    }

    public ComponentStyle(String node, String name, Type type) {
        this.type = type;
        this.id = 1;
        this.style = 1;
        this.name = name;
        this.node = node;
    }

    @Override
    public String toString() {
        return "ComponentStyle{name=" + name + ", type=" + type + ", id=" + id + ", style=" + style + "}";
    }

    public static Type name2Type(String name) {
        if ("pay6".equals(name)) {
            return Type.Pay6;
        } else if ("good-item".equals(name)) {
            return Type.goodItem;
        } else if ("pay66".equals(name)) {
            return Type.Pay66;
        } else if ("pay-board".equals(name)) {
            return Type.payBoard;
        } else if ("plane-payment".equals(name)) {
            return Type.planePayment;
        } else {
            throw new IllegalArgumentException("Unknown component style name: " + name);
        }
    }

}
