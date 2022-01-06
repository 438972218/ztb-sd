import service from '@/utils/request'
import * as env from "../../../config/index"

// 获取职务类别分页数据
export function getSysJobTypePageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/getSysJobTypePageByCondition',
    method: 'post',
    data: data
  })
}

// 获取职务类别下拉数据
export function getSysJobTypeByCondition () {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/getSysJobTypeByCondition',
    method: 'get'
  })
}

// 新增职务类别
export function SysJobTypeAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/insert',
    method: 'post',
    data: data
  })
}

//  根据ID获取职务类别
export function getSysJobTypeById (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/queryById/' + id,
    method: 'get'
  })
}

// 编辑职务类别
export function SysJobTypeUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/updateById',
    method: 'put',
    data: data
  })
}

// 删除职务类别
export function SysJobTypeDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJobType/deleteById/' + id,
    method: 'delete'
  })
}

// 分页查询职务信息
export function getSysJobPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJob/getSysJobPageByCondition',
    method: 'post',
    data: data
  })
}

// 新增职务
export function SysJobAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJob/insert',
    method: 'post',
    data: data
  })
}

// 编辑职务
export function SysJobUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJob/updateById',
    method: 'put',
    data: data
  })
}

// 删除职务
export function SysJobDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysJob/deleteById/' + id,
    method: 'delete'
  })
}

// 分页查询员工信息
export function getSysEmployeePageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/getSysEmployeePageByCondition',
    method: 'post',
    data: data
  })
}

// 分页查询员工信息（已绑用户）
export function getSysEmployeeByNoBindUser (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/getSysEmployeeByBindUser',
    method: 'post',
    data: data
  })
}

// 新增员工
export function SysEmployeeAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/insert',
    method: 'post',
    data: data
  })
}

// 编辑员工
export function SysEmployeeUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/updateById',
    method: 'put',
    data: data
  })
}

// 删除员工
export function SysEmployeeDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/deleteById/' + id,
    method: 'delete'
  })
}

// 根据公司/部门Id查询员工信息
export function queryByCompanyIdOrDepartIdPage (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysEmployee/queryByCompanyIdOrDepartIdPage',
    method: 'post',
    data: data
  })
}

// 获取部门下拉数据
export function getSysDepartmentByCondition () {
  return service({
    url: env.API_PCE_PREFIX + '/sysDepartment/getSysDepartmentByCondition',
    method: 'get'
  })
}

// 获取包含disabled的数据
export function getSysCompanyDepartmentTreeDisaled () {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/getSysCompanyDepartmentTreeDisaled',
    method: 'get'
  })
}

//  根据ID获取部门信息
export function getSysDepartmentById (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysDepartment/queryById/' + id,
    method: 'get'
  })
}

// 新增部门
export function sysDepartmentAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysDepartment/insert',
    method: 'post',
    data: data
  })
}

// 编辑部门
export function sysDepartmentUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysDepartment/updateById',
    method: 'put',
    data: data
  })
}

// 删除部门
export function sysDepartmentDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysDepartment/deleteById/' + id,
    method: 'delete'
  })
}

// 获取岗位下拉数据
export function getSysPositionByCondition () {
  return service({
    url: env.API_PCE_PREFIX + '/sysPosition/getSysPositionByCondition',
    method: 'get'
  })
}

// 分页查询行政区域
export function getSysRegionPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRegion/getSysRegionPageByCondition',
    method: 'post',
    data: data
  })
}

// 获取行政区域树型数据
export function getRegionTree () {
  return service({
    url: env.API_PCE_PREFIX + '/sysRegion/getRegionTree',
    method: 'get'
  })
}

// 新增行政区域
export function sysRegionAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRegion/insert',
    method: 'post',
    data: data
  })
}

// 编辑行政区域
export function sysRegionUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRegion/updateById',
    method: 'put',
    data: data
  })
}

// 删除行政区域
export function sysRegionDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRegion/deleteById/' + id,
    method: 'delete'
  })
}

// 分页查询角色信息
export function getSysRolePageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/getSysRolePageByCondition',
    method: 'post',
    data: data
  })
}

// 获取角色下拉
export function getSysRoleList () {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/getSysRoleList',
    method: 'get'
  })
}

// 新增角色
export function sysRoleAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/insert',
    method: 'post',
    data: data
  })
}

// 编辑角色
export function sysRoleUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/updateById',
    method: 'put',
    data: data
  })
}

// 删除角色
export function sysRoleDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/deleteById/' + id,
    method: 'delete'
  })
}

// 分页查询权限信息
export function getSysPermissionPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermission/getSysPermissionPageByCondition',
    method: 'post',
    data: data
  })
}

// 权限信息树型结构
export function getSysPermissionTree () {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermission/getSysPermissionTree',
    method: 'get'
  })
}

// 新增权限
export function sysPermissionAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermission/insert',
    method: 'post',
    data: data
  })
}

// 编辑权限
export function sysPermissionUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermission/updateById',
    method: 'put',
    data: data
  })
}

// 删除权限
export function sysPermissionDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermission/deleteById/' + id,
    method: 'delete'
  })
}

// 分页查询用户信息
export function getSysUserServicePageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/getSysUserServicePageByCondition',
    method: 'post',
    data: data
  })
}

// 新增用户
export function sysUserAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/insert',
    method: 'post',
    data: data
  })
}

