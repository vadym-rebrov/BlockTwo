package dev.profitsoft.internship.rebrov.blocktwo.controller;

import dev.profitsoft.internship.rebrov.blocktwo.service.FakeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fake")
public class FakeController {

    @Autowired
    private FakeDataService fakeDataGenerator;

    @PostMapping("/generate_movies")
    public String generateFakeData(@RequestParam(defaultValue = "10") int numberOfMovies) {
        fakeDataGenerator.generateFakeMoviesJson(numberOfMovies, "./src/main/resources/dataset/fake_movies.json");
        return "Fake data generated successfully!";
    }

}
