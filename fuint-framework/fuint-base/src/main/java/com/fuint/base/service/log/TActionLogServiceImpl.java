package com.fuint.base.service.log;

import com.fuint.base.dao.entities.TActionLog;
import com.fuint.base.dao.pagination.PaginationRequest;
import com.fuint.base.dao.pagination.PaginationResponse;
import com.fuint.base.dao.repositories.TActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志服务实现类
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@Service("sActionLogService")
public class TActionLogServiceImpl implements TActionLogService {

    /**
     * 日志服务Repository
     */
    @Autowired
    private TActionLogRepository tActionLogRepository;

    @Transactional
    public void saveActionLog(TActionLog actionLog) {
        this.tActionLogRepository.save(actionLog);
    }

    public PaginationResponse<TActionLog> findLogsByPagination(PaginationRequest paginationRequest) {
        return tActionLogRepository.findResultsByPagination(paginationRequest);
    }
}
