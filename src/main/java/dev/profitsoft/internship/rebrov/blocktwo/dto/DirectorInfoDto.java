package dev.profitsoft.internship.rebrov.blocktwo.dto;

import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorInfoDto{
    private Long id;
    private String fullName;
    private CountryInfoDto country;
    private LocalDate birthday;

    public DirectorInfoDto(Director director){
        this.id = director.getId();
        this.fullName = director.getFullName();
        this.birthday = director.getBirthday();
        this.country = new CountryInfoDto(director.getCountry().getId(), director.getCountry().getName());
    }

}
