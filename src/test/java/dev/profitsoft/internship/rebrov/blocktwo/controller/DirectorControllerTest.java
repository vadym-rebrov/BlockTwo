package dev.profitsoft.internship.rebrov.blocktwo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.rebrov.blocktwo.dto.CountryInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorSaveDto;
import dev.profitsoft.internship.rebrov.blocktwo.service.DirectorService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DirectorController.class)
@AutoConfigureMockMvc
class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DirectorService directorService;

    @Test
    @DisplayName("GET /api/director/{id}")
    void getById_Success() throws Exception {
        Long id = 1L;
        DirectorInfoDto infoDto = new DirectorInfoDto();
        infoDto.setId(id);
        infoDto.setFullName("Christopher Nolan");
        infoDto.setBirthday(LocalDate.of(1970, 7, 30));
        infoDto.setCountry(new CountryInfoDto(10L, "UK"));

        when(directorService.getById(id)).thenReturn(infoDto);

        mockMvc.perform(get("/api/director/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fullName").value("Christopher Nolan"))
                .andExpect(jsonPath("$.country.name").value("UK"));

        verify(directorService).getById(id);
    }

    @Test
    @DisplayName("GET /api/director")
    void getAll_Success() throws Exception {
        DirectorInfoDto d1 = new DirectorInfoDto();
        d1.setId(1L);
        d1.setFullName("Christopher Nolan");

        DirectorInfoDto d2 = new DirectorInfoDto();
        d2.setId(2L);
        d2.setFullName("Quentin Tarantino");

        List<DirectorInfoDto> mockDirectors = List.of(d1, d2);
        when(directorService.getAll()).thenReturn(mockDirectors);

        mockMvc.perform(get("/api/director"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].fullName").value("Christopher Nolan"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].fullName").value("Quentin Tarantino"));

        verify(directorService).getAll();
    }

    @Test
    @DisplayName("GET /api/director/{id}")
    void getById_NotFound() throws Exception {
        Long id = 99L;
        when(directorService.getById(id)).thenThrow(new EntityNotFoundException("Director not found"));
        mockMvc.perform(get("/api/director/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Director not found"));
    }

    @Test
    @DisplayName("POST /api/director")
    void save_Success() throws Exception {
        DirectorSaveDto saveDto = new DirectorSaveDto();
        saveDto.setFullName("Quentin Tarantino");
        saveDto.setCountryId(5L);
        saveDto.setBirthday(LocalDate.of(1963, 3, 27));

        doNothing().when(directorService).save(any(DirectorSaveDto.class));

        mockMvc.perform(post("/api/director")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk());

        verify(directorService).save(any(DirectorSaveDto.class));
    }

    @Test
    @DisplayName("POST /api/director")
    void save_ValidationError() throws Exception {
        DirectorSaveDto invalidDto = new DirectorSaveDto();
        invalidDto.setFullName("123 Invalid Name");
        invalidDto.setCountryId(null);

        mockMvc.perform(post("/api/director")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(directorService, never()).save(any());
    }

    @Test
    @DisplayName("PUT /api/director/{id}")
    void update_Success() throws Exception {
        Long id = 1L;
        DirectorSaveDto updateDto = new DirectorSaveDto();
        updateDto.setFullName("Steven Spielberg");
        updateDto.setCountryId(2L);
        updateDto.setBirthday(LocalDate.of(1946, 12, 18));

        doNothing().when(directorService).update(eq(id), any(DirectorSaveDto.class));

        mockMvc.perform(put("/api/director/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        verify(directorService).update(eq(id), any(DirectorSaveDto.class));
    }

    @Test
    @DisplayName("PUT /api/director/{id}")
    void update_ValidationError() throws Exception {
        Long id = 1L;
        DirectorSaveDto invalidDto = new DirectorSaveDto();
        invalidDto.setFullName("");

        mockMvc.perform(put("/api/director/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(directorService, never()).update(anyLong(), any());
    }


    @Test
    @DisplayName("DELETE /api/director/{id}")
    void delete_Success() throws Exception {
        Long id = 1L;

        doNothing().when(directorService).delete(id);

        mockMvc.perform(delete("/api/director/{id}", id))
                .andExpect(status().isOk());

        verify(directorService).delete(id);
    }
}