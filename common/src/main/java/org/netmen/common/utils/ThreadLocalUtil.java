package org.netmen.common.utils;

/**
 * 该工具类已经弃用 使用security的上下文
 *   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 *   LoginUser loginUser = (LoginUser) authentication.getPrincipal();
 *   User user = loginUser.getUser();
 */
public class ThreadLocalUtil {
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    public static void set (Object value) {
        THREAD_LOCAL.set(value);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
