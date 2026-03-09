package com.flowcenter.domain.dto.response;

public class SaveTenantResponse {
    private Long tenantId;
    private String status;

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
