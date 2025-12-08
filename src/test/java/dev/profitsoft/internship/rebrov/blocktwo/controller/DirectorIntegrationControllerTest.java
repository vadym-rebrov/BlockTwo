package dev.profitsoft.internship.rebrov.blocktwo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorSaveDto;
import dev.profitsoft.internship.rebrov.blocktwo.repository.DirectorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DirectorIntegrationControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should create and retrieve a director")
    void createAndGetDirector() throws Exception {

        DirectorSaveDto saveDto = new DirectorSaveDto();
        saveDto.setFullName("Greta Gerwig");
        saveDto.setBirthday(LocalDate.of(1983, 8, 4));
        saveDto.setCountryId(1L);

        mockMvc.perform(post("/api/director")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk());

        Director director = directorRepository.getAll().stream()
                .filter(d -> d.getFullName().equals("Greta Gerwig"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Director not found in DB"));

        mockMvc.perform(get("/api/director/{id}", director.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Greta Gerwig"))
                .andExpect(jsonPath("$.country.name").value("USA")); // Проверяем подтягивание страны
    }

    @Test
    @DisplayName("should update existing director")
    void updateDirector() throws Exception {
        createTestDirector("Original Name");
        Long directorId = directorRepository.getAll().stream()
                .filter(d -> d.getFullName().equals("Original Name"))
                .findFirst()
                .orElseThrow()
                .getId();

        DirectorSaveDto updateDto = new DirectorSaveDto();
        updateDto.setFullName("Updated Name");
        updateDto.setBirthday(LocalDate.of(1990, 1, 1));
        updateDto.setCountryId(2L);

        mockMvc.perform(put("/api/director/{id}", directorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/director/{id}", directorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.country.name").value("UK"));
    }

    @Test
    @DisplayName("should delete director")
    void deleteDirector() throws Exception {
        createTestDirector("To Delete");
        Long directorId = directorRepository.getAll().stream()
                .filter(d -> d.getFullName().equals("To Delete"))
                .findFirst()
                .orElseThrow()
                .getId();

        mockMvc.perform(delete("/api/director/{id}", directorId))
                .andExpect(status().isOk());

        boolean exists = directorRepository.getById(directorId).isPresent();
        assertFalse(exists, "Director should be deleted");
    }

    @Test
    @DisplayName("should validate invalid input")
    void validationCheck() throws Exception {
        DirectorSaveDto invalidDto = new DirectorSaveDto();
        invalidDto.setFullName("Director 123");
        invalidDto.setBirthday(LocalDate.now());
        invalidDto.setCountryId(1L);

        mockMvc.perform(post("/api/director")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); // Ожидаем 400
    }

    private void createTestDirector(String name) throws Exception {
        DirectorSaveDto dto = new DirectorSaveDto();
        dto.setFullName(name);
        dto.setBirthday(LocalDate.now());
        dto.setCountryId(1L);

        mockMvc.perform(post("/api/director")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
