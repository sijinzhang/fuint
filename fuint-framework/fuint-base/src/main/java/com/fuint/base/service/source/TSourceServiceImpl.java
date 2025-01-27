package com.fuint.base.service.source;

import com.fuint.base.annoation.OperationServiceLog;
import com.fuint.base.dao.entities.TSource;
import com.fuint.base.dao.pagination.PaginationRequest;
import com.fuint.base.dao.pagination.PaginationResponse;
import com.fuint.base.dao.repositories.TSourceRepository;
import com.fuint.base.service.entities.TreeNode;
import com.fuint.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理服务实现类
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@Service
public class TSourceServiceImpl implements TSourceService {

    @Autowired
    private TSourceRepository tSourceRepository;


    public PaginationResponse<TSource> findPlatformByPagination(PaginationRequest paginationRequest) {
        return tSourceRepository.findResultsByPagination(paginationRequest);
    }


    /**
     * 添加菜单
     *
     * @param tSource
     */
    @Override
    @OperationServiceLog(description = "新增后台菜单")
    @Transactional
    public void addSource(TSource tSource) {
        this.tSourceRepository.save(tSource);
    }

    /**
     * 根据菜单ID查询菜单信息
     *
     * @param id
     * @return
     */
    @Override
    public TSource findSourceById(Long id) {
        return this.tSourceRepository.findOne(id);
    }

    /**
     * 获取有效的角色集合
     *
     * @return
     */
    @Override
    public List<TSource> getAvailableSources() {
        return tSourceRepository.findByStatus("A");
    }

    /**
     * 获取菜单的属性结构
     *
     * @return
     */
    @Override
    public List<TreeNode> getSourceTree() {
        List<TSource> tSources = this.getAvailableSources();
        List<TreeNode> trees = new ArrayList<TreeNode>();
        if (tSources != null && tSources.size() > 0) {
            TreeNode sourceTreeNode = null;
            for (TSource tSource : tSources) {
                sourceTreeNode = new TreeNode();
                sourceTreeNode.setName(tSource.getName());
                sourceTreeNode.setId(tSource.getId());
                sourceTreeNode.setLevel(tSource.getLevel());
                sourceTreeNode.setSort(tSource.getStyle());
                sourceTreeNode.setPath(tSource.getPath());
                sourceTreeNode.setIcon(tSource.getNewIcon());
                sourceTreeNode.setIsMenu(tSource.getIsMenu());
                sourceTreeNode.setStatus(tSource.getStatus());
                sourceTreeNode.setPerms(tSource.getPath().replaceAll("/", ":"));
                if (tSource.getParent() != null) {
                    sourceTreeNode.setpId(tSource.getParent().getId());
                } else {
                    sourceTreeNode.setpId(0);
                }
                trees.add(sourceTreeNode);
            }
        }
        return trees;
    }

    /**
     * 修改菜单
     *
     * @param source
     */
    @Override
    @OperationServiceLog(description = "修改菜单")
    @Transactional
    public void editSource(TSource source) {
        tSourceRepository.merge(source);
    }

    /**
     * 删除菜单
     *
     * @param sourceId
     */
    @Override
    @OperationServiceLog(description = "删除菜单")
    @Transactional
    public void deleteSource(long sourceId) {
        tSourceRepository.delete(sourceId);
    }

    /**
     * 根据菜单ID集合查询菜单列表信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<TSource> findDatasByIds(String[] ids) {
        Long[] arrays = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            arrays[i] = Long.parseLong(ids[i]);
        }
        return tSourceRepository.findByIdIn(ArrayUtil.toList(arrays));
    }

    /**
     * 根据账户ID查询账户所能访问的source资源列表
     *
     * @param accountId
     * @return
     */
    @Override
    public List<TSource> findSourcesByAccountId(long accountId) {
        return tSourceRepository.findSourcesByAccountId(accountId);
    }

    /**
     * 根据资源CODE获取资源信息
     *
     * @param sourceCode
     * @return
     */
    @Override
    public TSource findSourceByCode(String sourceCode) {
        List<TSource> tSources = tSourceRepository.findBySourceCode(sourceCode);
        if (tSources != null && tSources.size() > 0) {
            return tSources.get(0);
        }
        return null;
    }
}
