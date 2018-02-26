package data.shipsystems.scripts;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class istl_BBPhaseCloak extends BaseShipSystemScript {

	public static final Color JITTER_COLOR = new Color(105,255,205,255);
	public static final float JITTER_FADE_TIME = 0.6f;
	
	public static final float SHIP_ALPHA_MULT = 0.3f;
	//public static final float VULNERABLE_FRACTION = 0.875f;
	public static final float VULNERABLE_FRACTION = 0f;
	public static final float INCOMING_DAMAGE_MULT = 0.15f;
	
	public static final float MAX_TIME_MULT = 3f;
	
	
	protected Object STATUSKEY1 = new Object();
	protected Object STATUSKEY2 = new Object();
	protected Object STATUSKEY3 = new Object();
	protected Object STATUSKEY4 = new Object();
	
	
	public static float getMaxTimeMult(MutableShipStatsAPI stats) {
		return 1f + (MAX_TIME_MULT - 1f) * stats.getDynamic().getValue(Stats.PHASE_TIME_BONUS_MULT);
	}
	
	protected void maintainStatus(ShipAPI playerShip, State state, float effectLevel) {
		float level = effectLevel;
		float f = VULNERABLE_FRACTION;
		
		ShipSystemAPI cloak = playerShip.getPhaseCloak();
		if (cloak == null) cloak = playerShip.getSystem();
		if (cloak == null) return;
		
		if (level > f) {
//			Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1,
//					cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "can not be hit", false);
			Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY2,
					cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "time flow altered", false);
		} else {
//			float INCOMING_DAMAGE_MULT = 0.25f;
//			float percent = (1f - INCOMING_DAMAGE_MULT) * getEffectLevel() * 100;
//			Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY3,
//					spec.getIconSpriteName(), cloak.getDisplayName(), "damage mitigated by " + (int) percent + "%", false);
		}
	}
	
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
			id = id + "_" + ship.getId();
		} else {
			return;
		}
		
		if (player) {
			maintainStatus(ship, state, effectLevel);
		}
		
		if (Global.getCombatEngine().isPaused()) {
			return;
		}
		
		if (state == State.COOLDOWN || state == State.IDLE) {
			unapply(stats, id);
			return;
		}
		
		float level = effectLevel;
		//float f = VULNERABLE_FRACTION;

		
		float jitterLevel = 0f;
		float jitterRangeBonus = 0f;
		float levelForAlpha = level;
		
		ShipSystemAPI cloak = ship.getPhaseCloak();
		if (cloak == null) cloak = ship.getSystem();
		
		
		if (state == State.IN || state == State.ACTIVE) {
			ship.setPhased(true);
			levelForAlpha = level;
		} else if (state == State.OUT) {
			ship.setPhased(true);
			levelForAlpha = level;
//			if (level >= f) {
//				ship.setPhased(true);
//				if (f >= 1) {
//					levelForAlpha = level;
//				} else {
//					levelForAlpha = (level - f) / (1f - f);
//				}
//				float time = cloak.getChargeDownDur();
//				float fadeLevel = JITTER_FADE_TIME / time;
//				if (level >= f + fadeLevel) {
//					jitterLevel = 0f;
//				} else {
//					jitterLevel = (fadeLevel - (level - f)) / fadeLevel;
//				}
//			} else {
//				ship.setPhased(false);
//				levelForAlpha = 0f;
//				
//				float time = cloak.getChargeDownDur();
//				float fadeLevel = JITTER_FADE_TIME / time;
//				if (level < fadeLevel) {
//					jitterLevel = level / fadeLevel;
//				} else {
//					jitterLevel = 1f;
//				}
//				//jitterLevel = level / f;
//				//jitterLevel = (float) Math.sqrt(level / f);
//			}
		}
		
//		ship.setJitter(JITTER_COLOR, jitterLevel, 1, 0, 0 + jitterRangeBonus);
//		ship.setJitterUnder(JITTER_COLOR, jitterLevel, 11, 0f, 7f + jitterRangeBonus);
		//ship.getEngineController().fadeToOtherColor(this, spec.getEffectColor1(), new Color(0,0,0,0), jitterLevel, 1f);
		//ship.getEngineController().extendFlame(this, -0.25f, -0.25f, -0.25f);
		
		ship.setExtraAlphaMult(1f - (1f - SHIP_ALPHA_MULT) * levelForAlpha);
		ship.setApplyExtraAlphaToEngines(true);
		
		
		//float shipTimeMult = 1f + (MAX_TIME_MULT - 1f) * levelForAlpha;
		float shipTimeMult = 1f + (getMaxTimeMult(stats) - 1f) * levelForAlpha;
		stats.getTimeMult().modifyMult(id, shipTimeMult);
		if (player) {
			Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
		} else {
			Global.getCombatEngine().getTimeMult().unmodify(id);
		}
		
//		float mitigationLevel = jitterLevel;
//		if (mitigationLevel > 0) {
//			stats.getHullDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * mitigationLevel);
//			stats.getArmorDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * mitigationLevel);
//			stats.getEmpDamageTakenMult().modifyMult(id, 1f - (1f - INCOMING_DAMAGE_MULT) * mitigationLevel);
//		} else {
//			stats.getHullDamageTakenMult().unmodify(id);
//			stats.getArmorDamageTakenMult().unmodify(id);
//			stats.getEmpDamageTakenMult().unmodify(id);
//		}
	}


	public void unapply(MutableShipStatsAPI stats, String id) {
//		stats.getHullDamageTakenMult().unmodify(id);
//		stats.getArmorDamageTakenMult().unmodify(id);
//		stats.getEmpDamageTakenMult().unmodify(id);
		
		ShipAPI ship = null;
		//boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			//player = ship == Global.getCombatEngine().getPlayerShip();
			//id = id + "_" + ship.getId();
		} else {
			return;
		}
		
		Global.getCombatEngine().getTimeMult().unmodify(id);
		stats.getTimeMult().unmodify(id);
		
		ship.setPhased(false);
		ship.setExtraAlphaMult(1f);
		
//		stats.getMaxSpeed().unmodify(id);
//		stats.getMaxTurnRate().unmodify(id);
//		stats.getTurnAcceleration().unmodify(id);
//		stats.getAcceleration().unmodify(id);
//		stats.getDeceleration().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
//		if (index == 0) {
//			return new StatusData("time flow altered", false);
//		}
//		float percent = (1f - INCOMING_DAMAGE_MULT) * effectLevel * 100;
//		if (index == 1) {
//			return new StatusData("damage mitigated by " + (int) percent + "%", false);
//		}
		return null;
	}
}
