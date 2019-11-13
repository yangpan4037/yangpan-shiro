package site.yangpan.shiro.jdbcRealm;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Jdbc Realm 测试
 * Created by yangpan on 2019-11-13 17:01.
 */
public class JdbcRealmTest {

    //数据源
    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/yangpan_shiro_realm?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("admin");
    }



    @Test
    public void testJdbcRealm(){

        //1.构建jdbc realm，可以点进去看JdbcRealm默认查询的sql,然后根据sql建表
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        //如果实际项目中表结构不一样可以这样做
        // String sql = "select password from 自定义表 where user_name = ?";
        // jdbcRealm.setAuthenticationQuery(sql);

        //2.构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

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
