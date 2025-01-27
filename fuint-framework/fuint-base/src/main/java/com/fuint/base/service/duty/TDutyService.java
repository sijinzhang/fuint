package com.fuint.base.service.duty;


import com.fuint.base.dao.entities.TDuty;
import com.fuint.base.dao.entities.TSource;
import com.fuint.base.dao.pagination.PaginationRequest;
import com.fuint.base.dao.pagination.PaginationResponse;
import com.fuint.base.service.entities.TreeNode;
import com.fuint.exception.BusinessCheckException;

import java.util.List;

/**
 * 角色服务接口
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public interface TDutyService {
    /**
     * 角色保存方法
     *
     * @param duty
     */
    void saveDuty(TDuty duty, List<TSource> sources) throws BusinessCheckException;


    /**
     * 获取有效的角色集合
     *
     * @return
     */
    List<TDuty> getAvailableRoles();

    /**
     * 根据ID获取角色实体
     *
     * @param roleId
     * @return
     */
    TDuty getRoleById(Long roleId);


    /**
     * 角色信息分页查询
     *
     * @param paginationRequest 分页查询请求对象
     * @return 分页查询结果对象
     */
    PaginationResponse<TDuty> findDutiesByPagination(PaginationRequest paginationRequest);

    /**
     * 根据ID数组获取角色集合
     *
     * @param ids
     * @return
     */
    List<TDuty> findDatasByIds(String[] ids);

    /**
     * 删除方法
     *
     * @param dutyId
     */
    void deleteDuty(long dutyId);

    /**
     * 修改角色
     *
     * @param tduty
     */
    void updateDuty(TDuty tduty, List<TSource> sources) throws BusinessCheckException;

    /**
     * 根据角色名称合状态查询角色
     *
     * @param name
     * @param status
     * @return
     */
    TDuty findByNameAndStatus(String name, String status);

    /**
     * 根据角色名称获取已经分配的菜单ID集合
     *
     * @param dutyId
     * @return
     */
    List<Long> getSourceIdsByDutyId(long dutyId);

    /**
     * 获取角色的树形结构
     *
     * @return
     */
    List<TreeNode> getDutyTree();

    /**
     * 根据账户获取角色
     *
     * @param accountId
     * @return
     */
    List<TDuty> findDutiesByAccountId(long accountId);


}
