package data.scripts;

import java.awt.Color;

/**
 * Utilities for working with Java colors.
 *
 * This class contains code originally distributed as part of ElizaLib.
 *
 * @author Eliza Weisman
 */
public class DMEUtils {
    private DMEUtils() { }

    /**
     * Linear interpolate between <code>a</code> and <code>b</code> by
     * <code>amount</code>.
     * @method lerp
     * @param   a the first number to lerp between
     * @param   b the second number to lerp between
     * @param   amount the amount to interpolate. must be between 0 and 1.
     * @return  a value that's <code>amount</code> between <code>a</code> and
     *           <code>b</code>
     */
    public static final float lerp(float a, float b, float amount) {
        assert amount >= 0: "Amount to lerp must not be less than 0.";
        assert amount <= 1: "Amount to lerp must not be greater than 1.";
        return a + (b - a) * amount;
    }

    /**
     * Linear interpolate two colors through the RGB color space.
     */
    public static final Color lerpRGB(Color a, Color b, float amount) {
        final float[] aC = a.getRGBComponents(null);
        final float[] bC = b.getRGBComponents(null);
        return new Color( lerp(aC[0], bC[0], amount)
                        , lerp(aC[1], bC[1], amount)
                        , lerp(aC[2], bC[2], amount)
                        , lerp(aC[3], bC[3], amount)
                        );

    }

}
