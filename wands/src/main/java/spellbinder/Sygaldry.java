package spellbinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enchiridion.Enchiridion;
import enchiridion.Enchiridion.TOME_OF_SPELLS;
import enchiridion.Incantation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Sygaldry {

	private static final Logger LOGGER = LogManager.getLogger();

	public static boolean doesSpellExist(String incName) {
		for (TOME_OF_SPELLS spell : TOME_OF_SPELLS.values()) {
			if (spell.toString().toUpperCase().equals(incName)) {
				return true;
			}
		}
		LOGGER.info(">>> SPELL " + incName + " DOES NOT EXIST");
		return false;
	}

	public static void inscribeSpell(PlayerEntity wizard, Entity mark, MarkType.MARK_TYPE type, Item wand,
			String incantationName, BlockPos position, World world) {
		
		
		Incantation inc = Enchiridion.createIncantation(wizard, mark, type, wand, incantationName, position, world);
		String message = String.format(
				"THE WIZARD %s CHANTED THE INCANTATION OF %s USING A WAND OF %s on a %s, AT POSITION %s",
				inc.getWizardName(), inc.getIncantationName(), inc.getWand().toString(), inc.getType().toString(),
				EnchantedBlocks.stringifyPosition(inc.getPosition()));
		LOGGER.info(">>> " + message);

		inc.chant();
	}

	public static void blotSpell(PlayerEntity wizard, String incantation) {

	}

}
