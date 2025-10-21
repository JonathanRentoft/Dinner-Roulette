package app.dto;

import app.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password; // Kun til login/register, sendes aldrig tilbage
    private String role;

    //Konverterer en User Entity til en UserDTO
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.role = user.getRole();

    }
}

