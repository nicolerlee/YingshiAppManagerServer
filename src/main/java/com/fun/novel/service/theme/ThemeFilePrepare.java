package com.fun.novel.service.theme;

import com.fun.novel.dto.CreateNovelAppRequest;
import com.fun.novel.dto.CreateNovelLogType;
import com.fun.novel.entity.ComponentStyle;
import com.fun.novel.entity.ThemeEntity;

import java.io.File;
import java.util.ArrayList;

public class ThemeFilePrepare {
    private final Data data;

    ThemeFilePrepare(Data data) {
        this.data = data;
    }

    private void copyDirectoryRecursivelyNoOverwrite(java.nio.file.Path source, java.nio.file.Path target) throws java.io.IOException {
        if (java.nio.file.Files.isDirectory(source)) {
            if (java.nio.file.Files.notExists(target)) {
                java.nio.file.Files.createDirectories(target);
            }
            try (java.nio.file.DirectoryStream<java.nio.file.Path> entries = java.nio.file.Files.newDirectoryStream(source)) {
                for (java.nio.file.Path entry : entries) {
                    copyDirectoryRecursivelyNoOverwrite(entry, target.resolve(entry.getFileName()));
                }
            }
        } else {
            if (java.nio.file.Files.notExists(target)) {
                java.nio.file.Files.copy(source, target);
            }
        }
    }

    String applyRootPreset(String taskId, String brand, int version, String node) {
        String themePath = data.getThemePath(true);
        String nodePath = themePath + File.separator + node;
        String sourcePath =  nodePath + File.separator + "astyle" + File.separator + version;
        String destPath = nodePath + File.separator + brand;
        try {
            copyDirectoryRecursivelyNoOverwrite(java.nio.file.Paths.get(sourcePath), java.nio.file.Paths.get(destPath));
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return "applyRootPreset failed with " + e;
        }
        return "applyRootPreset success";
    }
}
