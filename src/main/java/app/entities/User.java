package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // God praksis at bruge @Table for at undgå navnekonflikt med SQL's "USER"
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String role;


    // En-til-mange relation til FavoriteRecipe.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FavoriteRecipe> favoriteRecipes = new HashSet<>();

    // Mange-til-mange relation til Ingredient.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_ingredient",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    public void addIngredient(Ingredient managedIngredient) {
    }

    public void removeIngredient(Ingredient managedIngredient) {
    }
}

