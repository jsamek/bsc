package sam.bsc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sam.bsc.data.PackageEntry;
import sam.bsc.data.PackageInfo;

@Service
public class PackageService {

    /**
     * Loads initial data from file with provided path and stores in storage
     * @param storage package info storage
     * @param filePath path of file containing initial data
     */
    public void processInitialData(Map<String, PackageInfo> storage, Path filePath) {
        List<PackageEntry> initData = null;
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            initData = readFromFile(br);
        } catch (IOException e) {
            System.out.printf("Error while reading %s file due to: %s%n", filePath.getFileName(), e.getMessage());
        }

        if (initData != null) {
            initData.forEach(entry -> storePackageEntry(storage, entry));
        }
    }

    /**
     * Reads reader content and converts it to list of package entries
     * @param reader reader pointing to file
     * @return list of {@link PackageEntry}
     */
    private List<PackageEntry> readFromFile(BufferedReader reader) {
        List<PackageEntry> retVal = null;
        if (reader != null) {
            retVal = reader
                    .lines()
                    .map(this::getPackageEntry)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            ;
        }
        return retVal;
    }

    /**
     * Stores {@link PackageEntry} from line into internal storage
     * @param storage storage
     * @param line line from input
     */
    public void storePackageEntry(Map<String, PackageInfo> storage, String line) {
        PackageEntry packageEntry = getPackageEntry(line);
        storePackageEntry(storage, packageEntry);
    }

    /**
     * Stores {@link PackageEntry} into internal storage
     * @param storage storage
     * @param packageEntry package entry to be stored
     */
    public void storePackageEntry(Map<String, PackageInfo> storage, PackageEntry packageEntry) {
        if (packageEntry != null) {
            storage.putIfAbsent(packageEntry.getZipCode(), new PackageInfo());
            PackageInfo info = storage.get(packageEntry.getZipCode());
            info.setWeight(info.getWeight().add(packageEntry.getWeight()));
        } else {
            System.out.println("Invalid format. Package entry not accepted");
        }
    }

    /**
     * Validates line from file or console and retrieves {@link PackageEntry} data if available.
     * @param line line from console or file
     * @return package entry
     */
    private PackageEntry getPackageEntry(String line) {
        PackageEntry retVal = null;
        if (line.matches("^[0-9]+(?:\\.[0-9]{1,3})? [0-9]{5}$")) {
            String[] lineItems = line.split(" ");
            retVal = new PackageEntry(
                    lineItems[1],
                    BigDecimal.valueOf(Double.parseDouble(lineItems[0]))
            );
        }
        return retVal;
    }

}
