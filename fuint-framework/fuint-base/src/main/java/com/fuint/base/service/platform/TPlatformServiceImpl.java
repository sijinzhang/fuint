package com.fuint.base.service.platform;

import com.fuint.base.annoation.OperationServiceLog;
import com.fuint.base.dao.entities.TPlatform;
import com.fuint.base.dao.pagination.PaginationRequest;
import com.fuint.base.dao.pagination.PaginationResponse;
import com.fuint.base.dao.repositories.TPlatformRepository;
import com.fuint.exception.BusinessCheckException;
import com.fuint.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 平台信息接口服务类
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@Service
public class TPlatformServiceImpl implements TPlatformService {

    private static final int VALID_STATUS = 1;

    private static final int No_VALID_STATUS = 0;

    @Autowired
    private TPlatformRepository tPlatformRepository;

    public PaginationResponse<TPlatform> findPlatformByPagination(PaginationRequest paginationRequest) {
        return tPlatformRepository.findResultsByPagination(paginationRequest);
    }

    /**
     * 新增平台
     *
     * @param tPlatform
     */
    @Override
    @OperationServiceLog(description = "新增平台")
    @Transactional
    public void addPlatform(TPlatform tPlatform) throws BusinessCheckException {
        if(tPlatform == null || StringUtil.isBlank(tPlatform.getName())){
            throw new BusinessCheckException("平台信息错误.");
        }
        tPlatform.setStatus(VALID_STATUS);
        TPlatform platform = tPlatformRepository.findByName(tPlatform.getName());
        if(platform != null){
            throw new BusinessCheckException("平台已经存在");
        }
        tPlatformRepository.save(tPlatform);
    }

    /**
     * 删除平台
     *
     * @param platformId
     */
    @Override
    @OperationServiceLog(description = "删除平台")
    @Transactional
    public void deletePlatform(Long platformId) throws BusinessCheckException {
        TPlatform tPlatform = tPlatformRepository.findOne(platformId);
        if(tPlatform  == null){
            throw new BusinessCheckException("平台信息不存在.");
        }
        tPlatform.setStatus(No_VALID_STATUS);
        tPlatformRepository.merge(tPlatform);
    }

    /**
     * 根据ID获取平台信息
     *
     * @param id 平台ID
     * @return 平台信息实体
     */
    @Override
    public TPlatform getPlatformById(Long id) {
        return tPlatformRepository.findOne(id);
    }

    /**
     * 获取状态为有效的平台信息列表
     *
     * @return 状态为有效的平台信息列表
     */
    @Override
    public List<TPlatform> getPlatforms() {
        return tPlatformRepository.findByStatus(VALID_STATUS);
    }
}
