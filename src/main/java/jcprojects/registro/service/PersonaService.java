package jcprojects.registro.service;

import jcprojects.registro.dto.request.PersonaDto;
import jcprojects.registro.dto.response.MensajeResponse;

import java.util.List;

public interface PersonaService {
    MensajeResponse crearPersona(PersonaDto personaDto);
    List<PersonaDto> verPersonas();
    PersonaDto verPersona(Long id);
    MensajeResponse editarPersona(Long id, PersonaDto personaDto);
    MensajeResponse borrarPersona(Long id);
    MensajeResponse cantidadPersonasPorGenero();
    PersonaDto verPersonaMayor();
    PersonaDto verPersonaMenor();
    List<PersonaDto> verPersonasPorProvincia(String provincia);
}
