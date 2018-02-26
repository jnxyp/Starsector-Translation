package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Set;

public class SkipspaceReactionFurnace extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(2);
    public static final float FLUX_RESISTANCE = 50f;
    private static final float PROFILE_DECREASE = 25f;
    public static final float VENT_RATE_BONUS = 25f;
    //Engine damage float

    static
    {
        // These hullmods will automatically be removed
        // Not as elegant as blocking them in the first place, but
        // this method doesn't require editing every hullmod's script
        BLOCKED_HULLMODS.add("fluxbreakers"); // No immunity
        BLOCKED_HULLMODS.add("safetyoverrides"); // It would be bad

    }
    
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id)
    {
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f);
        stats.getSensorProfile().modifyPercent(id, -PROFILE_DECREASE);
        stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
        //Might do some bonus engine damage later
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
            return "" + (int) VENT_RATE_BONUS;
        }
        if (index == 2)
        {
            return "" + (int) PROFILE_DECREASE;
        }
        return null;
    }

}
