package data.shipsystems.scripts;

import java.awt.Color;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript.State;
import com.fs.starfarer.api.util.Misc;

public class istl_SigmaDisruptor extends BaseShipSystemScript {
	public static final float ENERGY_DAM_PENALTY_MULT = 0.5f;
	
	public static final float DISRUPTION_DUR = 3f;
	public static final float MIN_DISRUPTION_RANGE = 700f;
	
	public static final Color OVERLOAD_COLOR = new Color(105,255,205,255);
	
	public static final Color JITTER_COLOR = new Color(105,255,205,155);
	public static final Color JITTER_UNDER_COLOR = new Color(105,255,205,255);

	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		//boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			//player = ship == Global.getCombatEngine().getPlayerShip();
		} else {
			return;
		}
		
		//stats.getEnergyWeaponDamageMult().modifyMult(id, 1f - (1f - ENERGY_DAM_PENALTY_MULT) * effectLevel);
		stats.getEnergyWeaponDamageMult().modifyMult(id, ENERGY_DAM_PENALTY_MULT);
		
		float jitterLevel = effectLevel;
		if (state == State.OUT) {
			jitterLevel *= jitterLevel;
		}
		float maxRangeBonus = 50f;
		//float jitterRangeBonus = jitterLevel * maxRangeBonus;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		if (state == State.OUT) {
			//jitterRangeBonus = maxRangeBonus + (1f - jitterLevel) * maxRangeBonus; 
		}
		
		ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 21, 0f, 3f + jitterRangeBonus);
		//ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus * 0.67f);
		ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0f, 0 + jitterRangeBonus);
		
		String targetKey = ship.getId() + "_acausal_target";
		Object foundTarget = Global.getCombatEngine().getCustomData().get(targetKey); 
		if (state == State.IN) {
			if (foundTarget == null) {
				ShipAPI target = findTarget(ship);
				if (target != null) {
					Global.getCombatEngine().getCustomData().put(targetKey, target);
				}
			}
		} else if (effectLevel >= 1) {
			if (foundTarget instanceof ShipAPI) {
				ShipAPI target = (ShipAPI) foundTarget;
				if (target.getFluxTracker().isOverloadedOrVenting()) target = ship;
				applyEffectToTarget(ship, target);
			}
		} else if (state == State.OUT && foundTarget != null) {
			Global.getCombatEngine().getCustomData().remove(targetKey);
		}
	}
	
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().unmodify(id);
	}
	
	protected ShipAPI findTarget(ShipAPI ship) {
		float range = getMaxRange(ship);
		boolean player = ship == Global.getCombatEngine().getPlayerShip();
		ShipAPI target = ship.getShipTarget();
		if (target != null) {
			float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
			float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
			if (dist > range + radSum) target = null;
		} else {
			if (target == null || target.getOwner() == ship.getOwner()) {
				if (player) {
					target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), HullSize.FIGHTER, range, true);
				} else {
					Object test = ship.getAIFlags().getCustom(AIFlags.MANEUVER_TARGET);
					if (test instanceof ShipAPI) {
						target = (ShipAPI) test;
						float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
						float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
						if (dist > range + radSum) target = null;
					}
				}
			}
			if (target == null) {
				target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), HullSize.FIGHTER, range, true);
			}
		}
		if (target == null || target.getFluxTracker().isOverloadedOrVenting()) target = ship;
		
		return target;
	}
	
	
	protected float getMaxRange(ShipAPI ship) {
		if (true) return MIN_DISRUPTION_RANGE;
		
		float range = 0f;
		
		for (WeaponAPI w : ship.getAllWeapons()) {
			if (w.getType() == WeaponType.BALLISTIC || w.getType() == WeaponType.ENERGY) {
				float curr = w.getRange();
				if (curr > range) range = curr;
			}
		}
		
		return range + MIN_DISRUPTION_RANGE;
	}

	
	protected void applyEffectToTarget(final ShipAPI ship, final ShipAPI target) {
		if (target.getFluxTracker().isOverloadedOrVenting()) {
			return;
		}
		if (target == ship) return;
		
		target.setOverloadColor(OVERLOAD_COLOR);
		target.getFluxTracker().beginOverloadWithTotalBaseDuration(DISRUPTION_DUR);
		//target.getEngineController().forceFlameout(true);
		
		if (target.getFluxTracker().showFloaty() || 
				ship == Global.getCombatEngine().getPlayerShip() ||
				target == Global.getCombatEngine().getPlayerShip()) {
			target.getFluxTracker().playOverloadSound();
			target.getFluxTracker().showOverloadFloatyIfNeeded("Induced Overload!", OVERLOAD_COLOR, 4f, true);
		}
		
		Global.getCombatEngine().addPlugin(new BaseEveryFrameCombatPlugin() {
			@Override
			public void advance(float amount, List<InputEventAPI> events) {
				if (!target.getFluxTracker().isOverloadedOrVenting()) {
					target.resetOverloadColor();
					Global.getCombatEngine().removePlugin(this);
				}
			}
		});
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		//float percent = (1f - ENERGY_DAM_PENALTY_MULT) * effectLevel * 100;
		float percent = (1f - ENERGY_DAM_PENALTY_MULT) * 100;
		if (index == 0) {
			return new StatusData((int)percent + "% less energy damage", false);
		}
		return null;
	}


	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		if (system.isOutOfAmmo()) return null;
		if (system.getState() != SystemState.IDLE) return null;
		
		ShipAPI target = findTarget(ship);
		if (target != null && target != ship) {
			return "READY";
		}
		if ((target == null || target == ship) && ship.getShipTarget() != null) {
			return "OUT OF RANGE";
		}
		return "NO TARGET";
		//return super.getInfoText(system, ship);
	}

	
	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		ShipAPI target = findTarget(ship);
		return target != null && target != ship;
		//return super.isUsable(system, ship);
	}
	

	
}








