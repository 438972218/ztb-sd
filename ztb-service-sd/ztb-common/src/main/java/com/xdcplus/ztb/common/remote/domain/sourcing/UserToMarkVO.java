package com.xdcplus.ztb.common.remote.domain.sourcing;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户标记签证官
 *
 * @author Fish.Fei
 * @date 2021/09/29
 */
@Builder
@Data
public class UserToMarkVO implements Serializable {

    private static final long serialVersionUID = -5086551290597716726L;


    /**
     * 用户去向标识
     */
    private String userToMark;

    /**
     * 描述
     */
    private String description;


}
