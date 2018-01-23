package io.cess.core.jpa;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

//@Configuration
public class JpaCT  implements BeanDefinitionRegistryPostProcessor {

    private BeanFactory beanFactory;

    private static ClassPool classPool;

    static {
        classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(io.cess.core.jpa.JpaConfig.class)); //主要用于web环境
    }


    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {


//        XmlBeanDefinitionReader reader;
        Object obj = this.createDataSource();
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(obj.getClass());
//        AnnotatedGenericBeanDefinition abd = null;
//        try {
//            abd = new AnnotatedGenericBeanDefinition(Class.forName("lin.sample.RepositorySecondaryConfig"));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
//
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, "secondaryDS");

        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException{
        this.beanFactory = beanFactory;
    }

    protected DataSource createDataSource(){
        DataSource dataSource = DataSourceBuilder.create().build();
        return createDataSourceProxy(dataSource);
    }

    private DataSource createDataSourceProxy(DataSource target){

        CtClass ctCls = classPool.makeClass(target.getClass().getName()+"$$prooxy"+this.hashCode());
        try {
            ctCls.setSuperclass(classPool.get(target.getClass().getName()));

            ClassFile ccFile = ctCls.getClassFile();
            ConstPool constPool = ccFile.getConstPool();

            // 添加类注解
            AnnotationsAttribute bodyAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation bodyAnnot = new Annotation("org.springframework.boot.context.properties.ConfigurationProperties", constPool);
            bodyAnnot.addMemberValue("prefix", new StringMemberValue("lin.secondary.datasource", constPool));
            bodyAttr.addAnnotation(bodyAnnot);

            ccFile.addAttribute(bodyAttr);

        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            return (DataSource) ctCls.toClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        return null;

    }
}