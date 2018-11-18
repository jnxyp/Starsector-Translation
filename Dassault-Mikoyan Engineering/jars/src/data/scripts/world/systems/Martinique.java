package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
//import data.campaign.fleets.UnSecFleetManager;
import static data.scripts.world.systems.Nikolaev.addMarketplace;

import java.util.ArrayList;
import java.util.Arrays;

public class Martinique {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Martinique");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI martinique_star = system.initStar("martinique", // unique id for this star 
										    "star_red_dwarf",  // id in planets.json
										    320f, 		  // radius (in pixels at default zoom)
										    180, // corona
										    4f, // solar wind burn level
                                                                                    0.3f, // flare probability
                                                                                    1.0f); // CR loss multiplier, good values are in the range of 1-5
		
		system.setLightColor(new Color(240, 225, 200)); // light color in entire system, affects all entities
		
                PlanetAPI morningside = system.addPlanet("istl_planet_morningside", martinique_star, "Morningside", "barren", 240, 50, 840, 40);
                morningside.setCustomDescriptionId("planet_morningside");
                
                    // Add fixed conditions to Morningside.
                    Misc.initConditionMarket(morningside);
                    morningside.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    morningside.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    morningside.getMarket().addCondition(Conditions.VERY_HOT);
                    morningside.getMarket().addCondition(Conditions.ORE_MODERATE);
                    morningside.getMarket().getFirstCondition(Conditions.ORE_MODERATE).setSurveyed(true);
                    
                SectorEntityToken morningside_field = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(60f, // terrain effect band width 
			90, // terrain effect middle radius
			morningside, // entity that it's around
			60f, // visual band start
			120f, // visual band end
			new Color(50, 30, 100, 30), // base color
			0.3f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
                morningside_field.setCircularOrbit(morningside, 0, 0, 100);
            
		PlanetAPI valjean = system.addPlanet("istl_planet_valjean", martinique_star, "Valjean", "istl_aridbread", 0, 120, 2160, 80);
		valjean.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
		valjean.getSpec().setGlowColor( new Color(255,160,30,255) );
		valjean.getSpec().setUseReverseLightForGlow(true);
		valjean.getSpec().setPitch(-15f);
		valjean.getSpec().setTilt(20f);
		valjean.applySpecChanges();
                valjean.setInteractionImage("illustrations", "mine");
                
                // add the marketplace to Valjean ---------------
                MarketAPI valjeanMarket = addMarketplace("independent", valjean, null,
                        "Valjean", // name of the market
                        4, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.FRONTIER,
                                        Conditions.FARMLAND_POOR,
                                        Conditions.DISSIDENT,
                                        Conditions.LARGE_REFUGEE_POPULATION,
                                        Conditions.HABITABLE,
                                        Conditions.SPACEPORT,
                                        Conditions.POPULATION_4)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount

                valjean.setCustomDescriptionId("planet_valjean");
                
                // Add United Security spawn script to Valjean. Currently broken - if you're reading this and you wanna fix it, let me know.
                //system.addScript(new UnSecFleetManager(valjeanMarket));
                
                // Valjean dust belt.
                system.addRingBand(valjean, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 480, 90f, Terrain.RING, "The Baker's Lament");
                
                // Some kind of discoverable, later - a beacon, or something.
                
			// Valjean jump-point
			JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("martinique_jump", "Pont-Martinique");
			jumpPoint1.setCircularOrbit( system.getEntityById("martinique"), 30, 2160, 80);
			jumpPoint1.setRelatedPlanet(valjean);
			system.addEntity(jumpPoint1);
			
			// Martinique Relay - L5 (behind)
			SectorEntityToken relay = system.addCustomEntity("martinique_relay", // unique id
					 "Martinique Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "dassault_mikoyan"); // faction
			relay.setCircularOrbitPointingDown(system.getEntityById("martinique"), 150, 5100, 225);
			
		system.addAsteroidBelt(martinique_star, 90, 3750, 500, 100, 120, Terrain.ASTEROID_BELT,  "Martinique Alpha Belt");
		system.addRingBand(martinique_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 3600, 105f, null, null);
		system.addRingBand(martinique_star, "misc", "rings_asteroids0", 256f, 1, Color.white, 256f, 3720, 125f, null, null);
				
                PlanetAPI cosette = system.addPlanet("istl_planet_cosette", martinique_star, "Cosette", "barren-bombarded", 75, 40, 4200, 180);
                cosette.setCustomDescriptionId("planet_cosette");

                    // Add fixed conditions to Cosette.
                    Misc.initConditionMarket(cosette);
                    cosette.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    cosette.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    cosette.getMarket().addCondition(Conditions.METEOR_IMPACTS);
                    cosette.getMarket().addCondition(Conditions.VOLATILES_DIFFUSE);
                    cosette.getMarket().getFirstCondition(Conditions.VOLATILES_DIFFUSE).setSurveyed(true);
        
		system.addAsteroidBelt(martinique_star, 90, 4550, 500, 290, 310, Terrain.ASTEROID_BELT,  "Martinique Beta Belt");
		system.addRingBand(martinique_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 4500, 305f, null, null);
		system.addRingBand(martinique_star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 4600, 285f, null, null);
		
                // Add debris to Beta belt
                DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    450f, // field radius - should not go above 1000 for performance reasons
                    1.5f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params1.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisBeta1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
                debrisBeta1.setSensorProfile(1000f);
                debrisBeta1.setDiscoverable(true);
                debrisBeta1.setCircularOrbit(martinique_star, 60f, 4550, 210f);
                debrisBeta1.setId("martinique_debrisBeta1");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params2 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    300f, // field radius - should not go above 1000 for performance reasons
                    1.0f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params2.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisBeta2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
                debrisBeta2.setSensorProfile(1000f);
                debrisBeta2.setDiscoverable(true);
                debrisBeta2.setCircularOrbit(martinique_star, 150f, 4550, 210f);
                debrisBeta2.setId("martinique_debrisBeta2");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    400f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params3.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params3.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisBeta3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
                debrisBeta3.setSensorProfile(1000f);
                debrisBeta3.setDiscoverable(true);
                debrisBeta3.setCircularOrbit(martinique_star, 270f, 5500, 225f);
                debrisBeta3.setId("martinique_debrisBeta3");
                
            // Novy Mir Starforge
            SectorEntityToken indStation = system.addCustomEntity("martinique_ind", "Novy Mir Starforge", "station_dme_outpost", "dassault_mikoyan");
            indStation.setCircularOrbitPointingDown(system.getEntityById("martinique"), 90, 4900, 225);

            // add the marketplace to Novy Mir Starforge ---------------
            MarketAPI novymirMarket = addMarketplace("dassault_mikoyan", indStation, null,
                "Novy Mir Starforge", // name of the market
                5, // size of the market (from the JSON)
                new ArrayList<>(
                        Arrays.asList( // list of market conditions from martinique.json
                                Conditions.FREE_PORT,
                                Conditions.ORE_COMPLEX,
                                Conditions.ORE_REFINING_COMPLEX,
                                Conditions.VICE_DEMAND,
                                Conditions.ANTIMATTER_FUEL_PRODUCTION,
                                Conditions.SHIPBREAKING_CENTER,
                                Conditions.ORBITAL_STATION,
                                Conditions.POPULATION_5)),
                new ArrayList<>(
                        Arrays.asList( // which submarkets to generate
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                0.12f); // tariff amount

                indStation.setCustomDescriptionId("novymir_starforge");
                //indStation.setInteractionImage("illustrations", "industrial_facility");
		
                PlanetAPI javert = system.addPlanet("istl_planet_javert", martinique_star, "Javert", "cryovolcanic", 135, 120, 6000, 270);
                javert.setCustomDescriptionId("planet_javert");
                
                    // Add fixed conditions to Javert.
                    Misc.initConditionMarket(javert);
                    javert.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                    javert.getMarket().addCondition(Conditions.POOR_LIGHT);
                    javert.getMarket().addCondition(Conditions.COLD);
                    javert.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
                    javert.getMarket().addCondition(Conditions.VOLATILES_PLENTIFUL);
                    javert.getMarket().getFirstCondition(Conditions.VOLATILES_PLENTIFUL).setSurveyed(true);
                
		system.addAsteroidBelt(martinique_star, 90, 6460, 500, 150, 300, Terrain.ASTEROID_BELT,  "Martinique Gamma Belt");
		system.addRingBand(martinique_star, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 6400, 305f, null, null);
		system.addRingBand(martinique_star, "misc", "rings_ice0", 256f, 3, Color.white, 256f, 6520, 285f, null, null);
                
                // Salvage in Javert orbit
                SectorEntityToken scrap1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.EQUIPMENT_CACHE_SMALL, Factions.DERELICT);
		scrap1.setId("martinique_scrap1");
		scrap1.setCircularOrbit(javert, 45, 300, 100f);
		Misc.setDefenderOverride(scrap1, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
                scrap1.setDiscoverable(Boolean.TRUE);
                
                // Add debris to Gamma belt
                DebrisFieldTerrainPlugin.DebrisFieldParams params4 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    300f, // field radius - should not go above 1000 for performance reasons
                    1.0f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params4.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params4.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisGamma1 = Misc.addDebrisField(system, params4, StarSystemGenerator.random);
                debrisGamma1.setSensorProfile(1000f);
                debrisGamma1.setDiscoverable(true);
                debrisGamma1.setCircularOrbit(martinique_star, 120f, 6460, 270f);
                debrisGamma1.setId("martinique_debrisGamma1");
                
				// Gate of Martinique
				SectorEntityToken gate = system.addCustomEntity("martinique_gate", // unique id
						 "Martinique Gate", // name - if null, defaultName from custom_entities.json will be used
						 "inactive_gate", // type of object, defined in custom_entities.json
						 null); // faction

				gate.setCircularOrbit(system.getEntityById("martinique"), 180+60, 6800, 360);
                                
                                SectorEntityToken scrap2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.SUPPLY_CACHE_SMALL, Factions.DERELICT);
                                scrap2.setId("martinique_scrap2");
                                scrap2.setCircularOrbit(gate, 135, 240, 180f);
                                Misc.setDefenderOverride(scrap2, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
                                scrap2.setDiscoverable(Boolean.TRUE);
                                
        // Antilles Starport
        SectorEntityToken pirBase = system.addCustomEntity("antilles_port", "Antilles Starport", "station_mining00", "neutral");
        pirBase.setCircularOrbitWithSpin(system.getEntityById("martinique"), 360*(float)Math.random(), 6480, 270, 9, 27);
        pirBase.setDiscoverable(true);
        pirBase.setDiscoveryXP(2500f);
        pirBase.setSensorProfile(1.0f);

        // Abandoned marketplace for Antilles Starport
        pirBase.getMemoryWithoutUpdate().set("$abandonedStation", true);
        MarketAPI market = Global.getFactory().createMarket("abandoned_pirate_market", pirBase.getName(), 0);
        market.setPrimaryEntity(pirBase);
        market.setFactionId(pirBase.getFaction().getId());
        market.addCondition(Conditions.ABANDONED_STATION);
        market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
        ((StoragePlugin) market.getSubmarket(Submarkets.SUBMARKET_STORAGE).getPlugin()).setPlayerPaidToUnlock(true);
        pirBase.setMarket(market);
        pirBase.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("metals", 150);
        pirBase.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("hand_weapons", 100);
        pirBase.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("drugs", 50);
        
        // add the marketplace to Antilles Starport ---------------
        // MarketAPI pirbaseMarket = addMarketplace("pirates", pirBase, null,
                // "Antilles Starport", // name of the market
                // 3, // size of the market (from the JSON)
                // new ArrayList<>(
                        // Arrays.asList( // list of market conditions from martinique.json
                                // Conditions.FREE_PORT,
                                // Conditions.ORGANIZED_CRIME,
                                // Conditions.ORBITAL_STATION,
                                // Conditions.POPULATION_3)),
                // new ArrayList<>(
                        // Arrays.asList( // which submarkets to generate
                                // Submarkets.SUBMARKET_BLACK,
                                // Submarkets.SUBMARKET_OPEN,
                                // Submarkets.SUBMARKET_STORAGE)),
                // 0.3f); // tariff amount

        pirBase.setCustomDescriptionId("station_antilles");                                
				
		float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, martinique_star, StarAge.AVERAGE,
                        2, 4, // min/max entities to add
			10800, // radius to start adding at 
			3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
			true); // whether to use custom or system-name based names
		
		system.autogenerateHyperspaceJumpPoints(true, true);
	}
        
    // void cleanup(StarSystemAPI system){
        // HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
	// NebulaEditor editor = new NebulaEditor(plugin);        
        // float minRadius = plugin.getTileSize() * 2f;
        
        // float radius = system.getMaxRadiusInHyperspace();
        // editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        // editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);	     
//        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, system.getMaxRadiusInHyperspace()*1.25f, 0, 360, 0.25f);
    // }
}
