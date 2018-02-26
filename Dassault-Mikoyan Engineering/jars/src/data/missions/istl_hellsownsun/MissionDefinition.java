package data.missions.istl_hellsownsun;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "BCS", FleetGoal.ATTACK, false);
		api.initFleet(FleetSide.ENEMY, "SBS", FleetGoal.ATTACK, true);

//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.COORDINATED_MANEUVERS, 3);
//		api.getDefaultCommander(FleetSide.PLAYER).getStats().setSkillLevel(Skills.ELECTRONIC_WARFARE, 3);
		
		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Besson Active Service Unit");
		api.setFleetTagline(FleetSide.ENEMY, "Perfidious forces of the Sixth Bureau");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Destroy or drive off the Sixth Bureau task group.");
		api.addBriefingItem("Don't lose your flagship - your forces will be easy pickings if command is disrupted.");
		api.addBriefingItem("Acquit yourself well, volunteers. The Council expects great things from you.");
		
		// Set up the player's fleet.  Variant names come from the
		// files in data/variants and data/variants/fighters
		api.addToFleet(FleetSide.PLAYER, "istl_devilray_strike", FleetMemberType.SHIP, "Fang Leader", true);
		api.addToFleet(FleetSide.PLAYER, "istl_stormkestrel_elite", FleetMemberType.SHIP, "Base One", false);
		api.addToFleet(FleetSide.PLAYER, "istl_ifrit_support", FleetMemberType.SHIP, "Fang I", false);
		api.addToFleet(FleetSide.PLAYER, "istl_imp_assault", FleetMemberType.SHIP, "Fang II", false);
		api.addToFleet(FleetSide.PLAYER, "istl_imp_assault", FleetMemberType.SHIP, "Fang III", false);
		
		// Set up the enemy fleet.
		api.addToFleet(FleetSide.ENEMY, "istl_snowgoose_elite", FleetMemberType.SHIP, "SBS Night Killer V", false);
		api.addToFleet(FleetSide.ENEMY, "istl_kormoran_6e_elite", FleetMemberType.SHIP, "SBS Ultima Ratio", false);
		api.addToFleet(FleetSide.ENEMY, "istl_tunguska_6e_elite", FleetMemberType.SHIP, "SBS Eccentric Orbit", false);
		api.addToFleet(FleetSide.ENEMY, "afflictor_patrol", FleetMemberType.SHIP, "SBS String 1", false);
		api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 2", false);
		api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 3", false);
		api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 4", false);
		api.addToFleet(FleetSide.ENEMY, "wolf_patrol", FleetMemberType.SHIP, "SBS String 5", false);
		api.addToFleet(FleetSide.ENEMY, "omen_patrol", FleetMemberType.SHIP, "SBS String 6", false);
		api.addToFleet(FleetSide.ENEMY, "omen_patrol", FleetMemberType.SHIP, "SBS String 7", false);
		
		api.defeatOnShipLoss("Fang Leader");
		
		// Set up the map.
		float width = 16000f;
		float height = 16000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 560f, StarTypes.BROWN_DWARF, 320f, true);
		
	}

}
