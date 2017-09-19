package dev.fringe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import dev.fringe.socket.SocketServer;

@ComponentScan
@ImportResource(value={"classpath:/app.xml"})
public class Application {

    @Autowired 
    private SocketServer socketServer;

	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(Application.class).getBean(Application.class).run(args);
	}
    
    public void run(String[] args) {
    	try {
			socketServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
