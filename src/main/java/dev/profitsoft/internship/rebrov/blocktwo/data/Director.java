package dev.profitsoft.internship.rebrov.blocktwo.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
public class Director {
    private Integer id;
    private String fullName;
    private String country;
    private Integer birthYear;

    public Director(String fullName, String country, int birthYear) {
        this.fullName = fullName;
        this.country = country;
        this.birthYear = birthYear;
    }

    public Director(){}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Director director)) return false;
        return Objects.equals(birthYear, director.birthYear) && Objects.equals(fullName, director.fullName) && Objects.equals(country, director.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, country, birthYear);
    }

    @Override
    public String toString() {
        return "{" +
                "fullName='" + fullName + '\'' +
                ", country='" + country + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }
}
