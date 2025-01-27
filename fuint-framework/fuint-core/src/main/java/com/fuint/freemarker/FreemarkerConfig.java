package com.fuint.freemarker;

import com.fuint.util.StringUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * freemarker 配置
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
@Configuration
public class FreemarkerConfig {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerConfig.class);

    @Autowired
    private ServletContext context;

    @Autowired
    private Environment env;

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setViewClass(FreeMarkerView.class);
        resolver.setCache(false);
        resolver.setContentType(env.getProperty("freemarker.contentType"));
        resolver.setRequestContextAttribute(env.getProperty("freemarker.requestContextAttribute"));
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);
        resolver.setAllowRequestOverride(true);
        resolver.setAllowSessionOverride(true);
        resolver.setSuffix(env.getProperty("freemarker.suffix"));
        resolver.setPrefix("/");
        resolver.setOrder(0);
        resolver.setAttributesMap(getFreemarkerStaticModels());
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freemarker.template.Configuration configuration = freemarker.template.Configuration.getDefaultConfiguration();
        try {
            WebappTemplateLoader pages = new WebappTemplateLoader(context, env.getProperty("freemarker.templatePath"));
            pages.setURLConnectionUsesCaches(false);
            pages.setAttemptFileAccess(false);

            FileTemplateLoader macro = new FileTemplateLoader(new File(env.getProperty("freemarker.macroPath")));
            MultiTemplateLoader mtl = new MultiTemplateLoader(new TemplateLoader[]{pages, macro});
            configuration.setTemplateLoader(mtl);
            configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setDateFormat(env.getProperty("freemarker.dateFormat"));
            configuration.setTimeFormat(env.getProperty("freemarker.timeFormat"));
            configuration.setDateTimeFormat(env.getProperty("freemarker.dateTimeFormat"));
            configuration.setNumberFormat(env.getProperty("freemarker.numberFormat"));
            configuration.setClassicCompatible(true);
            configuration.setBooleanFormat(env.getProperty("freemarker.booleanFormat"));
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            String autoImports = env.getProperty("freemarker.autoImports");
            Map<String, String> imports = new HashMap<String, String>();
            if (StringUtil.isNotBlank(autoImports)) {
                String[] ftlArray = autoImports.split(",");
                if (ftlArray != null && ftlArray.length > 0) {
                    String[] kv;
                    for (String ftl : ftlArray) {
                        kv = ftl.split("as");
                        imports.put(StringUtil.trim(kv[1]), StringUtil.trim(kv[0]));
                    }
                }
            }
            configuration.setAutoImports(imports);
            freeMarkerConfigurer.setConfiguration(configuration);
        } catch (Exception e) {
            logger.error("freemarker template load error: {}", e);
            throw new RuntimeException("freemarker template load error" + e);
        }
        return freeMarkerConfigurer;
    }

    @Bean
    public PropertiesFactoryBean getFreemarkerPropertiesFactoryBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("staticClass.properties"));
        return propertiesFactoryBean;
    }

    @Bean
    public FreemarkerStaticModels getFreemarkerStaticModels() {
        FreemarkerStaticModels freemarkerStaticModels = FreemarkerStaticModels.getInstance();
        PropertiesFactoryBean propertiesFactoryBean = getFreemarkerPropertiesFactoryBean();
        try {
            Properties properties = propertiesFactoryBean.getObject();
            freemarkerStaticModels.setStaticModels(properties);
        } catch (IOException e) {
            logger.error("freemarker static class load error : {}", e);
            throw new RuntimeException("freemarker static class load error");
        }
        return freemarkerStaticModels;
    }
}
