package data.missions.istl_mothershipconnection;

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
        api.setFleetTagline(FleetSide.PLAYER, "Tech-mining Group 'Star Child'");
        api.setFleetTagline(FleetSide.ENEMY, "Funkless Raiders");

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Destroy the enemy carriers.");
        api.addBriefingItem("Don't lose your own carriers and command ship.");
        api.addBriefingItem("Stay frosty, stay funky.");

        // Set up the player's fleet.  Variant names come from the
        // files in data/variants and data/variants/fighters
        api.addToFleet(FleetSide.PLAYER, "istl_mindanao_std", FleetMemberType.SHIP, "Tear The Roof Off The Sucker", true);
        api.addToFleet(FleetSide.PLAYER, "istl_tereshkova_std", FleetMemberType.SHIP, "The Electric Spanking of War Babies", false);
        api.addToFleet(FleetSide.PLAYER, "istl_leyte_std", FleetMemberType.SHIP, "Night of the Thumpasorus Peoples", false);
        api.addToFleet(FleetSide.PLAYER, "istl_samoyed_elite", FleetMemberType.SHIP, "Bop Gunner", false);

        // Set up the enemy fleet.
        api.addToFleet(FleetSide.ENEMY, "heron_Strike", FleetMemberType.SHIP, "The Man", false);
        api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, "Tin God", false);
        api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, "Unfunky UFO", false);
        api.addToFleet(FleetSide.ENEMY, "mule_Standard", FleetMemberType.SHIP, "Volume Control", false);
        api.addToFleet(FleetSide.ENEMY, "centurion_Assault", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "centurion_Assault", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "vigilance_Standard", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "vigilance_Standard", FleetMemberType.SHIP, false);


        api.defeatOnShipLoss("Tear The Roof Off The Sucker");

        // Set up the map.
        float width = 12000f;
        float height = 12000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        // Add an asteroid field
        api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
                15f, 60f, 90);

        api.addPlanet(0, 0, 50f, "star_red_dwarf", 250f, true);

    }

}
