package site.yangpan.shiro.customRealm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm需要实现AuthorizingRealm
 * Created by yangpan on 2019-11-13 18:10.
 */
public class CustomRealm extends AuthorizingRealm {

    /**
     * 授权方法
     * @param principals 授权信息
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.从主体信息中获取用户名
        String username = (String)principals.getPrimaryPrincipal();

        //2.通过用户名获取角色
        Set<String> roles = getRolesByUsername(username);

        //3.通过角色获取权限
        Set<String> permissions = getPermissionsByRole(roles);

        //4.创建返回的authorizationInfo对象
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }


    /**
     * 认证方法
     * @param token 认证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体信息中获取用户名
        String username = (String) token.getPrincipal();

        //2.通过用户名去数据库获取凭证
        String password = getPasswordByUsername(username);

        if(password == null) return null;

        //3.创建返回的AuthorizationInfo对象
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password,"customRealm");

        return authenticationInfo;
    }



    /**
     * 这里模拟从数据库查询密码
     * @param username 用户名
     * @return
     */
    private String getPasswordByUsername(String username) {
        if("root".equals(username)) return "123456";
        return null;
    }

    /**
     * 这里模拟从数据库查询角色
     * @param username 用户名
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        return new HashSet<>(Arrays.asList(new String[]{"admin", "user"}));
    }

    /**
     * 这里模拟从数据库查询权限
     * @param roles 角色
     * @return
     */
    private Set<String> getPermissionsByRole(Set<String> roles) {
        return new HashSet<>(Arrays.asList(new String[]{"user:insert", "user:update", "user:delete", "user:select"}));
    }
}
