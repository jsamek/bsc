package sam.bsc.data;

import java.math.BigDecimal;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Package entry for input and output (for basic solution they are identical for both input and output)
 */
@Data
@AllArgsConstructor
public class PackageEntry {

    /** Post code */
    private String zipCode;

    /** Package weight */
    private BigDecimal weight;

    /**
     * Provides data output in required format.
     * @return formatted string
     */
    @Override
    public String toString() {
        return zipCode + " " + String.format(java.util.Locale.US, "%.3f", weight);
    }

}
