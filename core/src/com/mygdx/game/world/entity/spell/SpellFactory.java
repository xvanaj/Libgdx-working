package com.mygdx.game.world.entity.spell;

import com.mygdx.game.world.enums.spell.SpellTemplate;
import com.mygdx.game.utils.Randomizer;

public class SpellFactory {

    final static String icons = "BTNInferno\n" +
            "BTNAquaSpike\n" +
            "BTNThornSnare\n" +
            "BTNDemonForm\n" +
            "BTNManaBuff\n" +
            "BTNIceShower\n" +
            "BTNStaticShock\n" +
            "BTNThornTrap\n" +
            "BTNPIncantateGhost\n" +
            "BTNShadowRaze\n" +
            "BTNVolatileWaters\n" +
            "BTNNecromastery\n" +
            "BTNheart\n" +
            "BTNMagicRune\n" +
            "BTNArrowShower\n" +
            "BTNLightningStorm\n" +
            "BTNPunish\n" +
            "BTNMiasma\n" +
            "BTNsaveme\n" +
            "BTNLightning\n" +
            "BTNLiWarlockChannel\n" +
            "BTNFireStorm\n" +
            "BTNTrollRegen\n" +
            "BTNBearForm\n" +
            "BTNAcidShower\n" +
            "BTNPresenceoftheDarklord\n" +
            "BTNWarlockBolt\n" +
            "BTNLiquidFireShower\n" +
            "BTNNecromanticAura\n" +
            "BTNFallingStars\n" +
            "BTNFrostClash\n" +
            "BTNPheonixcall\n" +
            "BTNRockOn\n" +
            "BTNNecromancy2\n" +
            "BTNLiDemonicSummon\n" +
            "BTNMutant\n" +
            "BTNNecroMark\n" +
            "BTNNecroPowahS2\n" +
            "BTNDemonicShower\n" +
            "BTNNecroShackle2\n" +
            "BTNAnathema\n" +
            "BTNHolyCall\n" +
            "BTNAuraIdiocracy\n" +
            "BTNVoidShower\n" +
            "BTNSanctuary2\n" +
            "BTNProtection\n" +
            "BTNThornEnsnare\n" +
            "BTNFreeze\n" +
            "BTNHauntingWave\n" +
            "BTNNecroPowah\n" +
            "BTNRainFall\n" +
            "BTNRequiemofSouls\n" +
            "BTNNecromancy";
    
    public static Spell generateRandomSpell(){
        final SpellTemplate spellTemplate = Randomizer.get(SpellTemplate.values());

        Spell spell = new Spell(spellTemplate, spellTemplate.name(), Randomizer.get(icons.split("\n")));

        return spell;
    }

}
