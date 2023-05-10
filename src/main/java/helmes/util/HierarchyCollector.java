package helmes.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HierarchyCollector {

    public static final <K, E, R extends Hierarchy> Collector<R, ?, List<E>> intoHierarchy(
            Function<? super R, ? extends K> keyMapper,
            Function<? super R, ? extends K> parentKeyMapper,
            Function<? super R, ? extends E> nodeMapper,
            BiConsumer<? super E, ? super E> parentChildAppender
    ) {
        return Collectors.collectingAndThen(
                Collectors.toMap(keyMapper, r -> new AbstractMap.SimpleImmutableEntry<R, E>(
                        r, nodeMapper.apply(r)
                )),
                m -> {
                    List<E> r = new ArrayList<>();

                    m.forEach((k, v) -> {
                        Map.Entry<R, E> parent = m.get(parentKeyMapper.apply(v.getKey()));

                        if (parent != null)
                            parentChildAppender.accept(parent.getValue(), v.getValue());
                        else
                            r.add(v.getValue());
                    });

                    return r;
                }
        );
    }

}
