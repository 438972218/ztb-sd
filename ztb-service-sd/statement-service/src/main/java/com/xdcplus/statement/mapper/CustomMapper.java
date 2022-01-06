package com.xdcplus.statement.mapper;


import org.apache.ibatis.annotations.Param;

/**
 * 自定义映射器
 *
 * @author Fish.Fei
 * @date 2021/09/28
 * @since 2021-09-03 10:10:25
 */
public interface CustomMapper {
    Long getCustomToUserId(@Param("table") String table, @Param("field") String field, @Param("requestId") Long requestId);
}
