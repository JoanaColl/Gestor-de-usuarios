package jcprojects.registro.service;

import jcprojects.registro.dto.request.PersonaDto;
import jcprojects.registro.dto.response.MensajeResponse;
import jcprojects.registro.entity.Persona;
import jcprojects.registro.exception.InternalServerErrorException;
import jcprojects.registro.exception.UsrNotFoundException;
import jcprojects.registro.repository.PersonaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImp implements PersonaService {
    @Autowired
    PersonaRepository personaRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public MensajeResponse crearPersona(PersonaDto personaDto) {
        if(personaRepository.existsByDni(personaDto.getDni())) {
            throw new InternalServerErrorException("DNI de persona existente, no puede crear una persona que ya existe");
        }
        Persona persona = modelMapper.map(personaDto, Persona.class);

        personaRepository.save(persona);

        return new MensajeResponse("Persona creada correctamente");
    }

    @Override
    public List<PersonaDto> verPersonas() {
        List<PersonaDto> personasDto = new ArrayList<>();
        List<Persona> personas = personaRepository.findAll();

        personas.forEach((persona) -> personasDto.add(modelMapper.map(persona, PersonaDto.class)));

        return personasDto;
    }

    @Override
    public PersonaDto verPersona(Long id) {
        if(!personaRepository.existsById(id)) {
            throw new UsrNotFoundException("El ID de la persona no existe");
        }

        Persona persona = personaRepository.getById(id);

        return modelMapper.map(persona, PersonaDto.class);
    }

    @Override
    public MensajeResponse editarPersona(Long id, PersonaDto personaDto) {
        if(!personaRepository.existsById(id)) {
            throw new InternalServerErrorException("El ID de la persona no existe");
        }

        Persona persona = modelMapper.map(personaDto, Persona.class);
        persona.setIdPersona(id);
        personaRepository.save(persona);

        return new MensajeResponse("Persona editada correctamente");
    }

    @Override
    public MensajeResponse borrarPersona(Long id) {
        if(!personaRepository.existsById(id)) {
            throw new InternalServerErrorException("El ID de la persona no existe");
        }
        personaRepository.deleteById(id);
        return new MensajeResponse("Persona eliminada correctamente");
    }

    @Override
    public MensajeResponse cantidadPersonasPorGenero() {
        List<Persona> personas = personaRepository.findAll();

        List<Persona> personasM = personas.stream().filter(persona -> persona.getGenero().contains("M")).collect(Collectors.toList());
        List<Persona> personasF = personas.stream().filter(persona -> persona.getGenero().contains("F")).collect(Collectors.toList());
        int cantidadM = personasM.size();
        int cantidadF = personasF.size();

        return new MensajeResponse("Cantidad de personas masculinas: " + cantidadM + " - Cantidad de personas femeninas: " + cantidadF);
    }

    @Override
    public PersonaDto verPersonaMayor() {
        List<Persona> personas = personaRepository.findAll();

        personas.sort(Comparator.comparingInt(Persona::getEdad).reversed());

        return modelMapper.map(personas.get(0), PersonaDto.class);
    }

    @Override
    public PersonaDto verPersonaMenor() {
        List<Persona> personas = personaRepository.findAll();

        personas.sort(Comparator.comparingInt(Persona::getEdad));

        return modelMapper.map(personas.get(0), PersonaDto.class);
    }

    @Override
    public List<PersonaDto> verPersonasPorProvincia(String provincia) {
        List<PersonaDto> personasDto = new ArrayList<>();
        List<Persona> personas = personaRepository.findAll();

        if(!personaRepository.existsByProvincia(provincia)) {
            throw new InternalServerErrorException("No hay personas de esa provincia");
        }

        personas.forEach(persona ->
        {
            if (persona.getProvincia().equals(provincia)) {
                personasDto.add(modelMapper.map(persona, PersonaDto.class));
            }
        });

        return personasDto;
    }
}