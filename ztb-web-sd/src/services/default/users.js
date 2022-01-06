import service from '@/utils/request'
import * as env from "../../../config/index"


// 角色管理查询
export function EmployeeGetAllRequest(data) {
    return service({
        url: env.API_PCE_PREFIX+ '/sysRole/getSysRolePageByCondition',
        method: 'get',
        params: data
    })
}