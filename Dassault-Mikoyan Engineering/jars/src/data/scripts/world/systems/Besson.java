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
import com.fs.starfarer.api.fleet.FleetMemberType;
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
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

public class Besson {

	public void generate(SectorAPI sector) {	
		
		StarSystemAPI system = sector.createStarSystem("Besson");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");
                
                SectorEntityToken dalet_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/eos_nebula.png",
			0, 0, // center of nebula
			system, // location to add to
                        "terrain", "istl_nebula_sigma", // "nebula_blue", // texture to use, uses xxx_map for map
                        4, 4, StarAge.OLD); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		// Besson, an enormous blue giant
		PlanetAPI besson_star = system.initStar("besson", // unique id for this star 
										    "star_blue_giant",  // id in planets.json
										    1350f, // radius (in pixels at default zoom)
										    900, // corona radius, from star edge
										    6f, // solar wind burn level
                                                                                    0.6f, // flare probability
                                                                                    2.0f); // CR loss multiplier, good values are in the range of 1-5
                                                                                    
		system.setLightColor(new Color(210, 230, 255)); // light color in entire system, affects all entities
                
		// Inner Asteroid belt.
		system.addAsteroidBelt(besson_star, 75, 2400, 100, 30, 90, Terrain.ASTEROID_BELT, null);
		system.addRingBand(besson_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 2350, 300f, Terrain.ASTEROID_BELT, null);
                
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
                debrisInner1.setCircularOrbit(besson_star, 360*(float)Math.random(), 2375, 210f);
                debrisInner1.setId("besson_debrisInner1");
                
                // Add a magnetic field.
                SectorEntityToken besson_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(700f, // terrain effect band width 
			2850, // terrain effect middle radius
			besson_star, // entity that it's around
			2500f, // visual band start
			3200f, // visual band end
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
                besson_field1.setCircularOrbit(besson_star, 0, 0, 120);
                
                // Fuel dump for Nex start
                SectorEntityToken fueldump = DerelictThemeGenerator.addSalvageEntity(system, Entities.SUPPLY_CACHE, Factions.DERELICT);
                fueldump.setId("besson_fueldump");
                fueldump.setCircularOrbit(besson_star, 360*(float)Math.random(), 3750, 300f);
                fueldump.setDiscoverable(Boolean.TRUE);
                
                // Derelict survey probe around Besson.
                SectorEntityToken shipDerelict2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_PROBE, Factions.DERELICT);
                shipDerelict2.setId("besson_probe1");
                shipDerelict2.setCircularOrbit(besson_star, 360*(float)Math.random(), 4250, 320f);
                    
                // Le Monde des Mille Couleurs - Lenze, the enigmatic source of the Sigma Event.
                PlanetAPI lenze = system.addPlanet("istl_planet_lenze", besson_star, "Lenze", "istl_sigmaworld", 145, 150, 5600, -180);
                //lenze.getSpec().setAtmosphereColor(new Color(200,145,255,255));
		//lenze.getSpec().setCloudColor(new Color(200,185,255,200));
		//lenze.getSpec().setIconColor(new Color(145,195,255,255));
		lenze.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
		lenze.getSpec().setGlowColor(new Color(165, 215, 255, 255));
		lenze.getSpec().setUseReverseLightForGlow(true);
		//lenze.getSpec().setAtmosphereThickness(0.6f);
		lenze.applySpecChanges();
                
                    Misc.initConditionMarket(lenze);
                    lenze.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                    lenze.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                    lenze.getMarket().addCondition(Conditions.IRRADIATED);
                    lenze.getMarket().addCondition(Conditions.ORE_ULTRARICH);
                    lenze.getMarket().addCondition(Conditions.RARE_ORE_RICH);
                    lenze.getMarket().addCondition(Conditions.RUINS_SCATTERED);
                
