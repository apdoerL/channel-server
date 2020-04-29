import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//package org.apdoer.channel.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
///**
// * spring扩展配置<br/> 线程池控制定时任务
// *
// * @author apdoer
// * @since JDK 1.8
// */
//@Configuration
//public class SpringExConfiguration {
    //
//	/**
//	 * 自定义异步线程池，可控制线程数
//	 */
//	@Bean(destroyMethod = "destroy")
//	public AsyncTaskExecutor taskExecutor() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		// 诊断jvm信息时很有用
//		executor.setThreadNamePrefix( "msger-executor" );
//		// 单线程listener设计模式也能得到高吞吐量
//		executor.setCorePoolSize( 1 );
//		// 标准设置,cpu内核数*2,设置过大将影响线程上线文切换,开销巨大,整个msger应用只使用这一个线程池
//		int maxPoolSize = 2 * Runtime.getRuntime().availableProcessors();
//		executor.setMaxPoolSize( maxPoolSize );
//		// 队列最大数,拒绝使用Integer.MAX,并发过大时产生oom,可能导致jvm假死/宕机,提前做好堆内存容量规划并修改jvm启动参数
//		executor.setQueueCapacity( 10000 );
//		// 设置拒绝策略,超过队列最大数则直接丢弃当前所提交的任务以保护系统
//		executor.setRejectedExecutionHandler( new ThreadPoolExecutor.DiscardPolicy() );
//		return executor;
//	}
//    @Configuration
//    private class TaskSchedulerConfig extends WebMvcConfigurerAdapter {
//
//        @Value("${threadpool.size:10}")
//        private int threadPoolSize;
//
//        @Bean
//        public TaskScheduler scheduledExecutorService() {
//            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//            scheduler.setPoolSize(threadPoolSize);
//            scheduler.setThreadNamePrefix("scheduled-thread-");
//            return scheduler;
//        }
//    }
//}
