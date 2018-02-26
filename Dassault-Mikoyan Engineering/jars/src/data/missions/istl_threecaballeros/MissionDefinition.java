package data.missions.istl_threecaballeros;

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
        api.initFleet(FleetSide.PLAYER, "", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);

        // Set a small blurb for each fleet that shows up on the mission detail and
        // mission results screens to identify each side.
        api.setFleetTagline(FleetSide.PLAYER, "Three humble gentlemen, wandering the stars.");
        api.setFleetTagline(FleetSide.ENEMY, "A nasty bunch of rotten, weaselly scoundrels.");

        // These show up as items in the bulleted list under 
        // "Tactical Objectives" on the mission detail screen
        api.addBriefingItem("Destroy or drive off the attacking pirate swarm.");
        api.addBriefingItem("Don't lose 'Pup Tentacle' - it's a rare and powerful hull.");
        api.addBriefingItem("You've got the advantage in firepower and durability. Exploit it.");

        // Set up the player's fleet.  Variant names come from the
        // files in data/variants and data/variants/fighters
        api.addToFleet(FleetSide.PLAYER, "istl_tunguska_fsup", FleetMemberType.SHIP, "Pup Tentacle", true);
        api.addToFleet(FleetSide.PLAYER, "istl_wanderer_std", FleetMemberType.SHIP, "Peaches En Regalia", false);
        api.addToFleet(FleetSide.PLAYER, "istl_lodestar_heavy", FleetMemberType.SHIP, "Roxy & Elsewhere", false);

        // Set up the enemy fleet.
        api.addToFleet(FleetSide.ENEMY, "istl_grand_union_std", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "istl_stoat_c_attack", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "istl_sevastopol_mk1_std", FleetMemberType.SHIP, "I'm The Slime", false);
        api.addToFleet(FleetSide.ENEMY, "istl_naja_mk1_std", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "istl_naja_mk1_brawl", FleetMemberType.SHIP, false);
        api.addToFleet(FleetSide.ENEMY, "istl_naja_mk1_brawl", FleetMemberType.SHIP, false);

        api.defeatOnShipLoss("Pup Tentacle");

        // Set up the map.
        float width = 12000f;
        float height = 12000f;
        api.initMap((float) -width / 2f, (float) width / 2f, (float) -height / 2f, (float) height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        // Add an asteroid field
        api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
                20f, 70f, 100);

        api.addPlanet(0, 0, 50f, "star_red_giant", 250f, true);

    }

}
