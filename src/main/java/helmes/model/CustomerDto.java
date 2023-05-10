package helmes.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto implements Serializable {

    private Integer id;

    @NotEmpty(message = "User's name cannot be empty.")
    private String name;

    @NotEmpty(message = "Sectors cannot be empty.")
    private Set<Integer> sectors;

    @AssertTrue(message = "You have to agree with terms.")
    private boolean agree;

}
