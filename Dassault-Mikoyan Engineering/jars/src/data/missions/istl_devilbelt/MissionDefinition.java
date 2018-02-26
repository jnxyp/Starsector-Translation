package data.missions.istl_devilbelt;

import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.combat.EscapeRevealPlugin;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "SS", FleetGoal.ESCAPE, false, 5);
		api.initFleet(FleetSide.ENEMY, "BBS", FleetGoal.ATTACK, true, 5);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Contract Fleet 774-B");
		api.setFleetTagline(FleetSide.ENEMY, "Unknown Adversary");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("SS Just A Working Girl (and your rescued miner) must survive");
		api.addBriefingItem("At least 25% of your mining fleet must escape");
		
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "istl_tereshkova_export", FleetMemberType.SHIP, "SS Goats On Toast", true);
		api.addToFleet(FleetSide.PLAYER, "istl_lodestar_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.PLAYER, "istl_naja_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "istl_chamois_std", FleetMemberType.SHIP, "SS Just A Working Girl", false).getCaptain().setPersonality(Personalities.CAUTIOUS);
		
		api.addToFleet(FleetSide.PLAYER, "istl_centaur_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "istl_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.TIMID);
		api.addToFleet(FleetSide.PLAYER, "istl_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.TIMID);
		api.addToFleet(FleetSide.PLAYER, "istl_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.TIMID);
		api.addToFleet(FleetSide.PLAYER, "istl_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.TIMID);
		
		// Mark player flagship as essential
		api.defeatOnShipLoss("SS Just A Working Girl");
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "medusa_Attack", FleetMemberType.SHIP, "IMS Chastain", false).getCaptain().setPersonality(Personalities.RECKLESS);
		api.addToFleet(FleetSide.ENEMY, "istl_imp_assault", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.RECKLESS);
		api.addToFleet(FleetSide.ENEMY, "istl_imp_assault", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.RECKLESS);
		api.addToFleet(FleetSide.ENEMY, "istl_imp_support", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "istl_imp_support", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
                api.addToFleet(FleetSide.ENEMY, "tempest_Attack", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "istl_ifrit_support", FleetMemberType.SHIP, false);

		
		
		// Set up the map.
		float width = 15000f;
		float height = 27000f;
		
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		for (int i = 0; i < 15; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 900f; 
			api.addNebula(x, y, radius);
		}
		
		api.addNebula(minX + width * 0.8f, minY + height * 0.4f, 2000);
		api.addNebula(minX + width * 0.8f, minY + height * 0.5f, 2000);
		api.addNebula(minX + width * 0.8f, minY + height * 0.6f, 2000);
		
		api.addObjective(minX + width * 0.8f, minY + height * 0.4f, "sensor_array");
		api.addObjective(minX + width * 0.8f, minY + height * 0.6f, "nav_buoy");
		api.addObjective(minX + width * 0.3f, minY + height * 0.3f, "nav_buoy");
		api.addObjective(minX + width * 0.3f, minY + height * 0.7f, "sensor_array");
		api.addObjective(minX + width * 0.2f, minY + height * 0.5f, "comm_relay");

		api.addAsteroidField(minX + width * 0.5f, minY + height, 270, width,
								25f, 75f, 120);
				
		BattleCreationContext context = new BattleCreationContext(null, null, null, null);
		context.setInitialEscapeRange(7000f);
		api.addPlugin(new EscapeRevealPlugin(context));
	}

}






