package com.fr1nge.myblog.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CodeGenerator {

    /**
     * 作者名称
     */
    private static final String AUTHOR = "author";

    /**
     * mysql连接地址
     */
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/my_blog?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";

    /**
     * 驱动名称
     */
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * 连接数据库的用户名
     */
    private static final String USER_NAME = "root";

    /**
     * 连接数据库的密码
     */
    private static final String PASSWORD = "123456";

    /**
     * 生成文件输出目录
     */
    private static final String outPutDir = "/src/main/java/com/fr1nge/myblog";

    private static final String packageController = "com.fr1nge.myblog.controller";
    private static final String packageEntity = "com.fr1nge.myblog.entity";
    private static final String packageService = "com.fr1nge.myblog.service";
    private static final String packageMapper = "com.fr1nge.myblog.mapper";
    private static final String packageServiceImpl = "com.fr1nge.myblog.service.impl";




    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + outPutDir);
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setActiveRecord(true);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setBaseColumnList(true);
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(URL);
        dsc.setDriverName(DRIVER_NAME);
        dsc.setUsername(USER_NAME);
        dsc.setPassword(PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setEntity("entity").setMapper("dao");
        pc.setXml("mapper").setParent("");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("packageController", packageController);
                map.put("packageEntity", packageEntity);
                map.put("packageService", packageService);
                map.put("packageMapper", packageMapper);
                map.put("packageServiceImpl", packageServiceImpl);
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setXml("/templates/generator/mapper.xml");
        templateConfig.setController("/templates/generator/controller.java");
        templateConfig.setEntity("/templates/generator/entity.java");
        templateConfig.setService("/templates/generator/service.java");
        templateConfig.setServiceImpl("/templates/generator/serviceImpl.java");
        templateConfig.setMapper("/templates/generator/mapper.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setEntityBuilderModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");

        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(false);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}