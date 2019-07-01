package com.mc.Mozo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mc.Mozo.services.Initialize;

@SpringBootApplication
public class MozoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MozoApplication.class, args);
		Initialize init = new Initialize();
		try {
			try {
				init.startfirebase();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
