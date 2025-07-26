package com.fun.novel.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ThemeNodeConfigDTO {
    private Map<String, ComponentConfig> components;

    @Data
    public static class ComponentConfig {
        private String style;
        private String id;
    }
}
