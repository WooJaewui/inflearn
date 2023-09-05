package tobyspring.helloboot;

import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import tobyspring.config.autoconfig.annotation.MySpringBootApplication;

import javax.annotation.PostConstruct;

/*@Configuration
@ComponentScan*/
@MySpringBootApplication
public class HellobootApplication {

	/*@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		return args -> {
			String name = environment.getProperty("my.name");
			System.out.println("my.name : " + name);
		};
	}*/

	private final JdbcTemplate jdbcTemplate;

	public HellobootApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	void init() {
		jdbcTemplate.execute("create table if not exists hello(name varchar(50) primary key, count int)");
	}

	public static void main(String[] args) {
		SpringApplication.run(HellobootApplication.class, args);
	}
}
