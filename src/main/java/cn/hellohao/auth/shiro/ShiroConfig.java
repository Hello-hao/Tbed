package cn.hellohao.auth.shiro;

import cn.hellohao.auth.filter.SubjectFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/3 10:37
 */

@Configuration
public class ShiroConfig {


    //shiro过滤对象
    @Bean//(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String, Filter> filters = bean.getFilters();
//        Map<String,Filter> filter = new HashMap<>();
        filters.put("JWT",new SubjectFilter());
        bean.setFilters(filters);

        //添加shiro内置过滤器
/*
        * anon: 无需认证就可以访问
        * authc: 必须认证了才可以访问
        * user: 必须拥有 记住我 功能才能访问
        * perms: 拥有对某个资源的权限才能访问
        * roles: 拥有某个角色的权限才能访问
        * */

        Map<String,String> filterMap = new LinkedHashMap<>();

        //放行页面
        filterMap.put("/verifyCode","anon");
        filterMap.put("/verifyCodeForRegister","anon");
        filterMap.put("/verifyCodeForRetrieve","anon");
        filterMap.put("/api/**","anon");
        filterMap.put("/user/**","anon");
        filterMap.put("/ota/**","anon");
        filterMap.put("/admin/root/**","roles[admin]");
        filterMap.put("/**","JWT");

        //认证失败的返回页面
        bean.setLoginUrl("/jurisError");
        //未授权页面
        bean.setUnauthorizedUrl("/authError");
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;

    }


    //安全对象
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRememberMeManager(null);
        return defaultWebSecurityManager;
    }

    //创建 realm 对象
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }


    //配置禁用session
//    @Bean
//    public StatelessDefaultSubjectFactory statelessDefaultSubjectFactory(){
//        StatelessDefaultSubjectFactory statelessDefaultSubjectFactory = new StatelessDefaultSubjectFactory();
//        return statelessDefaultSubjectFactory;
//    }

}

