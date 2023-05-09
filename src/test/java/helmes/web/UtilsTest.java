package helmes.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import helmes.model.SectorDto;
import helmes.model.SectorRowDto;

@ExtendWith(MockitoExtension.class)
public class UtilsTest {

    @Test
    void shouldReturnEmptyListFormEmptySource() {
        List<SectorRowDto> result = Utils.tranformToPlain(Lists.newArrayList());
        assertThat(result).isEmpty();
    }

    @Test
    void throwExceptionWhenSourceIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Utils.tranformToPlain(null);
        });
    }

    @Test
    void shouldReturnPlainListFromNotEmptySource() {
        ArrayList<SectorDto> dtoList = Lists.newArrayList(
                SectorDto.builder().id(1).name("1").children(
                        Sets.newHashSet(
                                SectorDto.builder().id(11).name("11").build(),
                                SectorDto.builder().id(12).name("12").build(),
                                SectorDto.builder().id(13).name("13").build()
                        )
                ).build(),
                SectorDto.builder().id(2).name("2").children(
                        Sets.newHashSet(
                                SectorDto.builder().id(21).name("21").build(),
                                SectorDto.builder().id(22).name("22").build()
                        )
                ).build()
        );

        List<SectorRowDto> result = Utils.tranformToPlain(dtoList);

        assertThat(result).isNotEmpty().hasSize(7);
        assertThat(result).extracting(SectorRowDto::getId).containsExactly(1, 11, 12, 13, 2, 21, 22);
        assertThat(result).extracting(SectorRowDto::getName).containsExactly("1", "____11", "____12", "____13", "2", "____21", "____22");
    }

}
