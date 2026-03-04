package com.flowcenter.service;

import com.flowcenter.domain.dto.request.SaveProcessRequest;
import com.flowcenter.domain.dto.response.*;

public interface ProcessService {
    SaveProcessResponse saveProcess(SaveProcessRequest request);
    PublishProcessResponse publishProcess(Long id);
    PublishProcessResponse disableProcess(Long id);
    PageResponse<ProcessListItemResponse> queryProcessPage(Long pageNum, Long pageSize, String processName, String status);
    ProcessDetailResponse queryProcessDetail(Long id);
    void deleteProcess(Long id);
}
