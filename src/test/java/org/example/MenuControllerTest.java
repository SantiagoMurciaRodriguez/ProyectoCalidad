package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String IMAGEN_DEFAULT = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1TfeWRgY2ekpPB2om4uu9ZDjoaAnWnpkC4A&s";

    // Test para GET /api/menu (listar todos los platos)
    @Test
    void testGetMenu() throws Exception {
        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].nombre", notNullValue()))
                .andExpect(jsonPath("$[0].imagen", notNullValue()));
    }

    // Test para PUT con ID inexistente (debe devolver 404 Not Found)
    // Verifica que intentar actualizar un plato que no existe devuelve 404.
    @Test
    void testUpdatePlatoInexistente() throws Exception {
        var updatePlato = new PlatoDTO("Falso", "No existe", null);
        mockMvc.perform(put("/api/menu/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePlato)))
                .andExpect(status().isNotFound());
    }

    // Test para DELETE con ID inexistente (debe devolver 404 Not Found)
    // Verifica que intentar borrar un plato que no existe devuelve 404.
    @Test
    void testDeletePlatoInexistente() throws Exception {
        mockMvc.perform(delete("/api/menu/888888"))
                .andExpect(status().isNotFound());
    }

    // Test para POST con campos extra (debe ignorar los campos adicionales y no fallar)
    // Verifica que los campos no definidos en el modelo son ignorados y no generan error.
    @Test
    void testAddPlatoCamposExtra() throws Exception {
        String body = """
        {
            "nombre": "Hamburguesa",
            "descripcion": "Clásica.",
            "imagen": "",
            "precio": 12345,
            "categoria": "Rápida"
        }
        """;
        mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hamburguesa"))
                .andExpect(jsonPath("$.descripcion").value("Clásica."))
                .andExpect(jsonPath("$.imagen").value(IMAGEN_DEFAULT));
    }

    // Test para GET con ID inexistente (debe devolver 404 Not Found)
// Verifica que intentar obtener un plato que no existe devuelve 404.
    @Test
    void testGetPlatoInexistente() throws Exception {
        mockMvc.perform(get("/api/menu/777777"))
                .andExpect(status().isNotFound());
    }

    // Test para POST /api/menu (crear un nuevo plato con imagen personalizada)
    @Test
    void testAddPlatoConImagen() throws Exception {
        var nuevoPlato = new PlatoDTO("Pizza Margarita", "Pizza tradicional italiana.", "https://www.ejemplo.com/imagenpizza.jpg");
        mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPlato)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizza Margarita"))
                .andExpect(jsonPath("$.descripcion").value("Pizza tradicional italiana."))
                .andExpect(jsonPath("$.imagen").value("https://www.ejemplo.com/imagenpizza.jpg"));
    }

    // Test para POST /api/menu (crear un nuevo plato SIN imagen, debe quedar la default)
    @Test
    void testAddPlatoSinImagen() throws Exception {
        var nuevoPlato = new PlatoDTO("Sopa de Tomate", "Sopa caliente de tomate.", "");
        mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPlato)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sopa de Tomate"))
                .andExpect(jsonPath("$.imagen").value(IMAGEN_DEFAULT));
    }

    // Test para GET /api/menu/{id} (obtener un plato específico)
    @Test
    void testGetPlatoById() throws Exception {
        // Obtener un id existente
        var result = mockMvc.perform(get("/api/menu"))
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        var platos = objectMapper.readTree(responseJson);
        long platoId = platos.get(0).get("id").asLong();
        String imagenEsperada = platos.get(0).get("imagen").asText();

        mockMvc.perform(get("/api/menu/" + platoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(platoId))
                .andExpect(jsonPath("$.imagen").value(imagenEsperada));
    }

    // Test para PUT /api/menu/{id} (actualizar un plato y poner imagen personalizada)
    @Test
    void testUpdatePlatoConImagen() throws Exception {
        // Crear un plato para actualizarlo luego
        var nuevoPlato = new PlatoDTO("Tarta de Queso", "Postre delicioso.", "");
        String response = mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPlato)))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(response).get("id").asLong();

        var updatePlato = new PlatoDTO("Tarta de Queso", "Postre delicioso y cremoso.", "https://www.ejemplo.com/tarta.jpg");
        mockMvc.perform(put("/api/menu/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePlato)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imagen").value("https://www.ejemplo.com/tarta.jpg"));
    }

    // Test para PUT /api/menu/{id} (actualizar un plato SIN imagen, debe quedar la default)
    @Test
    void testUpdatePlatoSinImagen() throws Exception {
        // Crear un plato para actualizarlo luego
        var nuevoPlato = new PlatoDTO("Pollo Asado", "Pollo a la brasa.", "https://www.ejemplo.com/pollo.jpg");
        String response = mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPlato)))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(response).get("id").asLong();

        var updatePlato = new PlatoDTO("Pollo Asado", "Pollo a la brasa y ensalada.", "");
        mockMvc.perform(put("/api/menu/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePlato)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imagen").value(IMAGEN_DEFAULT));
    }

    // Test para DELETE /api/menu/{id} (eliminar un plato)
    @Test
    void testDeletePlato() throws Exception {
        // Crear un plato para borrar
        var nuevoPlato = new PlatoDTO("Temp", "Para eliminar.", "");
        String response = mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoPlato)))
                .andReturn().getResponse().getContentAsString();
        long id = objectMapper.readTree(response).get("id").asLong();

        // Eliminar
        mockMvc.perform(delete("/api/menu/" + id))
                .andExpect(status().isNoContent());

        // Confirmar que ya no existe
        mockMvc.perform(get("/api/menu/" + id))
                .andExpect(status().isNotFound());
    }

    // DTO para facilitar la escritura del JSON en los tests
    static class PlatoDTO {
        public String nombre;
        public String descripcion;
        public String imagen;

        public PlatoDTO() { }

        public PlatoDTO(String nombre, String descripcion, String imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.imagen = imagen;
        }
    }
}