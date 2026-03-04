package com.flowcenter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flowcenter.domain.entity.ProcessVersionEntity;
import com.flowcenter.repository.mapper.ProcessVersionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProcessVersionRepository {
    private final ProcessVersionMapper mapper;
    public ProcessVersionRepository(ProcessVersionMapper mapper) { this.mapper = mapper; }

    public int save(ProcessVersionEntity entity) { return mapper.insert(entity); }

    public List<ProcessVersionEntity> queryByProcessId(Long processId) {
        LambdaQueryWrapper<ProcessVersionEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ProcessVersionEntity::getProcessId, processId).orderByDesc(ProcessVersionEntity::getVersion);
        return mapper.selectList(qw);
    }
}
