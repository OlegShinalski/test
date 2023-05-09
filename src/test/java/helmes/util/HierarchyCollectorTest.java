package helmes.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;

import helmes.model.SectorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@ExtendWith(MockitoExtension.class)
public class HierarchyCollectorTest {

    @Data
    @AllArgsConstructor
    @Builder
    private static class HierarchyImpl implements Hierarchy {
        private Integer id;
        private Integer parentId;
    }

    @Test
    void shouldReturnEmptyListForEmptySource() {
        List<SectorDto> result = buildHierarchyFromPlain(Lists.newArrayList());
        assertThat(result).isEmpty();
    }

    @Test
    void throwExceptionWhenSourceIsNull() {
        assertThrows(NullPointerException.class, () -> {
            buildHierarchyFromPlain(null);
        });
    }

    @Test
    void shouldReturnHierarhyForSource() {
        List<SectorDto> result = buildHierarchyFromPlain(Lists.newArrayList(
                HierarchyImpl.builder().id(1).parentId(null).build(),
                HierarchyImpl.builder().id(11).parentId(1).build(),
                HierarchyImpl.builder().id(111).parentId(11).build(),
                HierarchyImpl.builder().id(112).parentId(11).build(),
                HierarchyImpl.builder().id(12).parentId(1).build(),
                HierarchyImpl.builder().id(13).parentId(1).build(),
                HierarchyImpl.builder().id(2).parentId(null).build(),
                HierarchyImpl.builder().id(21).parentId(2).build(),
                HierarchyImpl.builder().id(22).parentId(2).build()
        ));
        assertThat(result).isNotEmpty().hasSize(2);
        assertThat(result).extracting(e -> e.getId()).containsExactlyInAnyOrder(1, 2);
    }

    private List<SectorDto> buildHierarchyFromPlain(List<HierarchyImpl> plainList) {
        List<SectorDto> result =
                plainList.stream().collect(
                        HierarchyCollector.intoHierarchy(
                                HierarchyImpl::getId,
                                HierarchyImpl::getParentId,
                                e -> SectorDto.builder().id(e.getId()).build(),
                                (parent, child) -> parent.getChildren().add(child)
                        )
                );
        return result;
    }
}
