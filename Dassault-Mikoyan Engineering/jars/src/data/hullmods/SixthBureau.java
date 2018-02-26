package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class SixthBureau extends BaseHullMod {

	/* A Ship of the Sixth Bureau
	 * focus on stealth and flux stats over direct combat stuff.
	 * - much lower sensor profile
	 * - slightly better flux handling
	 * - slightly higher maintenance costs
	 */
	
	//private static final float ARMOR_BONUS_MULT = 1.1f;
	private static final float PROFILE_DECREASE = 25f;
	private static final float CAPACITY_MULT = 1.05f;
	private static final float DISSIPATION_MULT = 1.05f;
	private static final float SUPPLY_USE_MULT = 1.15f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		// 25% lower sensor profile
		stats.getSensorProfile().modifyMult(id, 1f - PROFILE_DECREASE * 0.01f);
		
		// 5% better flux stats
		stats.getFluxCapacity().modifyMult(id, CAPACITY_MULT);
		stats.getFluxDissipation().modifyMult(id, DISSIPATION_MULT);
                
		// 10% higher supply use
		stats.getSuppliesPerMonth().modifyMult(id, SUPPLY_USE_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) PROFILE_DECREASE;
		if (index == 1) return "" + (int) ((CAPACITY_MULT - 1f) * 100f + 0.01f); 
		if (index == 2) return "" + (int)((SUPPLY_USE_MULT - 1f) * 100f + 0.01f) + "%";
		return null;
	}

}
