package sam.bsc.data;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Initial version of package information to be stored. Currently they are grouped by zip code.
 * Can be simply extended by price.
 */
@Data
public class PackageInfo {
    /** Package weight */
    private BigDecimal weight = BigDecimal.ZERO;
}
