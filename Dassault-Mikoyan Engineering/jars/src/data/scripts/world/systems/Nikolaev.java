package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;

import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;


import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
//import com.fs.starfarer.api.impl.campaign.procgen.StarAge;

import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import com.fs.starfarer.api.util.Misc;
import data.campaign.fleets.SixthBureauFleetManager;

public class Nikolaev
{
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name,
            int size, ArrayList<String> marketConditions, ArrayList<String> submarkets, float tarrif)
    {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market";

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.setBaseSmugglingStabilityValue(0);
        newMarket.getTariff().modifyFlat("generator", tarrif);

        if (null != submarkets)
        {
            for (String market : submarkets)
            {
                newMarket.addSubmarket(market);
            }
        }

        for (String condition : marketConditions)
        {
            newMarket.addCondition(condition);
        }

        if (null != connectedEntities)
        {
            for (SectorEntityToken entity : connectedEntities)
            {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        globalEconomy.addMarket(newMarket);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities)
        {
            for (SectorEntityToken entity : connectedEntities)
            {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        return newMarket;
    }
    
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Nikolaev");
        LocationAPI hyper = Global.getSector().getHyperspace();

        system.setBackgroundTextureFilename("graphics/backgrounds/background2.jpg");
        
	// SectorEntityToken nikolaev_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/istl_new_nebula.png", // I have no idea how to make this work properly! Something about the png file? Something about this file?
		// 0, 0, // center of nebula
		// system, // location to add to
		// "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
		// 4, 4, StarAge.YOUNG); // number of cells in texture
                
        // create the star and generate the hyperspace anchor for this system
        PlanetAPI star = system.initStar("nikolaev", // unique id for this star
                "star_yellow", // id in planets.json
                600f, // radius (in pixels at default zoom)
                800, // corona radius, from star edge
                4f, // solar wind burn level
                0.3f, // flare probability
                1.6f); // cr loss mult

        system.setLightColor(new Color(235, 235, 225)); // light color in entire system, affects all entities

        /*
		 * addPlanet() parameters:
		 * 1. Unique id for this planet (or null to have it be autogenerated)
		 * 2. What the planet orbits (orbit is always circular)
		 * 3. Name
		 * 4. Planet type id in planets.json
		 * 5. Starting angle in orbit, i.e. 0 = to the right of the star
		 * 6. Planet radius, pixels at default zoom
		 * 7. Orbit radius, pixels at default zoom
		 * 8. Days it takes to complete an orbit. 1 day = 10 seconds.
         */
        
        
        // Add magnetic fields to Nikolaev.
//        SectorEntityToken nikolaev_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
//			new MagneticFieldTerrainPlugin.MagneticFieldParams(200f, // terrain effect band width 
//			750, // terrain effect middle radius
//			star, // entity that it's around
//			650f, // visual band start
//			850f, // visual band end
//			new Color(50, 30, 100, 60), // base color
//			0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
//			new Color(50, 20, 110, 130),
//			new Color(150, 30, 120, 150), 
//			new Color(200, 50, 130, 190),
//			new Color(250, 70, 150, 240),
//			new Color(200, 80, 130, 255),
//			new Color(75, 0, 160), 
//			new Color(127, 0, 255)
//			));
//	nikolaev_field2.setCircularOrbit(star, 0, 0, 80);
        
        SectorEntityToken nikolaev_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(400f, // terrain effect band width 
			1050, // terrain effect middle radius
			star, // entity that it's around
			850f, // visual band start
			1250f, // visual band end
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
	nikolaev_field1.setCircularOrbit(star, 0, 0, 120);
        
        // Etincelle, a volcanic world too close to Nikolaev to mine safely.
        PlanetAPI etincelle = system.addPlanet("istl_planet_etincelle", star, "Étincelle", "lava", 150, 60, 1400, 45);
        etincelle.setCustomDescriptionId("planet_etincelle");
        
            // Add fixed conditions to Etincelle.
            Misc.initConditionMarket(etincelle);
            etincelle.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
            etincelle.getMarket().addCondition(Conditions.VERY_HOT);
            etincelle.getMarket().addCondition(Conditions.RUINS_SCATTERED);
            etincelle.getMarket().addCondition(Conditions.ORE_ULTRARICH);
            etincelle.getMarket().addCondition(Conditions.RARE_ORE_RICH);
            
        SectorEntityToken etincelle_field = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(60f, // terrain effect band width 
			110, // terrain effect middle radius
			etincelle, // entity that it's around
			80f, // visual band start
			140f, // visual band end
			new Color(50, 30, 100, 30), // base color
			0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
        etincelle_field.setCircularOrbit(etincelle, 0, 0, 100);
        
            // Inner dust belt.
            system.addRingBand(star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 1900, 90f, Terrain.RING, "Nikolaev Dustband");

        // Odessa, home of the hundred-meter talking spacewhale. Try the kelp chips.
        PlanetAPI odessa = system.addPlanet("istl_planet_odessa", star, "Odessa", "water", 270, 90, 2700, 60);
        odessa.setCustomDescriptionId("planet_odessa");
        
                // Add fixed conditions to Odessa.
                Misc.initConditionMarket(odessa);
                odessa.getMarket().addCondition(Conditions.HABITABLE);
                odessa.getMarket().addCondition(Conditions.MILD_CLIMATE);
                odessa.getMarket().addCondition(Conditions.FARMLAND_RICH);
                odessa.getMarket().getFirstCondition(Conditions.FARMLAND_RICH).setSurveyed(true);
                odessa.getMarket().addCondition(Conditions.ORGANICS_COMMON);
                odessa.getMarket().getFirstCondition(Conditions.ORGANICS_COMMON).setSurveyed(false);
                
                // Cousteau Base, a non-econ market experiment.
                SectorEntityToken odessaBase = system.addCustomEntity("istl_odessabase", "Cousteau Base", "station_dme_small", "dassault_mikoyan");
                odessaBase.setCircularOrbitPointingDown(system.getEntityById("istl_planet_odessa"), 75, 180, 36);
                odessaBase.setCustomDescriptionId("station_cousteaubase");
                odessaBase.setInteractionImage("illustrations", "cargo_loading");
                odessaBase.addTag("istl_researchBase");
        
        // Peremohy, where a loathesome Putinoid met his end.
        PlanetAPI peremohy = system.addPlanet("istl_planet_peremohy", star, "Peremohy", "terran-eccentric", 0, 160, 4500, 100);
        peremohy.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "sindria"));
        peremohy.getSpec().setGlowColor(new Color(255, 255, 255, 255));
        peremohy.getSpec().setUseReverseLightForGlow(true);
        peremohy.applySpecChanges();
        peremohy.setInteractionImage("illustrations", "urban00");
        
        // Home sweet home port.
        SectorEntityToken station = system.addCustomEntity("nikolaev_port", "Peremohy Highport", "station_nikolaev_highport", "dassault_mikoyan");
        station.setCircularOrbitPointingDown(system.getEntityById("istl_planet_peremohy"), 45, 420, 45);
        station.setInteractionImage("illustrations", "orbital");

        // add the marketplace to Peremohy and Peremohy Highport.
        MarketAPI peremohyMarket = addMarketplace("dassault_mikoyan", peremohy, new ArrayList<>(Arrays.asList(station)),
                "Peremohy", // name of the market
                6, // size of the market (from the JSON)
                new ArrayList<>(
                        Arrays.asList( // list of market conditions from nikolaev.json
                                Conditions.HABITABLE,
                                Conditions.FARMLAND_ADEQUATE,
                                Conditions.ORE_MODERATE, 
                                Conditions.RARE_ORE_SPARSE, 
                                Conditions.URBANIZED_POLITY,
                                Conditions.REGIONAL_CAPITAL,
                                Conditions.MILITARY_BASE,
                                Conditions.ORBITAL_STATION,
                                Conditions.AUTOFAC_HEAVY_INDUSTRY,
                                Conditions.ORGANICS_COMPLEX,
                                Conditions.TERRAN,
                                Conditions.POPULATION_6)),
                new ArrayList<>(
                        Arrays.asList( // which submarkets to generate
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                0.12f); // tariff amount

        peremohy.setCustomDescriptionId("planet_peremohy");
        station.setCustomDescriptionId("nikolaev_highport");
        
        // Going to try adding the Sixth Bureau fleet script
        system.addScript(new SixthBureauFleetManager(peremohyMarket));

        // Add a jump point to the system.
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("nikolaev_jump_point_alpha", "Nikolaev Passage");
        OrbitAPI orbit = Global.getFactory().createCircularOrbit(peremohy, 0, 500, 30);
        jumpPoint1.setOrbit(orbit);
        jumpPoint1.setRelatedPlanet(peremohy);
        jumpPoint1.setStandardWormholeToHyperspaceVisual();
        jumpPoint1.setCircularOrbit(system.getEntityById("nikolaev"), 60, 3600, 100);
        system.addEntity(jumpPoint1);

        // Add a comm relay to the system.
        SectorEntityToken nikolaev_relay = system.addCustomEntity("nikolaev_relay", // unique id
                "Nikolaev Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "dassault_mikoyan"); // faction
        nikolaev_relay.setCircularOrbitPointingDown(system.getEntityById("nikolaev"), -60, 4500, 100);

        // And now, the outer system.
        system.addRingBand(star, "misc", "rings_dust0", 256f, 2, Color.gray, 256f, 6280, 203f, null, null);
        system.addRingBand(star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 6320, 211f, null, null);
        system.addAsteroidBelt(star, 150, 6300, 170, 200, 250, Terrain.ASTEROID_BELT, "Nikolaev Belt");
        
        system.addRingBand(star, "misc", "rings_ice0", 256f, 3, Color.white, 256f, 6670, 220f, null, null);
        system.addRingBand(star, "misc", "rings_asteroids0", 256f, 2, Color.white, 256f, 6715, 223f, null, null);
        system.addRingBand(star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 6760, 226f, null, null);
        system.addAsteroidBelt(star, 150, 6600, 170, 200, 250, Terrain.ASTEROID_BELT, "Nikolaev Belt");
        system.addAsteroidBelt(star, 100, 6750, 128, 200, 300, Terrain.ASTEROID_BELT, "Nikolaev Belt");

            // Belt debris fields.
            DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                450f, // field radius - should not go above 1000 for performance reasons
                1.2f, // density, visual - affects number of debris pieces
                10000000f, // duration in days 
                0f); // days the field will keep generating glowing pieces
            params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
            params1.baseSalvageXP = 500; // base XP for scavenging in field
            SectorEntityToken debrisBelt1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
            debrisBelt1.setSensorProfile(1000f);
            debrisBelt1.setDiscoverable(true);
            debrisBelt1.setCircularOrbit(star, 120f, 6480, 240f);
            debrisBelt1.setId("nikolaev_debrisBelt1");
                
            DebrisFieldTerrainPlugin.DebrisFieldParams params2 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                300f, // field radius - should not go above 1000 for performance reasons
                1.2f, // density, visual - affects number of debris pieces
                10000000f, // duration in days 
                0f); // days the field will keep generating glowing pieces
            params2.source = DebrisFieldTerrainPlugin.DebrisFieldSource.MIXED;
            params2.baseSalvageXP = 500; // base XP for scavenging in field
            SectorEntityToken debrisBelt2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
            debrisBelt2.setSensorProfile(1000f);
            debrisBelt2.setDiscoverable(true);
            debrisBelt2.setCircularOrbit(star, 360f, 6480, 240f);
            debrisBelt2.setId("nikolaev_debrisBelt2");
                
        // Abandoned Belt Station.
        SectorEntityToken neutralStation = system.addCustomEntity("nikolaev_abandoned_station", "Abandoned Belt Station", "station_dme_belt", "neutral");
        neutralStation.setCircularOrbitWithSpin(system.getEntityById("nikolaev"), 240, 6480, 240, 5, 15);
        neutralStation.setDiscoverable(true);
        neutralStation.setDiscoveryXP(1500f);
        neutralStation.setSensorProfile(0.25f);
        neutralStation.setCustomDescriptionId("nikolaev_beltstation");
        neutralStation.setInteractionImage("illustrations", "abandoned_station2");

        neutralStation.getMemoryWithoutUpdate().set("$abandonedStation", true);
        MarketAPI market = Global.getFactory().createMarket("nikolaev_abandoned_station_market", neutralStation.getName(), 0);
        market.setPrimaryEntity(neutralStation);
        market.setFactionId(neutralStation.getFaction().getId());
        market.addCondition(Conditions.ABANDONED_STATION);
        market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
        ((StoragePlugin) market.getSubmarket(Submarkets.SUBMARKET_STORAGE).getPlugin()).setPlayerPaidToUnlock(true);
        neutralStation.setMarket(market);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("metals", 500);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("rare_metals", 150);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("food", 100);
        neutralStation.getMarket().getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("heavy_machinery", 50);
        
        //Salvage in belt.
        SectorEntityToken scrap1 = DerelictThemeGenerator.addSalvageEntity(system, Entities.EQUIPMENT_CACHE_SMALL, Factions.DERELICT);
        scrap1.setId("nikolaev_scrap1");
	scrap1.setCircularOrbit(star, 165, 6420, 223);
	Misc.setDefenderOverride(scrap1, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
        scrap1.setDiscoverable(Boolean.TRUE);
        
        SectorEntityToken scrap2 = DerelictThemeGenerator.addSalvageEntity(system, Entities.WEAPONS_CACHE_SMALL_REMNANT, Factions.DERELICT);
        scrap2.setId("nikolaev_scrap2");
	scrap2.setCircularOrbit(star, 285, 6420, 223);
	Misc.setDefenderOverride(scrap2, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
        scrap2.setDiscoverable(Boolean.TRUE);
        
        SectorEntityToken scrap3 = DerelictThemeGenerator.addSalvageEntity(system, Entities.WEAPONS_CACHE_REMNANT, Factions.DERELICT);
        scrap3.setId("nikolaev_scrap3");
	scrap3.setCircularOrbit(star, 45, 6480, 223);
	Misc.setDefenderOverride(scrap3, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
        scrap3.setDiscoverable(Boolean.TRUE);
        
	// Nikolaev Gate.
	SectorEntityToken gate = system.addCustomEntity("nikolaev_gate", // unique id
		 "Nikolaev Gate", // name - if null, defaultName from custom_entities.json will be used
		 "inactive_gate", // type of object, defined in custom_entities.json
		 null); // faction

	gate.setCircularOrbit(system.getEntityById("nikolaev"), 180+60, 8000, 400);
        
        // Add a second jump in Stendhal's L3 point. Depreciated because I can't get it to look nice.
        // JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint("nikolaev_jump_point_beta", "Nikolaev Outer Jump-point");
	// jumpPoint2.setCircularOrbit( system.getEntityById("nikolaev"), -30, 10800, 270);
	// jumpPoint2.setRelatedPlanet(null);
	// system.addEntity(jumpPoint2);
        
        // Stendhal system.
        PlanetAPI stendhal = system.addPlanet("istl_planet_stendhal", star, "Stendhal", "gas_giant", 150, 500, 10800, 300);
        stendhal.getSpec().setPlanetColor(new Color(255, 210, 180, 255));
        stendhal.getSpec().setAtmosphereColor(new Color(135, 45, 15, 135));
        stendhal.getSpec().setCloudColor(new Color(215, 215, 200, 200));
        stendhal.getSpec().setIconColor(new Color(155, 125, 75, 255));
        stendhal.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
        stendhal.getSpec().setGlowColor(new Color(135, 45, 15, 135));
        stendhal.getSpec().setUseReverseLightForGlow(true);
        stendhal.getSpec().setAtmosphereThickness(0.6f);
        stendhal.applySpecChanges();
        
        stendhal.setCustomDescriptionId("planet_stendhal");

        SectorEntityToken stendhal_field1 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                new MagneticFieldParams(200f, // terrain effect band width 
                        625, // terrain effect middle radius
                        stendhal, // entity that it's around
                        525f, // visual band start
                        725f, // visual band end
                        new Color(50, 30, 100, 60), // base color
                        1f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                        new Color(50, 20, 110, 135),
                        new Color(150, 30, 120, 150),
                        new Color(200, 50, 130, 190),
                        new Color(250, 70, 150, 240),
                        new Color(200, 80, 130, 255),
                        new Color(75, 0, 160),
                        new Color(127, 0, 255)
                ));
        stendhal_field1.setCircularOrbit(stendhal, 0, 0, 100);
        
        SectorEntityToken stendhal_field2 = system.addTerrain(Terrain.MAGNETIC_FIELD,
                new MagneticFieldParams(400f, // terrain effect band width 
                        900, // terrain effect middle radius
                        stendhal, // entity that it's around
                        700f, // visual band start
                        1100f, // visual band end
                        new Color(50, 30, 100, 30), // base color
                        0.3f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
                        new Color(50, 20, 110, 135),
                        new Color(150, 30, 120, 150),
                        new Color(200, 50, 130, 190),
                        new Color(250, 70, 150, 240),
                        new Color(200, 80, 130, 255),
                        new Color(75, 0, 160),
                        new Color(127, 0, 255)
                ));
        stendhal_field2.setCircularOrbit(stendhal, 0, 0, 100);
        
        // Stendhal Rings
        system.addRingBand(stendhal, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 1520, 70f);
        system.addRingBand(stendhal, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 1500, 80f);
        system.addRingBand(stendhal, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 1445, 90f, Terrain.RING, "Stendhal Ring");

        // Stendhal Moons
        
        // Rouge, an Io-like volcanic hellhole.
        PlanetAPI stendhal1 = system.addPlanet("istl_planet_rouge", stendhal, "Rouge", "lava_minor", 36, 90, 960, 45);
        stendhal1.setCustomDescriptionId("planet_rouge");
        
            // Add fixed conditions to Rouge.
            Misc.initConditionMarket(stendhal1);
            stendhal1.getMarket().addCondition(Conditions.TECTONIC_ACTIVITY);
            stendhal1.getMarket().addCondition(Conditions.ORE_ABUNDANT);
            stendhal1.getMarket().getFirstCondition(Conditions.ORE_ABUNDANT).setSurveyed(true);
            stendhal1.getMarket().addCondition(Conditions.RARE_ORE_SPARSE);
            stendhal1.getMarket().getFirstCondition(Conditions.RARE_ORE_SPARSE).setSurveyed(true);
            
        SectorEntityToken rouge_field = system.addTerrain(Terrain.MAGNETIC_FIELD,
			new MagneticFieldTerrainPlugin.MagneticFieldParams(90f, // terrain effect band width 
			160, // terrain effect middle radius
			stendhal1, // entity that it's around
			120f, // visual band start
			210f, // visual band end
			new Color(50, 30, 100, 60), // base color
			0.6f, // probability to spawn aurora sequence, checked once/day when no aurora in progress
			new Color(50, 20, 110, 130),
			new Color(200, 50, 130, 190),
			new Color(250, 70, 150, 240),
			new Color(200, 80, 130, 255),
			new Color(75, 0, 160), 
			new Color(127, 0, 255)
			));
        rouge_field.setCircularOrbit(stendhal1, 0, 0, 100);
        
        // Noir, a barren shithole of little value.
        PlanetAPI stendhal2 = system.addPlanet("istl_planet_noir", stendhal, "Noir", "barren_castiron", 180, 120, 2100, 80);
        stendhal2.getSpec().setPlanetColor(new Color(220, 245, 255, 255));
        stendhal2.getSpec().setAtmosphereColor(new Color(150, 120, 100, 250));
        stendhal2.getSpec().setCloudColor(new Color(150, 120, 120, 150));
        //stendhal2.getSpec().setTexture(Global.getSettings().getSpriteName("planets", "barren"));
        stendhal2.setCustomDescriptionId("planet_noir");
        
            // Add fixed conditions to Noir.
            Misc.initConditionMarket(stendhal2);
            stendhal2.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
            stendhal2.getMarket().addCondition(Conditions.LOW_GRAVITY);
            stendhal2.getMarket().addCondition(Conditions.VOLATILES_DIFFUSE);
            stendhal2.getMarket().getFirstCondition(Conditions.VOLATILES_DIFFUSE).setSurveyed(true);

        // Stendhal tethers 
        SectorEntityToken stendhal_tether1 = system.addCustomEntity("stendhal_tether1", "Power Tether", "station_dme_tether", "dassault_mikoyan");
        stendhal_tether1.setCircularOrbitPointingDown(stendhal, 0, 720, 30);
        stendhal_tether1.setCustomDescriptionId("istl_powertether");

        SectorEntityToken stendhal_tether2 = system.addCustomEntity("stendhal_tether2", "Power Tether", "station_dme_tether", "dassault_mikoyan");
        stendhal_tether2.setCircularOrbitPointingDown(stendhal, 72, 720, 30);
        stendhal_tether2.setCustomDescriptionId("istl_powertether");

        SectorEntityToken stendhal_tether3 = system.addCustomEntity("stendhal_tether3", "Power Tether", "station_dme_tether", "dassault_mikoyan");
        stendhal_tether3.setCircularOrbitPointingDown(stendhal, 144, 720, 30);
        stendhal_tether3.setCustomDescriptionId("istl_powertether");

        SectorEntityToken stendhal_tether4 = system.addCustomEntity("stendhal_tether4", "Power Tether", "station_dme_tether", "dassault_mikoyan");
        stendhal_tether4.setCircularOrbitPointingDown(stendhal, 216, 720, 30);
        stendhal_tether4.setCustomDescriptionId("istl_powertether");

        SectorEntityToken stendhal_tether5 = system.addCustomEntity("stendhal_tether5", "Power Tether", "station_dme_tether", "dassault_mikoyan");
        stendhal_tether5.setCircularOrbitPointingDown(stendhal, 288, 720, 30);
        stendhal_tether5.setCustomDescriptionId("istl_powertether");

        // Lavoisier Base
        SectorEntityToken labStation = system.addCustomEntity("nikolaev_lab", "Lavoisier Base", "station_dme_lab", "dassault_mikoyan");
        labStation.setCircularOrbitPointingDown(system.getEntityById("istl_planet_noir"), 30, 300, 30);

        // add the marketplace to Lavoisier Base ---------------
        MarketAPI lavoisierMarket = addMarketplace("dassault_mikoyan", labStation, null,
                "Lavoisier Base", // name of the market
                4, // size of the market (from the JSON)
                new ArrayList<>(
                        Arrays.asList( // list of market conditions from nikolaev.json
                                Conditions.FREE_PORT,
                                Conditions.LIGHT_INDUSTRIAL_COMPLEX,
                                Conditions.OUTPOST,
                                Conditions.ORBITAL_STATION,
                                Conditions.POPULATION_4)),
                new ArrayList<>(
                        Arrays.asList( // which submarkets to generate
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                0.12f); // tariff amount

        labStation.setCustomDescriptionId("lavoisier_base");
        
        // Stendhal Trojans
        SectorEntityToken stendhalL4 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        840f, // min radius
                        1080f, // max radius
                        35, // min asteroid count
                        64, // max asteroid count
                        7f, // min asteroid radius 
                        21f, // max asteroid radius
                        "Stendhal L4 Shoal Zone")); // null for default name
        
        // Juliette, debris and wrecks in Stendhal L4
        PlanetAPI juliette = system.addPlanet("istl_planet_juliette", star, "Juliette", "barren-bombarded", 210, 40, 10850, 300);
        juliette.setCustomDescriptionId("planet_juliette");
        
            // Add fixed conditions to Juliette.
            Misc.initConditionMarket(juliette);
            juliette.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
            juliette.getMarket().addCondition(Conditions.LOW_GRAVITY);
            juliette.getMarket().addCondition(Conditions.METEOR_IMPACTS);
            juliette.getMarket().addCondition(Conditions.VOLATILES_TRACE);
            
            //addDerelict(system, juliette, "mercury_Standard", ShipRecoverySpecial.ShipCondition.BATTERED, 180f, true);
            //addDerelict(system, juliette, "istl_samoyed_std", ShipRecoverySpecial.ShipCondition.AVERAGE, 210f, true);
            //addDerelict(system, juliette, "kite_Interceptor", ShipRecoverySpecial.ShipCondition.BATTERED, 300f, false);
            //addDerelict(system, juliette, "istl_tereshkova_std", ShipRecoverySpecial.ShipCondition.BATTERED, 360f, false);
            //addDerelict(system, juliette, "istl_sevastopol_support", ShipRecoverySpecial.ShipCondition.BATTERED, 480f, true);
            
            SectorEntityToken scrap4 = DerelictThemeGenerator.addSalvageEntity(system, Entities.SUPPLY_CACHE, Factions.DERELICT);
            scrap4.setId("nikolaev_scrap4");
            scrap4.setCircularOrbit(juliette, 105, 240, 135);
            Misc.setDefenderOverride(scrap4, new DefenderDataOverride(Factions.DERELICT, 0, 0, 0));
            scrap4.setDiscoverable(Boolean.TRUE);
            
            DebrisFieldTerrainPlugin.DebrisFieldParams params3 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                700f, // field radius - should not go above 1000 for performance reasons
                1.0f, // density, visual - affects number of debris pieces
		10000000f, // duration in days 
		0f); // days the field will keep generating glowing pieces
            params3.source = DebrisFieldTerrainPlugin.DebrisFieldSource.SALVAGE;
            params3.baseSalvageXP = 500; // base XP for scavenging in field
            SectorEntityToken debrisL4 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
            debrisL4.setSensorProfile(600f);
            debrisL4.setDiscoverable(true);
            debrisL4.setCircularOrbit(star, 210f, 10800, 300f);
            debrisL4.setId("nikolaev_debrisL4");
            
            

        SectorEntityToken stendhalL5 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldParams(
                        840f, // min radius
                        1080f, // max radius
                        35, // min asteroid count
                        64, // max asteroid count
                        7f, // min asteroid radius 
                        21f, // max asteroid radius
                        "Stendhal L5 Shoal Zone")); // null for default name

        stendhalL4.setCircularOrbit(star, 210f, 10800, 300);
        stendhalL5.setCircularOrbit(star, 90f, 10800, 300);
        
        // Brightheaven (Stendhal L5 Port)
        SectorEntityToken pirStation = system.addCustomEntity("brightheaven", "Brightheaven", "station_sporeship_derelict", "pirates");
        pirStation.setCircularOrbitWithSpin(system.getEntityById("nikolaev"), 90, 10850, 300, 7, 21);

        // add the marketplace to Brightheaven ---------------
        MarketAPI pirbaseMarket = addMarketplace("pirates", pirStation, null,
                "Brightheaven", // name of the market
                4, // size of the market (from the JSON)
                new ArrayList<>(
                        Arrays.asList( // list of market conditions from nikolaev.json
                                Conditions.FREE_PORT,
                                Conditions.STEALTH_MINEFIELDS,
                                Conditions.ORGANIZED_CRIME,
                                Conditions.ORBITAL_STATION,
                                Conditions.POPULATION_4)),
                new ArrayList<>(
                        Arrays.asList( // which submarkets to generate
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                0.3f); // tariff amount

        pirStation.setCustomDescriptionId("station_brightheaven");
        
        // Outer belt.
        system.addRingBand(star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 15200, 450f, Terrain.RING, "Nikolaev Outer Band");

        system.addRingBand(star, "misc", "rings_dust0", 256f, 2, Color.gray, 256f, 15580, 453f, null, null);
        system.addRingBand(star, "misc", "rings_ice0", 256f, 2, Color.white, 256f, 15620, 461f, null, null);
        system.addAsteroidBelt(star, 150, 15600, 170, 200, 520, Terrain.ASTEROID_BELT, "Nikolaev Outer Belt");
        
        //procgen body in outer system.
        float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, star, StarAge.AVERAGE,
                1, 2, // min/max entities to add
                16800, // radius to start adding at 
                5, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true); // whether to use custom or system-name based names
        
//		system.addOrbitalJunk(peremohy,
//				 "orbital_junk", // from custom_entities.json
//				 8, // num of junk
//				 4, 12, // min/max sprite size (assumes square)
//				 200, // orbit radius
//				 50, // orbit width
//				 10, // min orbit days
//				 20, // max orbit days
//				 60f, // min spin (degress/day)
//				 360f); // max spin (degrees/day)

        // example of using custom visuals below
//		peremohy.setCustomInteractionDialogImageVisual(new InteractionDialogImageVisual("illustrations", "hull_breach", 800, 800));
//		jumpPoint.setCustomInteractionDialogImageVisual(new InteractionDialogImageVisual("illustrations", "space_wreckage", 1200, 1200));
//		station.setCustomInteractionDialogImageVisual(new InteractionDialogImageVisual("illustrations", "cargo_loading", 1200, 1200));
        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

        // Commenting this out until I can hack on it for Sixth Bureau shenanigans.
        // /*
		// DiktatPatrolSpawnPoint patrolSpawn = new DiktatPatrolSpawnPoint(sector, system, 5, 3, peremohy);
		// system.addScript(patrolSpawn);
		// for (int i = 0; i < 5; i++)
		// 	patrolSpawn.spawnFleet();

		// DiktatGarrisonSpawnPoint garrisonSpawn = new DiktatGarrisonSpawnPoint(sector, system, 30, 1, peremohy, peremohy);
		// system.addScript(garrisonSpawn);
		// garrisonSpawn.spawnFleet();


		// system.addScript(new IndependentTraderSpawnPoint(sector, hyper, 1, 10, hyper.createToken(-6000, 2000), station));
        //  */
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

    // private void initStationCargo(SectorEntityToken station) // Bet I can just remove this
    // {
        // CargoAPI cargo = station.getCargo();
        // addRandomWeapons(cargo, 5);

        // cargo.addCrew(1800);
        // cargo.addMarines(80);
        // cargo.addSupplies(48000);
        // cargo.addFuel(24000);

        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_baikal_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_leyte_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_leyte_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_mindanao_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_lodestar_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_centaur_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_kormoran_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_carabao_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_carabao_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_samoyed_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_samoyed_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_sevastopol_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_sevastopol_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_tereshkova_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_starsylph_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_naja_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_stoat_Hull"));
        // cargo.getMothballedShips().addFleetMember(Global.getFactory().createFleetMember(FleetMemberType.SHIP, "istl_stoat_Hull"));
    // }

    // private void addRandomWeapons(CargoAPI cargo, int count)
    // {
        // List weaponIds = Global.getSector().getAllWeaponIds();
        // for (int i = 0; i < count; i++)
        // {
            // String weaponId = (String) weaponIds.get((int) (weaponIds.size() * Math.random()));
            // int quantity = (int) (Math.random() * 4f + 2f);
            // cargo.addWeapons(weaponId, quantity);
        // }
    // }

     // rivate void addDerelict(StarSystemAPI system, PlanetAPI juliette, String wolf_Assault, ShipRecoverySpecial.ShipCondition shipCondition, float f, boolean b) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    // }

}
