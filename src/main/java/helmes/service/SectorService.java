package helmes.service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import helmes.model.SectorDto;
import helmes.persistence.entity.Sector;
import helmes.persistence.repository.SectorRepository;
import helmes.transformer.SectorMapper;
import helmes.util.HierarchyCollector;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    public List<Sector> findById(List<Integer> ids) {
        return newArrayList(sectorRepository.findAllById(ids));
    }

    /**
     * Load sections iterating by parent-> child relation
     * @return
     */
    public List<SectorDto> loadAll() {
        List<Sector> sectors = sectorRepository.findAllByParentIsNull();
        return sectors.stream().map(sectorMapper::mapToDtoHierarchycally).collect(toList());
    }

    /**
     * Load all sections by one query and build hierarchy locally
     * @return
     */
    public List<SectorDto> loadAllInOneQuery() {
        List<Sector> sectors = newArrayList(sectorRepository.findAll());
        return buildHierarchyFromPlain(sectors);
    }

    private List<SectorDto> buildHierarchyFromPlain(List<Sector> sectors) {
        List<SectorDto> result =
                sectors.stream().collect(
                        HierarchyCollector.intoHierarchy(
                                Sector::getId,
                                Sector::getParentId,
                                sectorMapper::mapToDto,
                                (parent, child) -> parent.getChildren().add(child)
                        )
                );
        return result;
    }

}
