package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;

import data.scripts.world.systems.Nikolaev;
import data.scripts.world.systems.Martinique;
import data.scripts.world.systems.Kostroma;
import data.scripts.world.systems.Besson;
import data.scripts.world.systems.Aleph;
import data.scripts.world.systems.Dalet;
import data.scripts.world.systems.Kaf;
import data.scripts.world.systems.Yod;


public class ISTLGen implements SectorGeneratorPlugin
{

    @Override
    public void generate(SectorAPI sector)
    {
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("dassault_mikoyan");
        initFactionRelationships(sector);
        
        new Nikolaev().generate(sector);
        new Martinique().generate(sector);
        new Kostroma().generate(sector);
        new Besson().generate(sector);
        new Aleph().generate(sector);
        new Dalet().generate(sector);
        new Kaf().generate(sector);
        new Yod().generate(sector);
    }

    public static void initFactionRelationships(SectorAPI sector)
    {
        FactionAPI dassault = sector.getFaction("dassault_mikoyan");
        FactionAPI dassault2 = sector.getFaction("6eme_bureau");
        FactionAPI breakerdeserter = sector.getFaction("the_deserter");
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI remnants = sector.getFaction(Factions.REMNANTS);
        
        //FactionAPI sra = sector.getFaction("shadow_industry");
        //FactionAPI birdy = sector.getFaction("blackrock_driveyards");
        //FactionAPI thi = sector.getFaction("tiandong");


        dassault.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
        dassault.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);
        dassault.setRelationship(pirates.getId(), RepLevel.VENGEFUL);
        dassault.setRelationship(independent.getId(), RepLevel.FAVORABLE);
        dassault.setRelationship(kol.getId(), RepLevel.HOSTILE);
        dassault.setRelationship(church.getId(), RepLevel.HOSTILE);
        dassault.setRelationship(path.getId(), RepLevel.VENGEFUL);
        dassault.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        dassault.setRelationship(league.getId(), RepLevel.WELCOMING);
        dassault.setRelationship(remnants.getId(), RepLevel.HOSTILE);
        
        dassault.setRelationship("blade_breakers", RepLevel.HOSTILE);
        dassault.setRelationship("shadow_industry", RepLevel.FAVORABLE);
        dassault.setRelationship("blackrock_driveyards", RepLevel.SUSPICIOUS);
        dassault.setRelationship("tiandong", RepLevel.WELCOMING);
        dassault.setRelationship("diableavionics", RepLevel.HOSTILE);
        dassault.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
        
        dassault2.setRelationship(dassault.getId(), RepLevel.COOPERATIVE);
        dassault2.setRelationship(path.getId(), RepLevel.VENGEFUL);
        dassault2.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        dassault2.setRelationship(remnants.getId(), RepLevel.VENGEFUL);
        
        dassault2.setRelationship("blade_breakers", RepLevel.VENGEFUL);
        dassault2.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
        
        breakerdeserter.setRelationship(dassault.getId(), RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship(dassault2.getId(), RepLevel.VENGEFUL);
        breakerdeserter.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship(tritachyon.getId(), RepLevel.HOSTILE);
        breakerdeserter.setRelationship(pirates.getId(), RepLevel.VENGEFUL);
        breakerdeserter.setRelationship(independent.getId(), RepLevel.SUSPICIOUS);
        breakerdeserter.setRelationship(kol.getId(), RepLevel.HOSTILE);
        breakerdeserter.setRelationship(church.getId(), RepLevel.HOSTILE);
        breakerdeserter.setRelationship(path.getId(), RepLevel.VENGEFUL);
        breakerdeserter.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        breakerdeserter.setRelationship(league.getId(), RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship(remnants.getId(), RepLevel.VENGEFUL);
        
        breakerdeserter.setRelationship("blade_breakers", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("shadow_industry", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("blackrock_driveyards", RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship("tiandong", RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship("diableavionics", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("ORA", RepLevel.SUSPICIOUS);
        breakerdeserter.setRelationship("SCY", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("neutrinocorp", RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship("interstellarimperium", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("syndicate_asp", RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship("pack", RepLevel.INHOSPITABLE);
        breakerdeserter.setRelationship("junk_pirates", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("fob", RepLevel.HOSTILE);
        breakerdeserter.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
    }
}
