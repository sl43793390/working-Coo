package com.sl.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 全链路 traceId（T3-5）。
 * <p>
 * 每个请求进来生成一个 8 位短 ID，写进三处：
 * <ol>
 *   <li><b>MDC</b>：log4j2 的 pattern 里用 {@code %X{traceId}} 输出，
 *       同一次请求的所有日志都带同一个 ID —— 排查线上问题时，
 *       从浏览器响应头里拿到 traceId，直接 grep 日志就能把这条请求的全部日志串起来；</li>
 *   <li><b>响应头 X-Trace-Id</b>：用户报错时只要他把这个值贴出来就够了；</li>
 *   <li><b>请求属性</b>：同线程内其它组件（比如错误处理器）也能拿到。</li>
 * </ol>
 * 上游已经带 X-Trace-Id 时（网关 / Nginx 透传）沿用上游的值，不重新生成，
 * 这样跨服务的链路能连起来。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

	public static final String HEADER = "X-Trace-Id";
	public static final String MDC_KEY = "traceId";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String traceId = resolve(request);
		MDC.put(MDC_KEY, traceId);
		request.setAttribute(MDC_KEY, traceId);
		try {
			response.setHeader(HEADER, traceId);
			chain.doFilter(request, response);
		} finally {
			// 必须清掉：Servlet 容器线程是复用的，不清会让下一个请求复用上一个用户的 traceId
			MDC.remove(MDC_KEY);
		}
	}

	private static String resolve(HttpServletRequest request) {
		String upstream = request.getHeader(HEADER);
		if (StringUtils.hasText(upstream) && upstream.length() <= 64) {
			return upstream;
		}
		// 取 UUID 前 8 位：碰撞概率在单机量级可忽略，肉眼可读性远好于 36 位全串
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}
}
