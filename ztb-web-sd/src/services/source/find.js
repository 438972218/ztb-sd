import service from '@/utils/request'
import * as env from "../../../config/index"
import store from '@/store'
export function inquirySheetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet',
        method: 'get',
        params: data
    })
}
// 查询询价单
export function inquirySheetGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/request',
        method: 'get',
        params: data
    })
}
// 创建询价单
export function inquirySheetPostRequest(data, name) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/returnvo',
        method: 'post',
      
        data: data
    })
}
// 删除询价单
export function inquirySheetDeleteRequest(id) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/' + id,
        method: 'delete',
    })
}
// 编辑询价单
export function inquirySheetPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet',
        method: 'put',
        data: data
    })
}
// 物品查询
export function inquiryItemsGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryMaterial',
        method: 'get',
        params: data
    })
}
// 物品查询(最新报价)


// 物品新增
export function inquiryItemsPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryMaterial',
        method: 'post',
        data: data
    })
}
// 物品删除
export function inquiryItemsDeleteRequest(id) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryMaterial/' + id,
        method: 'delete'
    })
}

// 物品查询
export function inquiryVendorGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendor',
        method: 'get',
        params: data
    })
}
// 物品新增
export function inquiryVendorPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendor',
        method: 'post',
        data: data
    })
}
// 物品删除
export function inquiryVendorDeleteRequest(id) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendor/' + id,
        method: 'delete'
    })
}

// 附件查询
export function inquiryAttachmentGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryAttachment',
        method: 'get',
        params: data
    })
}
// 附件新增
export function inquiryAttachmentPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryAttachment',
        method: 'post',
        data: data
    })
}
// 附件删除
export function inquiryAttachmentDeleteRequest(id) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryAttachment/' + id,
        method: 'delete'
    })
}

// 供应商询价单查询
export function inquirySheetVendorGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/vendor',
        method: 'get',
        params: data
    })
}
// 供应商物品查询
export function inquiryVendorMaterialGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/offeringNumber',
        method: 'get',
        params: data
    })
}
// 供应商最新报价物品查询
export function inquiryVendorNewPriceGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/newPrice',
        method: 'post',
        params: data
    })
}
// 供应商报价新增
export function inquiryVendorMaterialPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/offeringNumber',
        method: 'post',
        data: data
    })
}
// 修改报价
export function inquiryVendorMaterialPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial',
        method: 'put',
        data: data
    })
}
// 提交审批
export function inquirySheetSubmitPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/submit2',
        headers: {
            'account': store.state.account.user.userName
        },
        method: 'post',
        data: data
    })
}
// 审批通过
export function inquirySheetAgreePostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquirySheet/agree2',
        method: 'post',
        data: data
    })
}
// 修改供应商状态
export function inquiryVendorPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendor/status',
        method: 'put',
        data: data
    })
}
// 供应商提交报价
export function inquiryVendorMaterialSubmitPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/submit',
        method: 'post',
        data: data
    })
}
// 用户物品
export function inquiryMaterialWithDickerGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryMaterial/withDicker',
        method: 'get',
        params: data
    })
}
// 用户物品环比价
export function inquiryVendorMaterialDickerPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/dicker',
        method: 'put',
        data: data
    })
}
export function inquiryVendorMaterialnewPriceWithPricingPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/newPriceWithPricing',
        method: 'post',
        params: data
    })
}
// 核价查询物品信息
export function inquiryMaterialwithPricingGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryMaterial/withPricing',
        method: 'get',
        params: data
    })
}
// 根据流程id查询表单信息
export function inquirySheetShowGetRequest(id) {
    return service({
      url: env.API_ZTB_PREFIX+ '/inquirySheet/show/'+id,
      method: 'get',
    })
  }
// 根据流程id查询表单信息
export function inquirySheetshowByRequestIdGetRequest(id) {
    return service({
      url: env.API_ZTB_PREFIX+ '/inquirySheet/showByRequestId/'+id,
      method: 'get',
    })
  }

  //审核

export function requestapprovePostRequest (data) {
    return service({
      url: env.API_ZTB_PREFIX + '/request/approve',
      method: 'post',
      data: data
    })
  }
  // 批量修改招标方投标（决标阶段）
export function inquiryVendorMaterialBatchWithTotalPricePutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX + '/inquiryVendorMaterial/batchWithTotalPrice',
        method: 'put',
        data: data
    })
}