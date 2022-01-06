import service from '@/utils/request'
import * as env from "../../../config/index"
import store from '@/store'
//字典查询
export function dictionaryGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/dictionary',
    method: 'get',
    params: data
  })
}
// 查询招标单
export function bidSheetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet',
    method: 'get',
    params: data
  })
}
// 查询招标单
export function biddingGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/request',
    method: 'get',
    params: data
  })
}
// 创建招标单
export function bidInvitationPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/returnvo',
    method: 'post',
    data: data
  })
}
// 删除招标单
export function bidInvitationDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/' + id,
    method: 'delete',
  })
}
// 编辑招标单
export function bidInvitationPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet',
    method: 'put',
    data: data
  })
}
// 查询物品
export function bidItemsGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidMaterial',
    method: 'get',
    params: data
  })
}

// 查询招标物品（评标与定标）
export function bidMaterialWithPricingGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidMaterial/withPricing',
    method: 'get',
    params: data
  })
}
// 查询投标方
export function bidVendorGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor',
    method: 'get',
    params: data
  })
}
// 供应商修改招标投标方（查看时间）
export function bidVendorPutCheckTime (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor/checkTime',
    method: 'put',
    data: data
  })
}
// 查询供应商附件
export function bidAttachmentGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidAttachment',
    method: 'get',
    params: data
  })
}
// 添加招标单附件
export function bidAttachmentPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidAttachment',
    method: 'post',
    data: data
  })
}
// 附件删除
export function bidAttachmentDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidAttachment/' + id,
    method: 'delete',
  })
}
// 新建物品
export function bidItemsPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidMaterial',
    method: 'post',
    data: data
  })
}
// 新建投标方
export function bidVendorPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor',
    method: 'post',
    data: data
  })
}
// 修改投标方状态
export function bidVendorStatusPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor/status',
    method: 'put',
    data: data
  })
}
// 删除物品
export function bidItemsDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidMaterial/' + id,
    method: 'delete',
  })
}
// 删除投标方
export function bidVendorDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor/' + id,
    method: 'delete',
  })
}
// 查询资格预审
export function bidPreTrialItemGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidPreTrialItem',
    method: 'get',
    params: data
  })
}
// 新建资格预审
export function bidPreTrialItemPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidPreTrialItem',
    method: 'post',
    data: data
  })
}
// 删除资格预审
export function bidPreTrialItemDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidPreTrialItem/' + id,
    method: 'delete',
  })
}
// 专家查询
export function bidSpecialistGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSpecialist',
    method: 'get',
    params: data
  })
}
// 专家新建
export function bidSpecialistPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSpecialist',
    method: 'post',
    data: data
  })
}
// 专家删除
export function bidSpecialistDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSpecialist/' + id,
    method: 'delete',
  })
}
// 评分要素查询
export function bidScoringElementsGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidScoringElements',
    method: 'get',
    params: data
  })
}
// 评分要素新建
export function bidScoringElementsPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidScoringElements',
    method: 'post',
    data: data
  })
}
// 评分要素删除
export function bidScoringElementsDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidScoringElements/' + id,
    method: 'delete',
  })
}

// 供应商招标单查询
export function bidSheetVendorGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/vendor',
    method: 'get',
    params: data
  })
}
// 供应商物品查询
export function bidVendorMaterialOfferingNumberGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial/offeringNumber',
    method: 'get',
    params: data
  })
}
// 供应商报价新增
export function bidVendorMaterialOfferingNumberPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial/offeringNumber',
    method: 'post',
    data: data
  })
}

