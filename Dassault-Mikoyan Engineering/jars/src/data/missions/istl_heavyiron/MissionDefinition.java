package data.missions.istl_heavyiron;

import java.util.List;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		
		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Intervention Group Juno");
		api.setFleetTagline(FleetSide.ENEMY, "Antilles Slaver Ring");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces");
		api.addBriefingItem("Use your range advantage and deeper flux pool to finish enemies. Don't get swarmed.");
		api.addBriefingItem("Remember: Your armor will absorb tremendous punishment. Use your shield sparingly.");
		
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		//api.addToFleet(FleetSide.PLAYER, "harbinger_Strike", FleetMemberType.SHIP, "TTS Invisible Hand", true, CrewXPLevel.VETERAN);
		api.addToFleet(FleetSide.PLAYER, "istl_baikal_brone_elite", FleetMemberType.SHIP, "DMS Rokossovsky", true);
		api.addToFleet(FleetSide.PLAYER, "istl_grand_union_std", FleetMemberType.SHIP, "UNS Nameless", false);
		api.addToFleet(FleetSide.PLAYER, "istl_feeder_std", FleetMemberType.SHIP, "UNS Dar es Salaam", false);
		api.addToFleet(FleetSide.PLAYER, "istl_stoat_c_std", FleetMemberType.SHIP, "UNS Balls To The Wall", false);
		api.addToFleet(FleetSide.PLAYER, "istl_stoat_c_std", FleetMemberType.SHIP, "UNS Plowman", false);

		api.defeatOnShipLoss("DMS Rokossovsky");
		
		// Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "heron_Attack", FleetMemberType.SHIP, "Hiryu", false);
		api.addToFleet(FleetSide.ENEMY, "istl_naja_export", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "istl_naja_mk1_std", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "colossus3_Pirate", FleetMemberType.SHIP, "Golden Circle", true);
		api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "hound_Overdriven", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "hound_d_pirates_Shielded", FleetMemberType.SHIP, false);
		
		api.addToFleet(FleetSide.ENEMY, "istl_lodestar_export", FleetMemberType.SHIP, false);
                api.addToFleet(FleetSide.ENEMY, "nebula_Standard", FleetMemberType.SHIP, false);

		
		// Set up the map.
		float width = 27000f;
		float height = 21000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		api.addNebula(minX + width * 0.5f - 300, minY + height * 0.5f, 1000);
		api.addNebula(minX + width * 0.5f + 300, minY + height * 0.5f, 1000);
		
		for (int i = 0; i < 5; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 400f; 
			api.addNebula(x, y, radius);
		}
		
		// Add an asteroid field
		api.addAsteroidField(minX + width/2f, minY + height/2f, 15, 9200f,
								25f, 75f, 135);
		
                api.addPlanet(0, 0, 120f, "istl_aridbread", 50f, true);
                
		api.addPlugin(new BaseEveryFrameCombatPlugin() {
			public void init(CombatEngineAPI engine) {
				engine.getContext().setStandoffRange(6000f);
			}
			public void advance(float amount, List events) {
			}
		});
		
	}

}




