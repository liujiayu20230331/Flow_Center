package com.flowcenter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowcenter.domain.entity.TenantEntity;
import com.flowcenter.repository.mapper.TenantMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TenantRepository {
    private final TenantMapper mapper;

    public TenantRepository(TenantMapper mapper) {
        this.mapper = mapper;
    }

    public TenantEntity queryById(Long id) {
        return mapper.selectById(id);
    }

    public TenantEntity queryByTenantCode(String tenantCode) {
        LambdaQueryWrapper<TenantEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TenantEntity::getTenantCode, tenantCode).last("LIMIT 1");
        return mapper.selectOne(qw);
    }

    public int save(TenantEntity entity) {
        return entity.getId() == null ? mapper.insert(entity) : mapper.updateById(entity);
    }

    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }

    public Page<TenantEntity> queryPage(Page<TenantEntity> page, String tenantCode, String tenantName, String status) {
        LambdaQueryWrapper<TenantEntity> qw = new LambdaQueryWrapper<>();
        qw.like(tenantCode != null && !tenantCode.trim().isEmpty(), TenantEntity::getTenantCode, tenantCode);
        qw.like(tenantName != null && !tenantName.trim().isEmpty(), TenantEntity::getTenantName, tenantName);
        qw.eq(status != null && !status.trim().isEmpty(), TenantEntity::getStatus, status);
        qw.orderByDesc(TenantEntity::getUpdatedAt);
        return mapper.selectPage(page, qw);
    }
}