                // Lenze's various crazyass FX and radiation belts.
                    // Strangelets and particles.
                    system.addRingBand(lenze, "misc", "rings_dust0", 96f, 1, Color.white, 32f, 180, 75f, Terrain.ASTEROID_BELT, null);
                    // Purple.
                    SectorEntityToken lenze_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                            190, // terrain effect middle radius
                            lenze, // entity that it's around
                            145f, // visual band start
                            345f, // visual band end
                            new Color(50, 30, 100, 120), // base color, increment brightness down by 15 each time
                            0.5f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(50, 20, 110, 130), // Standard aurora colors.
                            new Color(150, 30, 120, 150), 
                            new Color(200, 50, 130, 190),
                            new Color(250, 70, 150, 240),
                            new Color(200, 80, 130, 255),
                            new Color(75, 0, 160), 
                            new Color(127, 0, 255)
                            ));
                    lenze_field1.setCircularOrbit(lenze, 0, 0, 70);
                        // Green-blue inner layer. Very optional.
                        //SectorEntityToken lenze_field4 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                                //new MagneticFieldTerrainPlugin.MagneticFieldParams(360f, // terrain effect band width 
                                //230, // terrain effect middle radius
                                //lenze, // entity that it's around
                                //185f, // visual band start, increment by 15
                                //505f, // visual band end
                                //new Color(30, 105, 120, 90), // base color, increment brightness down by 15 each time
                                //0.3f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                                //new Color(25, 90, 90, 130), // We're winging it here.
                                //new Color(40, 120, 120, 150), 
                                //new Color(50, 135, 135, 190),
                                //new Color(60, 150, 150, 240),
                                //new Color(70, 175, 175, 255),
                                //new Color(40, 0, 125), 
                                //new Color(45, 100, 100)
                                //));
                        //lenze_field4.setCircularOrbit(lenze, 0, 0, 100);
                    // Pale blue auroral shocks.
                    SectorEntityToken lenze_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(600f, // terrain effect band width 
                            300, // terrain effect middle radius
                            lenze, // entity that it's around
                            150f, // visual band start, increment by 15
                            750f, // visual band end
                            new Color(10, 25, 60, 5), // base color, increment brightness down by 15 each time
                            0.1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(25, 60, 150, 120), // We're winging it here.
                            new Color(40, 90, 180, 135), 
                            new Color(50, 105, 195, 165),
                            new Color(60, 120, 210, 185),
                            new Color(70, 145, 225, 195),
                            new Color(10, 0, 125), 
                            new Color(15, 50, 100)
                            ));
                    lenze_field2.setCircularOrbit(lenze, 0, 0, 85);
                    // Bright blue-white boundary layer.
                    SectorEntityToken lenze_field3 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                            new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
                            185, // terrain effect middle radius
                            lenze, // entity that it's around
                            140f, // visual band start, increment by 15
                            230f, // visual band end
                            new Color(165, 215, 255, 120), // base color, increment brightness down by 15 each time
                            0f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                            new Color(25, 60, 150, 100), // We're winging it here.
                            new Color(40, 90, 180, 115), 
                            new Color(50, 105, 195, 145),
                            new Color(60, 120, 210, 160),
                            new Color(70, 145, 225, 170),
                            new Color(10, 0, 125), 
                            new Color(15, 50, 100)
                            ));
                    lenze_field3.setCircularOrbit(lenze, 0, 0, 120);
                    
                lenze.setCustomDescriptionId("planet_lenze");
                
                // Add a warning beacon for Lenze - reference the Daedaleon beacon code in the Eos Exodus system file and the entry in rules.csv.
		CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, "istl_bladebreaker_beacon", Factions.NEUTRAL);
		//CustomCampaignEntityAPI beacon = system.addCustomEntity(null, null, Entities.WARNING_BEACON, Factions.NEUTRAL);
		beacon.setCircularOrbitPointingDown(lenze, 0, 450, 35);
		beacon.getMemoryWithoutUpdate().set("$istl_lenzewarn", true);
		//Misc.setWarningBeaconGlowColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//Misc.setWarningBeaconPingColor(beacon, Global.getSector().getFaction(Factions.DASSAULT).getBrightUIColor());
		//And then use $istl_lenzewarn as a condition for custom interaction text in rules.csv.
            
                // Spawn defense fleets around Lenze. Should be naaasty.
                
                
                // Inner jump point ---------------
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("besson_inner_jump", "Besson Jump-point");
		jumpPoint1.setCircularOrbit( system.getEntityById("besson"), 300, 7200, 240);
		//jumpPoint1.setRelatedPlanet(lenze);
		system.addEntity(jumpPoint1);       
                
                // More asteroid belts.
                system.addAsteroidBelt(besson_star, 90, 7750, 480, 95, 120, Terrain.ASTEROID_BELT,  "Nikita Belt");
		system.addRingBand(besson_star, "misc", "rings_dust0", 256f, 0, Color.gray, 256f, 7600, 105f, null, null);
		system.addRingBand(besson_star, "misc", "rings_asteroids0", 256f, 1, Color.white, 256f, 7720, 125f, null, null);
                
                system.addAsteroidBelt(besson_star, 90, 8400, 540, 105, 135, Terrain.ASTEROID_BELT,  "Paradise Belt");
		system.addRingBand(besson_star, "misc", "rings_dust0", 256f, 0, Color.gray, 256f, 8450, 128f, null, null);
                
                // Some more debris fields. Random locations.
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
                debrisInner2.setCircularOrbit(besson_star, 360*(float)Math.random(), 7720, 180f);
                debrisInner2.setId("besson_debrisInner2");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    240f, // field radius - should not go above 1000 for performance reasons
                    1.0f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params3.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params3.baseSalvageXP = 350; // base XP for scavenging in field
                SectorEntityToken debrisInner3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
                debrisInner3.setSensorProfile(600f);
                debrisInner3.setDiscoverable(true);
                debrisInner3.setCircularOrbit(besson_star, 360*(float)Math.random(), 7720, 180f);
                debrisInner3.setId("besson_debrisInner3");
                
                DebrisFieldTerrainPlugin.DebrisFieldParams params4 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                    360f, // field radius - should not go above 1000 for performance reasons
                    1.2f, // density, visual - affects number of debris pieces
                    10000000f, // duration in days 
                    0f); // days the field will keep generating glowing pieces
                params4.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
                params4.baseSalvageXP = 500; // base XP for scavenging in field
                SectorEntityToken debrisInner4 = Misc.addDebrisField(system, params4, StarSystemGenerator.random);
                debrisInner4.setSensorProfile(800f);
                debrisInner4.setDiscoverable(true);
                debrisInner4.setCircularOrbit(besson_star, 360*(float)Math.random(), 8425, 210f);
                debrisInner4.setId("besson_debrisInner4");         
                
                // Defunct relay.
                SectorEntityToken besson_relay = system.addCustomEntity("besson_relay", // unique id
                        "Besson Relay", // name - if null, defaultName from custom_entities.json will be used
                        "comm_relay", // type of object, defined in custom_entities.json
                        "independent"); // faction
                besson_relay.setCircularOrbitPointingDown(system.getEntityById("besson"), 120, 9600, 400);     
                
                //Let's try a derelict ship at L1.
                
                // Nevsky Polis - no longer at L1, orbits the outer system.
                SectorEntityToken bessonHabitat = system.addCustomEntity("besson_habitat", "Nevsky Polis", "station_side07", "neutral");
                bessonHabitat.setCircularOrbitWithSpin(system.getEntityById("besson"), 360*(float)Math.random(), 19200, 480, 9, 21);
                bessonHabitat.setDiscoverable(true);
                bessonHabitat.setDiscoveryXP(3500f);
                bessonHabitat.setSensorProfile(0.3f);

                    // Abandoned marketplace for Nevsky Polis.
                    bessonHabitat.getMemoryWithoutUpdate().set("$abandonedStation", true);
                    MarketAPI market = Global.getFactory().createMarket("abandoned_habitat_market", bessonHabitat.getName(), 0);
                    market.setPrimaryEntity(bessonHabitat);
                    market.setFactionId(bessonHabitat.getFaction().getId());
                    market.addCondition(Conditions.ABANDONED_STATION);
                    market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
                    ((StoragePlugin) market.getSubmarket(Submarkets.SUBMARKET_STORAGE).getPlugin()).setPlayerPaidToUnlock(true);
                    bessonHabitat.setMarket(market);
                    bessonHabitat.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("volatiles", 150);
                    bessonHabitat.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("food", 100);
                    bessonHabitat.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("rare_ore", 50);
                    bessonHabitat.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addMothballedShip(FleetMemberType.SHIP, "istl_stormkestrel_proto_test", "Unit 01");
                
                    bessonHabitat.setCustomDescriptionId("station_besson");
                
                // Ice giant at L3.
                PlanetAPI marat = system.addPlanet("marat", besson_star, "Marat", "ice_giant", 120, 350, 12800, 400);
                marat.setCustomDescriptionId("planet_marat");
                    
                    // Thin ring
                    system.addRingBand(marat, "misc", "rings_dust0", 256f, 0, Color.white, 128f, 450, 135f, null, null);
                    // Moon #1
                    PlanetAPI marat1 = system.addPlanet("istl_planet_marat1", marat, "Marat I", "cryovolcanic", 75, 45, 540, 180);
                    // Add fixed conditions to Marat 1.
                    Misc.initConditionMarket(marat1);
                    marat1.getMarket().addCondition(Conditions.THIN_ATMOSPHERE);
                    marat1.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    marat1.getMarket().addCondition(Conditions.COLD);
                    marat1.getMarket().addCondition(Conditions.VOLATILES_ABUNDANT);
                    //marat1.setCustomDescriptionId("planet_marat1");
                    // Thick ring
                    system.addRingBand(marat, "misc", "rings_special0", 256f, 1, new Color(180,180,180,255), 160f, 760, 30f, Terrain.RING, null); 
                    // Moon #2
                    PlanetAPI marat2 = system.addPlanet("istl_planet_marat2", marat, "Marat II", "frozen", 135, 90, 1100, 200);
                    // Add fixed conditions to Marat 2.
                    Misc.initConditionMarket(marat2);
                    marat2.getMarket().addCondition(Conditions.COLD);
                    marat2.getMarket().addCondition(Conditions.ORE_SPARSE);
                    marat2.getMarket().addCondition(Conditions.VOLATILES_TRACE);
                    marat2.getMarket().addCondition(Conditions.ORGANICS_COMMON);
                    marat2.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
                    //marat2.setCustomDescriptionId("planet_marat2");
                    // Thin ring again
                    system.addRingBand(marat, "misc", "rings_dust0", 256f, 0, Color.gray, 192f, 1350, 160f, null, null);
                    // Moon #3
                    PlanetAPI marat3 = system.addPlanet("istl_planet_marat3", marat, "Marat III", "barren", 215, 60, 1560, 220);
                    // Add fixed conditions to Marat 3.
                    Misc.initConditionMarket(marat3);
                    marat3.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    marat3.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    marat3.getMarket().addCondition(Conditions.VERY_COLD);
                    marat3.getMarket().addCondition(Conditions.ORE_MODERATE);
                    marat3.getMarket().addCondition(Conditions.RUINS_SCATTERED);
                    //marat3.setCustomDescriptionId("planet_marat3");
                    
                    // Survey ship.
                    SectorEntityToken shipDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_SHIP, Factions.DERELICT);
                    shipDerelict1.setId("besson_surveyship");
                    shipDerelict1.setCircularOrbit(marat, 225, 600, 180f);
                    
                    // Add a domain probe orbiting further out.
                    SectorEntityToken shipDerelict3 = DerelictThemeGenerator.addSalvageEntity(system, Entities.DERELICT_SURVEY_PROBE, Factions.DERELICT);
                    shipDerelict3.setId("besson_probe2");
                    shipDerelict3.setCircularOrbit(marat, 360*(float)Math.random(), 1960, 270f);
                    
                // Besson's secondary, Lucille.
                PlanetAPI besson_star_b = system.addPlanet("lucille", besson_star, "Lucille", "star_browndwarf", 300, 560, 12800, 400);
		system.setSecondary(besson_star_b);
                //besson_star_b.setCustomDescriptionId("star_lucille");
                    
                    // Let's throw in a magnetic field
                    SectorEntityToken lucille_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                                    new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
                                    660, // terrain effect middle radius
                                    besson_star_b, // entity that it's around
                                    560f, // visual band start
                                    760f, // visual band end
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
                    lucille_field1.setCircularOrbit(besson_star_b, 0, 0, 100);
               
                    // Lucille's dust ring, ooh la la.
                    system.addRingBand(besson_star_b, "misc", "rings_dust0", 256f, 1, Color.gray, 256f, 960, 100f);
                    system.addAsteroidBelt(besson_star_b, 120, 960, 300, 200, 300, Terrain.ASTEROID_BELT, "Laplace Stream");
                          
                    // And another magnetic field
                    SectorEntityToken lucille_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                                    new MagneticFieldTerrainPlugin.MagneticFieldParams(120f, // terrain effect band width 
                                    1140, // terrain effect middle radius
                                    besson_star_b, // entity that it's around
                                    1080f, // visual band start
                                    1200f, // visual band end
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
                    lucille_field2.setCircularOrbit(besson_star_b, 0, 0, 100); 
                    
                    // Lucille needs some moons.
                        // First moon.
                        PlanetAPI lucille1 = system.addPlanet("istl_planet_lucille1", besson_star_b, "Lucille I", "barren-bombarded", 360*(float)Math.random(), 40, 1280, 140);
                            // Add fixed conditions to Lucille I.
                            Misc.initConditionMarket(lucille1);
                            lucille1.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                            lucille1.getMarket().addCondition(Conditions.LOW_GRAVITY);
                            lucille1.getMarket().addCondition(Conditions.METEOR_IMPACTS);
                            //lucille1.setCustomDescriptionId("planet_lucille1");

                        // Second moon. The habitable one.
                        PlanetAPI lucille2 = system.addPlanet("istl_planet_lucille2", besson_star_b, "Source", "terran-eccentric", 360*(float)Math.random(), 60, 1600, 180);
                            // Add fixed conditions to Source.
                            Misc.initConditionMarket(lucille2);
                            lucille2.getMarket().addCondition(Conditions.HABITABLE);
                            lucille2.getMarket().addCondition(Conditions.LOW_GRAVITY);
                            lucille2.getMarket().addCondition(Conditions.FARMLAND_RICH);
                            lucille2.getMarket().addCondition(Conditions.RUINS_WIDESPREAD);
                            lucille2.setCustomDescriptionId("planet_source");
                            
                        // Should get a Blade Breaker station in orbit.
                        
                    
                    // Lucille trojans.
                    SectorEntityToken lucilleL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                            920f, // min radius
                            1280f, // max radius
                            40, // min asteroid count
                            72, // max asteroid count
                            8f, // min asteroid radius 
                            24f, // max asteroid radius
                            "Lucille L4 Shoal Zone")); // null for default name
                    
                    SectorEntityToken lucilleL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
                            new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                            920f, // min radius
                            1280f, // max radius
                            40, // min asteroid count
                            72, // max asteroid count
                            8f, // min asteroid radius 
                            24f, // max asteroid radius
                            "Lucille L5 Shoal Zone")); // null for default name

                    lucilleL4.setCircularOrbit(besson_star, 360f, 12800, 400);
                    lucilleL5.setCircularOrbit(besson_star, 240f, 12800, 400);
                    
                    // Mining station at L4.
                    SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.STATION_MINING, Factions.DERELICT);
                    stationDerelict1.setId("besson_derelict1");
                    stationDerelict1.setCircularOrbit(besson_star, 360, 12800, 400f);
                    //Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride("blade_breakers", 1f, 5, 11));
                    
                    // Something fun at L5.
                    SectorEntityToken l5scrap = DerelictThemeGenerator.addSalvageEntity(system, Entities.WEAPONS_CACHE_REMNANT, Factions.DERELICT);
                    l5scrap.setId("besson_l5scrap");
                    l5scrap.setCircularOrbit(besson_star, 240, 12800, 400);
                    Misc.setDefenderOverride(l5scrap, new DefenderDataOverride(Factions.DERELICT, 1, 2, 0));
                    //Misc.setDefenderOverride(l5scrap, new DefenderDataOverride("blade_breakers", 1f, 3, 7));
                    l5scrap.setDiscoverable(Boolean.TRUE);

                    l5scrap.setDiscoverable(Boolean.TRUE);
        
                // Abandoned habitat at L2.
                SectorEntityToken stationDerelict2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.ORBITAL_HABITAT, Factions.DERELICT);
		stationDerelict2.setId("besson_derelict2");
		stationDerelict2.setCircularOrbit(besson_star, 300, 16000, 400f);
                Misc.setDefenderOverride(stationDerelict2, new DefenderDataOverride(Factions.DERELICT, 1f, 4, 12));
		//Misc.setDefenderOverride(stationDerelict2, new DefenderDataOverride("blade_breakers", 1f, 8, 21));
		CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
		extraStationSalvage.addCommodity(Commodities.ALPHA_CORE, 1);
		BaseSalvageSpecial.setExtraSalvage(extraStationSalvage, stationDerelict2.getMemoryWithoutUpdate(), -1);
                
                // Planetoid for spacing?
                PlanetAPI besson4 = system.addPlanet("istl_planet_besson4", besson_star, "Besson IV", "barren", 360*(float)Math.random(), 60, 17600, 480);
                
                // And a last tiny ring to top it all off.
                system.addRingBand(besson_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 19200, 520f, Terrain.RING, "Outer Band");
                
                // Procgen makes you strong.
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, besson_star, StarAge.AVERAGE,
                        3, 6, // min/max entities to add
                        21600, // radius to start adding at 
                        4, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system-name based names
                
                // Add a nebula to the system.
		//StarSystemGenerator.addSystemwideNebula(system, StarAge.OLD);
                
                // generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
        }
}
