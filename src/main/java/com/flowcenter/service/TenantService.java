package com.flowcenter.service;

import com.flowcenter.domain.dto.request.SaveTenantRequest;
import com.flowcenter.domain.dto.response.PageResponse;
import com.flowcenter.domain.dto.response.SaveTenantResponse;
import com.flowcenter.domain.dto.response.TenantDetailResponse;
import com.flowcenter.domain.dto.response.TenantListItemResponse;

public interface TenantService {
    SaveTenantResponse saveTenant(SaveTenantRequest request);
    PageResponse<TenantListItemResponse> queryTenantPage(Long pageNum, Long pageSize, String tenantCode, String tenantName, String status);
    TenantDetailResponse queryTenantDetail(Long id);
    void deleteTenant(Long id);
}
