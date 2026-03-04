package com.flowcenter.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowcenter.domain.entity.ProcessDefinitionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ProcessDefinitionMapper extends BaseMapper<ProcessDefinitionEntity> {
    @Select("SELECT id, process_key, process_name, bpmn_xml, status, current_version, created_at, updated_at FROM process_definition WHERE id = #{id} FOR UPDATE")
    ProcessDefinitionEntity queryByIdForUpdate(@Param("id") Long id);
}
