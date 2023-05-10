package helmes.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Sets;

import helmes.model.SectorDto;
import helmes.persistence.entity.Sector;
import helmes.persistence.repository.SectorRepository;
import helmes.transformer.SectorMapper;

@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    @InjectMocks
    private SectorService sectorService;
    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private SectorMapper sectorMapper;

    @Test
    void shouldFindByIds() {
        List<Integer> ids = newArrayList(1, 2, 3);
        List<Sector> sectors = newArrayList(new Sector());

        given(sectorRepository.findAllById(ids))
                .willReturn(sectors);

        List<Sector> result = sectorService.findById(ids);

        assertThat(result).isEqualTo(sectors);

        verify(sectorRepository).findAllById(ids);
    }

    @Test
    void shouldLoadAll() {
        Sector sector1 = Sector.builder().id(1).build();
        Sector sector2 = Sector.builder().id(2).build();

        given(sectorRepository.findAllByParentIsNull())
                .willReturn(newArrayList(
                        sector1,
                        sector2
                ));

        given(sectorMapper.mapToDtoHierarchycally(sector1))
                .willReturn(
                        SectorDto.builder().id(1).children(
                                Sets.newHashSet(
                                        SectorDto.builder().id(11).build(),
                                        SectorDto.builder().id(12).build(),
                                        SectorDto.builder().id(13).build()
                                )
                        ).build()
                );
        given(sectorMapper.mapToDtoHierarchycally(sector2))
                .willReturn(
                        SectorDto.builder().id(2).children(
                                Sets.newHashSet(
                                        SectorDto.builder().id(21).build(),
                                        SectorDto.builder().id(22).build()
                                )
                        ).build()
                );

        List<SectorDto> result = sectorService.loadAll();

        assertThat(result).isNotEmpty().hasSize(2);
        assertThat(result).extracting(SectorDto::getId).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldLoadAllInOneQuery() {
        Sector sector1 = Sector.builder().id(1).build();
        Sector sector11 = Sector.builder().id(11).parent(sector1).build();
        Sector sector12 = Sector.builder().id(12).parent(sector1).build();
        Sector sector2 = Sector.builder().id(2).build();
        Sector sector21 = Sector.builder().id(21).parent(sector2).build();

        SectorDto dto1 = SectorDto.builder().id(1).build();
        SectorDto dto11 = SectorDto.builder().id(11).build();
        SectorDto dto12 = SectorDto.builder().id(12).build();
        SectorDto dto2 = SectorDto.builder().id(2).build();
        SectorDto dto21 = SectorDto.builder().id(21).build();

        given(sectorRepository.findAll())
                .willReturn(newArrayList(sector1, sector2, sector11, sector21, sector12));
        given(sectorMapper.mapToDto(sector1))
                .willReturn(dto1);
        given(sectorMapper.mapToDto(sector11))
                .willReturn(dto11);
        given(sectorMapper.mapToDto(sector12))
                .willReturn(dto12);
        given(sectorMapper.mapToDto(sector2))
                .willReturn(dto2);
        given(sectorMapper.mapToDto(sector21))
                .willReturn(dto21);

        List<SectorDto> result = sectorService.loadAllInOneQuery();
        assertThat(result).isNotEmpty().hasSize(2);
        assertThat(result).extracting(SectorDto::getId).containsExactlyInAnyOrder(1, 2);
    }
}
