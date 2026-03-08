package com.flowcenter.controller;

import com.flowcenter.domain.dto.request.ProcessIdRequest;
import com.flowcenter.domain.dto.request.QueryProcessPageRequest;
import com.flowcenter.domain.dto.request.SaveProcessRequest;
import com.flowcenter.domain.dto.response.PageResponse;
import com.flowcenter.domain.dto.response.ProcessDetailResponse;
import com.flowcenter.domain.dto.response.ProcessListItemResponse;
import com.flowcenter.domain.dto.response.PublishProcessResponse;
import com.flowcenter.domain.dto.response.SaveProcessResponse;
import com.flowcenter.domain.vo.ApiResponse;
import com.flowcenter.service.ProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/process")
@Validated
public class ProcessController {
    private final ProcessService processService;

    public ProcessController(ProcessService processService) { this.processService = processService; }

    @PostMapping("/save")
    public ApiResponse<SaveProcessResponse> saveProcess(@Valid @RequestBody SaveProcessRequest request) {
        return ApiResponse.success(processService.saveProcess(request));
    }

    @PostMapping("/publish")
    public ApiResponse<PublishProcessResponse> publishProcess(@Valid @RequestBody ProcessIdRequest request) {
        return ApiResponse.success(processService.publishProcess(request.getId()));
    }

    @PostMapping("/disable")
    public ApiResponse<PublishProcessResponse> disableProcess(@Valid @RequestBody ProcessIdRequest request) {
        return ApiResponse.success(processService.disableProcess(request.getId()));
    }

    @PostMapping("/list")
    public ApiResponse<PageResponse<ProcessListItemResponse>> queryProcessList(@RequestBody(required = false) QueryProcessPageRequest request) {
        QueryProcessPageRequest actualRequest = request == null ? new QueryProcessPageRequest() : request;
        return ApiResponse.success(processService.queryProcessPage(
                actualRequest.getPageNum(),
                actualRequest.getPageSize(),
                actualRequest.getProcessName(),
                actualRequest.getStatus()
        ));
    }

    @PostMapping("/detail")
    public ApiResponse<ProcessDetailResponse> queryProcessDetail(@Valid @RequestBody ProcessIdRequest request) {
        return ApiResponse.success(processService.queryProcessDetail(request.getId()));
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteProcess(@Valid @RequestBody ProcessIdRequest request) {
        processService.deleteProcess(request.getId());
        return ApiResponse.success(null);
    }
}
