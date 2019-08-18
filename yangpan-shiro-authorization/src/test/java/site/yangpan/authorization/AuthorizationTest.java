package site.yangpan.authorization;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Shiro授权
 * Created by yangpan on 2019-08-18 17:50.
 */
public class AuthorizationTest {

    @Test
    public void testAuthorization(){
        //1.构建Realm
        SimpleAccountRealm realm = new SimpleAccountRealm();
        realm.addAccount("root", "123", "role1", "role2", "role3");
        realm.addAccount("yangpan", "123", "role1", "role2");

        //2.构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);

        //3.通过SecurityUtils设置SecurityManager
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //4.通过SecurityUtils获取Subject
        Subject subject = SecurityUtils.getSubject();

        //5.构建登录的账号
        AuthenticationToken token = new UsernamePasswordToken("root", "123");
        try {
            //6.登录
            subject.login(token);
            System.out.println("isAuthenticated:" + subject.isAuthenticated());
        } catch ( UnknownAccountException uae ) {
            System.err.println("用户名不存在！");
        }


        //=====================================================================================================

        /*
         * 授权操作
         * Shiro 提供的 checkRole/checkRoles 和 hasRole/hasAllRoles 不同的地方是它在判断为假的情况下会抛出 UnauthorizedException 异常。
         */


        //判断拥有角色：role1
        Assert.assertTrue(subject.hasRole("role1"));
        //判断拥有角色：role1 and role2
        Assert.assertTrue(subject.hasAllRoles(Arrays.asList("role1", "role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] result = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true, result[0]);
        Assert.assertEquals(true, result[1]);
        Assert.assertEquals(true, result[2]);



        //断言拥有角色：role1
        subject.checkRole("role1");
        //断言拥有角色：role1 and role3 失败抛出异常
        subject.checkRoles("role1", "role3");

        //=====================================================================================================

    }
}
