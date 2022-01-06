package com.xdcplus.ztb.common.tool.pojo.dto;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.xdcplus.ztb.common.tool.anotation.ToUserTypeAnnotation;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 用户去向
 *
 * @author Martin.Ji
 * @date 2021/9/29 14:52
 */
public class ToUserDTO {

    @ToUserTypeAnnotation(sort = 100, name = "scm_vendor_kpi.responsible_person_id", description = "供应商绩效负责人")
    private String responsiblePerson;



























































    /**
     * 获取用户去向映射对象
     *
     * @return {@link Map}<{@link String}, {@link Integer}>
     */
    public static Map<String, Integer> getToUsers() {

        Map<String, Integer> toUsers = MapUtil.newHashMap();
        Field[] fields = ReflectUtil.getFields(ToUserDTO.class);
        for (Field field : fields) {
            if (field.isAnnotationPresent(ToUserTypeAnnotation.class)) {
                ToUserTypeAnnotation annotation = field.getAnnotation(ToUserTypeAnnotation.class);
                toUsers.put(annotation.description(), annotation.sort());
            }
        }

        return toUsers;
    }

    /**
     * 根据序号获取名称
     *
     * @param sort 序号
     * @return {@link String}
     */
    public static String getToUserNameBySort(Integer sort) {
        Field[] fields = ReflectUtil.getFields(ToUserDTO.class);
        for (Field field : fields) {
            ToUserTypeAnnotation annotation = field.getAnnotation(ToUserTypeAnnotation.class);
            if (Validator.equal(annotation.sort(), sort)) {
                return annotation.name();
            }
        }

        return null;
    }






}
