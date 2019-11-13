package site.yangpan.shiro.customRealm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义realm测试
 * Created by yangpan on 2019-11-13 18:22.
 */
public class CustomRealmTest {

    @Test
    public void testCustomRealm(){

        //1.构建自定义realm
        CustomRealm customRealm = new CustomRealm();

        //2.构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //3.通过SecurityUtils设置SecurityManager
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //4.通过SecurityUtils获取Subject
        Subject subject = SecurityUtils.getSubject();

        //5.构建登录的账号
        AuthenticationToken token = new UsernamePasswordToken("root", "123456");

        try {
            //6.登录
            subject.login(token);
            System.out.println("isAuthenticated:" + subject.isAuthenticated());
        } catch (UnknownAccountException uae ) {
            System.err.println("用户名不存在！");
        } catch (IncorrectCredentialsException ice){
            System.err.println("密码错误！");
        }

        //7.检查角色
        subject.checkRole("admin");


        //8.检查权限
        subject.checkPermission("user:update");

        //9.退出
        subject.logout();
        System.out.println("isAuthenticated:" + subject.isAuthenticated());
    }

}
