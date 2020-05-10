package enchiridion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import spellbinder.MarkType;

public class Incantation {
	
	PlayerEntity wizard;
	Entity mark;
	MarkType.MARK_TYPE type;
	Item wand;
	String incantationName;
	BlockPos position;
	World world;
	
	public Incantation(PlayerEntity wizard, Entity mark, MarkType.MARK_TYPE type, Item wand, String incantationName,
			BlockPos position, World world) {
		this.wizard = wizard;
		this.mark = mark;
		this.type = type;
		this.wand = wand;
		this.incantationName = incantationName;
		this.position = position;
		this.world = world;
	}
	
	public Entity getMark() {
		return this.mark;
	}

	public MarkType.MARK_TYPE getType() {
		return this.type;
	}

	public Item getWand() {
		return this.wand;
	}

	public String getIncantationName() {
		return this.incantationName;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public PlayerEntity getWizard() {
		return this.wizard;
	}
	
	public String getWizardName() {
		return this.getWizard().getName().getString();
	}
	
	public boolean isSustainable() {
		return false; // sustain-able subclasses override this 
	}
	
	public void chant() {
		System.out.println("INCANTATION CHANT");
	}
	
	public void halt() {
		System.out.println("INCANTATION HALT");
	}

}
