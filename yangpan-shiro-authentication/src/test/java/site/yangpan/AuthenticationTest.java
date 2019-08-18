package site.yangpan;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Shiro 认证
 * Created by yangpan on 2019-08-18 12:53.
 */
public class AuthenticationTest {

    @Test
    public void testAuthentication(){

        //1.构建Realm
        SimpleAccountRealm realm = new SimpleAccountRealm();
        realm.addAccount("root", "123");
        realm.addAccount("yangpan", "123");

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

        //7.退出
        subject.logout();
        System.out.println("isAuthenticated:" + subject.isAuthenticated());

    }
}
