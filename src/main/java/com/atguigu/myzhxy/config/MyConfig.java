package com.atguigu.myzhxy.config;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@MapperScan("com.atguigu.myzhxy.mapper")
public class MyConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){

        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500条，小于 0 如 -1 不受限制);
        return paginationInnerInterceptor;
    }


}
