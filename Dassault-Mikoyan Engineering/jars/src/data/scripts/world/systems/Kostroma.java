package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain.RingParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.campaign.fleets.SixthBureauFleetManager;
import static data.scripts.world.systems.Nikolaev.addMarketplace;

import java.util.ArrayList;
import java.util.Arrays;

public class Kostroma {
    	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Kostroma");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI kostroma_star = system.initStar("kostroma", // unique id for this star 
										    "star_orange_giant",  // id in planets.json
										    520f, 		  // radius (in pixels at default zoom)
										    400); // corona radius, from star edge
		kostroma_star.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "banded"));
		kostroma_star.getSpec().setGlowColor(new Color(255,235,50,128));
		kostroma_star.getSpec().setAtmosphereThickness(0.5f);
		kostroma_star.applySpecChanges();
		
		system.setLightColor(new Color(255, 240, 220)); // light color in entire system, affects all entities
                
                PlanetAPI cendre = system.addPlanet("istl_planet_cendre", kostroma_star, "Cendre", "barren", 210, 35, 920, 30);
                cendre.setCustomDescriptionId("planet_cendre");
                
                    // Add fixed conditions to Cendre.
                    Misc.initConditionMarket(cendre);
                    cendre.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    cendre.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    cendre.getMarket().addCondition(Conditions.VERY_HOT);
                    
                // Inner magnetic field.
                SectorEntityToken kostroma_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(400f, // terrain effect band width 
			1050, // terrain effect middle radius
			kostroma_star, // entity that it's around
			850f, // visual band start
			1250f, // visual band end
			new Color(50, 30, 100, 45), // base color
			0.3f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(150, 30, 120, 150), 
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
                kostroma_field1.setCircularOrbit(kostroma_star, 0, 0, 120);
                    
                // Astalon, a fiercely independent Persean outpost ---------------
                PlanetAPI astalon = system.addPlanet("istl_planet_astalon", kostroma_star, "Astalon", "toxic", 30, 75, 1540, 80);
		astalon.getSpec().setPitch(-15f);
		astalon.getSpec().setTilt(12f);
		astalon.applySpecChanges();
                astalon.setInteractionImage("illustrations", "desert_moons_ruins");
                
                // add the marketplace to Astalon ---------------
                MarketAPI astalonMarket = addMarketplace("independent", astalon, null,
                        "Astalon", // name of the market
                        3, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.FRONTIER,
                                        Conditions.ORGANICS_COMPLEX,
                                        Conditions.ORGANICS_PLENTIFUL,
                                        Conditions.VERY_HOT,
                                        Conditions.POPULATION_3)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN)),
                        0.3f); // tariff amount

                astalon.setCustomDescriptionId("planet_astalon");
                
                // add first asteroid belt of the disk ---------------
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 1760, 98f);
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 1800, 102f);
		system.addAsteroidBelt(kostroma_star, 120, 1780, 300, 200, 300, Terrain.ASTEROID_BELT, "Astalon Necklace");
                
                // Another magnetic field, just outside the inner ring.
                SectorEntityToken kostroma_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
				new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
				2020, // terrain effect middle radius
				kostroma_star, // entity that it's around
				1920f, // visual band start
				2120f, // visual band end
				new Color(50, 30, 100, 30), // base color
				0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
				new Color(50, 20, 110, 130),
				new Color(150, 30, 120, 150), 
				new Color(200, 50, 130, 190),
				new Color(250, 70, 150, 240),
				new Color(200, 80, 130, 255),
				new Color(75, 0, 160), 
				new Color(127, 0, 255)
				));
		kostroma_field2.setCircularOrbit(kostroma_star, 0, 0, 180);
                
                // Marie-Galante, a colony sunk so low, the pirates were an improvement ---------------
                PlanetAPI mariegalante = system.addPlanet("istl_planet_mariegalante", kostroma_star, "Marie-Galante", "jungle", 270, 150, 2400, 135);
                mariegalante.setCustomDescriptionId("planet_mariegalante");
                
                mariegalante.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
		mariegalante.getSpec().setGlowColor( new Color(235,235,255,255) );
		mariegalante.getSpec().setUseReverseLightForGlow(true);
		mariegalante.getSpec().setCloudColor(new Color(245,255,235,235));
                mariegalante.getSpec().setCloudRotation(12f);
                mariegalante.getSpec().setPitch(30f);
		mariegalante.getSpec().setTilt(35f);
		mariegalante.applySpecChanges();
                
                    // Add fixed conditions to Marie-Galante.
                    Misc.initConditionMarket(mariegalante);
                    mariegalante.getMarket().addCondition(Conditions.DECIVILIZED);
                    mariegalante.getMarket().addCondition(Conditions.RUINS_VAST);
                    mariegalante.getMarket().getFirstCondition(Conditions.RUINS_VAST).setSurveyed(true);
                    mariegalante.getMarket().addCondition(Conditions.INIMICAL_BIOSPHERE);
                    mariegalante.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                
                // add the marketplace to Marie-Galante ---------------
                // MarketAPI mariegalanteMarket = addMarketplace("pirates", mariegalante, null,
                        // "Marie-Galante", // name of the market
                        // 5, // size of the market (from the JSON)
                        // new ArrayList<>(
                                // Arrays.asList( // list of market conditions from kostroma.json
                                        // Conditions.FRONTIER,
                                        // Conditions.RUINS_EXTENSIVE,
                                        // Conditions.ORGANIZED_CRIME,
                                        // Conditions.STEALTH_MINEFIELDS,
                                        // Conditions.INIMICAL_BIOSPHERE,
                                        // Conditions.SPACEPORT,
                                        // Conditions.POPULATION_5)),
                        // new ArrayList<>(
                                // Arrays.asList( // which submarkets to generate
                                        // Submarkets.GENERIC_MILITARY,
                                        // Submarkets.SUBMARKET_BLACK,
                                        // Submarkets.SUBMARKET_OPEN,
                                        // Submarkets.SUBMARKET_STORAGE)),
                        // 0.3f); // tariff amount

                // Abandoned orbital terminal.
                SectorEntityToken derelictTerminal = system.addCustomEntity("mariegalante_station", "Orbital Terminal", "station_side03", "neutral");
                derelictTerminal.setCircularOrbitWithSpin(system.getEntityById("istl_planet_mariegalante"), 210, 240, 45, 7, 21);
                derelictTerminal.setInteractionImage("illustrations", "space_wreckage");
                derelictTerminal.setCustomDescriptionId("station_mariegalante");   
                
                // Cayenne, the decivilized moon ---------------
                PlanetAPI cayenne = system.addPlanet("istl_planet_cayenne", mariegalante, "Cayenne", "barren-bombarded", 30, 50, 300, 45);
                cayenne.setCustomDescriptionId("planet_cayenne");
                
                cayenne.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
		cayenne.getSpec().setGlowColor(new Color(255,245,235,255));
		cayenne.getSpec().setUseReverseLightForGlow(true);
		cayenne.getSpec().setAtmosphereThicknessMin(25);
		cayenne.getSpec().setAtmosphereThickness(0.2f);
		cayenne.getSpec().setAtmosphereColor( new Color(80,90,100,120) );
		cayenne.applySpecChanges();
                
                    // Add fixed conditions to Cayenne.
                    Misc.initConditionMarket(cayenne);
                    cayenne.getMarket().addCondition(Conditions.DECIVILIZED);
                    cayenne.getMarket().addCondition(Conditions.RUINS_EXTENSIVE);
                    cayenne.getMarket().getFirstCondition(Conditions.RUINS_EXTENSIVE).setSurveyed(true);
                    cayenne.getMarket().addCondition(Conditions.METEOR_IMPACTS);
                    cayenne.getMarket().addCondition(Conditions.POLLUTION);
                    cayenne.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                
                // Inner jump point ---------------
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("kostroma_inner_jump", "Kostroma Jump-point");
		jumpPoint1.setCircularOrbit( system.getEntityById("kostroma"), 330, 2700, 135);
		jumpPoint1.setRelatedPlanet(mariegalante);
		system.addEntity(jumpPoint1);
                
                // Marie-Galante Relay ---------------
                SectorEntityToken mariegalante_relay = system.addCustomEntity("marie_galante_relay", // unique id
                        "Marie-Galante Relay", // name - if null, defaultName from custom_entities.json will be used
                        "comm_relay", // type of object, defined in custom_entities.json
                        "pirates"); // faction
                mariegalante_relay.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 210, 2400, 135);
                
                // Middle asteroid belts; should be really thick, really impenetrable ---------------
                system.addAsteroidBelt(kostroma_star, 100, 3100, 256, 150, 250, Terrain.ASTEROID_BELT, null);
		system.addAsteroidBelt(kostroma_star, 100, 3500, 256, 150, 250, Terrain.ASTEROID_BELT, null);
		
		system.addAsteroidBelt(kostroma_star, 100, 3950, 128, 200, 300, Terrain.ASTEROID_BELT, null);
		system.addAsteroidBelt(kostroma_star, 100, 4250, 188, 200, 300, Terrain.ASTEROID_BELT, null);
		system.addAsteroidBelt(kostroma_star, 100, 4475, 256, 200, 300, Terrain.ASTEROID_BELT, null);
			
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 3000, 120f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 3200, 150f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 3400, 180f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 3600, 120f);
		
                    // add one ring that covers all of the above
                    SectorEntityToken ring = system.addTerrain(Terrain.RING, new BaseRingTerrain.RingParams(600 + 256, 3300, null, "Kostroma Meridian Disk"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 100);
                
                // Inner Ring salvage.
                SectorEntityToken scrap1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.EQUIPMENT_CACHE_SMALL, Factions.DERELICT);
		scrap1.setId("kostroma_scrap1");
		scrap1.setCircularOrbit(kostroma_star, 0, 3700, 275f);
		Misc.setDefenderOverride(scrap1, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
                scrap1.setDiscoverable(Boolean.TRUE);
                
                SectorEntityToken scrap2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.WEAPONS_CACHE_SMALL, Factions.DERELICT);
		scrap2.setId("kostroma_scrap2");
		scrap2.setCircularOrbit(kostroma_star, 120, 3700, 275f);
		Misc.setDefenderOverride(scrap2, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
                scrap2.setDiscoverable(Boolean.TRUE);
                
                SectorEntityToken scrap3 = DerelictThemeGenerator.addSalvageEntity(system, Entities.WEAPONS_CACHE, Factions.DERELICT);
		scrap3.setId("kostroma_scrap3");
		scrap3.setCircularOrbit(kostroma_star, 240, 3700, 275f);
		Misc.setDefenderOverride(scrap3, new DefenderDataOverride(Factions.DERELICT, 2, 8, 0));
                scrap3.setDiscoverable(Boolean.TRUE);
                
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 3800, 80f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 3900, 120f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 4000, 160f);
                
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 3900, null, "Kostroma Meridian Disk"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 100);
                
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 4100, 140f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 4200, 180f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 4300, 220f);
		
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 4200, null, "Kostroma Meridian Disk"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 100);
                    
                // And debris fields for meridian rings ---------------
                DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    520f, // field radius - should not go above 1000 for performance reasons
                    1.8f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params1.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisMeridian1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
                debrisMeridian1.setSensorProfile(1000f);
                debrisMeridian1.setDiscoverable(true);
                debrisMeridian1.setCircularOrbit(kostroma_star, 135f, 4400, 210f);
                debrisMeridian1.setId("kostroma_debrisMeridian1");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params2 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params2.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisMeridian2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
                debrisMeridian2.setSensorProfile(1000f);
                debrisMeridian2.setDiscoverable(true);
                debrisMeridian2.setCircularOrbit(kostroma_star, 180f, 4000, 210f);
                debrisMeridian2.setId("kostroma_debrisMeridian2");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    450f, // field radius - should not go above 1000 for performance reasons
                    1.5f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params3.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params3.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisMeridian3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
                debrisMeridian3.setSensorProfile(1000f);
                debrisMeridian3.setDiscoverable(true);
                debrisMeridian3.setCircularOrbit(kostroma_star, 270f, 4200, 210f);
                debrisMeridian3.setId("kostroma_debrisMeridian3");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params4 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params4.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params4.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisMeridian4 = Misc.addDebrisField(system, params4, StarSystemGenerator.random);
                debrisMeridian4.setSensorProfile(1000f);
                debrisMeridian4.setDiscoverable(true);
                debrisMeridian4.setCircularOrbit(kostroma_star, 315f, 4200, 210f);
                debrisMeridian4.setId("kostroma_debrisMeridian4");
                
                //Two small pirate stations, pirate when market perf improves.
                SectorEntityToken derelictPatrol = system.addCustomEntity("patrol_station", "Patrol Station", "station_side05", "neutral");
                derelictPatrol.setCircularOrbitWithSpin(system.getEntityById("kostroma"), 225, 4400, 210, 9, 27);
                derelictPatrol.setInteractionImage("illustrations", "space_wreckage");
                derelictPatrol.setCustomDescriptionId("station_pirate1");
                
                SectorEntityToken derelictCustoms = system.addCustomEntity("customs_station", "Customs Port", "station_side05", "neutral");
                derelictCustoms.setCircularOrbitWithSpin(system.getEntityById("kostroma"), 345, 4400, 210, 6, 18);
                derelictCustoms.setInteractionImage("illustrations", "space_wreckage");
                derelictCustoms.setCustomDescriptionId("station_pirate2");

                // Meridian Station, a wretched hive of s/cum and fetishry ---------------
                SectorEntityToken meridianStation = system.addCustomEntity("meridian_station", "Meridian Station", "station_side06", "pirates");
                meridianStation.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 105, 4400, 210);

                // add the marketplace to Meridian Station ---------------
                MarketAPI meridianStationMarket = addMarketplace("pirates", meridianStation, null,
                        "Meridian Station", // name of the market
                        6, // size of the market (from the JSON)
                        new ArrayList<>(
                        Arrays.asList( // list of market conditions from nikolaev.json
                                Conditions.FREE_PORT,
                                Conditions.VICE_DEMAND,
                                Conditions.STEALTH_MINEFIELDS,
                                Conditions.VOLATILES_DEPOT,
                                Conditions.ORBITAL_STATION,
                                Conditions.POPULATION_6)),
                        new ArrayList<>(
                        Arrays.asList( // which submarkets to generate
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                0.3f); // tariff amount

                meridianStation.setCustomDescriptionId("station_meridian");
                
                // a few more asteroids! ---------------
                system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 0, Color.white, 256f, 4700, 150f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 4800, 190f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 4900, 210f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 5000, 230f);
		
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(300 + 256, 4850, null, "Kostroma Meridian Disk"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 100);
                    
                system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 3, Color.white, 256f, 5350, 150f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 5575, 190f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 5600, 210f);
		
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(300 + 256, 5575, null, "Kostroma Meridian Disk"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 100);
                    
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 5800, 235f);
                    
                // And debris fields for outer rings ---------------
                DebrisFieldTerrainPlugin.DebrisFieldParams params5 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    240f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params5.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params5.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisOuter1 = Misc.addDebrisField(system, params5, StarSystemGenerator.random);
                debrisOuter1.setSensorProfile(1000f);
                debrisOuter1.setDiscoverable(true);
                debrisOuter1.setCircularOrbit(kostroma_star, 60f, 5500, 240f);
                debrisOuter1.setId("kostroma_debrisOuter1");

                DebrisFieldTerrainPlugin.DebrisFieldParams params6 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    300f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params6.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params6.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisOuter2 = Misc.addDebrisField(system, params6, StarSystemGenerator.random);
                debrisOuter2.setSensorProfile(1000f);
                debrisOuter2.setDiscoverable(true);
                debrisOuter2.setCircularOrbit(kostroma_star, 15f, 5500, 240f);
                debrisOuter2.setId("kostroma_debrisOuter2");
                
                //More dust!
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 5900, 240f);
                
                // Gironde and magnetic fields ---------------
                PlanetAPI gironde = system.addPlanet("istl_planet_gironde", kostroma_star, "Gironde", "gas_giant", 300, 360, 8000, 240);
		gironde.getSpec().setPitch(35f);
		gironde.getSpec().setPlanetColor(new Color(200,235,245,255));
		gironde.getSpec().setAtmosphereColor(new Color(210,240,250,250));
		gironde.getSpec().setCloudColor(new Color(220,250,240,200));
		gironde.getSpec().setIconColor(new Color(100,130,120,255));
		gironde.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		gironde.getSpec().setGlowColor(new Color(225,45,225,145));
		gironde.getSpec().setUseReverseLightForGlow(true);
		gironde.getSpec().setAtmosphereThickness(0.5f);
		gironde.applySpecChanges();
		
		system.addRingBand(gironde, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 520, 45f, Terrain.RING, "Gironde Rings");
		system.addRingBand(gironde, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 540, 60, null, null);
                
                gironde.setCustomDescriptionId("planet_gironde");

                	SectorEntityToken gironde_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
					new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
					460, // terrain effect middle radius
					gironde, // entity that it's around
					360f, // visual band start
					560f, // visual band end
					new Color(50, 30, 100, 60), // base color
					1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
					new Color(50, 20, 110, 130),
					new Color(150, 30, 120, 150), 
					new Color(200, 50, 130, 190),
					new Color(250, 70, 150, 240),
					new Color(200, 80, 130, 255),
					new Color(75, 0, 160), 
					new Color(127, 0, 255)
					));
			gironde_field1.setCircularOrbit(gironde, 0, 0, 100);
                        
                        SectorEntityToken gironde_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
					new MagneticFieldTerrainPlugin.MagneticFieldParams(120f, // terrain effect band width 
					680, // terrain effect middle radius
					gironde, // entity that it's around
					640f, // visual band start
					760f, // visual band end
					new Color(50, 30, 100, 30), // base color
					0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
					new Color(50, 20, 110, 130),
					new Color(150, 30, 120, 150), 
					new Color(200, 50, 130, 190),
					new Color(250, 70, 150, 240),
					new Color(200, 80, 130, 255),
					new Color(75, 0, 160), 
					new Color(127, 0, 255)
					));
			gironde_field2.setCircularOrbit(gironde, 0, 0, 100);
                        
                        // Gironde Trojans ---------------
                        SectorEntityToken girondeL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                                600f, // min radius
                                900f, // max radius
                                28, // min asteroid count
                                52, // max asteroid count
                                7f, // min asteroid radius 
                                18f, // max asteroid radius
                                "Gironde L4 Trojans")); // null for default name
                        
                        SectorEntityToken girondeL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                                600f, // min radius
                                900f, // max radius
                                28, // min asteroid count
                                52, // max asteroid count
                                7f, // min asteroid radius 
                                18f, // max asteroid radius
                                "Gironde L5 Trojans")); // null for default name

                        girondeL4.setCircularOrbit(kostroma_star, 360f, 8000, 240);
                        girondeL5.setCircularOrbit(kostroma_star, 240f, 8000, 240);
                
                        // Pyla, an inner moon where nutcases death-race. ---------------
                        PlanetAPI pyla = system.addPlanet("istl_planet_pyla", gironde, "Pyla", "arid", 150, 40, 760, 60);
                        pyla.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
                        pyla.getSpec().setGlowColor(new Color(153,50,204,225));
                        pyla.getSpec().setUseReverseLightForGlow(true);
                        pyla.applySpecChanges();
                        
                        pyla.setCustomDescriptionId("planet_pyla");
                        
                            // Add fixed conditions to Pyla.
                            Misc.initConditionMarket(pyla);
                            pyla.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                            pyla.getMarket().addCondition(Conditions.LOW_GRAVITY);
                            pyla.getMarket().addCondition(Conditions.ORGANICS_COMMON);
                            pyla.getMarket().getFirstCondition(Conditions.ORGANICS_COMMON).setSurveyed(true);
                            
                        SectorEntityToken pyla_field = system.addTerrain(Terrain.MAGNETIC_FIELD,
					new MagneticFieldTerrainPlugin.MagneticFieldParams(80f, // terrain effect band width 
					80, // terrain effect middle radius
					pyla, // entity that it's around
					40f, // visual band start
					120f, // visual band end
					new Color(50, 30, 100, 30), // base color
					0.8f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
					new Color(50, 20, 110, 130),
					new Color(150, 30, 120, 150), 
					new Color(200, 50, 130, 190),
					new Color(250, 70, 150, 240),
					new Color(200, 80, 130, 255),
					new Color(75, 0, 160), 
					new Color(127, 0, 255)
					));
			pyla_field.setCircularOrbit(pyla, 0, 0, 100);
                        
                        //Light dust ring.
                        system.addRingBand(gironde, "misc", "rings_dust0", 256f, 0, Color.gray, 128f, 850, 60f);
                        
                        // La Réole, a lovely little water moon ---------------
                        PlanetAPI lareole = system.addPlanet("istl_planet_lareole", gironde, "La Réole", "water", 40, 120, 1280, 105);
                        lareole.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
                        lareole.getSpec().setGlowColor( new Color(255,255,255,255) );
                        lareole.getSpec().setUseReverseLightForGlow(true);
                        lareole.getSpec().setPitch(35f);
                        lareole.applySpecChanges();
                
                        //La Réole Polis, its station ---------------
                        SectorEntityToken lareolePolis = system.addCustomEntity("kostroma_port", "La Réole Polis", "station_dme_outpost", "dassault_mikoyan");
                        lareolePolis.setCircularOrbitPointingDown(system.getEntityById("istl_planet_lareole"), 90, 250, 75);
                        lareolePolis.setInteractionImage("illustrations", "space_bar");

                        
                // add the marketplace to La Réole/La Réole Polis ---------------
                MarketAPI lareoleMarket = addMarketplace("dassault_mikoyan", lareole, new ArrayList<>(Arrays.asList(lareolePolis)),
                        "La Réole", // name of the market
                        5, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.HABITABLE,
                                        Conditions.FREE_PORT,
                                        Conditions.TRADE_CENTER,
                                        Conditions.AQUACULTURE,
                                        Conditions.VOLTURNIAN_LOBSTER_PENS,
                                        Conditions.LARGE_REFUGEE_POPULATION,
                                        Conditions.URBANIZED_POLITY,
                                        Conditions.MILITARY_BASE,
                                        Conditions.ORBITAL_STATION,
                                        Conditions.POPULATION_5)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.GENERIC_MILITARY,
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.12f); // tariff amount

                lareole.setCustomDescriptionId("planet_lareole");
                lareolePolis.setCustomDescriptionId("station_lareole_polis");
                
                // Adding the Sixth Bureau fleet script.
                system.addScript(new SixthBureauFleetManager(lareoleMarket));
                
                //Another dust ring.
                system.addRingBand(gironde, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 1720, 120f);
                system.addAsteroidBelt(gironde, 120, 1720, 135, 200, 300, Terrain.ASTEROID_BELT, "Gironde Belt");


                //A small outer moon, Chantilly.
                PlanetAPI chantilly = system.addPlanet("istl_planet_chantilly", gironde, "Chantilly", "barren", 300, 50, 1920, 135);
                chantilly.setCustomDescriptionId("planet_chantilly");

                    //Add fixed conditions to Chantilly.
                    Misc.initConditionMarket(chantilly);
                            chantilly.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                            chantilly.getMarket().addCondition(Conditions.LOW_GRAVITY);
                            chantilly.getMarket().addCondition(Conditions.VOLATILES_TRACE);
                            chantilly.getMarket().getFirstCondition(Conditions.VOLATILES_TRACE).setSurveyed(true);
                
                // Gironde jump point in L5 ---------------
                JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint("kostroma_outer_jump", "Gironde Bridge");
		jumpPoint2.setCircularOrbit( system.getEntityById("kostroma"), 240, 8000, 240);
		jumpPoint2.setRelatedPlanet(lareole);
		system.addEntity(jumpPoint2);
                
                // L4 debris field ---------------
                DebrisFieldTerrainPlugin.DebrisFieldParams params7 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    600f, // field radius - should not go above 1000 for performance reasons
                    1.8f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params7.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params7.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisL4 = Misc.addDebrisField(system, params7, StarSystemGenerator.random);
                debrisL4.setSensorProfile(1000f);
                debrisL4.setDiscoverable(true);
                debrisL4.setCircularOrbit(kostroma_star, 360f, 8000, 240f);
                debrisL4.setId("kostroma_debrisL4");
                
                // Kostroma Relay at L3 ---------------
                SectorEntityToken kostroma_relay = system.addCustomEntity("kostroma_relay", // unique id
                        "Kostroma Relay", // name - if null, defaultName from custom_entities.json will be used
                        "comm_relay", // type of object, defined in custom_entities.json
                        "dassault_mikoyan"); // faction
                kostroma_relay.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 120, 8000, 240);
                
                // Mid-outer asteroid belt ---------------
		system.addAsteroidBelt(kostroma_star, 100, 10350, 188, 200, 300, Terrain.ASTEROID_BELT, null);
                
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 10400, 220f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 10500, 260f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 10600, 300f);
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 10750, 340f);
		
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 10575, null, "Kostroma's Tears"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 120);
                    
                // Tenacity, a Luddic tundra world ---------------
                PlanetAPI tenacity = system.addPlanet("istl_planet_tenacity", kostroma_star, "Tenacity", "tundra", 150, 135, 11350, 300);
		tenacity.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
		tenacity.getSpec().setGlowColor( new Color(255,160,30,255) );
		tenacity.getSpec().setUseReverseLightForGlow(true);
		tenacity.getSpec().setPitch(-15f);
		tenacity.getSpec().setTilt(20f);
		tenacity.applySpecChanges();
                
                    // Add orbital mirrors to Tenacity ---------------
                    SectorEntityToken tenacity_mirror1 = system.addCustomEntity("tenacity_mirror1", "Tenacity Stellar Mirror Alpha", "stellar_mirror", "independent");
                    SectorEntityToken tenacity_mirror2 = system.addCustomEntity("tenacity_mirror2", "Tenacity Stellar Mirror Beta", "stellar_mirror", "independent");	
                    SectorEntityToken tenacity_mirror3 = system.addCustomEntity("tenacity_mirror3", "Tenacity Stellar Mirror Gamma", "stellar_mirror", "independent");

                    tenacity_mirror1.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 150 - 37, 360, 300);
                    tenacity_mirror2.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 150 + 0, 360, 300);	
                    tenacity_mirror3.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 150 + 37, 360, 300);	
       	
                    tenacity_mirror1.setCustomDescriptionId("stellar_mirror");
                    tenacity_mirror2.setCustomDescriptionId("stellar_mirror");
                    tenacity_mirror3.setCustomDescriptionId("stellar_mirror");

                
                // add the marketplace to Tenacity ---------------
                MarketAPI tenacityMarket = addMarketplace("luddic_church", tenacity, new ArrayList<>(Arrays.asList(tenacity_mirror1, tenacity_mirror2, tenacity_mirror3)),
                        "Tenacity", // name of the market
                        5, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.FARMLAND_ADEQUATE,
                                        Conditions.LARGE_REFUGEE_POPULATION,
                                        Conditions.HABITABLE,
                                        Conditions.COTTAGE_INDUSTRY,
                                        Conditions.LUDDIC_MAJORITY,
                                        Conditions.SPACEPORT,
                                        Conditions.POPULATION_5)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount

                tenacity.setCustomDescriptionId("planet_tenacity");
                
                //More dust.
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 11800, 360f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 12000, 360f);
		system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 12200, 360f);
                
                    // Add a ring to cover the above.
                    ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 12000, null, "Kostroma's Tears"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 360);
                
                // Kostroma Gate ---------------
                SectorEntityToken gate = system.addCustomEntity("kostroma_gate", // unique id
				 "Kostroma Gate", // name - if null, defaultName from custom_entities.json will be used
				 "inactive_gate", // type of object, defined in custom_entities.json
				 null); // faction

                    gate.setCircularOrbit(system.getEntityById("kostroma"), 180+60, 12800, 400);
                
                //Yet more dust.
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 13000, 390f);
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 13200, 420f);
                system.addRingBand(kostroma_star, "misc", "rings_dust0", 256f, 0, Color.white, 256f, 13400, 400f);
                    
                ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 13200, null, "Kostroma Fringe Belt"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 400);
                    
                //Some salvage.
                DebrisFieldTerrainPlugin.DebrisFieldParams params8 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params8.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params8.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisOuter3 = Misc.addDebrisField(system, params8, StarSystemGenerator.random);
                debrisOuter3.setSensorProfile(1000f);
                debrisOuter3.setDiscoverable(true);
                debrisOuter3.setCircularOrbit(kostroma_star, 360*(float)Math.random(), 13200, 400f);
                debrisOuter3.setId("kostroma_debrisOuter3");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params9 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    450f, // field radius - should not go above 1000 for performance reasons
                    1.5f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params9.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params9.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisOuter4 = Misc.addDebrisField(system, params9, StarSystemGenerator.random);
                debrisOuter4.setSensorProfile(1000f);
                debrisOuter4.setDiscoverable(true);
                debrisOuter4.setCircularOrbit(kostroma_star, 360*(float)Math.random(), 13200, 400f);
                debrisOuter4.setId("kostroma_debrisOuter4");
                
                // Davout, a frozen shithole ---------------
                PlanetAPI davout = system.addPlanet("istl_planet_davout", kostroma_star, "Davout", "frozen", 180, 90, 14000, 360);
                davout.setCustomDescriptionId("planet_davout");
                
                    // Add fixed conditions to Davout.
                    Misc.initConditionMarket(davout);
                    davout.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                    davout.getMarket().addCondition(Conditions.VERY_COLD);
                    davout.getMarket().addCondition(Conditions.METEOR_IMPACTS);
                    davout.getMarket().addCondition(Conditions.VOLATILES_TRACE);
                    davout.getMarket().getFirstCondition(Conditions.VOLATILES_TRACE).setSurveyed(true);
                    
                // Outer asteroid belt; mostly ice ---------------
		system.addAsteroidBelt(kostroma_star, 100, 14800, 188, 200, 300, Terrain.ASTEROID_BELT, null);
                
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 3, Color.white, 256f, 14750, 320f);
		system.addRingBand(kostroma_star, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 14850, 360f);
		
                    // add one ring that covers all of the above
                    ring = system.addTerrain(Terrain.RING, new RingParams(200 + 256, 14800, null, "Kostroma Fringe Belt"));
                    ring.setCircularOrbit(kostroma_star, 0, 0, 150);
                    
                // Mining station in outer belt
                SectorEntityToken stationDerelict = DerelictThemeGenerator.addSalvageEntity(system, Entities.STATION_MINING, Factions.DERELICT);
		stationDerelict.setId("kostroma_derelict");
		stationDerelict.setCircularOrbit(kostroma_star, 360*(float)Math.random(), 14800, 400f);
		Misc.setDefenderOverride(stationDerelict, new DefenderDataOverride(Factions.DERELICT, 1f, 2, 8));
		CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
		extraStationSalvage.addCommodity(Commodities.BETA_CORE, 1);
		BaseSalvageSpecial.setExtraSalvage(extraStationSalvage, stationDerelict.getMemoryWithoutUpdate(), -1);
                
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, kostroma_star, StarAge.AVERAGE,
				1, 3, // min/max entities to add
				16000, // radius to start adding at 
				6, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
				true); // whether to use custom or system-name based names

                // Add a nebula to the system.
		StarSystemGenerator.addSystemwideNebula(system, StarAge.OLD);
                
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
