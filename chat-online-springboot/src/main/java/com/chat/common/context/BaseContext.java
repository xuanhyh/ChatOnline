package com.chat.common.context;

/*
使用 ThreadLocal 可以避免在方法参数中传递用户身份信息，
保证每个线程都有自己独立的用户身份信息，
而且可以确保在同一个请求中的不同方法或组件中能够共享同一个用户身份信息，
而不会受到线程池的影响
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(()->8888L);

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
