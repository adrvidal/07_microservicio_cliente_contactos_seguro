package controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import model.Persona;

@RestController
public class PersonasController {

	String urlBase = "http://localhost:8080";
	@Autowired
	RestTemplate template;

	@GetMapping(value = "/personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Persona> altaNueva(@PathVariable("nombre") String nombre, @PathVariable("email") String email,
			@PathVariable("edad") int edad) {
		Persona persona = new Persona(nombre, email, edad);
		// realiza dos llamadas al servicio remoto, la primera para agregar una nueva
		// persona
		// y la segunda para recuperar las personas existentes
		template.postForLocation(urlBase + "/contactos", persona);
		Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
		return Arrays.asList(personas);
	}

	@GetMapping(value = "/personas/{edad1}/{edad2}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Persona> buscarEdades(@PathVariable("edad1") int edad1, @PathVariable("edad2") int edad2) {
		Persona[] personas = template.getForObject(urlBase + "/contactos", Persona[].class);
		List<Persona> listPersona= Arrays.asList(personas);
        Stream<Persona> filtered_data = listPersona.stream().filter(p -> (p.getEdad() >= edad1) && (p.getEdad() <= edad2));
		return filtered_data.collect(Collectors.toList());
	}

}
