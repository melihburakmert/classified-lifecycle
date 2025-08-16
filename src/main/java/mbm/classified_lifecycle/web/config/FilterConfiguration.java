package mbm.classified_lifecycle.web.config;

import mbm.classified_lifecycle.web.filter.RequestTimingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

  @Bean
  public FilterRegistrationBean<RequestTimingFilter> requestTimingFilter() {
    final FilterRegistrationBean<RequestTimingFilter> registrationBean =
        new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestTimingFilter());
    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }
}
