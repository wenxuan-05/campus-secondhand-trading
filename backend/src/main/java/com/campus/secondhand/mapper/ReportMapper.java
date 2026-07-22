package com.campus.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.secondhand.entity.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {

    @Select("SELECT COUNT(*) FROM reports WHERE reported_user_id = #{userId} AND status = 1")
    int countReviewedReportsByUser(@Param("userId") Long userId);

    @Select("SELECT * FROM reports WHERE reported_user_id = #{userId} ORDER BY created_at DESC")
    List<Report> findByReportedUserId(@Param("userId") Long userId);
}
