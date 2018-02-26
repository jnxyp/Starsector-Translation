package data.campaign.fleets;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory.PatrolType;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV2;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParams;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAI;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolFleetManager.PatrolFleetData;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.FleetTypes;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.WeightedRandomPicker;

public class UnSecFleetManager extends BaseCampaignEventListener implements EveryFrameScript {

	public static Logger log = Global.getLogger(UnSecFleetManager.class);
	
	
	private MarketAPI market;
	private List<PatrolFleetData> activePatrols = new ArrayList<PatrolFleetData>();
	private IntervalUtil tracker;
	private int maxPatrols;
	
	public UnSecFleetManager(MarketAPI market) {
		super(true);
		this.market = market;
		
		float interval = Global.getSettings().getFloat("averagePatrolSpawnInterval");
		tracker = new IntervalUtil(interval * 0.75f, interval * 1.25f);
	}
	
	
	public void advance(float amount) {
		float days = Global.getSector().getClock().convertToDays(amount);
		tracker.advance(days);
		if (!tracker.intervalElapsed()) return;
		
		if (market.hasCondition(Conditions.DECIVILIZED)) return;
		
		List<PatrolFleetData> remove = new ArrayList<PatrolFleetData>();
		for (PatrolFleetData data : activePatrols) {
			if (data.fleet.getContainingLocation() == null ||
				!data.fleet.getContainingLocation().getFleets().contains(data.fleet)) {
				remove.add(data);
				log.info("Cleaning up orphaned patrol [" + data.fleet.getNameWithFaction() + "] for market [" + market.getName() + "]");
			}
		}
		activePatrols.removeAll(remove);
		
		maxPatrols = 5;
		
		if (activePatrols.size() < maxPatrols) {
			WeightedRandomPicker<PatrolType> picker = new WeightedRandomPicker<PatrolType>();
			picker.add(PatrolType.FAST, 
					Math.max(1, maxPatrols - getCount(PatrolType.COMBAT, PatrolType.HEAVY)));
			picker.add(PatrolType.COMBAT, 
					Math.max(1, maxPatrols - getCount(PatrolType.FAST, PatrolType.HEAVY) + market.getSize()));
			picker.add(PatrolType.HEAVY, 
						Math.max(1, maxPatrols - getCount(PatrolType.FAST, PatrolType.COMBAT) + market.getSize() - 5));
			
			PatrolType type = picker.pick();
			//FactionAPI faction = Global.getSector().getFaction(Factions.LIONS_GUARD);
			
			
			float combat = 0f;
			float tanker = 0f;
			float freighter = 0f;
			String fleetType = FleetTypes.PATROL_SMALL;
			switch (type) {
			case FAST:
				fleetType = FleetTypes.PATROL_SMALL;
				combat = Math.round(3f + (float) Math.random() * 2f);
				break;
			case COMBAT:
				fleetType = FleetTypes.PATROL_MEDIUM;
				combat = Math.round(6f + (float) Math.random() * 3f);
				tanker = Math.round((float) Math.random());
				break;
			case HEAVY:
				fleetType = FleetTypes.PATROL_LARGE;
				combat = Math.round(10f + (float) Math.random() * 5f);
				tanker = 1f;
				freighter = 1f;
				break;
			}
			combat *= 1f + (market.getStabilityValue() / 18f);

			CampaignFleetAPI fleet = FleetFactoryV2.createFleet(new FleetParams(
					null,
					market, 
					"united_security",
					null, // fleet's faction, if different from above, which is also used for source market picking
					fleetType,
					combat, // combatPts
					freighter, // freighterPts 
					tanker, // tankerPts
					0f, // transportPts
					0f, // linerPts
					0f, // civilianPts 
					0f, // utilityPts
					0.75f, // qualityBonus
					-1f, // qualityOverride
					1f, // officer num mult - faction already has bonus
					0 // officer level bonus - faction already has bonus
					));
			if (fleet == null) return;
			
			//CampaignFleetAPI fleet = FleetFactory.createPatrol(type, faction, 1f, 1f, market);
			
			fleet.setNoFactionInName(false);
			//fleet.setFaction("united_security", true);
			
			SectorEntityToken entity = market.getPrimaryEntity();
			entity.getContainingLocation().addEntity(fleet);
			fleet.setLocation(entity.getLocation().x, entity.getLocation().y);
			
			PatrolFleetData data = new PatrolFleetData(fleet, type);
			data.startingFleetPoints = fleet.getFleetPoints();
			data.sourceMarket = market;
			activePatrols.add(data);
			
			PatrolAssignmentAI ai = new PatrolAssignmentAI(fleet, data);
			fleet.addScript(ai);
			
			fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PATROL_FLEET, true);

//			if ((type == PatrolType.FAST && (float) Math.random() > 0.25f) ||
//					(type == PatrolType.COMBAT && (float) Math.random() > 0.5f)) {
			
			if (type == PatrolType.FAST || type == PatrolType.COMBAT) {
				fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_CUSTOMS_INSPECTOR, true);
			}
			log.info("Spawned United Securities patrol [" + fleet.getNameWithFaction() + "] from market " + market.getName());
		}
	}
	
	private int getCount(PatrolType ... types) {
		int count = 0;
		for (PatrolType type : types) {
			for (PatrolFleetData data : activePatrols) {
				if (data.type == type) count++;
			}
		}
		return count;
	}

	public boolean isDone() {
		return false;
	}

	public boolean runWhilePaused() {
		return false;
	}

	@Override
	public void reportFleetDespawned(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
		super.reportFleetDespawned(fleet, reason, param);
		
		for (PatrolFleetData data : activePatrols) {
			if (data.fleet == fleet) {
				activePatrols.remove(data);
				break;
			}
		}
	}

	
	


}














