package com.xdcplus.workflow.common.pojo.bo;

import com.xdcplus.workflow.common.pojo.entity.DiscussGroup;
import com.xdcplus.workflow.common.pojo.entity.DiscussRecord;
import com.xdcplus.workflow.common.pojo.entity.DiscussRecordRelation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 讨论信息BO
 *
 * @author Rong.Jia
 * @date 2021/10/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscussInfoBO extends DiscussRecord implements Serializable {

    private static final long serialVersionUID = -7404677482343532216L;

    /**
     *  讨论组
     */
    private DiscussGroup group;

    /**
     * 关系
     */
    private List<DiscussRecordRelation> relations;



























}
