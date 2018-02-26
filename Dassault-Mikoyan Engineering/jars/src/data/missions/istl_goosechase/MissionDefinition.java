package data.missions.istl_goosechase;

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
        api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);

        // Set a small blurb for each fleet that shows up on the mission detail and
        // mission results screens to identify each side.
        api.setFleetTagline(FleetSide.PLAYER, "Snow Goose Test Group");
        api.setFleetTagline(FleetSide.ENEMY, "Automated Test Targets");

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Destroy the enemy fleet.");
        api.addBriefingItem("Don't lose the prototype. We'll bill you.");

        // Set up the player's fleet.  Variant names come from the
        // files in data/variants and data/variants/fighters
        api.addToFleet(FleetSide.PLAYER, "istl_snowgoose_std", FleetMemberType.SHIP, "DMS Incontrovertible", true);
        api.addToFleet(FleetSide.PLAYER, "istl_samoyed_std", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.PLAYER, "istl_samoyed_std", FleetMemberType.SHIP, false);

        // Set up the enemy fleet.
        api.addToFleet(FleetSide.ENEMY, "dominator_Assault", FleetMemberType.SHIP, "Incapable Of Listening To Reason", false);
        api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, "Smooth Jazz Will Be Deployed", false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "The Part Where He Kills You", false);
        api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Android Hell Is A Real Place", false);
        api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, "I Guess I Haven't Killed You Yet", false);
        api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, "Making A Note Here, Huge Success", false);

        api.defeatOnShipLoss("DMS Incontrovertible");

        // Set up the map.
        float width = 24000f;
        float height = 18000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        for (int i = 0; i < 25; i++)
        {
            float x = (float) Math.random() * width - width / 2;
            float y = (float) Math.random() * height - height / 2;
            float radius = 1000f + (float) Math.random() * 1000f;
            api.addNebula(x, y, radius);
        }

        api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.4f, 2000);
        api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.5f, 2000);
        api.addNebula(minX + width * 0.8f - 2000, minY + height * 0.6f, 2000);

        api.addObjective(minX + width * 0.4f + 1000, minY + height * 0.4f, "sensor_array");
        api.addObjective(minX + width * 0.8f - 2000, minY + height * 0.3f + 1000, "comm_relay");

        api.addObjective(minX + width * 0.85f - 3000, minY + height * 0.7f - 1000, "nav_buoy");
        api.addObjective(minX + width * 0.2f + 2000, minY + height * 0.7f - 1000, "comm_relay");

        api.addAsteroidField(minX, minY + height * 0.5f, 0, height,
                20f, 70f, 50);

        //api.addPlanet(0, 0, 350f, "barren", 200f, true);
    }

}
