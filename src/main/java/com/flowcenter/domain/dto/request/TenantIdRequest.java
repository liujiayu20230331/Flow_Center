package com.flowcenter.domain.dto.request;

import javax.validation.constraints.NotNull;

public class TenantIdRequest {
    @NotNull
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
