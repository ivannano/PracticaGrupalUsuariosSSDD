package es.codeurjc.PracticaGrupalUsuariosSSDD;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios.User;
import es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios.UserService;


@Component
@Profile("local")
public class DataBase {
		
	@Autowired
	private UserService userService;

		
	@PostConstruct
	public void init() {
		//Usuarios
		userService.save(new User(123L, "pepito", "Pepe", "password"));
		userService.save(new User(101L, "david123","David", "password1"));
		userService.save(new User(102L, "alex_valor","Valor", "password2"));
		userService.save(new User(103L, "ivansito", "Ivan", "password3"));
		userService.save(new User(104L, "mariete", "Mario", "password4"));
				
	}
}