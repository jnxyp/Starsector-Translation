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
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class Kaf {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Kaf");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");
                
                SectorEntityToken kaf_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
			0, 0, // center of nebula
			system, // location to add to
                        "terrain", "istl_nebula_sigma", // "nebula_blue", // texture to use, uses xxx_map for map
                        4, 4, StarAge.AVERAGE); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		// Aleph, a swollen brown dwarf.
		PlanetAPI kaf_star = system.initStar("kaf", // unique id for this star 
										    "star_yellow",  // id in planets.json
										    350f, // radius (in pixels at default zoom)
										    200, // corona radius, from star edge
										    0.6f, // solar wind burn level
                                                                                    0.3f, // flare probability
                                                                                    1.2f); // CR loss multiplier, good values are in the range of 1-5
                                                                                    
		system.setLightColor(new Color(200, 235, 255)); // light color in entire system, affects all entities
                
                // Close, weak magnetic field.
                SectorEntityToken kaf_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(360f, // terrain effect band width 
			670, // terrain effect middle radius
			kaf_star, // entity that it's around
			720f, // visual band start
			1080f, // visual band end
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
                kaf_field1.setCircularOrbit(kaf_star, 0, 0, 108);
                
                // A less dramatic Sigma world than Lenze.
                PlanetAPI kaf1 = system.addPlanet("istl_planet_kaf1", kaf_star, "Kaf I", "istl_sigmaworld", 360*(float)Math.random(), 100, 1440, -120);
                //kaf2.getSpec().setAtmosphereColor(new Color(200,145,255,255));
		//kaf2.getSpec().setCloudColor(new Color(200,185,255,200));
		//kaf2.getSpec().setIconColor(new Color(145,195,255,255));
		kaf1.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		kaf1.getSpec().setGlowColor(new Color(165, 215, 255, 195));
		kaf1.getSpec().setUseReverseLightForGlow(true);
		//kaf2.getSpec().setAtmosphereThickness(0.6f);
		kaf1.applySpecChanges();
                
                    Misc.initConditionMarket(kaf1);
                    kaf1.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                    kaf1.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    kaf1.getMarket().addCondition(Conditions.IRRADIATED);
                    kaf1.getMarket().addCondition(Conditions.ORE_RICH);
                    kaf1.getMarket().addCondition(Conditions.RARE_ORE_ABUNDANT);
                
                // The various less-crazyass FX and radiation belts.
                    // Strangelets and particles.
                    system.addRingBand(kaf1, "misc", "rings_dust0", 96f, 1, Color.white, 24f, 120, 60f, Terrain.ASTEROID_BELT, null);
                    // Purple.
                    SectorEntityToken kaf1_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                            140, // terrain effect middle radius
                            kaf1, // entity that it's around
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
                    kaf1_field1.setCircularOrbit(kaf1, 0, 0, 70);

                    // Pale blue auroral shocks.
                    SectorEntityToken kaf1_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(600f, // terrain effect band width 
                            250, // terrain effect middle radius
                            kaf1, // entity that it's around
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
                    kaf1_field2.setCircularOrbit(kaf1, 0, 0, 85);
                    // Bright blue-white boundary layer.
                    SectorEntityToken kaf1_field3 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
                            135, // terrain effect middle radius
                            kaf1, // entity that it's around
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
                    kaf1_field3.setCircularOrbit(kaf1, 0, 0, 120);
                
                // Add a warning beacon for Kaf-1 - reference the Daedaleon beacon code in the Eos Exodus system file and the entry in rules.csv.
		CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, "istl_bladebreaker_beacon", Factions.NEUTRAL);
		beacon.setCircularOrbitPointingDown(kaf1, 0, 325, 28);
		beacon.getMemoryWithoutUpdate().set("$istl_lenzewarn", true);
		//Misc.setWarningBeaconGlowColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//Misc.setWarningBeaconPingColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//And then use $istl_lenzewarn as a condition for custom interaction text in rules.csv.
                
                // Spawn a defense fleet around Kaf-1. Should be moderately challenging.
               
                
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
                debrisInner1.setCircularOrbit(kaf_star, 360*(float)Math.random(), 2300, 180f);
                debrisInner1.setId("kaf_debrisInner1");
                
                // Toxic world.
                PlanetAPI kaf2 = system.addPlanet("istl_planet_kaf2", kaf_star, "Kaf II", "toxic", 360*(float)Math.random(), 75, 2900, 150);
                // Market conditions.
                Misc.initConditionMarket(kaf2);
                    kaf2.getMarket().addCondition(Conditions.HOT);
                    kaf2.getMarket().addCondition(Conditions.TOXIC_ATMOSPHERE);
                    kaf2.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
                    kaf2.getMarket().addCondition(Conditions.ORGANICS_ABUNDANT);
                    kaf2.getMarket().addCondition(Conditions.VOLATILES_DIFFUSE);
                    
                // A disk of asteroids filling the inner system.
                system.addRingBand(kaf_star, "misc", "rings_dust0", 256f, 1, Color.gray, 144f, 3600, 210f);
                system.addRingBand(kaf_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 3800, 235f);
                system.addAsteroidBelt(kaf_star, 280, 3740, 720, 180, 250, Terrain.ASTEROID_BELT, "Kaf Shoal Zone");
                system.addAsteroidBelt(kaf_star, 560, 3740, 1520, 220, 290, Terrain.ASTEROID_BELT, "Kaf Shoal Zone");
                                
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
                debrisInner2.setCircularOrbit(kaf_star, 360*(float)Math.random(), 3440, 270f);
                debrisInner2.setId("kaf_debrisInner2");
                		
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
                debrisInner3.setCircularOrbit(kaf_star, 360*(float)Math.random(), 3640, 300f);
                debrisInner3.setId("kaf_debrisInner3");
                
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
                debris4.setCircularOrbit(kaf_star, 360*(float)Math.random(), 4040, 330f);
                debris4.setId("kaf_debrisInner4");
                
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
                debris5.setCircularOrbit(kaf_star, 360*(float)Math.random(), 4240, 360f);
                debris5.setId("kaf_debrisInner5");
                
                // Inner jump point ---------------
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("kaf_inner_jump", "Kaf Jump-point");
		jumpPoint1.setCircularOrbit( system.getEntityById("kaf"), 360*(float)Math.random(), 4800, 180f);
		jumpPoint1.setRelatedPlanet(kaf2);
		system.addEntity(jumpPoint1);
                
                // Mining station.
                SectorEntityToken stationDerelict = DerelictThemeGenerator.addSalvageEntity(system, Entities.STATION_MINING, Factions.DERELICT);
		stationDerelict.setId("kaf_derelict");
		stationDerelict.setCircularOrbitWithSpin(kaf_star, 360*(float)Math.random(), 5400, 320f, 3, 14);
		Misc.setDefenderOverride(stationDerelict, new DefenderDataOverride(Factions.DERELICT, 1f, 4, 9));
		CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
		extraStationSalvage.addCommodity(Commodities.GAMMA_CORE, 1);
		BaseSalvageSpecial.setExtraSalvage(extraStationSalvage, stationDerelict.getMemoryWithoutUpdate(), -1);
                
                // Dust and debris.
                system.addRingBand(kaf_star, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 6100, 240f);
                system.addAsteroidBelt(kaf_star, 120, 6700, 300, 200, 300, Terrain.ASTEROID_BELT, "Kaf Outer Belt");
                
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
                debris6.setCircularOrbit(kaf_star, 360*(float)Math.random(), 6400, 440f);
                debris6.setId("kaf_debris6");
                
                // And a last tiny ring to top it all off.
                system.addRingBand(kaf_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 7200, 480f, Terrain.RING, "Outer Band");
                
                // Some procgen.
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, kaf_star, StarAge.AVERAGE,
                        3, 7, // min/max entities to add
                        7600, // radius to start adding at 
                        2, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system-name based names
                
                // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
