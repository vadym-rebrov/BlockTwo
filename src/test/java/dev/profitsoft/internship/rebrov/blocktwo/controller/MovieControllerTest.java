package dev.profitsoft.internship.rebrov.blocktwo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import dev.profitsoft.internship.rebrov.blocktwo.dto.*;
import dev.profitsoft.internship.rebrov.blocktwo.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    @DisplayName("GET /api/movie/{id}")
    void getMovie_Success() throws Exception {
        Long movieId = 1L;
        MovieInfoDto mockInfo = new MovieInfoDto();
        mockInfo.setId(movieId);
        mockInfo.setTitle("Test Movie");
        mockInfo.setRating(8.5);

        when(movieService.getById(movieId)).thenReturn(mockInfo);

        mockMvc.perform(get("/api/movie/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movieId))
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.rating").value(8.5));

        verify(movieService).getById(movieId);
    }

    @Test
    @DisplayName("GET /api/movie/{id}")
    void getMovie_NotFound() throws Exception {
        Long movieId = 999L;
        when(movieService.getById(movieId)).thenThrow(new EntityNotFoundException("Movie not found"));

        mockMvc.perform(get("/api/movie/{id}", movieId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    @DisplayName("POST /api/movie")
    void addMovie_Success() throws Exception {
        MovieSaveDto saveDto = new MovieSaveDto();
        saveDto.setTitle("New Movie");
        saveDto.setReleased(LocalDate.now().minusDays(10));
        saveDto.setRating(9.0);
        saveDto.setDirectorId(1L);
        saveDto.setGenresId(Set.of(1L, 2L));
        saveDto.setAwards(List.of("Oscar"));

        doNothing().when(movieService).add(any(MovieSaveDto.class));

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk());

        verify(movieService).add(any(MovieSaveDto.class));
    }

    @Test
    @DisplayName("POST /api/movie")
    void addMovie_ValidationError() throws Exception {
        MovieSaveDto invalidDto = new MovieSaveDto();
        invalidDto.setTitle("");
        invalidDto.setRating(11.0);

        mockMvc.perform(post("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(movieService, never()).add(any());
    }

    @Test
    @DisplayName("PUT /api/movie/{id}")
    void updateMovie_Success() throws Exception {
        Long id = 1L;
        MovieSaveDto saveDto = new MovieSaveDto();
        saveDto.setTitle("Updated Title");
        saveDto.setReleased(LocalDate.now());
        saveDto.setRating(5.0);
        saveDto.setDirectorId(2L);
        saveDto.setGenresId(Set.of(3L));
        saveDto.setAwards(Collections.emptyList());

        mockMvc.perform(put("/api/movie/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk());

        verify(movieService).update(eq(id), any(MovieSaveDto.class));
    }

    @Test
    @DisplayName("DELETE /api/movie/{id}")
    void deleteMovie_Success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/movie/{id}", id))
                .andExpect(status().isOk());

        verify(movieService).deleteById(id);
    }

    @Test
    @DisplayName("POST /api/movie/_list")
    void getMovieList_Success() throws Exception {
        MovieQueryDto queryDto = new MovieQueryDto();
        queryDto.setPage(0);
        queryDto.setSize(10);

        MovieDetailsDto movieDetails = new MovieDetailsDto();
        movieDetails.setTitle("Found Movie");
        List<MovieDetailsDto> resultList = List.of(movieDetails);

        PageDto<MovieDetailsDto> pageDto = new PageDto<>(resultList, 5, 50L);
        when(movieService.findMoviesByCriteria(any(MovieQueryDto.class))).thenReturn(pageDto);
        mockMvc.perform(post("/api/movie/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].title").value("Found Movie"))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.totalElements").value(50));

        verify(movieService).findMoviesByCriteria(any(MovieQueryDto.class));
    }

    @Test
    @DisplayName("POST /api/movie/_upload")
    void uploadMovies_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "movies.json",
                MediaType.APPLICATION_JSON_VALUE,
                "[{\"title\": \"Movie 1\"}]".getBytes()
        );

        UploadReport report = new UploadReport();
        report.setSuccess(5);
        report.setFailed(0);

        when(movieService.uploadMultipartFile(any())).thenReturn(report);

        mockMvc.perform(multipart("/api/movie/_upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(5))
                .andExpect(jsonPath("$.failed").value(0));
    }

    @Test
    @DisplayName("POST /api/movie/_report")
    void exportMoviesReport_Success() throws Exception {
        MovieQueryDto queryDto = new MovieQueryDto();

        String filename = "movies_report.xlsx";
        byte[] excelBytes = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);

        FileReportResponseDto reportResponse = new FileReportResponseDto(filename, inputStream);

        when(movieService.getExcelReportByCriteria(any(MovieQueryDto.class)))
                .thenReturn(reportResponse);

        mockMvc.perform(post("/api/movie/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDto)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("filename=\"" + filename + "\"")))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(excelBytes));

        verify(movieService).getExcelReportByCriteria(any(MovieQueryDto.class));
    }
}