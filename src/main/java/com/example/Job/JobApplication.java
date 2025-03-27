package com.example.Job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class JobApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(JobApplication.class, args);
		// tự động mở trình duyệt, vào link swagger
//		openSwaggerUI(context);
	}

	private static void openSwaggerUI(ApplicationContext context) {
		try {
			Environment env = context.getEnvironment();
			String host = env.getProperty("server.address", "localhost"); // Lấy host, mặc định localhost
			String port = env.getProperty("server.port", "8080"); // Lấy port, mặc định 8080

			String url = String.format("http://%s:%s/swagger-ui/index.html", host, port);
			System.out.println("Swagger UI: " + url);

			// if (Desktop.isDesktopSupported()) {
			// Desktop.getDesktop().browse(new URI(url));
			// } else {
			// System.out.println("Unable to open browser, please visit: " + url);
			// }

			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win")) {
				new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
			} else if (os.contains("mac")) {
				new ProcessBuilder("open", url).start();
			} else if (os.contains("nix") || os.contains("nux")) {
				new ProcessBuilder("xdg-open", url).start();
			} else {
				System.out.println("Unable to open browser, please visit: " + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
