package es.codeurjc.PracticaGrupalUsuariosSSDD;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios.UserService;
import es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios.User;
import es.codeurjc.PracticaGrupalUsuariosSSDD.Usuarios.User.Estados;

@RestController
public class RestControllerUser {
	
	private Logger log = LoggerFactory.getLogger(RestControllerUser.class);
	
	@Autowired
	private UserService userService;
	

//*************************************************************************************************
	
	@Operation(summary = "Get a list of users")
	@GetMapping("/users/")
	public Collection<User> getUsers() {
		return userService.findAll(); 
	}

//*************************************************************************************************	
	
	@GetMapping("/users/{id}")
	@Operation(summary = "Get a user by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the user", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	
	public ResponseEntity<User> getUser(@Parameter(description = "id of user to be searched") @PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		
		if(user.isPresent()) {
			log.info("Usuario encontrado");
			return ResponseEntity.ok(user.get());
		}
		else {
			log.error("Usuario no encontrado");
			return ResponseEntity.notFound().build();
		}
	}
	
//*************************************************************************************************	
	
	@Operation(summary = "Create a new user")
	@PostMapping("/users/")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		
		log.info("Usuario creado correctamente");
		return ResponseEntity.created(location).body(user);
	}
	
//*************************************************************************************************	
	
	@Operation(summary = "Delete an existing user by its id")
	@DeleteMapping("/users/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User deleted", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	
	public ResponseEntity<User> deleteUser(@PathVariable Long id) {
		
		Optional<User> user = userService.findById(id);
		if(user.isPresent()) {
			user.get().setEstado(Estados.Inactivo);
			userService.save(user.get());
			log.info("Usuario borrado correctamente");
			return ResponseEntity.ok(user.get());
		}
		else {
			log.error("Usuario no encontrado");
			return ResponseEntity.notFound().build();
		}
	}	
	
//*************************************************************************************************	
	
	@Operation(summary = "Modify an existing user by its id")
	@PutMapping("/users/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	
	public ResponseEntity<User> updateUser(@PathVariable Long id,
										@RequestBody User newUser) {
		
		Optional<User> user = userService.findById(id);
		if(user.isPresent()) {
			newUser.setId(id);
			userService.save(newUser);
			log.info("Usuario modificado correctamente");
			return ResponseEntity.ok(user.get());
		}
		else {
			log.error("Usuario no encontrado");
			return ResponseEntity.notFound().build();
		}
	}
	
//*************************************************************************************************
	
	@Operation(summary = "Decrease the user´s balance")
	@PutMapping("/users/bookings/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User´s balance updated", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	
	public ResponseEntity<User> reservaBicicleta(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		if(user.isPresent()) {
			double coste_bici = 10;
			double coste_total = coste_bici + 2*coste_bici;
			if(user.get().getEstado().equals(Estados.Activo) && user.get().getSaldo() >= coste_total) {
				user.get().setSaldo(user.get().getSaldo() - coste_total);
				userService.save(user.get());
				log.info("Reserva cobrada correctamente");
				return ResponseEntity.ok(user.get());
			}
			else {
				log.error("Usuario está inactivo o no tiene saldo");
				return ResponseEntity.badRequest().build();
			}
		}
		else {
			log.error("Usuario no encontrado");
			return ResponseEntity.notFound().build();
		}
	}
	
//*************************************************************************************************
	
	@Operation(summary = "Returns the deposit to the user")
	@PutMapping("/users/devolucion_bookings/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User´s balance updated", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	
	public ResponseEntity<User> devolverFianza(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		if(user.isPresent()) {
			double coste_bici = 10;
			double coste_fianza = 2*coste_bici;
			user.get().setSaldo(user.get().getSaldo() + coste_fianza);
			userService.save(user.get());
			log.info("Fianza devuelta correctamente");
			return ResponseEntity.ok(user.get());
		}
		else {
			log.error("Usuario no encontrado");
			return ResponseEntity.notFound().build(); 
		}
	}
	
	
}
