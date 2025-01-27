package com.fuint.application.web.rest;

import com.fuint.application.dao.entities.MtRefund;
import com.fuint.application.dto.RefundDto;
import com.fuint.application.dto.UserOrderDto;
import com.fuint.application.service.order.OrderService;
import com.fuint.application.service.refund.RefundService;
import com.fuint.exception.BusinessCheckException;
import com.fuint.application.ResponseObject;
import com.fuint.application.BaseController;
import com.fuint.application.dao.entities.MtUser;
import com.fuint.application.service.token.TokenService;
import com.fuint.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 售后类controller
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@RestController
@RequestMapping(value = "/rest/refund")
public class RefundController extends BaseController {

    @Autowired
    private TokenService tokenService;

    /**
     * 售后服务接口
     * */
    @Autowired
    private RefundService refundService;

    /**
     * 订单服务接口
     * */
    @Autowired
    private OrderService orderService;

    /**
     * 获取订单列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseObject list(HttpServletRequest request, @RequestParam Map<String, Object> param) throws BusinessCheckException{
        String userToken = request.getHeader("Access-Token");
        MtUser userInfo = tokenService.getUserInfoByToken(userToken);

        if (userInfo == null) {
            return getFailureResult(1001, "用户未登录");
        }

        param.put("userId", userInfo.getId());
        ResponseObject orderData = refundService.getUserRefundList(param);
        return getSuccessResult(orderData.getData());
    }

    /**
     * 售后订单提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseObject submit(HttpServletRequest request, @RequestBody Map<String, Object> param) throws BusinessCheckException {
        String token = request.getHeader("Access-Token");
        MtUser mtUser = tokenService.getUserInfoByToken(token);
        if (null == mtUser) {
            return getFailureResult(1001);
        }
        param.put("userId", mtUser.getId());

        String orderId = param.get("orderId") == null ? "" : param.get("orderId").toString();
        String remark = param.get("remark") == null ? "" : param.get("remark").toString();
        String type = param.get("type") == null ? "" : param.get("type").toString();
        String images = param.get("images") == null ? "" : param.get("images").toString();

        UserOrderDto order = orderService.getOrderById(Integer.parseInt(orderId));
        if (order == null || (!order.getUserId().equals(mtUser.getId()))) {
            return getFailureResult(2001);
        }

        RefundDto refundDto = new RefundDto();
        refundDto.setUserId(mtUser.getId());
        refundDto.setOrderId(order.getId());
        refundDto.setRemark(remark);
        refundDto.setType(type);
        if (order.getStoreInfo() != null) {
            refundDto.setStoreId(order.getStoreInfo().getId());
        }
        refundDto.setAmount(order.getPayAmount());
        refundDto.setImages(images);
        MtRefund refundInfo = refundService.createRefund(refundDto);

        Map<String, Object> outParams = new HashMap();
        outParams.put("refundInfo", refundInfo);

        ResponseObject responseObject = getSuccessResult(outParams);

        return getSuccessResult(responseObject.getData());
    }

    /**
     * 获取售后订单详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseObject detail(HttpServletRequest request) throws BusinessCheckException {
        String userToken = request.getHeader("Access-Token");
        MtUser mtUser = tokenService.getUserInfoByToken(userToken);

        if (mtUser == null) {
            return getFailureResult(1001, "用户未登录");
        }

        String refundId = request.getParameter("refundId");
        if (StringUtil.isEmpty(refundId)) {
            return getFailureResult(2000, "售后订单ID不能为空");
        }

        MtRefund refundInfo = refundService.getRefundById(Integer.parseInt(refundId));

        return getSuccessResult(refundInfo);
    }
}
