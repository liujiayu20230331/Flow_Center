package com.flowcenter.domain.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SaveProcessRequest {
    private Long id;
    @NotBlank @Size(max = 64)
    private String processKey;
    @NotBlank @Size(max = 128)
    private String processName;
    @NotBlank
    private String bpmnXml;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProcessKey() { return processKey; }
    public void setProcessKey(String processKey) { this.processKey = processKey; }
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }
    public String getBpmnXml() { return bpmnXml; }
    public void setBpmnXml(String bpmnXml) { this.bpmnXml = bpmnXml; }
}
