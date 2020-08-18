package sam.bsc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sam.bsc.data.PackageEntry;
import sam.bsc.data.PackageInfo;

public class PackageServiceTest {

    public static final String ZIP_1 = "12345";
    public static final BigDecimal WEIGHT_1 = BigDecimal.valueOf(3.2d);

    private final PackageService packageServicw = new PackageService();

    @Test
    public void test() {
        Map<String, PackageInfo> storage = new HashMap<>();

        // store package data from file
        Path filePath = Paths.get("src/test/resources/packages.txt");
        packageServicw.processInitialData(storage, filePath);
        assertEquals(4, storage.size());

        // store package entry manually
        PackageEntry pe1 = new PackageEntry(ZIP_1, WEIGHT_1);
        packageServicw.storePackageEntry(storage, pe1);
        assertEquals(5, storage.size());

        // store the same entry again
        packageServicw.storePackageEntry(storage, pe1);
        // item count in storage does not change
        assertEquals(5, storage.size());
        // weight doubles
        assertEquals(WEIGHT_1.add(WEIGHT_1), storage.get(ZIP_1).getWeight());

    }

}
