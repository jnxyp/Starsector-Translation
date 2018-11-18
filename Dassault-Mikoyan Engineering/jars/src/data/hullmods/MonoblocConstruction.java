package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MonoblocConstruction extends BaseHullMod
{
    private static Map mag = new HashMap(); //
    static {
	mag.put(HullSize.FIGHTER, 600f);
	mag.put(HullSize.FRIGATE, 600f);
	mag.put(HullSize.DESTROYER, 700f);
	mag.put(HullSize.CRUISER, 800f);
	mag.put(HullSize.CAPITAL_SHIP, 900f);
    }
    
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(1);
    public static final float FLUX_RESISTANCE = 25f; // Stack this with Resistant Flux Conduits and you're EMP-proof
    private static final float OVERLOAD_DUR_MULT = 0.5f; // Overload duration cut by 50%.
    //private static final float BALLISTIC_RANGE_MULT = 0.85f; // Range reduction multiplier for ballistics. Depreciated.
    //private static final float RANGE_THRESHOLD = 600f; // Threshold for range reduction; no effect under 600; Depreciated.
    private static final float RANGE_MULT = 0.5f; // Range penalty above threshold.
    
    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("heavyarmor"); // No heavy armor on DME ships.
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id)
    {
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f); // GET RECKLESS.
        stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_DUR_MULT); // Whee what are consequences.
        //stats.getBallisticWeaponRangeBonus().modifyMult(id, BALLISTIC_RANGE_MULT); // range cut to ballistics.
        //stats.getWeaponRangeThreshold().modifyFlat(id, RANGE_THRESHOLD); // old fixed range threshold.
        stats.getWeaponRangeThreshold().modifyFlat(id, (Float) mag.get(hullSize)); // new range threshold by hull size
	stats.getWeaponRangeMultPastThreshold().modifyMult(id, RANGE_MULT); // range multiplier beyond threshold.
    }
    
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id)
    {
        for (String tmp : BLOCKED_HULLMODS)
        {
            if (ship.getVariant().getHullMods().contains(tmp))
            {
                ship.getVariant().removeMod(tmp);
                DMEBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    public String getDescriptionParam(int index, HullSize hullSize)
    {
        if (index == 0)
        {
            return "" + (int) FLUX_RESISTANCE;
        }
        if (index == 1)
        {
            return "" + (int) Math.round((1f - OVERLOAD_DUR_MULT) * 100f);
        }
        if (index == 2)
        {
            //return "" + (int) Math.round((1f - BALLISTIC_RANGE_MULT) * 100f);
            return "" + (int) Math.round((1f - RANGE_MULT) * 100f);
        }
        //if (index == 3)
        //{
        //    return "" + (int) Math.round(RANGE_THRESHOLD);
        //}
        if (index == 3) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
	if (index == 4) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
	if (index == 5) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
	if (index == 6) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
        return null;
    }

}
