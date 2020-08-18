package sam.bsc.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import sam.bsc.data.PackageInfo;

public class OutputGeneratorTest {

    public static final String ZIP_1 = "12345";
    public static final BigDecimal WEIGHT_1 = BigDecimal.valueOf(3.2d);

    @Test
    public void testEmpty() {

        Map<String, PackageInfo> storage = new HashMap<>();

        List<String> output = OutputGenerator.generateOutput(storage);

        assertEquals(0, output.size());
    }

    @Test
    public void testOne() {

        Map<String, PackageInfo> storage = new HashMap<>();
        final PackageInfo packageInfo = new PackageInfo();
        packageInfo.setWeight(WEIGHT_1);
        storage.put(ZIP_1, packageInfo);

        List<String> output = OutputGenerator.generateOutput(storage);

        assertEquals(1, output.size());
        final String packageEntry = output.get(0);
        assertNotNull(packageEntry);
        assertEquals(ZIP_1 + " " + String.format(Locale.US, "%.3f", WEIGHT_1), packageEntry);

    }

}
