package cn.hellohao.auth.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 跨域请求过滤器
 *
 * @author Hellohao
 * @date 2021/6/10
 */
@Configuration
public class OriginFilter {

	@Value("${CROS_ALLOWED_ORIGINS}")
  	private String allowedOrigins;

	@SuppressWarnings("unchecked")
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOriginPatterns(Collections.singletonList(CorsConfiguration.ALL));
		corsConfiguration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
		corsConfiguration.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
		corsConfiguration.addExposedHeader("Authorization");
		source.registerCorsConfiguration("/**", corsConfiguration);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}

