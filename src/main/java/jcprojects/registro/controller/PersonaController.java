package jcprojects.registro.controller;

import jcprojects.registro.dto.request.PersonaDto;
import jcprojects.registro.dto.response.MensajeResponse;
import jcprojects.registro.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/persona")
public class PersonaController {

    @Autowired
    PersonaService personaService;

    @PostMapping("/crear")
    public MensajeResponse crearPersona(@Valid @RequestBody PersonaDto personaDto) {
        return personaService.crearPersona(personaDto);
    }

    @GetMapping("/verTodos")
    public ResponseEntity<List<PersonaDto>> verPersonas() {
        return new ResponseEntity<>(personaService.verPersonas(), HttpStatus.OK);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<PersonaDto> verPersona(@Valid @PathVariable Long id) {
        return new ResponseEntity<>(personaService.verPersona(id), HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    public MensajeResponse editarPersona(@Valid @PathVariable Long id, @Valid @RequestBody PersonaDto personaDto) {
        return personaService.editarPersona(id, personaDto);
    }

    @DeleteMapping("/borrar/{id}")
    public MensajeResponse borrarPersona(@Valid @PathVariable Long id) {
        return personaService.borrarPersona(id);
    }

    @GetMapping("/porGenero")
    public MensajeResponse cantidadPersonasPorGenero() {
        return personaService.cantidadPersonasPorGenero();
    }

    @GetMapping("/mayor")
    public PersonaDto verPersonaMayor() {
        return personaService.verPersonaMayor();
    }

    @GetMapping("/menor")
    public PersonaDto verPersonaMenor() {
        return personaService.verPersonaMenor();
    }

    @GetMapping("/porProvincia/{provincia}")
    public List<PersonaDto> verPersonasPorProvincia(@Valid @PathVariable String provincia) {
        return personaService.verPersonasPorProvincia(provincia);
    }
}