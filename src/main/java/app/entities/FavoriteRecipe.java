package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorite_recipes")
@NoArgsConstructor
@Getter
@Setter
public class FavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ID fra TheMealDB
    @Column(nullable = false)
    private String externalRecipeId;

    @Column(nullable = false)
    private String recipeName;

    // Mange til en relation til User.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Definerer foreign key
    private User user;
}
