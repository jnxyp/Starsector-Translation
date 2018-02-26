package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
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
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class Yod {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Yod");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		// Yod, a swollen supergiant.
		PlanetAPI yod_star = system.initStar("yod", // unique id for this star 
										    "star_red_giant",  // id in planets.json
										    1200f, // radius (in pixels at default zoom)
										    800, // corona radius, from star edge
										    4.5f, // solar wind burn level
                                                                                    0.6f, // flare probability
                                                                                    2.0f); // CR loss multiplier, good values are in the range of 1-5
                                                                                    
		system.setLightColor(new Color(255, 210, 200)); // light color in entire system, affects all entities
                
                // Close, intense magnetic field.
                SectorEntityToken yod_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(480f, // terrain effect band width 
			1560, // terrain effect middle radius
			yod_star, // entity that it's around
			1380f, // visual band start
			1860f, // visual band end
			new Color(50, 30, 100, 60), // base color
			0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(150, 30, 120, 150), 
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
                yod_field1.setCircularOrbit(yod_star, 0, 0, 150);
                                
                // Yod 1, a thoroughly worthless volcanic rock.
                PlanetAPI yod1 = system.addPlanet("istl_planet_yod1", yod_star, "Yod I", "lava_minor", 360*(float)Math.random(), 60, 1800, 180);
                    // Add fixed conditions to Yod I.
                    Misc.initConditionMarket(yod1);
                    yod1.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    yod1.getMarket().addCondition(Conditions.ORE_SPARSE);
                    
                // Derelict survey probe around Yod.
                SectorEntityToken shipDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_SHIP, Factions.DERELICT);
                shipDerelict1.setId("yod_probe1");
                shipDerelict1.setCircularOrbit(yod_star, 360*(float)Math.random(), 2150, 90f);
                
                // Some sparse asteroids.
                system.addAsteroidBelt(yod_star, 150, 2400, 120, 220, 290, Terrain.ASTEROID_BELT, "Yod Inner Belt");
                
                // Some debris in the inner system.
                DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    520f, // field radius - should not go above 1000 for performance reasons
                    1.6f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params1.baseSalvageXP = 750; // base XP for scavenging in field
                SectorEntityToken debrisInner1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
                debrisInner1.setSensorProfile(1200f);
                debrisInner1.setDiscoverable(true);
                debrisInner1.setCircularOrbit(yod_star, 360*(float)Math.random(), 3200, 300f);
                debrisInner1.setId("yod_debrisInner1");
                		
                DebrisFieldTerrainPlugin.DebrisFieldParams params2 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params2.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisInner2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
                debrisInner2.setSensorProfile(800f);
                debrisInner2.setDiscoverable(true);
                debrisInner2.setCircularOrbit(yod_star, 360*(float)Math.random(), 3700, 330f);
                debrisInner2.setId("yod_debrisInner2");

                // A hot Jupiter, swollen and massive.
                PlanetAPI yod2 = system.addPlanet("istl_planet_yod2", yod_star, "Yod II", "gas_giant", 300, 450, 5600, 300);
                    // Add fixed conditions to Yod II.
                    Misc.initConditionMarket(yod2);
                    yod2.getMarket().addCondition(Conditions.HOT);
                    yod2.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    yod2.getMarket().addCondition(Conditions.DENSE_ATMOSPHERE);
                    yod2.getMarket().addCondition(Conditions.HIGH_GRAVITY);
                    yod2.getMarket().addCondition(Conditions.VOLATILES_DIFFUSE);
                    yod2.getMarket().addCondition(Conditions.ORGANICS_TRACE);
                    yod2.setCustomDescriptionId("planet_yod2");
                
                    // And a station in orbit.
                    SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.ORBITAL_HABITAT, Factions.DERELICT);
                    stationDerelict1.setId("yod_derelict1");
                    stationDerelict1.setCircularOrbitWithSpin(yod2, 360*(float)Math.random(), 500, 75f, 7, 18);
                    Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride(Factions.DERELICT, 1f, 7, 17));
                    //Misc.setDefenderOverride(stationDerelict2, new DefenderDataOverride("blade_breakers", 1f, 8, 21));
                    CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
                    extraStationSalvage.addCommodity(Commodities.BETA_CORE, 2);
                    
                    // First moon.
                    PlanetAPI yod2a = system.addPlanet("istl_planet_yod2a", yod2, "Yod II A", "barren", 360*(float)Math.random(), 45, 600, 90f);
                    yod2a.getSpec().setTexture(Global.getSettings().getSpriteName("planets", "barren02"));
                    // Add fixed conditions to Yod 2 A.
                    Misc.initConditionMarket(yod2a);
                    yod2a.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    yod2a.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    yod2a.getMarket().addCondition(Conditions.HOT);
                    yod2a.getMarket().addCondition(Conditions.ORE_MODERATE);
                    //yod2a.setCustomDescriptionId("planet_yod2a");
                    
                    // Second moon.
                    PlanetAPI yod2b = system.addPlanet("istl_planet_yod2b", yod2, "Yod II B", "barren-desert", 360*(float)Math.random(), 120, 960, 128f);
                    // Add fixed conditions to Yod 2 B.
                    Misc.initConditionMarket(yod2b);
                    yod2b.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                    yod2b.getMarket().addCondition(Conditions.HOT);
                    yod2b.getMarket().addCondition(Conditions.ORE_SPARSE);
                    yod2b.getMarket().addCondition(Conditions.RUINS_SCATTERED);
                    yod2b.getMarket().addCondition(Conditions.POLLUTION);
                    //yod2a.setCustomDescriptionId("planet_yod2b");
                    
                    // Abandoned small fuelling base around Yod 2 B.
                    
                    
                    // Add a ring here.
                    system.addRingBand(yod2, "misc", "rings_special0", 256f, 1, new Color(155,155,155,225), 256f, 1320, 30f, Terrain.RING, null);
                    
                    // Probe in the ring.
                    SectorEntityToken shipDerelict2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_PROBE, Factions.DERELICT);
                    shipDerelict2.setId("yod_probe2");
                    shipDerelict2.setCircularOrbit(yod2, 360*(float)Math.random(), 1320, 135f);
                    Misc.setDefenderOverride(shipDerelict2, new DefenderDataOverride(Factions.DERELICT, 1f, 5, 13));
                    //Misc.setDefenderOverride(shipDerelict2, new DefenderDataOverride("blade_breakers", 1f, 8, 21));
                    
                    // Third moon.
                    PlanetAPI yod2c = system.addPlanet("istl_planet_yod2c", yod2, "Yod II C", "rocky_metallic", 90, 45, 1560, 156f);
                    // Add fixed conditions to Yod 2 C.
                    Misc.initConditionMarket(yod2c);
                    yod2c.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    yod2c.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    yod2c.getMarket().addCondition(Conditions.HOT);
                    yod2c.getMarket().addCondition(Conditions.ORE_ABUNDANT);
                    yod2c.getMarket().addCondition(Conditions.RARE_ORE_MODERATE);
                    //yod2c.setCustomDescriptionId("planet_yod2c");
                    
                    // Yod II's trojans.
                    SectorEntityToken yodL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                            920f, // min radius
                            1280f, // max radius
                            40, // min asteroid count
                            72, // max asteroid count
                            8f, // min asteroid radius 
                            24f, // max asteroid radius
                            "Yod L4 Shoal Zone")); // null for default name
                    
                    SectorEntityToken yodL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                            920f, // min radius
                            1280f, // max radius
                            40, // min asteroid count
                            72, // max asteroid count
                            8f, // min asteroid radius 
                            24f, // max asteroid radius
                            "Yod L5 Shoal Zone")); // null for default name

                    yodL4.setCircularOrbit(yod_star, 360f, 5600, 300);
                    yodL5.setCircularOrbit(yod_star, 240f, 5600, 300);
                    
                    // Small nebula opposite Yod II.
                    SectorEntityToken yod_L2_nebula = system.addTerrain(Terrain.NEBULA, new BaseTiledTerrain.TileParams(
				" x    " +
				"  xx x" +
				"x xxxx" +
				" xxx  " +
				" x  x " +
				"   x  ",
				6, 6, // size of the nebula grid, should match above string
				"terrain", "istl_yod_nebula", 4, 4, null));
                    yod_L2_nebula.setId("yod_L2");
                    yod_L2_nebula.setCircularOrbit(yod_star, 120, 5600, 300);
                    
                // Inner jump point ---------------
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("yod_inner_jump", "Yod Jump-point");
		jumpPoint1.setCircularOrbit( system.getEntityById("yod"), 120, 6400, 210);
		jumpPoint1.setRelatedPlanet(yod2);
		system.addEntity(jumpPoint1);
                
                // Another light sprinkling of asteroids.
                system.addAsteroidBelt(yod_star, 150, 7900, 210, 320, 400, Terrain.ASTEROID_BELT, "Yod Outer Belt");
                
                // Further out, a less dramatic Sigma world than Lenze.
                PlanetAPI yod3 = system.addPlanet("istl_planet_yod3", yod_star, "Yod III", "istl_sigmaworld", 360*(float)Math.random(), 100, 8600, -360);
                //yod3.getSpec().setAtmosphereColor(new Color(200,145,255,255));
		//yod3.getSpec().setCloudColor(new Color(200,185,255,200));
		//yod3.getSpec().setIconColor(new Color(145,195,255,255));
		yod3.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		yod3.getSpec().setGlowColor(new Color(165, 215, 255, 195));
		yod3.getSpec().setUseReverseLightForGlow(true);
		//yod3.getSpec().setAtmosphereThickness(0.6f);
		yod3.applySpecChanges();
                
                    Misc.initConditionMarket(yod3);
                    yod3.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                    yod3.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    yod3.getMarket().addCondition(Conditions.IRRADIATED);
                    yod3.getMarket().addCondition(Conditions.ORE_RICH);
                    yod3.getMarket().addCondition(Conditions.RARE_ORE_ABUNDANT);
                
                // The various less-crazyass FX and radiation belts.
                    // Strangelets and particles.
                    system.addRingBand(yod3, "misc", "rings_dust0", 96f, 1, Color.white, 24f, 120, 60f, Terrain.ASTEROID_BELT, null);
                    // Purple.
                    SectorEntityToken yod3_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                            140, // terrain effect middle radius
                            yod3, // entity that it's around
                            95f, // visual band start
                            295f, // visual band end
                            new Color(50, 30, 100, 60), // base color, increment brightness down by 15 each time
                            0.5f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(50, 20, 110, 65), // Standard aurora colors.
                            new Color(150, 30, 120, 75), 
                            new Color(200, 50, 130, 95),
                            new Color(250, 70, 150, 120),
                            new Color(200, 80, 130, 125),
                            new Color(75, 0, 80), 
                            new Color(127, 0, 125)
                            ));
                    yod3_field1.setCircularOrbit(yod3, 0, 0, 70);

                    // Pale blue auroral shocks.
                    SectorEntityToken yod3_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(600f, // terrain effect band width 
                            250, // terrain effect middle radius
                            yod3, // entity that it's around
                            100f, // visual band start, increment by 15
                            500f, // visual band end
                            new Color(10, 25, 60, 5), // base color, increment brightness down by 15 each time
                            0.1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(25, 60, 150, 80), // We're winging it here.
                            new Color(40, 90, 180, 90), 
                            new Color(50, 105, 195, 110),
                            new Color(60, 120, 210, 125),
                            new Color(70, 145, 225, 125),
                            new Color(10, 0, 65), 
                            new Color(15, 50, 50)
                            ));
                    yod3_field2.setCircularOrbit(yod3, 0, 0, 85);
                    // Bright blue-white boundary layer.
                    SectorEntityToken yod3_field3 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
                            135, // terrain effect middle radius
                            yod3, // entity that it's around
                            90f, // visual band start, increment by 15
                            180f, // visual band end
                            new Color(165, 215, 255, 75), // base color, increment brightness down by 15 each time
                            0f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(25, 60, 150, 65), // We're winging it here.
                            new Color(40, 90, 180, 75), 
                            new Color(50, 105, 195, 95),
                            new Color(60, 120, 210, 110),
                            new Color(70, 145, 225, 115),
                            new Color(10, 0, 65), 
                            new Color(15, 50, 50)
                            ));
                    yod3_field3.setCircularOrbit(yod3, 0, 0, 120);
                
                // Add a warning beacon for Yod-3 - reference the Daedaleon beacon code in the Eos Exodus system file and the entry in rules.csv.
		CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, "istl_bladebreaker_beacon", Factions.NEUTRAL);
		beacon.setCircularOrbitPointingDown(yod3, 0, 325, 28);
		beacon.getMemoryWithoutUpdate().set("$istl_lenzewarn", true);
		//Misc.setWarningBeaconGlowColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//Misc.setWarningBeaconPingColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//And then use $istl_lenzewarn as a condition for custom interaction text in rules.csv.
                
                // Spawn defense fleets around Yod-3. Should be moderately challenging.
                

                // Some procgen can go out here.
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, yod_star, StarAge.AVERAGE,
                        3, 7, // min/max entities to add
                        10800, // radius to start adding at 
                        3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system-name based names
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    480f, // field radius - should not go above 1000 for performance reasons
                    1.5f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params2.baseSalvageXP = 750; // base XP for scavenging in field
                SectorEntityToken debris3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
                debris3.setSensorProfile(1000f);
                debris3.setDiscoverable(true);
                debris3.setCircularOrbit(yod_star, 360*(float)Math.random(), 9600, 270f);
                debris3.setId("yod_debris3");
                                
                // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
