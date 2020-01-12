package com.mygdx.game.world.entity.civilization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PoliticalStructure {

    ANARCHY("","Society without formal government"),
    ARISTOCRACY("Aristocrat","Government by a class of people who have attained power due to birth or wealth. Aristocrats are people who have social position as well as political power. Autocracy"),
    CLAN("Clan leader","See Tribal hereafter."),
    COLONIAL("","Government set up to rule a foreign territory by a “parent” state. Though these governments may reflect the parent government, they usually assume some form of oligarchy or dictatorship."),
    CONFEDERACY("","These refer to the grouping of two or more states with legitimate governments under a singular leader or governing concept."),
    DEMOCRACY("President","Government whereby all eligible citizens gather on common grounds to vote on matters of state, finance, etc."),
    DESPOTISM("","A form of Autocracy whereby an individual has seized power illegitimately."),
    DICTATORSHIP("Dictator","A form of Autocracy whereby an individual has seized power illegitimately."),
    FEUDAL("","There are no feudal governments but rather a society or region wherein feudalism is the dominate social, political, economic and military organization. It implies a complicated arrangement that governs the personal relations of lords, vassals and peasants. It entails a plethora of rights and duties between “superiors” and “inferiors,” where social rank is determined by one’s land rights, and military service and duty to the lord’s demesne replaced monetary payments. In theory it is a simple form of government whereby the one who owns land farms sections of his land out to others for payment in services. However, feudal societies are generally horribly entangled in hereditary rights, land ownership, war, vassalage to two or more lords and so forth."),
    MAGOCRACY("High mage","Government my those able to employ magic, a form of Aristocracy."),
    MANORIALISM("","An outcropping of feudalism, which concerns the local feudal arrangements, generally around a town, village, monastery, manor or any other small form of settlement which possesses a lord. The economic arrangement between the lord and vassals is not a military one but rather one of service only. Peasants are required to tend the lords crops, bring wood etc for his protection and for their right to live on the land. As with feudalsim, manorialism becomes complicated with hereditary rights, ownership, etc."),
    MATRIARCHY("","Generally speaking this is an oligarchy whereby females rule the state. "),
    MONARCHY("","A form of government whereby rule is held by one man or woman, a hereditary or elected king or emperor. Generally associated with autocracy, however, monarchial governments often share power with oligarches, aristocrats and the people."),
    OLIGARCHY("Oligarch","The rule by a group of persons, families or commercial groups. Generally these achieve power through heredity or wealth."),
    PATRIARCHY("","Generally speaking this is an oligarchy whereby males rule the state."),
    PLUTOCRACY("","When a state’s wealth is concentrated in the hands of a few and these few form an Oligarch, their rule is generally referred to as a plutocracy."),
    REPUBLIC("President","A form of democratic rule, whereby the people lay aside direct rule and empower chosen or elected representative citizens to rule."),
    THEOCRACY("Hight priest","Government by a priesthood, a form of oligarchy, whereby the church or religious leaders control the state."),
    TRIBAL("Chieftain","Government of primitive sort acting under a chief. These range widely from the autocratic to democratic."),
;

    private String title;
    private String description;

}
