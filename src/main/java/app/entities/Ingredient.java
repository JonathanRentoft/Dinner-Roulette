package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@NoArgsConstructor
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    // "mappedBy" betyder, at User-klassen "ejer" denne relation og definerer join-tabellen.
    @ManyToMany(mappedBy = "ingredients")
    private Set<User> users = new HashSet<>();

    // til at oprette en ny ingrediens med et navn
    public Ingredient(String name) {
        this.name = name;
    }
}
