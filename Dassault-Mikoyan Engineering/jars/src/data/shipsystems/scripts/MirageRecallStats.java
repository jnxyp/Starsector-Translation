package data.shipsystems.scripts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.State;

public class MirageRecallStats extends BaseShipSystemScript {
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_COLOR = new Color(85, 115, 215,255);

	
        @Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		
		
		if (effectLevel > 0) {
			float jitterLevel = effectLevel;
			
			boolean firstTime = false;
			final String fightersKey = ship.getId() + "_recall_device_target";
			List<ShipAPI> fighters = null;
			if (!Global.getCombatEngine().getCustomData().containsKey(fightersKey)) {
				fighters = getFighters(ship);
				Global.getCombatEngine().getCustomData().put(fightersKey, fighters);
				firstTime = true;
			} else {
				fighters = (List<ShipAPI>) Global.getCombatEngine().getCustomData().get(fightersKey);
			}
			if (fighters == null) { // shouldn't be possible, but still
				fighters = new ArrayList<ShipAPI>();
			}
			
			for (ShipAPI fighter : fighters) {
				if (fighter.isHulk()) continue;
				
				float maxRangeBonus = fighter.getCollisionRadius() * 1f;
				float jitterRangeBonus = 5f + jitterLevel * maxRangeBonus;
				
				if (firstTime) {
					Global.getSoundPlayer().playSound("system_phase_skimmer", 1f, 0.5f, fighter.getLocation(), fighter.getVelocity());
				}
				
				fighter.setJitter(KEY_JITTER, JITTER_COLOR, jitterLevel, 10, 0f, jitterRangeBonus);
				fighter.setPhased(true);
				
				if (state == State.IN) {
					float alpha = 1f - effectLevel * 0.5f;
					fighter.setExtraAlphaMult(alpha);
				}
	
				if (effectLevel == 1) {
					if (fighter.getWing() != null && fighter.getWing().getSource() != null) {
						fighter.getWing().getSource().makeCurrentIntervalFast();
						fighter.getWing().getSource().land(fighter);
					} else {
						fighter.setExtraAlphaMult(1);
					}
				}
			}
		}
	}
	
	public static List<ShipAPI> getFighters(ShipAPI carrier) {
		List<ShipAPI> result = new ArrayList<ShipAPI>();
		
		for (ShipAPI ship : Global.getCombatEngine().getShips()) {
			if (!ship.isFighter()) continue;
			if (ship.getWing() == null) continue;
			if (ship.getWing().getSourceShip() == carrier) {
				result.add(ship);
			}
		}
		
		return result;
	}
	
	
        @Override
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		
		final String fightersKey = ship.getId() + "_recall_device_target";
		Global.getCombatEngine().getCustomData().remove(fightersKey);
		
//		for (ShipAPI fighter : getFighters(ship)) {
//			fighter.setPhased(false);
//			fighter.setCopyLocation(null, 1f, fighter.getFacing());
//		}
	}
	
	
        @Override
	public StatusData getStatusData(int index, State state, float effectLevel) {
		return null;
	}

	
}