// 提交审批
export function bidSheetSubmitPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/submit2',
    headers: {
      'account': store.state.account.user.userName
    },
    method: 'post',
    data: data
  })
}
// 审批通过
export function bidSheetAgreePostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/agree2',
    method: 'post',
    data: data
  })
}
// 招标商资质查询
export function bidPreTrialItemwithVendorGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidPreTrialItem/withVendor',
    method: 'get',
    params: data
  })
}
// 招标商资质添加
export function bidVendorPreTrialItemGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorPreTrialItem',
    method: 'get',
    params: data
  })
}
// 招标商资质添加
export function bidVendorPreTrialItemPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorPreTrialItem',
    method: 'post',
    data: data
  })
}
// 招标书资质修改
export function bidVendorPreTrialItemPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorPreTrialItem',
    method: 'put',
    data: data
  })
}
// 批量修改审批意见
export function bidVendorBatchPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor/batch',
    method: 'put',
    data: data
  })
}
// 供应商提交报价
export function bidVendorMaterialSubmitPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial/submit',
    method: 'post',
    data: data
  })
}
// 供应商修改报价
export function bidVendorMaterialSubmitPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial',
    method: 'post',
    data: data
  })
}

// 根据物品id查询所有最新报价
export function bidVendorMaterialNewPricePostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial/newPrice',
    method: 'post',
    data: data
  })
}
// 批量分配数量
// export function bidVendorMaterialBatchPutRequest(data) {
//     return service({
//         url: env.API_ZTB_PREFIX+ '/bidVendorMaterial/batch',
//         method: 'put',
//         data: data
//     })
// }/bidVendorMaterial/batchWithTotalPrice
export function bidVendorMaterialBatchPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorMaterial/batchWithTotalPrice',
    method: 'put',
    data: data
  })
}
// 专家组长评分查询
export function bidVendorWithScoreGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendor/withScore',
    method: 'get',
    params: data
  })
}
// 专家评分查询/bidSpecialistScore/batch
export function bidSpecialistScoreWithSpecialistGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSpecialistScore/withSpecialist',
    method: 'get',
    params: data
  })
}
// 批量专家打分
export function bidSpecialistScoreBatchPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSpecialistScore/batch',
    method: 'put',
    data: data
  })
}
// 根据流程id查询表单信息
export function bidSheetshowByRequestIdGetRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/showByRequestId/' + id,
    method: 'get',
  })
}
// 根据数据id查询表单信息
export function bidSheetShowGetRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet/show/' + id,
    method: 'get',
  })
}

// 查询公告板信息
export function bulletinBoard (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bulletinBoard',
    method: 'get',
    params: data
  })
}
// 下载附件
export function attachmentGetfile (data) {
  return service({
    url: env.API_SRG_PREFIX + '/attachment/getfile',
    method: 'get',
    params: data
  })
}

// 查询供应商内容明细（国内报价、国外报价）
export function bidVendorDetail (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorDetail',
    method: 'get',
    params: data
  })
}
// 查询供应商内容明细（国内报价、国外报价）
export function bidVendorDetailList (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorDetail/list',
    method: 'post',
    data: data
  })
}
// 根据操作状态修改供应商内容明细状态
export function bidVendorDetailOperation (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorDetail/operation',
    method: 'put',
    data: data
  })
}
// 批量修改供应商内容明细（国内报价、国外报价）
export function bidVendorDetailBatchUpdate (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorDetail/batchUpdate',
    method: 'put',
    data: data
  })
}
// 批量修改供应商内容明细（国内报价、国外报价）
export function requestFlowTransferNode (data) {
  return service({
    url: env.API_IE_PREFIX + '/requestFlow/transferNode',
    method: 'get',
    params: data
  })
}
// 查询招标供应商附件
export function bidVendorGetAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorAttachment',
    method: 'get',
    params: data
  })
}
// 新增招标供应商附件
export function bidVendorPostAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorAttachment',
    method: 'post',
    data: data
  })
}
// 修改招标供应商附件
export function bidVendorPutAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorAttachment',
    method: 'put',
    data: data
  })
}
// 删除招标供应商附件
export function bidVendorDeleteAttachment (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidVendorAttachment/' + id,
    method: 'delete'
  })
}
// 上传附件
export function uploadfilePostRequest (data) {
  return service({
    url: env.API_SRG_PREFIX + "/attachment/uploadfile",
    method: "post",
    data: data,
  })
}
// 查询招标附件
export function bidGetAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidAttachment',
    method: 'get',
    params: data
  })
}