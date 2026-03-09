package com.flowcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowcenter.domain.dto.request.SaveTenantRequest;
import com.flowcenter.domain.dto.response.PageResponse;
import com.flowcenter.domain.dto.response.SaveTenantResponse;
import com.flowcenter.domain.dto.response.TenantDetailResponse;
import com.flowcenter.domain.dto.response.TenantListItemResponse;
import com.flowcenter.domain.entity.TenantEntity;
import com.flowcenter.exception.BizException;
import com.flowcenter.exception.ErrorCode;
import com.flowcenter.repository.TenantRepository;
import com.flowcenter.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {
    private static final long DEFAULT_PAGE_NUM = 1L;
    private static final long DEFAULT_PAGE_SIZE = 20L;
    private static final long MAX_PAGE_SIZE = 100L;
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveTenantResponse saveTenant(SaveTenantRequest request) {
        if (request.getId() == null) {
            return createTenant(request);
        }
        return updateTenant(request);
    }

    @Override
    public PageResponse<TenantListItemResponse> queryTenantPage(Long pageNum, Long pageSize, String tenantCode, String tenantName, String status) {
        long pn = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        long ps = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);

        Page<TenantEntity> page = tenantRepository.queryPage(new Page<>(pn, ps), tenantCode, tenantName, status);
        List<TenantListItemResponse> records = new ArrayList<>();
        for (TenantEntity entity : page.getRecords()) {
            TenantListItemResponse item = new TenantListItemResponse();
            item.setId(entity.getId());
            item.setTenantCode(entity.getTenantCode());
            item.setTenantName(entity.getTenantName());
            item.setStatus(entity.getStatus());
            item.setUpdatedAt(String.valueOf(entity.getUpdatedAt()));
            records.add(item);
        }

        PageResponse<TenantListItemResponse> response = new PageResponse<>();
        response.setTotal(page.getTotal());
        response.setPageNum(pn);
        response.setPageSize(ps);
        response.setRecords(records);
        return response;
    }

    @Override
    public TenantDetailResponse queryTenantDetail(Long id) {
        TenantEntity entity = tenantRepository.queryById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "tenant not found");
        }

        TenantDetailResponse response = new TenantDetailResponse();
        response.setId(entity.getId());
        response.setTenantCode(entity.getTenantCode());
        response.setTenantName(entity.getTenantName());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(String.valueOf(entity.getCreatedAt()));
        response.setUpdatedAt(String.valueOf(entity.getUpdatedAt()));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTenant(Long id) {
        TenantEntity entity = tenantRepository.queryById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "tenant not found");
        }
        tenantRepository.deleteById(id);
    }

    private SaveTenantResponse createTenant(SaveTenantRequest request) {
        if (tenantRepository.queryByTenantCode(request.getTenantCode()) != null) {
            throw new BizException(ErrorCode.DUPLICATE_KEY, "tenantCode exists");
        }

        TenantEntity entity = new TenantEntity();
        entity.setTenantCode(request.getTenantCode());
        entity.setTenantName(request.getTenantName());
        entity.setStatus(STATUS_ACTIVE);
        tenantRepository.save(entity);

        SaveTenantResponse response = new SaveTenantResponse();
        response.setTenantId(entity.getId());
        response.setStatus(entity.getStatus());
        return response;
    }

    private SaveTenantResponse updateTenant(SaveTenantRequest request) {
        TenantEntity entity = tenantRepository.queryById(request.getId());
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "tenant not found");
        }
        if (!entity.getTenantCode().equals(request.getTenantCode())) {
            throw new BizException(ErrorCode.PARAM_ERROR, "tenantCode is immutable");
        }

        entity.setTenantName(request.getTenantName());
        tenantRepository.save(entity);

        SaveTenantResponse response = new SaveTenantResponse();
        response.setTenantId(entity.getId());
        response.setStatus(entity.getStatus());
        return response;
    }
}
