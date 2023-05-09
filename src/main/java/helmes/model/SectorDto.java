package helmes.model;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorDto implements Serializable {

    private Integer id;

    private String name;

    @Builder.Default
    private Set<SectorDto> children = Sets.newHashSet();

}
