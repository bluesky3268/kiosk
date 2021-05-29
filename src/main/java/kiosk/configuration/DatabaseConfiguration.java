//package kiosk.configuration;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//import javax.sql.DataSource;
//
//@Configuration
//@PropertySource("classpath:/application.properties")
//public class DatabaseConfiguration {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.datasource.hikari")
//    public HikariConfig hikariConfig(){
//        return new HikariConfig();
//    }
//
//    @Bean
//    public DataSource dataSource() throws Exception{
//        DataSource dataSource = new HikariDataSource(hikariConfig());
////        log.info(dataSource.toString());
//        return dataSource;
//    }
//
//    /**
//     * MyBatis설정
//     */
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResource("classPath:/mapper/**/*.xml"));
//        return sqlSessionFactoryBean.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

//}
