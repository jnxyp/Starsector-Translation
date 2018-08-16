package data.missions.istl_theyshallnotpass;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin
{

    public void defineMission(MissionDefinitionAPI api)
    {

        // Set up the fleets so we can add ships and fighter wings to them.
        // In this scenario, the fleets are attacking each other, but
        // in other scenarios, a fleet may be defending or trying to escape
        api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "NIS", FleetGoal.ATTACK, true);

        // Set a small blurb for each fleet that shows up on the mission detail and
        // mission results screens to identify each side.
        api.setFleetTagline(FleetSide.PLAYER, "3rd Fleet");
        api.setFleetTagline(FleetSide.ENEMY, "The Guarantor's Guard");

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Destroy the enemy.");
        api.addBriefingItem("Don't lose the DMS Talleyrand");

        // Set up the player's fleet.  Variant names come from the
        // files in data/variants and data/variants/fighters
        api.addToFleet(FleetSide.PLAYER, "istl_jeannedarc_std", FleetMemberType.SHIP, "DMS Talleyrand", true);
        api.addToFleet(FleetSide.PLAYER, "istl_baikal_std", FleetMemberType.SHIP, "DMS Konev", false);
        api.addToFleet(FleetSide.PLAYER, "istl_leyte_std", FleetMemberType.SHIP, "DMS Hougoumont", false);
        api.addToFleet(FleetSide.PLAYER, "istl_stoat_std", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "istl_stoat_std", FleetMemberType.SHIP, false);

        // Set up the enemy fleet.
        api.addToFleet(FleetSide.ENEMY, "conquest_Standard", FleetMemberType.SHIP, "NIS Imperator", false);
        api.addToFleet(FleetSide.ENEMY, "dominator_d_Assault", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "condor_Strike", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "falcon_CS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "hammerhead_Balanced", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "hammerhead_d_CS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "lasher_d_CS", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "lasher_d_CS", FleetMemberType.SHIP, false);

        api.defeatOnShipLoss("DMS Talleyrand");

        // Set up the map.
        float width = 15000f;
        float height = 18000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        api.addObjective(minX + width * 0.4f + 1000, minY + height * 0.4f, "sensor_array");
	api.addObjective(minX + width * 0.8f - 2000, minY + height * 0.3f + 1000, "comm_relay");
		
	api.addObjective(minX + width * 0.85f - 3000, minY + height * 0.7f - 1000, "nav_buoy");
	api.addObjective(minX + width * 0.6f - 1000, minY + height * 0.6f, "sensor_array");

        api.addPlanet(0, 0, 120f, "water", 100f, true);

    }

}
