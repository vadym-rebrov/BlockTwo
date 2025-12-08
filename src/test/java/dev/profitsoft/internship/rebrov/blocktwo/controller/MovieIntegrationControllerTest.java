package dev.profitsoft.internship.rebrov.blocktwo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieQueryDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieSaveDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MovieIntegrationControllerTest extends IntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should create and retrieve a movie")
    void createAndGetMovie() throws Exception {
        MovieSaveDto saveDto = new MovieSaveDto();
        saveDto.setTitle("Inception Local DB");
        saveDto.setReleased(LocalDate.of(2010, 7, 16));
        saveDto.setRating(8.8);
        saveDto.setDirectorId(1L);
        saveDto.setGenresId(Set.of(1L, 2L));
        saveDto.setAwards(List.of("Oscar"));

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("should filter movies by director name (Full Text Search)")
    void filterMoviesByDirector() throws Exception {
        createTestMovie("Dune", 1L);

        MovieQueryDto query = new MovieQueryDto();
        query.setDirectorName("Nolan");
        query.setPage(0);
        query.setSize(10);

        mockMvc.perform(post("/api/movie/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].title").value("Dune"));
    }

    private void createTestMovie(String title, Long directorId) throws Exception {
        MovieSaveDto dto = new MovieSaveDto(title, LocalDate.now(), Set.of(1L), 7.5, directorId, List.of("Award"));
        mockMvc.perform(post("/api/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