// 编辑用户
export function sysUserUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/updateById',
    method: 'put',
    data: data
  })
}

// 删除用户
export function sysUserDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/deleteById/' + id,
    method: 'delete'
  })
}

// 修改密码
export function updatePasswordById (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/updatePasswordById',
    method: 'put',
    data: data
  })
}

// 分页查询权限组信息
export function getSysPermissionGroupPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermissionGroup/getSysPermissionGroupPageByCondition',
    method: 'post',
    data: data
  })
}

// 权限组列表
export function getSysPermissionGroupByCondition () {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermissionGroup/getSysPermissionGroupByCondition',
    method: 'get'
  })
}

// 新增权限组
export function sysPermissionGroupAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermissionGroup/insert',
    method: 'post',
    data: data
  })
}

// 编辑权限组
export function sysPermissionGroupUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermissionGroup/updateById',
    method: 'put',
    data: data
  })
}

// 删除权限组
export function sysPermissionGroupDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPermissionGroup/deleteById/' + id,
    method: 'delete'
  })
}

// 公司与部门合起的tree
export function getSysCompanyDepartmentTree () {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/getSysCompanyDepartmentTree',
    method: 'get'
  })
}

// 根据id查询是否为公司 还是集团
export function getjudgeGroupCompany (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/judgeGroupCompany/' + id,
    method: 'get'
  })
}

//  根据ID获取公司信息
export function getSysCompanyById (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/queryById/' + id,
    method: 'get'
  })
}

// 公司分页查询
export function getSysCompanyPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/getSysCompanyPageByCondition',
    method: 'post',
    data: data
  })
}

// 公司树型结构数据
export function getSysCompanyTree () {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/getSysCompanyTree',
    method: 'get'
  })
}

// 新增公司
export function sysCompanyAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/insert',
    method: 'post',
    data: data
  })
}

// 编辑公司
export function sysCompanyUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/updateById',
    method: 'put',
    data: data
  })
}

// 删除公司
export function sysCompanyDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysCompany/deleteById/' + id,
    method: 'delete'
  })
}

// 岗位分页查询
export function getSysPositionPageByCondition (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPosition/getSysPositionPageByCondition',
    method: 'post',
    data: data
  })
}

// 新增岗位
export function sysPositionAdd (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPosition/insert',
    method: 'post',
    data: data
  })
}

// 编辑岗位
export function sysPositionUpdate (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPosition/updateById',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function sysPositionDelete (id) {
  return service({
    url: env.API_PCE_PREFIX + '/sysPosition/deleteById/' + id,
    method: 'delete'
  })
}

// 库存组织树型数据
export function getInventoryOrzTree () {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/tree',
    method: 'get'
  })
}

// 查询工厂树状
export function getInventoryOrzFactoryTree () {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/factoryTree',
    method: 'get'
  })
}

// 查询工厂树状（筛选）
export function getInventoryOrzFactoryTreeBySelect (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/factoryTreeBySelect/' + id,
    method: 'get'
  })
}

// 库存组织分页查询
export function getInventoryOrzPageByCondition (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz',
    method: 'get',
    params: data
  })
}

// 库存组织根据id查询是否可以新增
export function getInventoryOrzJudgeGroupCompany (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/judgeGroupCompany/' + id,
    method: 'get'
  })
}

// 库存组织新增
export function postInventoryOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz',
    method: 'post',
    data: data
  })
}

// 库存组织编辑
export function putInventoryOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz',
    method: 'put',
    data: data
  })
}

// 删除库存组织
export function deleteInventoryOrz (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/' + id,
    method: 'delete'
  })
}

// 采购组织树型数据
export function getPurchaseOrzTree () {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrz/tree',
    method: 'get'
  })
}

// 采购组织分页查询
export function getPurchaseOrzPageByCondition (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrz',
    method: 'get',
    params: data
  })
}

// 采购组织新增
export function postPurchaseOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrz',
    method: 'post',
    data: data
  })
}

// 采购组织编辑
export function putPurchaseOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrz',
    method: 'put',
    data: data
  })
}

// 删除采购组织
export function deletePurchaseOrz (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/inventoryOrz/' + id,
    method: 'delete'
  })
}

// 查询采购组织 工厂(库存组织)对应关系
export function getpurchaseOrzInventoryOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrzInventoryOrz',
    method: 'get',
    params: data
  })
}

// 新增采购组织 工厂(库存组织)对应关系
export function postpurchaseOrzInventoryOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrzInventoryOrz',
    method: 'post',
    data: data
  })
}

// 维护采购组织 工厂(库存组织)对应关系
export function putpurchaseOrzInventoryOrz (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrzInventoryOrz/maintenance',
    method: 'post',
    data: data
  })
}

// 查询多个库存组织
export function getpurchaseOrzMaintenance (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/purchaseOrzInventoryOrz/getMaintenance/' + id,
    method: 'get'
  })
}
// 查询品类管理树状结构
export function getCategoryTree () {
  return service({
    url: env.API_ZTB_PREFIX + '/category/tree',
    method: 'get'
  })
}

// 新增,维护用户关联
export function postUserRelation (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/userRelation',
    method: 'post',
    data: data
  })
}

// 查询用户关联详情
export function getUserRelation (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/userRelation/' + id,
    method: 'get'
  })
}