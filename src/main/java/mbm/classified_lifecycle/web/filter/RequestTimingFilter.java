package mbm.classified_lifecycle.web.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestTimingFilter implements Filter {

  private static final long THRESHOLD_MS = 5;

  @Override
  public void init(final FilterConfig filterConfig) {
    // No initialization required
  }

  @Override
  public void doFilter(
      final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    final long startTime = System.currentTimeMillis();

    chain.doFilter(request, response);

    final long duration = System.currentTimeMillis() - startTime;
    if (duration > THRESHOLD_MS && request instanceof jakarta.servlet.http.HttpServletRequest) {
      log.warn(
          "Request to [{}] took {} ms to process",
          ((HttpServletRequest) request).getRequestURL(),
          duration);
    }
  }

  @Override
  public void destroy() {
    // No cleanup required
  }
}
