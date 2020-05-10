package enchiridion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spellbinder.MarkType;

public class SustainableIncantation extends Incantation{

	public SustainableIncantation(PlayerEntity wizard, Entity mark, MarkType.MARK_TYPE type, Item wand, String incantationName, BlockPos position, World world) {
		super(wizard, mark, type, wand, incantationName, position, world);
	}
	
	public boolean isSustainable() {
		return true; // sustain-able subclasses override this 
	}

}
