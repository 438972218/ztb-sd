package com.xdcplus.statement.generator;

import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.statement.common.pojo.entity.VendorQuestionBank;
import com.xdcplus.statement.common.pojo.dto.VendorQuestionBankDTO;
import com.xdcplus.statement.common.pojo.dto.VendorQuestionBankFilterDTO;
import com.xdcplus.statement.common.pojo.vo.VendorQuestionBankVO;

import java.util.List;

/**
 * 模板题库表(VendorQuestionBank)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:28
 */
public interface VendorQuestionBankBaseService<S, T, E> extends BaseService<VendorQuestionBank, VendorQuestionBankVO, VendorQuestionBank> {

    /**
     * 添加模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankDTO 模板题库表(VendorQuestionBankDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveVendorQuestionBank(VendorQuestionBankDTO vendorQuestionBankDTO);

    /**
     * 修改模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankDTO 模板题库表(VendorQuestionBankDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updateVendorQuestionBank(VendorQuestionBankDTO vendorQuestionBankDTO);

    /**
     * 批量保存或更新模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankList 模板题库表(VendorQuestionBankList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatch(List<VendorQuestionBank> vendorQuestionBankList);

    /**
     * 批量保存或更新模板题库表(VendorQuestionBankDTOList)
     *
     * @param vendorQuestionBankDTOList 模板题库表(VendorQuestionBankDTOList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatchByDTOList(List<VendorQuestionBankDTO> vendorQuestionBankDTOList);

    /**
     * 删除模板题库表(VendorQuestionBank)
     *
     * @param id 模板题库表(VendorQuestionBank)主键
     * @return {@link Boolean} 是否成功
     */
    Boolean deleteVendorQuestionBankLogical(Long id);

    /**
     * 查询模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<VendorQuestionBankVO>} 状态信息
     */
    List<VendorQuestionBankVO> queryVendorQuestionBankVOList(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO);

    /**
     * 查询模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<VendorQuestionBankVO>} 状态信息
     */
    PageVO<VendorQuestionBankVO> queryVendorQuestionBank(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO);

    /**
     * 查询一个
     *
     * @param id 模板题库表(VendorQuestionBank)主键
     * @return {@link VendorQuestionBankVO} 模板题库表(VendorQuestionBank)信息
     */
    VendorQuestionBankVO queryVendorQuestionBankById(Long id);


}
