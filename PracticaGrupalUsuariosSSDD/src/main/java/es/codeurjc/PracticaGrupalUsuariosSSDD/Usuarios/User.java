package es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String login;
	private String password;
	private String name;
	private LocalDate date;
	private Estados estado;
	private float saldo;
	
	public enum Estados {
		Activo, Inactivo;
	}
	
	public User() {
		
	}
	
	public User(Long id, String login, String nombre, String contraseña) {
		this.id = id;
		this.login = login;
		this.password = contraseña;
		this.name = nombre;
		this.date = LocalDate.now();
		this.estado = Estados.Activo;
		this.saldo = 0;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getLogin( ) {
		return this.login;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getName() {
		return this.name;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public Estados getEstado() {
		return this.estado;
	}
	
	public float getSaldo() {
		return this.saldo;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	
	public void setEstado(Estados newActive) {
		this.estado = newActive;
	}
	
	public void setSaldo(float newSaldo) {
		this.saldo = newSaldo;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User) {
			User user = (User) obj;
			if(this.getId().equals(user.getId())) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		String s = "Nombre: " + this.name + ", Identificador: " + this.id + ", Estado: ";
		if(this.estado.equals("activo")) {
			s = s + "activo";
		}
		else {
			s = s + "inactivo";
		}
		return s;
	}
}
