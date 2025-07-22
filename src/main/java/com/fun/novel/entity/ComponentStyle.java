package com.fun.novel.entity;

import lombok.Data;

@Data
public class ComponentStyle {
    public enum Type {
        Pay6, pay6Item, Pay66, payBoard
    }

    final Type type;
    final int style;
    final int id;
    final String name;

    public ComponentStyle(String name, Type type, int id, int style) {
        this.type = type;
        this.id = id;
        this.style = style;
        this.name = name;
    }

    public ComponentStyle(String name, Type type) {
        this.type = type;
        this.id = 1;
        this.style = 1;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ComponentStyle{name=" + name + ", type=" + type + ", id=" + id + ", style=" + style + "}";
    }

}
