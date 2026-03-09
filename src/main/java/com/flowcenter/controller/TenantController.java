package com.flowcenter.controller;

import com.flowcenter.domain.dto.request.QueryTenantPageRequest;
import com.flowcenter.domain.dto.request.SaveTenantRequest;
import com.flowcenter.domain.dto.request.TenantIdRequest;
import com.flowcenter.domain.dto.response.PageResponse;
import com.flowcenter.domain.dto.response.SaveTenantResponse;
import com.flowcenter.domain.dto.response.TenantDetailResponse;
import com.flowcenter.domain.dto.response.TenantListItemResponse;
import com.flowcenter.domain.vo.ApiResponse;
import com.flowcenter.service.TenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/tenant")
@Validated
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/save")
    public ApiResponse<SaveTenantResponse> saveTenant(@Valid @RequestBody SaveTenantRequest request) {
        return ApiResponse.success(tenantService.saveTenant(request));
    }

    @PostMapping("/list")
    public ApiResponse<PageResponse<TenantListItemResponse>> queryTenantList(@RequestBody(required = false) QueryTenantPageRequest request) {
        QueryTenantPageRequest actualRequest = request == null ? new QueryTenantPageRequest() : request;
        return ApiResponse.success(tenantService.queryTenantPage(
                actualRequest.getPageNum(),
                actualRequest.getPageSize(),
                actualRequest.getTenantCode(),
                actualRequest.getTenantName(),
                actualRequest.getStatus()
        ));
    }

    @PostMapping("/detail")
    public ApiResponse<TenantDetailResponse> queryTenantDetail(@Valid @RequestBody TenantIdRequest request) {
        return ApiResponse.success(tenantService.queryTenantDetail(request.getId()));
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteTenant(@Valid @RequestBody TenantIdRequest request) {
        tenantService.deleteTenant(request.getId());
        return ApiResponse.success(null);
    }
}
