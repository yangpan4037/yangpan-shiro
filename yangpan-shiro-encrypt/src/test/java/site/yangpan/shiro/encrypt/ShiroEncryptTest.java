package site.yangpan.shiro.encrypt;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Shiro 加密测试
 * Created by yangpan on 2019-11-14 12:18.
 */
public class ShiroEncryptTest {

    @Test
    public void testEncrypt(){

        //1.构建自定义realm
        CustomEncryptRealm customEncryptRealm = new CustomEncryptRealm();

        //2.构建凭证匹配器（加密对象）HashedCredentialsMatcher
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//加密算法md5，具体看注册时使用的加密算法
        matcher.setHashIterations(1);//加密次数，具体看注册时对密码加密的次数

        //3.在自定义realm中设置凭证匹配器
        customEncryptRealm.setCredentialsMatcher(matcher);

        //4.构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customEncryptRealm);


        //.通过SecurityUtils设置SecurityManager
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //.通过SecurityUtils获取Subject
        Subject subject = SecurityUtils.getSubject();

        //.构建登录的账号
        AuthenticationToken token = new UsernamePasswordToken("root", "123456");

        try {
            //.登录
            subject.login(token);
            System.out.println("isAuthenticated:" + subject.isAuthenticated());
        } catch (UnknownAccountException uae ) {
            System.err.println("用户名不存在！");
        } catch (IncorrectCredentialsException ice){
            System.err.println("密码错误！");
        }

    }
}
