package com.fuint.application.web.backend.goods;

import com.fuint.application.enums.StatusEnum;
import com.fuint.application.service.setting.SettingService;
import com.fuint.application.util.CommonUtil;
import com.fuint.base.shiro.ShiroUser;
import com.fuint.exception.BusinessCheckException;
import com.fuint.base.shiro.util.ShiroUserHelper;
import com.fuint.application.dao.entities.*;
import com.fuint.application.dto.*;
import com.fuint.application.service.goods.CateService;
import com.fuint.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.fuint.base.dao.pagination.PaginationRequest;
import com.fuint.base.dao.pagination.PaginationResponse;
import com.fuint.base.util.RequestHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商品分类管理controller
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@Controller
@RequestMapping(value = "/backend/goods/cate")
public class cateController {

    /**
     * 商品分类服务接口
     */
    @Autowired
    private CateService cateService;

    @Autowired
    private SettingService settingService;

    /**
     * 商品分类列表
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws BusinessCheckException
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("/backend/goods/cate/list")
    public String queryList(HttpServletRequest request, HttpServletResponse response, Model model) throws BusinessCheckException {
        PaginationRequest paginationRequest = RequestHandler.buildPaginationRequest(request, model);
        paginationRequest.getSearchParams().put("NQ_status", StatusEnum.DISABLE.getKey());
        paginationRequest.setSortColumn(new String[]{"sort asc", "status asc"});
        PaginationResponse<MtGoodsCate> paginationResponse = cateService.queryCateListByPagination(paginationRequest);

        String imagePath = settingService.getUploadBasePath();

        model.addAttribute("paginationResponse", paginationResponse);
        model.addAttribute("imagePath", imagePath);

        return "goods/cate/list";
    }

    /**
     * 删除分类
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("backend/goods/cate/delete")
    @RequestMapping(value = "/delete/{id}")
    public String delete(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("id") Integer id) throws BusinessCheckException {
        ShiroUser shiroUser = ShiroUserHelper.getCurrentShiroUser();

        if (shiroUser == null) {
            return "redirect:/login";
        }

        String operator = shiroUser.getAcctName();
        cateService.deleteCate(id, operator);

        ReqResult reqResult = new ReqResult();
        reqResult.setResult(true);

        return "redirect:/backend/goods/cate/list";
    }

    /**
     * 添加分类初始化页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("backend/goods/cate/add")
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response, Model model) throws BusinessCheckException {
        return "goods/cate/add";
    }

    /**
     * 新增商品分类页面
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param model    SpringFramework Model对象
     */
    @RequiresPermissions("backend/goods/cate/create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createHandler(HttpServletRequest request, HttpServletResponse response, Model model) throws BusinessCheckException {
        String name = CommonUtil.replaceXSS(request.getParameter("name"));
        String description = CommonUtil.replaceXSS(request.getParameter("description"));
        String logo = CommonUtil.replaceXSS(request.getParameter("image"));
        String sort = StringUtil.isNotEmpty(request.getParameter("sort")) ? request.getParameter("sort") : "1";

        MtGoodsCate info = new MtGoodsCate();

        info.setName(name);
        info.setDescription(description);
        info.setLogo(logo);
        info.setSort(Integer.parseInt(sort));

        String operator = ShiroUserHelper.getCurrentShiroUser().getAcctName();
        info.setOperator(operator);

        cateService.addCate(info);

        return "redirect:/backend/goods/cate/list";
    }

    /**
     * 编辑初始化页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("backend/goods/cate/edit")
    @RequestMapping(value = "/edit/{id}")
    public String edit(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("id") Integer id) throws BusinessCheckException {
        MtGoodsCate mtCate = cateService.queryCateById(id);

        model.addAttribute("info", mtCate);

        String imagePath = settingService.getUploadBasePath();
        model.addAttribute("imagePath", imagePath);

        return "goods/cate/edit";
    }

    /**
     * 编辑商品分类
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("backend/goods/cate/update")
    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, Model model) throws BusinessCheckException {
        String id = request.getParameter("id");
        String name = CommonUtil.replaceXSS(request.getParameter("name"));
        String description = CommonUtil.replaceXSS(request.getParameter("description"));
        String logo = CommonUtil.replaceXSS(request.getParameter("image"));
        String status = CommonUtil.replaceXSS(request.getParameter("status"));
        String sort = CommonUtil.replaceXSS(request.getParameter("sort"));

        MtGoodsCate info = new MtGoodsCate();
        info.setId(Integer.parseInt(id));
        info.setName(name);
        info.setDescription(description);
        info.setLogo(logo);
        info.setSort(Integer.parseInt(sort));
        info.setStatus(status);

        String operator = ShiroUserHelper.getCurrentShiroUser().getAcctName();
        info.setOperator(operator);
        cateService.updateCate(info);

        return "redirect:/backend/goods/cate/list";
    }
}
