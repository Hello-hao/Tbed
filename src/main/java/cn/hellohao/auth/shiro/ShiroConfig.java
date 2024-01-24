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

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String, Filter> filters = bean.getFilters();
        filters.put("JWT",new SubjectFilter());
        bean.setFilters(filters);
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/verifyCode","anon");
        filterMap.put("/verifyCodeForRegister","anon");
        filterMap.put("/verifyCodeForRetrieve","anon");
        filterMap.put("/verifyCodeFortowSendEmail","anon");
        filterMap.put("/api/**","anon");
        filterMap.put("/w/**","anon");
        filterMap.put("/user/**","anon");
        filterMap.put("/ota/**","anon");
        filterMap.put("/admin/root/**","roles[admin]");
        filterMap.put("/**","JWT");
        bean.setLoginUrl("/jurisError");
        bean.setUnauthorizedUrl("/authError");
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;

    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRememberMeManager(null);
        return defaultWebSecurityManager;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }


}

