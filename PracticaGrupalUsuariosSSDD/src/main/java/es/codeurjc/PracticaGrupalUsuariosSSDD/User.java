package es.codeurjc.PracticaGrupalUsuariosSSDD;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	private String name;
}
