package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.VendorDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorFilterDTO;
import com.xdcplus.vendor.generator.VendorBaseService;
import com.xdcplus.vendor.common.pojo.entity.Vendor;
import com.xdcplus.vendor.common.pojo.vo.VendorVO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.VendorExcelDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 供应商(Vendor)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-23 10:14:53
 */
public interface VendorService extends VendorBaseService<Vendor, VendorVO, Vendor> {

    /**
     * 注册供应商
     *
     * @param vendorDTO 供应商dto
     * @return {@link Boolean}
     */
    Boolean registerVendor(VendorDTO vendorDTO);

    /**
     * 注册供应商(不发邮件通知)
     *
     * @param vendorDTO 供应商dto
     * @return {@link Boolean}
     */
    void registerVendorNotSendMail(VendorDTO vendorDTO);

    /**
     * 发现供应商voby名称和营业执照
     *
     * @param name            的名字
     * @param businessLicense 营业执照
     * @return {@link List<Vendor>}
     */
    List<Vendor> findVendorVOByNameAndBusinessLicense(String name, String businessLicense);

    /**
     * 查询供应商与用户
     *
     * @param vendorFilterDTO 供应商筛选dto
     * @return {@link List<VendorVO>}
     */
    List<VendorVO> queryVendorsWithUser(VendorFilterDTO vendorFilterDTO);

    /**
     * 根据用户id查询供应商
     *
     * @param userId 用户id
     * @return {@link VendorVO}
     */
    VendorVO queryVendorsByUserId(Long userId);

    /**
     * 供应商文件导入
     *
     * @param vendorExcelDTOS 供应商excel dto
     */
    void importVendorFile(List<VendorExcelDTO> vendorExcelDTOS);

    /**
     * 供应商文件导入(上线后)
     *
     * @param vendorExcelDTOS 供应商excel dto
     */
    void importVendorFileOnLine(List<VendorExcelDTO> vendorExcelDTOS);

}
