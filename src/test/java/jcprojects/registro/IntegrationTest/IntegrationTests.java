package jcprojects.registro.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import jcprojects.registro.dto.request.PersonaDto;
import jcprojects.registro.dto.response.MensajeResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@TestPropertySource(properties = {"SCOPE = integration_test"})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    MockMvc mockMvc;

    ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false).writer();

    @Test
    @Order(1)
    @DisplayName("Test Integración verPersonas OK")
    public void verPersonasTestOk() throws Exception {
        List<PersonaDto> listaPersonas = new ArrayList<>();
        listaPersonas.add(new PersonaDto(1L, "Jorge", "Perez", 60, "M", "10123456", "Mendoza"));
        listaPersonas.add(new PersonaDto(2L, "Marta", "Gomez", 55, "F", "20123456", "Buenos Aires"));
        listaPersonas.add(new PersonaDto(3L, "Carola", "Rodriguez", 27, "F", "30123456", "Cordoba"));
        listaPersonas.add(new PersonaDto(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz"));
        listaPersonas.add(new PersonaDto(5L, "Florencia", "Lopez", 44, "F", "50123456", "Buenos Aires"));
        listaPersonas.add(new PersonaDto(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires"));
        listaPersonas.add(new PersonaDto(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires"));
        listaPersonas.add(new PersonaDto(8L, "Marcelo", "Suarez", 38, "M", "80123456", "San Luis"));
        listaPersonas.add(new PersonaDto(9L, "Carlos", "Gonzalez", 59, "M", "90123456", "Tucuman"));
        listaPersonas.add(new PersonaDto(10L, "Martin", "Fernandez", 30, "M", "11123456", "Santa Fe"));

        String listaEsperada = writer.writeValueAsString(listaPersonas);

        MvcResult listaObtenida = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/verTodos"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(listaEsperada, listaObtenida.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    @DisplayName("Test Integración de crearPersona OK")
    public void crearPersonaTestOk() throws Exception {
        PersonaDto personaDto = new PersonaDto(11L, "Tatiana", "Cabrera", 31, "F", "12123456", "Buenos Aires");
        MensajeResponse mensajeEsperadoDto = new MensajeResponse("Persona creada correctamente");

        String persona = writer.writeValueAsString(personaDto);
        String mensajeEsperado = writer.writeValueAsString(mensajeEsperadoDto);

        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.post("/persona/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(persona))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mensajeEsperado, mensajeObtenido.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    @DisplayName("Test Integración de crearPersona con DNI existente")
    public void crearPersonaTestKo() throws Exception {
        PersonaDto personaDto = new PersonaDto(11L, "Lucrecia", "Suarez", 46, "F", "20123456", "La Pampa");

        String persona = writer.writeValueAsString(personaDto);

        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.post("/persona/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(persona))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is(500))
                .andReturn();

        assertTrue(mensajeObtenido.getResponse().getContentAsString().contains("DNI de persona existente, no puede crear una persona que ya existe"));
    }

    @Test
    @Order(4)
    @DisplayName("Test Integración verPersona OK")
    public void verPersonaTestOk() throws Exception {
        PersonaDto personaDto = new PersonaDto(4L, "Santiago", "Diaz", 30, "M", "40123456", "Santa Cruz");

        String personaEsperada = writer.writeValueAsString(personaDto);

        MvcResult personaObtenida = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/ver/{id}", 4L))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(personaEsperada, personaObtenida.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    @DisplayName("Test Integración verPersona con ID inexistente")
    public void verPersonaTestKo() throws Exception {
        MvcResult personaObtenida = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/ver/{id}", 300L))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is(404))
                .andReturn();

        assertTrue(personaObtenida.getResponse().getContentAsString().contains("El ID de la persona no existe"));
    }

    @Test
    @Order(6)
    @DisplayName("Test Integración editarPersona OK")
    public void editarPersonaTestOk() throws Exception {
        PersonaDto personaDto = new PersonaDto(1L, "Juan", "Perez", 60, "M", "10123456", "Mendoza");
        MensajeResponse mensajeEsperadoDto = new MensajeResponse("Persona editada correctamente");

        String persona = writer.writeValueAsString(personaDto);
        String mensajeEsperado = writer.writeValueAsString(mensajeEsperadoDto);

        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.put("/persona/editar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(persona))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mensajeEsperado, mensajeObtenido.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    @DisplayName("Test Integración editarPersona con ID inexistente")
    public void editarPersonaTestKo() throws Exception {
        PersonaDto personaDto = new PersonaDto(350L, "Roberto", "Tortugas", 65, "M", "99999999", "Buenos Aires");

        String persona = writer.writeValueAsString(personaDto);

        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.put("/persona/editar/{id}", 350L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(persona))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is(500))
                .andReturn();

        assertTrue(mensajeObtenido.getResponse().getContentAsString().contains("El ID de la persona no existe"));
    }

    @Test
    @Order(8)
    @DisplayName("Test Integración cantidadPersonasPorGenero OK")
    public void cantidadPersonasPorGeneroTestOk() throws Exception {
        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/porGenero"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(mensajeObtenido.getResponse().getContentAsString().contains("Cantidad de personas masculinas"));
    }

    @Test
    @Order(9)
    @DisplayName("Test Integración verPersonaMayor OK")
    public void verPersonaMayorTestOk() throws Exception {
        PersonaDto personaDto = new PersonaDto(6L, "Viviana", "Martinez", 73, "F", "60123456", "Buenos Aires");

        String personaEsperada = writer.writeValueAsString(personaDto);

        MvcResult personaObtenida = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/mayor"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(personaEsperada, personaObtenida.getResponse().getContentAsString());
    }

    @Test
    @Order(10)
    @DisplayName("Test Integración verPersonaMenor OK")
    public void verPersonaMenorTestOk() throws Exception {
        PersonaDto personaDto = new PersonaDto(7L, "Lucas", "Nuniez", 25, "M", "70123456", "Buenos Aires");

        String personaEsperada = writer.writeValueAsString(personaDto);

        MvcResult personaObtenida = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/menor"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(personaEsperada, personaObtenida.getResponse().getContentAsString());
    }

    @Test
    @Order(11)
    @DisplayName("Test Integración verPersonasPorProvincia OK")
    public void verPersonasPorProvinciaTestOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/porProvincia/{provincia}", "Buenos Aires"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(12)
    @DisplayName("Test Integración verPersonasPorProvincia con provincia inexistente")
    public void verPersonasPorProvinciaTestKo() throws Exception {
        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.get("/persona/porProvincia/{provincia}", "Jujuy"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is(500))
                .andReturn();

        assertTrue(mensajeObtenido.getResponse().getContentAsString().contains("No hay personas de esa provincia"));
    }

    @Test
    @Order(13)
    @DisplayName("Test Integración borrarPersona OK")
    public void borrarPersonaTestOk() throws Exception {
        MensajeResponse mensajeEsperadoDto = new MensajeResponse("Persona eliminada correctamente");

        String mensajeEsperado = writer.writeValueAsString(mensajeEsperadoDto);

        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.delete("/persona/borrar/{id}", 9L))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mensajeEsperado, mensajeObtenido.getResponse().getContentAsString());
    }

    @Test
    @Order(14)
    @DisplayName("Test Integración borrarPersona con ID inexistente")
    public void borrarPersonaTestKo() throws Exception {
        MvcResult mensajeObtenido = this.mockMvc.perform(MockMvcRequestBuilders.delete("/persona/borrar/{id}", 400L))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().is(500))
                .andReturn();

        assertTrue(mensajeObtenido.getResponse().getContentAsString().contains("El ID de la persona no existe"));
    }
}