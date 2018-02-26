package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Set;

public class MonoblocConstruction extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(1);
    public static final float FLUX_RESISTANCE = 25f; // Stack this with Resistant Flux Conduits and you're EMP-proof
    private static final float OVERLOAD_DUR = 50f; // Overload duration cut by 33%.
    private static final float BALLISTIC_RANGE_MULT = 0.85f; // Range reduction for ballistics. Dizzy, you're a bitch, but you're also right.
    
    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("heavyarmor"); // No immunity
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id)
    {
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f); // GET RECKLESS.
        stats.getOverloadTimeMod().modifyPercent(id, OVERLOAD_DUR); // Whee what are consequences.
        stats.getBallisticWeaponRangeBonus().modifyMult(id, BALLISTIC_RANGE_MULT); // range cut to ballistics.
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
            return "" + (int) OVERLOAD_DUR;
        }
        if (index == 2)
        {
            return "" + (int) Math.round((1f - BALLISTIC_RANGE_MULT) * 100f);
        }
        return null;
    }

}
