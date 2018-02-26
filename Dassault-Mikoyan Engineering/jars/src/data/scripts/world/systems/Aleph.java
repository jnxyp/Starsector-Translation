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
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class Aleph {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Aleph");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		// Aleph, a swollen brown dwarf.
		PlanetAPI aleph_star = system.initStar("aleph", // unique id for this star 
										    "star_red_dwarf",  // id in planets.json
										    450f, // radius (in pixels at default zoom)
										    250, // corona radius, from star edge
										    0.6f, // solar wind burn level
                                                                                    0.3f, // flare probability
                                                                                    1.0f); // CR loss multiplier, good values are in the range of 1-5
                                                                                    
		system.setLightColor(new Color(245, 225, 175)); // light color in entire system, affects all entities
                
                // Close, intense magnetic field.
                SectorEntityToken aleph_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(360f, // terrain effect band width 
			900, // terrain effect middle radius
			aleph_star, // entity that it's around
			720f, // visual band start
			1080f, // visual band end
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
                aleph_field1.setCircularOrbit(aleph_star, 0, 0, 120);
                
                // Aleph 1, a thoroughly worthless volcanic rock.
                PlanetAPI aleph1 = system.addPlanet("istl_planet_aleph1", aleph_star, "Aleph I", "lava_minor", 360*(float)Math.random(), 75, 1080, 90);
                    // Add fixed conditions to Aleph II.
                    Misc.initConditionMarket(aleph1);
                    aleph1.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    aleph1.getMarket().addCondition(Conditions.ORE_SPARSE);
                    aleph1.setCustomDescriptionId("planet_aleph1");
                    
                // Derelict survey probe around Aleph.
                SectorEntityToken shipDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_PROBE, Factions.DERELICT);
                shipDerelict1.setId("aleph_probe1");
                shipDerelict1.setCircularOrbit(aleph_star, 360*(float)Math.random(), 1200, 180f);
                
                // Some sparse asteroids.
                system.addRingBand(aleph_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 1400, 120f);
                system.addAsteroidBelt(aleph_star, 120, 1280, 300, 200, 300, Terrain.ASTEROID_BELT, "The Tears");
                
                // Some debris in the inner system.
                DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params1.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisInner1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
                debrisInner1.setSensorProfile(800f);
                debrisInner1.setDiscoverable(true);
                debrisInner1.setCircularOrbit(aleph_star, 360*(float)Math.random(), 1400, 180f);
                debrisInner1.setId("aleph_debrisInner1");
                
                // Less intense magnetic field.
                SectorEntityToken aleph_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(400f, // terrain effect band width 
			1740f, // terrain effect middle radius
			aleph_star, // entity that it's around
			1540f, // visual band start
			1940f, // visual band end
			new Color(50, 30, 100, 30), // base color
			0.3f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(150, 30, 120, 150), 
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
                aleph_field2.setCircularOrbit(aleph_star, 0, 0, 120);
                
                // More asteroids.
		system.addAsteroidBelt(aleph_star, 90, 2160, 500, 150, 300, Terrain.ASTEROID_BELT,  "The Wailing River");
		system.addRingBand(aleph_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 2100, 305f, null, null);
		system.addRingBand(aleph_star, "misc", "rings_dust0", 256f, 3, Color.gray, 256f, 2120, 285f, null, null);
                
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
                debrisInner2.setCircularOrbit(aleph_star, 360*(float)Math.random(), 2130, 210f);
                debrisInner2.setId("aleph_debrisInner2");

                // A tundra world, recently abandoned by... someone.
                PlanetAPI aleph2 = system.addPlanet("istl_planet_aleph2", aleph_star, "Aleph II", "tundra", 60, 100, 2800, 180);
                    // Add fixed conditions to Aleph II.
                    Misc.initConditionMarket(aleph2);
                    aleph2.getMarket().addCondition(Conditions.HABITABLE);
                    aleph2.getMarket().addCondition(Conditions.RUINS_EXTENSIVE);
                    aleph2.getMarket().addCondition(Conditions.COLD);
                    aleph2.getMarket().addCondition(Conditions.POOR_LIGHT);
                    aleph2.getMarket().addCondition(Conditions.ORE_MODERATE);
                    aleph2.getMarket().addCondition(Conditions.ORGANICS_TRACE);
                    aleph2.getMarket().addCondition(Conditions.FARMLAND_ADEQUATE);
                    aleph2.setCustomDescriptionId("planet_aleph2");
                
                    // And a station in orbit.
                    SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.ORBITAL_HABITAT, Factions.DERELICT);
                    stationDerelict1.setId("aleph_derelict1");
                    stationDerelict1.setCircularOrbit(aleph2, 360*(float)Math.random(), 270, 90f);
                    Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride(Factions.DERELICT, 1f, 4, 12));
                    //Misc.setDefenderOverride(stationDerelict2, new DefenderDataOverride("blade_breakers", 1f, 8, 21));
                    CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
                    extraStationSalvage.addCommodity(Commodities.ALPHA_CORE, 1); 
                    
                // Inner jump point ---------------
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("aleph_inner_jump", "Aleph Jump-point");
		jumpPoint1.setCircularOrbit( system.getEntityById("aleph"), 120, 3350, 180);
		jumpPoint1.setRelatedPlanet(aleph2);
		system.addEntity(jumpPoint1);
                
                // Yet more asteroids.
                system.addRingBand(aleph_star, "misc", "rings_ice0", 256f, 3, Color.white, 256f, 3670, 220f, null, null);
                system.addRingBand(aleph_star, "misc", "rings_asteroids0", 256f, 2, Color.white, 256f, 3715, 223f, null, null);
                system.addRingBand(aleph_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 3760, 226f, null, null);
                system.addAsteroidBelt(aleph_star, 150, 3600, 170, 200, 250, Terrain.ASTEROID_BELT, "Aleph Outer Belt");
                system.addAsteroidBelt(aleph_star, 100, 3750, 128, 200, 300, Terrain.ASTEROID_BELT, "Aleph Outer Belt");                
                
                // Further out, a less dramatic Sigma world than Lenze.
                PlanetAPI aleph3 = system.addPlanet("istl_planet_aleph3", aleph_star, "Aleph Null", "istl_sigmaworld", 360*(float)Math.random(), 100, 5400, -240);
                //aleph3.getSpec().setAtmosphereColor(new Color(200,145,255,255));
		//aleph3.getSpec().setCloudColor(new Color(200,185,255,200));
		//aleph3.getSpec().setIconColor(new Color(145,195,255,255));
		aleph3.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		aleph3.getSpec().setGlowColor(new Color(165, 215, 255, 195));
		aleph3.getSpec().setUseReverseLightForGlow(true);
		//aleph3.getSpec().setAtmosphereThickness(0.6f);
		aleph3.applySpecChanges();
                
                    Misc.initConditionMarket(aleph3);
                    aleph3.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                    aleph3.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    aleph3.getMarket().addCondition(Conditions.IRRADIATED);
                    aleph3.getMarket().addCondition(Conditions.ORE_RICH);
                    aleph3.getMarket().addCondition(Conditions.RARE_ORE_ABUNDANT);
                
                // The various less-crazyass FX and radiation belts.
                    // Strangelets and particles.
                    system.addRingBand(aleph3, "misc", "rings_dust0", 96f, 1, Color.white, 24f, 120, 60f, Terrain.ASTEROID_BELT, null);
                    // Purple.
                    SectorEntityToken aleph3_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                            140, // terrain effect middle radius
                            aleph3, // entity that it's around
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
                    aleph3_field1.setCircularOrbit(aleph3, 0, 0, 70);

                    // Pale blue auroral shocks.
                    SectorEntityToken aleph3_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(600f, // terrain effect band width 
                            250, // terrain effect middle radius
                            aleph3, // entity that it's around
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
                    aleph3_field2.setCircularOrbit(aleph3, 0, 0, 85);
                    // Bright blue-white boundary layer.
                    SectorEntityToken aleph3_field3 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
                            135, // terrain effect middle radius
                            aleph3, // entity that it's around
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
                    aleph3_field3.setCircularOrbit(aleph3, 0, 0, 120);
                
                // Add a warning beacon for Aleph Null - reference the Daedaleon beacon code in the Eos Exodus system file and the entry in rules.csv.
		CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, "istl_bladebreaker_beacon", Factions.NEUTRAL);
		beacon.setCircularOrbitPointingDown(aleph3, 0, 325, 28);
		beacon.getMemoryWithoutUpdate().set("$istl_lenzewarn", true);
		//Misc.setWarningBeaconGlowColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//Misc.setWarningBeaconPingColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//And then use $istl_lenzewarn as a condition for custom interaction text in rules.csv.
                
                // Spawn defense fleets around Aleph Null. Should be slighly more challenging.
                
                
                // Abandoned station. Something of a fifth wheel right now, saving for later.
                //SectorEntityToken derelictBase = system.addCustomEntity("aleph_station", "Lanner Station", "station_side02", "neutral");
                //derelictBase.setCircularOrbit(system.getEntityById("aleph"), 360*(float)Math.random(), 7600, 300);
                //derelictBase.setDiscoverable(true);
                //derelictBase.setDiscoveryXP(2500f);
                //derelictBase.setSensorProfile(1.0f);
                //derelictBase.setInteractionImage("illustrations", "space_wreckage");
                //derelictBase.setCustomDescriptionId("station_aleph");                
                
                // Yet *another* asteroid belt. This system, I tell you...
                system.addRingBand(aleph_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 6000, 400f, Terrain.RING, "Helene Band");
                
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
                debris3.setCircularOrbit(aleph_star, 360*(float)Math.random(), 8400, 270f);
                debris3.setId("aleph_debris3");
                
                //Procgen motherfuckers!
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, aleph_star, StarAge.AVERAGE,
                        3, 7, // min/max entities to add
                        10800, // radius to start adding at 
                        3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system-name based names
                
                
                // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
