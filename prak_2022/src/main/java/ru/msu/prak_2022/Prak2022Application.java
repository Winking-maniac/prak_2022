package ru.msu.prak_2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.postgresql.Driver;
//import ru.msu.prak_2022.student;
//import ru.msu.prak_2022.DAOFactory;

import java.util.List;

@SpringBootApplication
public class Prak2022Application {
	public static void main(String[] args) {
		SpringApplication.run(Prak2022Application.class, args);
//		Class.forName("org.postgresql.Driver")
	}
}
