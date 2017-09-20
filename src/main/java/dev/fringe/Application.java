package dev.fringe;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@ImportResource("classpath:/app.xml")
@PropertySource("classpath:/app.properties")
@SuppressWarnings("resource")
public class Application {
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Application.class).getBean(Application.class);
	}
}
