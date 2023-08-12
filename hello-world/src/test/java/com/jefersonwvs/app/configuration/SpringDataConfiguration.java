package com.jefersonwvs.app.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/*
 * The @EnableJpaRepositories annotation enables scanning of the package
 * received as an argument for Spring Data repositories.
 */
@EnableJpaRepositories("com.jefersonwvs.app.repositories")
public class SpringDataConfiguration {

	@Bean
	public DataSource dataSource() {
		/*
		 * Create a data source bean.
		 */
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		/*
		 * Specify the JDBC properties—the driver.
		 */
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

		/*
		 * The URL of the database.
		 */
		dataSource.setUrl("jdbc:mysql://localhost:3306/CH02?serverTimezone=UTC");

		/*
		 * The username.
		 */
		dataSource.setUsername("root");

		/*
		 * The password.
		 */
		dataSource.setPassword("root");
		return dataSource;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		/*
		 * Create a transaction manager bean based on an entity manager factory. Every
		 * interaction with the database should occur within transaction boundaries, and
		 * Spring Data needs a transaction manager bean.
		 */
		return new JpaTransactionManager(emf);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		/*
		 * Create the JPA vendor adapter bean needed by JPA to interact with Hibernate.
		 */
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		/*
		 * Configure this vendor adapter to access a MySQL database.
		 */
		jpaVendorAdapter.setDatabase(Database.MYSQL);

		/*
		 * Show the SQL code while it is executed.
		 */
		jpaVendorAdapter.setShowSql(true);
		return jpaVendorAdapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		/*
		 * Create a LocalContainerEntityManagerFactoryBean. This is a factory bean that
		 * produces an EntityManagerFactory following the JPA standard container boot-
		 * strap contract.
		 */
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
			new LocalContainerEntityManagerFactoryBean();

		/*
		 * Set the data source.
		 */
		localContainerEntityManagerFactoryBean.setDataSource(dataSource());
		Properties properties = new Properties();

		/*
		 * Set the database to be created from scratch every time the program is executed.
		 */
		properties.put("hibernate.hbm2ddl.auto", "create");
		localContainerEntityManagerFactoryBean.setJpaProperties(properties);

		/*
		 * Set the vendor adapter.
		 */
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());

		/*
		 * Set the packages to scan for entity classes. As the Message entity is located in
		 * com.jefersonwvs.app.entities, we set this package to be scanned.
		 */
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.jefersonwvs.app.entities");

		return localContainerEntityManagerFactoryBean;
	}

}
