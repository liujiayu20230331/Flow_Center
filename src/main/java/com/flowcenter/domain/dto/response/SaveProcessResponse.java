package com.flowcenter.domain.dto.response;

public class SaveProcessResponse {
    private Long processId;
    private String status;
    public Long getProcessId() { return processId; }
    public void setProcessId(Long processId) { this.processId = processId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
