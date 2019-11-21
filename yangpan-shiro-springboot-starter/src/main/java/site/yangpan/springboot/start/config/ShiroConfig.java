package site.yangpan.springboot.start.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * shiro配置类
 * Created by yangpan on 2019-11-21 14:35.
 */
@Configuration
public class ShiroConfig {

    /**
     * 构建凭证匹配器
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//加密算法md5，具体看注册时使用的加密算法
        matcher.setHashIterations(1);//加密次数，具体看注册时对密码加密的次数
        return matcher;
    }

    /**
     * 构建自定义realm
     * @return
     */
    @Bean
    CustomRealm customRealm() {
        CustomRealm myShiroRealm = new CustomRealm();
        //设置凭证匹配器
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 构建SecurityManager
     * @return
     */
    @Bean
    DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(customRealm());
        return manager;
    }

    /**
     * 构建shiro过滤器
     * @return
     */
    @Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();

        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        definition.addPathDefinition("/user/login", "anon");

        definition.addPathDefinition("/user/logout", "logout");

        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        definition.addPathDefinition("/**", "authc");


        //登录用户拥有“admin”角色才能访问"/admin/**"
        //chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        //登录用户拥有"document:read"权限才能访问"docs/**"
        //chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
        return definition;
    }
}
