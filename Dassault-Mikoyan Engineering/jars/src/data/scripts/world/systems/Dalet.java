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
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class Dalet {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Dalet");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
                
                SectorEntityToken dalet_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/hybrasil_nebula.png",
			0, 0, // center of nebula
			system, // location to add to
                        "terrain", "istl_nebula_sigma", // "nebula_blue", // texture to use, uses xxx_map for map
                        4, 4, StarAge.OLD); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		// Aleph, a swollen brown dwarf.
		PlanetAPI dalet_star = system.initStar("dalet", // unique id for this star 
										    "star_white",  // id in planets.json
										    320f, // radius (in pixels at default zoom)
										    180, // corona radius, from star edge
										    0.8f, // solar wind burn level
                                                                                    0.4f, // flare probability
                                                                                    1.5f); // CR loss multiplier, good values are in the range of 1-5
                                                                                    
		system.setLightColor(new Color(200, 235, 255)); // light color in entire system, affects all entities
                
                // Close, intense magnetic field.
                SectorEntityToken dalet_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(360f, // terrain effect band width 
			670, // terrain effect middle radius
			dalet_star, // entity that it's around
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
                dalet_field1.setCircularOrbit(dalet_star, 0, 0, 108);
                
                // Close-in hellscape world.
                PlanetAPI dalet1 = system.addPlanet("istl_planet_dalet1", dalet_star, "Dalet I", "lava_minor", 360*(float)Math.random(), 60, 1280, 90);
                // Market conditions.
                Misc.initConditionMarket(dalet1);
                    dalet1.getMarket().addCondition(Conditions.VERY_HOT);
                    dalet1.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                    dalet1.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
                    dalet1.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    dalet1.getMarket().addCondition(Conditions.ORE_MODERATE);
                
                // Some debris.
                DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing piec
                params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params1.baseSalvageXP = 750; // base XP for scavenging in field
                SectorEntityToken debrisInner1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
                debrisInner1.setSensorProfile(1200f);
                debrisInner1.setDiscoverable(true);
                debrisInner1.setCircularOrbit(dalet_star, 360*(float)Math.random(), 1470, 180f);
                debrisInner1.setId("dalet_debrisInner1");
                
                // A disk of asteroids filling the inner system.
                system.addRingBand(dalet_star, "misc", "rings_dust0", 256f, 1, Color.gray, 144f, 2600, 210f);
                system.addRingBand(dalet_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 2800, 235f);
                system.addAsteroidBelt(dalet_star, 280, 2740, 720, 180, 250, Terrain.ASTEROID_BELT, "Dalet Shoal Zone");
                system.addAsteroidBelt(dalet_star, 560, 2740, 1520, 220, 290, Terrain.ASTEROID_BELT, "Dalet Shoal Zone");
                
                // Abandoned pirate hideout in the dust disk.
                
                                
                // Some debris in the disk.
                DebrisFieldTerrainPlugin.DebrisFieldParams params2 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    520f, // field radius - should not go above 1000 for performance reasons
                    1.6f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params2.baseSalvageXP = 750; // base XP for scavenging in field
                SectorEntityToken debrisInner2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
                debrisInner2.setSensorProfile(1200f);
                debrisInner2.setDiscoverable(true);
                debrisInner2.setCircularOrbit(dalet_star, 360*(float)Math.random(), 2440, 270f);
                debrisInner2.setId("dalet_debrisInner2");
                		
                DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params3.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params3.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisInner3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
                debrisInner3.setSensorProfile(800f);
                debrisInner3.setDiscoverable(true);
                debrisInner3.setCircularOrbit(dalet_star, 360*(float)Math.random(), 2640, 300f);
                debrisInner3.setId("dalet_debrisInner3");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params4 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    480f, // field radius - should not go above 1000 for performance reasons
                    1.4f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params4.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params4.baseSalvageXP = 600; // base XP for scavenging in field
                SectorEntityToken debris4 = Misc.addDebrisField(system, params4, StarSystemGenerator.random);
                debris4.setSensorProfile(1000f);
                debris4.setDiscoverable(true);
                debris4.setCircularOrbit(dalet_star, 360*(float)Math.random(), 3040, 330f);
                debris4.setId("dalet_debrisInner4");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params5 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params5.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params5.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debris5 = Misc.addDebrisField(system, params5, StarSystemGenerator.random);
                debris5.setSensorProfile(800f);
                debris5.setDiscoverable(true);
                debris5.setCircularOrbit(dalet_star, 360*(float)Math.random(), 3240, 360f);
                debris5.setId("dalet_debrisInner5");
                
                // Further out, a less dramatic Sigma world than Lenze.
                PlanetAPI dalet2 = system.addPlanet("istl_planet_dalet2", dalet_star, "Dalet II", "istl_sigmaworld", 360*(float)Math.random(), 100, 4200, -150);
                //dalet2.getSpec().setAtmosphereColor(new Color(200,145,255,255));
		//dalet2.getSpec().setCloudColor(new Color(200,185,255,200));
		//dalet2.getSpec().setIconColor(new Color(145,195,255,255));
		dalet2.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		dalet2.getSpec().setGlowColor(new Color(165, 215, 255, 195));
		dalet2.getSpec().setUseReverseLightForGlow(true);
		//dalet2.getSpec().setAtmosphereThickness(0.6f);
		dalet2.applySpecChanges();
                
                    Misc.initConditionMarket(dalet2);
                    dalet2.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                    dalet2.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    dalet2.getMarket().addCondition(Conditions.IRRADIATED);
                    dalet2.getMarket().addCondition(Conditions.ORE_RICH);
                    dalet2.getMarket().addCondition(Conditions.RARE_ORE_ABUNDANT);
                
                // The various less-crazyass FX and radiation belts.
                    // Strangelets and particles.
                    system.addRingBand(dalet2, "misc", "rings_dust0", 96f, 1, Color.white, 24f, 120, 60f, Terrain.ASTEROID_BELT, null);
                    // Purple.
                    SectorEntityToken dalet2_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                            140, // terrain effect middle radius
                            dalet2, // entity that it's around
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
                    dalet2_field1.setCircularOrbit(dalet2, 0, 0, 70);

                    // Pale blue auroral shocks.
                    SectorEntityToken dalet2_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(600f, // terrain effect band width 
                            250, // terrain effect middle radius
                            dalet2, // entity that it's around
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
                    dalet2_field2.setCircularOrbit(dalet2, 0, 0, 85);
                    // Bright blue-white boundary layer.
                    SectorEntityToken dalet2_field3 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
                            135, // terrain effect middle radius
                            dalet2, // entity that it's around
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
                    dalet2_field3.setCircularOrbit(dalet2, 0, 0, 120);
                
                // Add a warning beacon for Dalet-2 - reference the Daedaleon beacon code in the Eos Exodus system file and the entry in rules.csv.
		CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, "istl_bladebreaker_beacon", Factions.NEUTRAL);
		beacon.setCircularOrbitPointingDown(dalet2, 0, 325, 28);
		beacon.getMemoryWithoutUpdate().set("$istl_lenzewarn", true);
		//Misc.setWarningBeaconGlowColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//Misc.setWarningBeaconPingColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//And then use $istl_lenzewarn as a condition for custom interaction text in rules.csv.
                
                // Spawn a defense fleet around Dalet-2. Should be moderately challenging.
               
                
                // Dust and debris.
                system.addRingBand(dalet_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 6700, 240f);
                system.addAsteroidBelt(dalet_star, 120, 6700, 300, 200, 300, Terrain.ASTEROID_BELT, "Dalet Outer Belt");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params6 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params6.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params6.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debris6 = Misc.addDebrisField(system, params6, StarSystemGenerator.random);
                debris6.setSensorProfile(800f);
                debris6.setDiscoverable(true);
                debris6.setCircularOrbit(dalet_star, 360*(float)Math.random(), 6100, 440f);
                debris6.setId("dalet_debris6");
                
                // Small desert world.
                PlanetAPI dalet3 = system.addPlanet("istl_planet_dalet3", dalet_star, "Dalet III", "desert", 0, 75, 7200, 240);
                // Market conditions.
                Misc.initConditionMarket(dalet3);
                    dalet3.getMarket().addCondition(Conditions.POOR_LIGHT);
                    dalet3.getMarket().addCondition(Conditions.COLD);
                    dalet3.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
                    dalet3.getMarket().addCondition(Conditions.ORE_MODERATE);
                    dalet3.getMarket().addCondition(Conditions.RARE_ORE_SPARSE);
                
                // Busted station.
                SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.STATION_RESEARCH, Factions.DERELICT);
                stationDerelict1.setId("dalet_derelict1");
                stationDerelict1.setCircularOrbit(dalet_star, 360*(float)Math.random(), 10800, 400f);
		//Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride("blade_breakers", 1f, 9, 25));
                
                // And a last tiny ring to top it all off.
                system.addRingBand(dalet_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 10800, 480f, Terrain.RING, "Outer Band");
                
                // Some procgen.
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, dalet_star, StarAge.AVERAGE,
                        2, 5, // min/max entities to add
                        12800, // radius to start adding at 
                        3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system-name based names
                
                // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
