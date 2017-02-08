package com.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.project.common.spring.initializer.MyWebBinding;
import com.project.common.spring.interceptor.ProjectInterceptor;

@Controller
@SpringBootApplication
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})//不用springboot的默认文件配置
@EnableAsync
@EnableSwagger2//启动api文档
public class ProjectApplication extends WebMvcConfigurerAdapter{
    
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 设置参数转换器
     */
    @PostConstruct
    public void addConversionConfig() {
        handlerAdapter.setWebBindingInitializer(new MyWebBinding());
    }
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProjectApplication.class, args);
    }
    public static void main1(String[] args) throws Exception {
        SpringApplication.run(ProjectApplication.class, args);
    }
     public static void main2(String[] args) throws Exception {
        SpringApplication.run(ProjectApplication.class, args);
    }
    /**
     * 页面跳转
     * @param response
     * @throws IOException
     */
    @RequestMapping("/index")
    @ApiIgnore
    public void index(HttpServletResponse response) throws IOException{
        response.sendRedirect("login/main");
    }
    /**
     * 配置文件上传bean
     * @return
     */
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(20480000);//20m
        return multipartResolver;
    }
    /**
     *  配置转换器
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)
     */
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        //fastJsonHttpMessageConverter.getFastJsonConfig().setFeatures(Feature.DisableCircularReferenceDetect);//有了setSerializerFeatures，禁止循环应用失效
        fastJsonHttpMessageConverter.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
        fastJsonHttpMessageConverter.getFastJsonConfig().setDateFormat("yyyy-MM-dd HH:mm");//时间转换
        converters.add(fastJsonHttpMessageConverter);
        super.configureMessageConverters(converters);
    }
    /**
     * 配置拦截器
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ProjectInterceptor());
        super.addInterceptors(registry);
    }

}

