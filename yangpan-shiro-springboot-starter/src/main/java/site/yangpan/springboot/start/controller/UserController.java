package site.yangpan.springboot.start.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.yangpan.springboot.start.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * UserController
 * Created by yangpan on 2019-11-21 14:43.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public Map login(UserEntity userEntity, HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        //判断用户是否认证，然后登录
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(userEntity.getUsername(), userEntity.getPassword());
            try {
                subject.login(token);
                System.out.println("isAuthenticated:" + subject.isAuthenticated());
            } catch (UnknownAccountException uae ) {
                result.put("message", "用户名不存在！");
                return result;
            } catch (IncorrectCredentialsException ice){
                result.put("message", "密码错误！");
                return result;
            }catch (Exception e){
                result.put("message", e.getMessage());
                return result;
            }
        }
        //获取上一次请求路径
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String url = savedRequest == null ? "/index" : savedRequest.getRequestUrl();
        result.put("redirectUrl", url);
        result.put("message", "登录成功！");
        return result;
    }


    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "退出成功！";
    }

    @RequestMapping("/403")
    public String auth(){
        return "没有登录，或者权限不足,请先登录！";
    }


    @RequiresRoles({"admin", "user"})
    @RequiresPermissions({"user:insert", "user:update"})
    @RequestMapping("/save")
    public String insert(){
        return "success！";
    }
}
