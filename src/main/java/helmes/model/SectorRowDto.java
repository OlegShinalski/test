package helmes.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SectorRowDto implements Serializable {

    private Integer id;

    private String name;

}
