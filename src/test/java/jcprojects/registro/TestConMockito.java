package jcprojects.registro;

import jcprojects.registro.dto.request.PersonaDto;
import jcprojects.registro.dto.response.ErrorResponse;
import jcprojects.registro.dto.response.MensajeResponse;
import jcprojects.registro.entity.Persona;
import jcprojects.registro.exception.InternalServerErrorException;
import jcprojects.registro.exception.UsrNotFoundException;
import jcprojects.registro.repository.PersonaRepository;
import jcprojects.registro.service.PersonaServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TestConMockito {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaServiceImp personaServiceImp;

    @DisplayName("Testeo crearPersona OK")
    @Test
    void crearPersonaTestOk() {
        //Arrange
        Persona persona = new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        PersonaDto personaDto = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        MensajeResponse mensajeEsperado = new MensajeResponse("Persona creada correctamente");

        //Act
        when(personaRepository.existsByDni("10123456")).thenReturn(false);
        when(personaRepository.save(persona)).thenReturn(persona);
        MensajeResponse mensajeObtenido = personaServiceImp.crearPersona(personaDto);

        //Assert
        assertEquals(mensajeEsperado, mensajeObtenido);
    }

    @DisplayName("Testeo crearPersona con DNI existente")
    @Test
    void crearPersonaTestKo() {
        //Arrange
        PersonaDto personaDto = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        ErrorResponse mensajeEsperado = new ErrorResponse(500, "DNI de persona existente, no puede crear una persona que ya existe");

        //Act
        when(personaRepository.existsByDni("10123456")).thenReturn(true);

        //Assert
        Exception mensajeObtenido = assertThrows(InternalServerErrorException.class, () -> personaServiceImp.crearPersona(personaDto));
        assertEquals(mensajeEsperado.getMensaje(), mensajeObtenido.getMessage());
    }

    @DisplayName("Testeo verPersonas con lista llena")
    @Test
    void verPersonasTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        personaList.add(new Persona(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        personaList.add(new Persona(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        personaList.add(new Persona(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));

        List<PersonaDto> listaEsperada = new ArrayList<>();
        listaEsperada.add(new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        listaEsperada.add(new PersonaDto(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        listaEsperada.add(new PersonaDto(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        listaEsperada.add(new PersonaDto(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        List<PersonaDto> listaObtenida = personaServiceImp.verPersonas();

        //Assert
        assertAll(()->{
            assertEquals(listaEsperada.size(), listaObtenida.size());
            assertEquals(listaEsperada.get(0), listaObtenida.get(0));
        });
    }

    @DisplayName("Testeo verPersonas con lista vacia")
    @Test
    void verPersonasVaciaTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        List<PersonaDto> listaObtenida = personaServiceImp.verPersonas();

        //Assert
        assertEquals(0, listaObtenida.size());
    }

    @DisplayName("Testeo verPersona con id correcto")
    @Test
    void verPersonaTestOk() {
        //Arrange
        Persona persona = new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        PersonaDto personaEsperada = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");

        //Act
        when(personaRepository.existsById(1L)).thenReturn(true);
        when(personaRepository.getById(1L)).thenReturn(persona);
        PersonaDto personaObtenida = personaServiceImp.verPersona(1L);

        //Assert
        assertEquals(personaEsperada, personaObtenida);
    }

    @DisplayName("Testeo verPersona con ID inexistente")
    @Test
    void verPersonaTestKo() {
        //Arrange
        ErrorResponse mensajeEsperado = new ErrorResponse(404, "El ID de la persona no existe");
        //Act
        when(personaRepository.existsById(9L)).thenReturn(false);
        //Assert
        Exception mensajeObtenido = assertThrows(UsrNotFoundException.class, () -> personaServiceImp.verPersona(9L));
        assertEquals(mensajeEsperado.getMensaje(), mensajeObtenido.getMessage());
    }

    @DisplayName("Testeo editarPersona OK")
    @Test
    void editarPersonaTestOk() {
        //Arrange
        PersonaDto personaDto = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        MensajeResponse mensajeEsperado = new MensajeResponse("Persona editada correctamente");

        //Act
        when(personaRepository.existsById(1L)).thenReturn(true);
        MensajeResponse mensajeObtenido = personaServiceImp.editarPersona(1L, personaDto);

        //Assert
        assertEquals(mensajeEsperado, mensajeObtenido);
    }

    @DisplayName("Testeo editarPersona con ID inexistente")
    @Test
    void editarPersonaTestKo() {
        //Arrange
        PersonaDto personaDto = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        ErrorResponse mensajeEsperado = new ErrorResponse(500, "El ID de la persona no existe");

        //Act
        when(personaRepository.existsById(9L)).thenReturn(false);

        //Assert
        Exception mensajeObtenido = assertThrows(InternalServerErrorException.class, () -> personaServiceImp.editarPersona(9L, personaDto));
        assertEquals(mensajeEsperado.getMensaje(), mensajeObtenido.getMessage());
    }

    @DisplayName("Testeo borrarPersona OK")
    @Test
    void borrarPersonaTestOk() {
        //Arrange
        MensajeResponse mensajeEsperado = new MensajeResponse("Persona eliminada correctamente");

        //Act
        when(personaRepository.existsById(1L)).thenReturn(true);
        personaRepository.deleteById(1L);
        MensajeResponse mensajeObtenido = personaServiceImp.borrarPersona(1L);

        //Assert
        assertEquals(mensajeEsperado, mensajeObtenido);
    }

    @DisplayName("Testeo borrarPersona con ID inexistente")
    @Test
    void borrarPersonaTestKo() {
        //Arrange
        ErrorResponse mensajeEsperado = new ErrorResponse(500, "El ID de la persona no existe");

        //Act
        when(personaRepository.existsById(9L)).thenReturn(false);

        //Assert
        Exception mensajeObtenido = assertThrows(InternalServerErrorException.class, () -> personaServiceImp.borrarPersona(9L));
        assertEquals(mensajeEsperado.getMensaje(), mensajeObtenido.getMessage());
    }

    @DisplayName("Testeo cantidadPersonasPorGenero OK")
    @Test
    void cantidadPersonasPorGeneroTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        personaList.add(new Persona(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        personaList.add(new Persona(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        personaList.add(new Persona(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));

        MensajeResponse mensajeEsperado = new MensajeResponse("Cantidad de personas masculinas: 2 - Cantidad de personas femeninas: 2");

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        MensajeResponse mensajeObtenido = personaServiceImp.cantidadPersonasPorGenero();

        //Assert
        assertEquals(mensajeEsperado, mensajeObtenido);
    }

    @DisplayName("Testeo verPersonaMayor OK")
    @Test
    void verPersonaMayorTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        personaList.add(new Persona(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        personaList.add(new Persona(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        personaList.add(new Persona(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));
        personaList.add(new Persona(5L, "Florencia", "Lopez", 44, "F", "50123456", "Buenos Aires"));
        personaList.add(new Persona(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires"));
        personaList.add(new Persona(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires"));

        PersonaDto personaEsperada = new PersonaDto(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires");

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        PersonaDto personaObtenida = personaServiceImp.verPersonaMayor();

        //Assert
        assertEquals(personaEsperada, personaObtenida);
    }

    @DisplayName("Testeo verPersonaMenor OK")
    @Test
    void verPersonaMenorTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        personaList.add(new Persona(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        personaList.add(new Persona(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        personaList.add(new Persona(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));
        personaList.add(new Persona(5L, "Florencia", "Lopez", 44, "F", "50123456", "Buenos Aires"));
        personaList.add(new Persona(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires"));
        personaList.add(new Persona(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires"));

        PersonaDto personaEsperada = new PersonaDto(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires");

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        PersonaDto personaObtenida = personaServiceImp.verPersonaMenor();

        //Assert
        assertEquals(personaEsperada, personaObtenida);
    }

    @DisplayName("Testeo verPersonasPorProvincia OK")
    @Test
    void verPersonasPorProvinciaTestOk() {
        //Arrange
        List<Persona> personaList = new ArrayList<>();
        personaList.add(new Persona(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza"));
        personaList.add(new Persona(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        personaList.add(new Persona(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        personaList.add(new Persona(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));
        personaList.add(new Persona(5L, "Florencia", "Lopez", 44, "F", "50123456", "Buenos Aires"));
        personaList.add(new Persona(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires"));
        personaList.add(new Persona(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires"));

        List<PersonaDto> listaEsperada = new ArrayList<>();
        listaEsperada.add(new PersonaDto(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        listaEsperada.add(new PersonaDto(5L, "Florencia", "Lopez", 44, "F", "50123456", "Buenos Aires"));
        listaEsperada.add(new PersonaDto(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires"));
        listaEsperada.add(new PersonaDto(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires"));

        //Act
        when(personaRepository.findAll()).thenReturn(personaList);
        when(personaRepository.existsByProvincia("Buenos Aires")).thenReturn(true);
        List<PersonaDto> listaObtenida = personaServiceImp.verPersonasPorProvincia("Buenos Aires");

        //Assert
        assertAll(()->{
            assertEquals(listaEsperada.size(), listaObtenida.size());
            assertEquals(listaEsperada.get(0), listaObtenida.get(0));
        });
    }

    @DisplayName("Testeo verPersonasPorProvincia con provincia inexistente")
    @Test
    void verPersonasPorProvinciaTestKo() {
        //Arrange
        ErrorResponse mensajeEsperado = new ErrorResponse(500, "No hay personas de esa provincia");

        //Act
        when(personaRepository.existsByProvincia("Jujuy")).thenReturn(false);

        //Assert
        Exception mensajeObtenido = assertThrows(InternalServerErrorException.class, () -> personaServiceImp.verPersonasPorProvincia("Jujuy"));
        assertEquals(mensajeEsperado.getMensaje(), mensajeObtenido.getMessage());
    }
}
