package org.apdoer.channel.server.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/27 10:49
 */
@Slf4j
public class ThreadPool {

    /**
     * 线程池中会维护一个最小线程数量,即使处于空闲,也不会被销毁,除非设置 allowCoreThreadTimeOut
     */
    private static final int CORE_POOL_SIZE = 10;
    /**
     * 线程池最大大小.一个任务被提交到线程池,会先缓存到队列中,如果队列满了,且core < max,会创建一个新线程来执行任务,
     * 所以对于无解队列,此参数无效
     */
    private static final int MAX_POOL_SIZE = 10;

    /**
     * 工作队列的容量,工作队列用于保存和传输等待执行任务的阻塞队列,
     *    关于工作队列
     *          1.如果当前线程数<core,就不会入队,直接创建新线程执行
     *          2.如果当前线程>=core,就会先入队等待,而不是直接创建线程
     *          3.如果队列满了,当前线程数量<max.会直接创建线程执行,否则会执行对应的线程饱和策略
     */
    private final static int INITIAL_CAPACITY = 500;
    /**
     * 空闲线程存活时间:大于核心线程数的线程在空闲状态,会在此指定时间后被销毁,
     * TimeUnit 即为空闲线程存活的时间单位
     */
    private final static long DEFAULT_KEEP_ALIVE_TIME = 30L;

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * ArrayBlockingQueue有界队列,
     * LinkedBlockingQueue 无界队列,无界队列时拒绝策略无效
     * SynchronousQueue 同步阻塞队列,其中每个插入操作必须等待另一个线程的对应移除操作，等待过程一直处于阻塞状态，
     *          同理，每一个移除操作必须等到另一个线程的对应插入操作
     * PriorityBlockingQueue 优先级队列,提交的Runnable应该定义优先级策略
     *
     *
     *
     * 自定义的线程工厂,用于创建线程,方法为new Thread(),同一个Factory创建的线程属于同一个线程组,优先级都为normal
     * 命名风格都为 pool-m-thread-n（m为线程池的编号，n为线程池内的线程编号）,当然也可以自定义
     *
     * 拒绝策略:线程池满了,新的任务提交的处理方式
     *  1.拒绝,默认
     *  2.交由提交任务的线程处理,
     *  3.删除新提交任务
     *  4.删除队列头部任务,尝试入队,失败则重复此过程
     */
    private ThreadPool() {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                DEFAULT_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(INITIAL_CAPACITY),
                ChannelThreadFactory.getInstance(),
                (r, executor) -> {

        });
    }

    private static class innerThreadPool {
        private static final ThreadPool INSTANCE = new ThreadPool();
    }

    public static ThreadPool getInstance() {
        return innerThreadPool.INSTANCE;
    }

    public void execute(Runnable r) {
        if (size() == INITIAL_CAPACITY) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.execute(r);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public int size() {
        return threadPoolExecutor.getQueue().size();
    }
}
