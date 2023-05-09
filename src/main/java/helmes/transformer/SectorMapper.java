package helmes.transformer;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

import org.springframework.stereotype.Service;

import helmes.model.SectorDto;
import helmes.persistence.entity.Sector;

@Service
public class SectorMapper {

    public SectorDto mapToDto(Sector entity) {
        return entity == null ?
                new SectorDto() :
                SectorDto.builder().id(entity.getId()).name(entity.getName()).children(newHashSet()).build();
    }

    public SectorDto mapToDtoHierarchycally(Sector entity) {
        return entity == null ?
                new SectorDto() :
                SectorDto.builder().id(entity.getId())
                        .name(entity.getName())
                        .children(entity.getChildren().stream().map(this::mapToDtoHierarchycally).collect(toSet()))
                        .build();
    }

}
