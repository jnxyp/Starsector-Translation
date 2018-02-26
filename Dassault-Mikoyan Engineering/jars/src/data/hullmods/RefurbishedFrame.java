package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class RefurbishedFrame extends BaseHullMod
{

    public static final float REPAIR_RATE_BONUS = 25f;
    public static final float CR_RECOVERY_BONUS = 25f;
    public static final float REPAIR_BONUS = 25f;

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id)
    {
        stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
        stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);

        stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
        stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
    }

    public String getDescriptionParam(int index, HullSize hullSize)
    {
        if (index == 0)
        {
            return "" + (int) REPAIR_BONUS;
        }
        if (index == 1)
        {
            return "" + (int) CR_RECOVERY_BONUS;
        }
        return null;
    }

}
