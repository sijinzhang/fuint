package com.fuint.base.shiro;

import java.util.List;
import com.fuint.base.dao.entities.TDuty;
import com.fuint.base.service.Base.ShiroUserService;
import com.fuint.base.service.BaseService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * shiro认证授权realm实现类
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

    @Lazy //延迟到第一次使用时候注入
    @Autowired
    private ShiroUserService shiroUserService;
    @Lazy //延迟到第一次使用时候注入
    @Autowired
    private BaseService baseService;

    /**
     * 认证回调函数,登录时调用.
     *
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
        return shiroUserService.createShiroUserByAccountName(token.getUsername());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     *
     * @see AuthorizingRealm#doGetAuthorizationInfo(PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(baseService.findRoles(shiroUser.getId()));
        List<TDuty> duties = shiroUser.getDuties();
        if (duties != null && duties.size() > 0) {
            for (TDuty tDuty : duties) {
                authorizationInfo.addStringPermissions(tDuty.getPermissionsName());
            }
        }
        return authorizationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);

    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    @Override
    protected void doClearCache(PrincipalCollection principalcollection) {
        Object principal = principalcollection.getPrimaryPrincipal();
        super.getAuthenticationCache().remove(principal.toString());
        super.getAuthorizationCache().remove(principal.toString());
    }
}
