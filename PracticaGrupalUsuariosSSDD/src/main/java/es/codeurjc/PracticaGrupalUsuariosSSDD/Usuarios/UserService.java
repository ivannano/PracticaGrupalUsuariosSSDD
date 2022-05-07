package es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private UserRepository repository;
	
	public UserService(UserRepository repo) {
		this.repository = repo;
	}
	
	public User save(User user) {
		return repository.save(user);
	}
	
	/*
	public User save(User user) {
		Long id = user.getId();
		if(repository.findById(id).isPresent()) {
			return null;
		}
		else {
			User newUser = repository.save(user);
			return newUser;
		}
		
	}
	*/
	public void delete(Long Id) {
		repository.deleteById(Id);
	}
	
	
	public List<User> findAll() {
		return repository.findAll();
	}
	
	public Optional<User> findOne(long id) {
		return repository.findById(id);
	}
	
	public Optional<User> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}
}
