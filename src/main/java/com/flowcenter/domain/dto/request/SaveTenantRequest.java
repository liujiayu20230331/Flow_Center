package com.flowcenter.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SaveTenantRequest {
    private Long id;
    @NotBlank
    @Size(max = 64)
    private String tenantCode;
    @NotBlank
    @Size(max = 128)
    private String tenantName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantCode() { return tenantCode; }
    public void setTenantCode(String tenantCode) { this.tenantCode = tenantCode; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
}
