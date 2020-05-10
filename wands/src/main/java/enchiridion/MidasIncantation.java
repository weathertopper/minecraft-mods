package enchiridion;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spellbinder.MarkType.MARK_TYPE;

public class MidasIncantation extends Incantation {
	
	BlockState GOLD_BLOCK = Blocks.GOLD_BLOCK.getDefaultState();

	public MidasIncantation(PlayerEntity wizard, Entity mark, MARK_TYPE type, Item wand, String incantationName, BlockPos position, World world) {
		super(wizard, mark, type, wand, incantationName, position, world);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void chant() {
		// all subclasses override this
		if (type == MARK_TYPE.WORLD || type == MARK_TYPE.CREATURE) {
			this.world.setBlockState(this.position, this.GOLD_BLOCK);
		}
	}

}
