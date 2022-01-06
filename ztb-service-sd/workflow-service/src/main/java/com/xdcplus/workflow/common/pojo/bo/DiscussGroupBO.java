package com.xdcplus.workflow.common.pojo.bo;

import com.xdcplus.workflow.common.pojo.entity.DiscussGroup;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 讨论组信息
 *
 * @author Rong.Jia
 * @date 2021/11/08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscussGroupBO extends DiscussGroup implements Serializable {

    private static final long serialVersionUID = 1118208017443232925L;;



    /**
     * 讨论组关联信息
     */
    private List<DiscussGroupRelation> relations;







}
