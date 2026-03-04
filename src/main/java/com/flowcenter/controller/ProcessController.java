package com.flowcenter.controller;

import com.flowcenter.domain.dto.request.SaveProcessRequest;
import com.flowcenter.domain.dto.response.*;
import com.flowcenter.domain.vo.ApiResponse;
import com.flowcenter.service.ProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/publish/{id}")
    public ApiResponse<PublishProcessResponse> publishProcess(@PathVariable Long id) {
        return ApiResponse.success(processService.publishProcess(id));
    }

    @PostMapping("/disable/{id}")
    public ApiResponse<PublishProcessResponse> disableProcess(@PathVariable Long id) {
        return ApiResponse.success(processService.disableProcess(id));
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<ProcessListItemResponse>> queryProcessList(
            @RequestParam(required = false) Long pageNum,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) String processName,
            @RequestParam(required = false) String status) {
        return ApiResponse.success(processService.queryProcessPage(pageNum, pageSize, processName, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProcessDetailResponse> queryProcessDetail(@PathVariable Long id) {
        return ApiResponse.success(processService.queryProcessDetail(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProcess(@PathVariable Long id) {
        processService.deleteProcess(id);
        return ApiResponse.success(null);
    }
}
