package com.example.emtechelppathbackend;

import com.example.emtechelppathbackend.utils.HostNameCapture;
import com.example.emtechelppathbackend.utils.PhoneNoFormatter;

import net.sf.jasperreports.engine.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Controller
public class EmtElpPathBackendApplication {
	private static final String folderPath = System.getProperty("user.dir")+"/images";
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public String getHost() throws UnknownHostException {
		HostNameCapture hostNameCapture = new HostNameCapture();
		return hostNameCapture.getHost();
	}

	@GetMapping("/images/{imageName}")
	public ResponseEntity<Resource> serveImage(@PathVariable String imageName) throws IOException {

		if (folderExists(folderPath)) {
			System.out.println("The folder exists.");
		} else {
			System.out.println("The folder does not exist.");
		}
		Path imagePath = Paths.get(folderPath).resolve(imageName);
		Resource resource = new UrlResource(imagePath.toUri());

		if (resource.exists() && resource.isReadable()) {

			String contentType = "image/*";
			if (imageName.toLowerCase().endsWith(".jpg") || imageName.toLowerCase().endsWith(".jpeg")) {
				contentType = "image/jpeg";
			} else if (imageName.toLowerCase().endsWith(".png")) {
				contentType = "image/png";
			}else if (imageName.toLowerCase().endsWith(".gif")) {
				contentType = "image/gif";
			}


			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, contentType)
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	public static void main(String[] args) {


			if (!folderExists(folderPath)) {
				if (createFolder(folderPath)) {
					System.out.println("The folder was created successfully.");
				} else {
					System.err.println("Failed to create the folder.");
				}
			}

		SpringApplication.run(EmtElpPathBackendApplication.class, args);
		
	}
	private static boolean createFolder(String folderPath) {
		try {
			Path path = Paths.get(folderPath);
			Files.createDirectories(path);
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private static boolean folderExists(String folderPath) {
		Path path = Paths.get(folderPath);
		return Files.exists(path) && Files.isDirectory(path);
	}
}
