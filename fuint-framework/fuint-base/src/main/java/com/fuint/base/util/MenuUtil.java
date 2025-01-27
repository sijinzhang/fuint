package com.fuint.base.util;

import com.fuint.base.dao.entities.TSource;
import com.fuint.base.service.entities.TreeNode;
import com.fuint.base.shiro.util.ShiroUserHelper;
import com.fuint.util.StringUtil;
import java.util.*;

/**
 * 用户菜单工具类
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public class MenuUtil {

    public static String getMeuns(String context) {
        List<TSource> sources = ShiroUserHelper.getCurrentShiroUser().getSources();
        StringBuffer menustr = new StringBuffer();
        if (sources != null && sources.size() > 0) {
            List<TreeNode> trees = new ArrayList<TreeNode>();
            TreeNode treeNode;
            for (TSource tSource : sources) {
                if (tSource.getIsMenu() == 1) {
                    treeNode = new TreeNode();
                    treeNode.setName(tSource.getName());
                    treeNode.setId(tSource.getId());
                    treeNode.setLevel(tSource.getLevel());
                    if (tSource.getParent() != null) {
                        treeNode.setpId(tSource.getParent().getId());
                    }
                    treeNode.setUrl(tSource.getSourceCode());
                    treeNode.setIcon(tSource.getIcon());
                    trees.add(treeNode);
                }
            }
            if (trees.size() > 0) {
                List<TreeNode> treeNodes = TreeUtil.sourceTreeNodes(trees);
                for (TreeNode tn : treeNodes) {
                    menustr.append("<li class='has-sub'>");
                    menustr.append("<a href='javascript:;'>");
                    menustr.append("<b class='caret pull-right'></b>");
                    if (StringUtil.isNotBlank(tn.getIcon())) {
                        menustr.append("<i class='fa fa-" + tn.getIcon() + "'></i>");
                    } else {
                        menustr.append("<i class='fa fa-star'></i>");
                    }
                    menustr.append("<span>" + tn.getName() + "</span>");
                    menustr.append("</a>");
                    List<TreeNode> childrens = tn.getChildrens();
                    if (childrens != null && childrens.size() > 0) {
                        menustr.append("<ul class='sub-menu'>");
                        for (TreeNode cn : childrens) {
                            if (StringUtil.equals(cn.getUrl(), "######") && cn.getChildrens().size() > 0) {
                                menustr.append("<li class='has-sub'>");
                                menustr.append("<a href='javascript:;'><b class='caret pull-right'></b> " + cn.getName() + "</a>");
                                menustr.append("<ul class='sub-menu'>");
                                for (TreeNode subNode : cn.getChildrens()) {
                                    String url = context + subNode.getUrl();
                                    menustr.append("<li><a href=\"javascript:void(0);\" class=\"item-menu\" onclick=\"getMenuData(\'");
                                    menustr.append(url);
                                    menustr.append("\',\'displayArea\',this);\" class=\"active\">" + subNode.getName() + "</a></li>");
                                }
                                menustr.append("</ul>");
                                menustr.append("</li>");
                            } else {
                                String url = context + cn.getUrl();
                                menustr.append("<li><a href=\"javascript:void(0);\" onclick=\"getData(\'");
                                menustr.append(url);
                                menustr.append("\',\'displayArea\');\" class=\"active\">" + cn.getName() + "</a></li>");
                            }
                        }
                        menustr.append("</ul>");
                    }
                    menustr.append("</li>");
                }
            }
        }
        return menustr.toString();
    }
}
