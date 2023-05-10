package helmes.web;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import helmes.model.SectorDto;
import helmes.model.SectorRowDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static final String PREFIX = "____";

    public static List<SectorRowDto> tranformToPlain(Collection<SectorDto> sectors) {
        return tranformToPlain(sectors, 0);
    }

    private static List<SectorRowDto> tranformToPlain(Collection<SectorDto> sectors, int level) {
        List result = Lists.newArrayList();
        List<SectorDto> sorted = sectors.stream().sorted(Comparator.comparing(SectorDto::getName)).collect(toList());
        for (SectorDto dto : sorted) {
            result.add(buildRow(dto, level));
            result.addAll(tranformToPlain(dto.getChildren(), level + 1));
        }
        return result;
    }

    private static SectorRowDto buildRow(SectorDto entity, int level) {
        return SectorRowDto.builder().id(entity.getId()).name(addPrefix(entity.getName(), level)).build();
    }

    private static String addPrefix(String name, int level) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < level; i++) {
            result.append(PREFIX);
        }
        result.append(name);
        return result.toString();
    }

}
