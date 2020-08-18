package sam.bsc.generator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import sam.bsc.data.PackageEntry;
import sam.bsc.data.PackageInfo;

/**
 * Output generator.
 */
public class OutputGenerator {

    /**
     * Generates output of storage content.
     * @param storage {@link PackageEntry} storage
     * @return generated lines for output to console
     */
    public static List<String> generateOutput(Map<String, PackageInfo> storage) {
        List<PackageEntry> output = new ArrayList<>();
        storage
                .keySet()
                .forEach((key) -> output.add(
                        new PackageEntry(key, storage.get(key).getWeight())
                ))
        ;
        return output
                .stream()
                .sorted(Comparator.comparing(PackageEntry::getWeight).reversed())
                .map(PackageEntry::toString)
                .collect(Collectors.toList())
        ;
    }
}
