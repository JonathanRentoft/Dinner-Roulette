package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // God praksis at bruge @Table for at undgå navnekonflikt med SQL's "USER"
@Getter
@Setter
@NoArgsConstructor // Lombok-konstruktør uden argumenter (krævet af JPA)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;

    private String password;
    private String role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_ingredient",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FavoriteRecipe> favoriteRecipes = new HashSet<>();

    // NY KONSTRUKTØR - Denne løser din fejl
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.getUsers().add(this);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.getUsers().remove(this);
    }

    public Object getId() {
        return username;
    }
}

