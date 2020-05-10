package enchiridion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spellbinder.MarkType;

public class Enchiridion {

	public static enum TOME_OF_SPELLS {
		MIDAS
	}
	
	public static Incantation createIncantation(PlayerEntity wizard, Entity mark, MarkType.MARK_TYPE type, Item wand, String incName, BlockPos position, World world) {

		switch(incName) {
		case "MIDAS":
			return new MidasIncantation(wizard, mark, type, wand, incName, position, world);
		default:
			return null;
		}
	}

}
