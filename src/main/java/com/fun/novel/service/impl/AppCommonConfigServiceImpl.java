package com.fun.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fun.novel.dto.AppCommonConfigDTO;
import com.fun.novel.entity.AppCommonConfig;
import com.fun.novel.entity.AppTheme;
import com.fun.novel.entity.PayBoard3;
import com.fun.novel.entity.PayDlg;
import com.fun.novel.mapper.AppCommonConfigMapper;
import com.fun.novel.mapper.AppThemeMapper;
import com.fun.novel.mapper.PayBoard3Mapper;
import com.fun.novel.mapper.PayDlgMapper;
import com.fun.novel.service.AppCommonConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppCommonConfigServiceImpl implements AppCommonConfigService {

    private final AppCommonConfigMapper appCommonConfigMapper;
    // peng ->
    private final AppThemeMapper appThemeMapper;
    private final PayDlgMapper payDlgMapper;
    private final PayBoard3Mapper payBoard3Mapper;
    // peng <-

    @Override
    public AppCommonConfig createAppCommonConfig(AppCommonConfigDTO dto) {
        // 检查是否已存在配置
        LambdaQueryWrapper<AppCommonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppCommonConfig::getAppId, dto.getAppId());
        AppCommonConfig existingConfig = appCommonConfigMapper.selectOne(wrapper);
        log.info("Checking existing config for appId: {}, result: {}", dto.getAppId(), existingConfig);
        
        if (existingConfig != null) {
            throw new IllegalArgumentException("应用ID为 " + dto.getAppId() + " 的通用配置已存在，每个应用只能创建一条通用配置");
        }

        // 创建新配置
        AppCommonConfig config = new AppCommonConfig();
        BeanUtils.copyProperties(dto, config);

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        config.setCreateTime(now);
        config.setUpdateTime(now);
        
        appCommonConfigMapper.insert(config);
        return config;
    }

    @Override
    public AppCommonConfig getAppCommonConfig(String appid) {
        LambdaQueryWrapper<AppCommonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppCommonConfig::getAppId, appid);
        return appCommonConfigMapper.selectOne(wrapper);
    }

    // peng -->
    @Override
    public AppTheme getAppTheme(String brand) {
        LambdaQueryWrapper<AppTheme> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppTheme::getBrand, brand);
        return appThemeMapper.selectOne(wrapper);
    }
    @Override
    public PayDlg getPayDlg(String brand) {
        LambdaQueryWrapper<PayDlg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayDlg::getBrand, brand);
        PayDlg payDlg = payDlgMapper.selectOne(wrapper);
        if (payDlg == null) {
            return null;
        }
        String payBox = payDlg.getPayBoard();
        if (payBox != null && payBox.equals("pay_board3")) {
            LambdaQueryWrapper<PayBoard3> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(PayBoard3::getBrand, brand);
            PayBoard3 payBoard3 = payBoard3Mapper.selectOne(wrapper3);
            payDlg.setPayBoard3(payBoard3);
        }
        return payDlg;
    }
    // <-- peng

    @Override
    public boolean deleteAppCommonConfig(String appId) {
        LambdaQueryWrapper<AppCommonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppCommonConfig::getAppId, appId);
        return appCommonConfigMapper.delete(wrapper) > 0;
    }

    @Override
    public AppCommonConfig updateAppCommonConfig(AppCommonConfigDTO dto) {
        // 检查配置是否存在
        LambdaQueryWrapper<AppCommonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppCommonConfig::getAppId, dto.getAppId());
        AppCommonConfig existingConfig = appCommonConfigMapper.selectOne(wrapper);
        
        if (existingConfig == null) {
            throw new IllegalArgumentException("应用ID为 " + dto.getAppId() + " 的通用配置不存在");
        }

        // 更新配置
        BeanUtils.copyProperties(dto, existingConfig);
        
        // 更新修改时间
        existingConfig.setUpdateTime(LocalDateTime.now());
        
        appCommonConfigMapper.updateById(existingConfig);
        return existingConfig;
    }
} 