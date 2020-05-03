package com.example.examplemod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class DRIVER {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public DRIVER() {
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	public enum TARGET_TYPES {
		LIVING_THING, BLOCK, AIR, IGNORE
	}

	public TARGET_TYPES getTargetType(LivingEvent event) {

		if (event instanceof LivingHurtEvent) {
			if (((LivingHurtEvent) event).getSource().getImmediateSource() instanceof PlayerEntity) {
				return TARGET_TYPES.LIVING_THING;
			}
		} else if (event instanceof PlayerInteractEvent.LeftClickBlock) {
			Hand eventHand = ((PlayerInteractEvent.LeftClickBlock) event).getHand();
			if (eventHand == Hand.MAIN_HAND) {
				return TARGET_TYPES.BLOCK;
			}
		} else if (event instanceof PlayerInteractEvent.LeftClickEmpty) {
			return TARGET_TYPES.AIR;
		}
		return TARGET_TYPES.IGNORE;
	}

	static ArrayList<Pair<String, Long>> LOCKED_BLOCKS = new ArrayList<Pair<String,Long>>();
	
	static Long LOCK_BLOCKS_EPSILON = new Long(5);

	public Pair<String, Long> createLockBlockPair(BlockPos pos, Long time) {
		return new Pair<String, Long> (getPositionString(pos), time); 
	}
	
	public String getPositionString(BlockPos pos) {
		return String.format("(%o,%o,%o)", pos.getX(), pos.getY(), pos.getZ());
	}

	public boolean isBlockLocked(BlockPos pos) {
		for (int i=LOCKED_BLOCKS.size()-1; i >= 0 ; i--){
//			LOGGER.info(">>> LOCKED_BLOCKS INCLUDES " + LOCKED_BLOCKS.get(i).toString() );
			if (getPositionString(pos).equals(LOCKED_BLOCKS.get(i).getFirst())) {
//				LOGGER.info(">>> BLOCK AT " + getPositionString(pos)  + " IS LOCKED");
				return true;
			}
		}
//		LOGGER.info(">>> BLOCK AT " + getPositionString(pos)  + " IS NOT LOCKED");
		return false;
	}

	public void lockBlock(BlockPos pos, Long time) {
		LOCKED_BLOCKS.add(createLockBlockPair(pos, time));
	}

	public void unlockBlock(BlockPos pos, Long time) {
//		LOGGER.info(">>> UNLOCKING BLOCK " + getPositionString(pos) + " AT TIME " + time);
		for (int i=LOCKED_BLOCKS.size()-1; i >= 0 ; i--){
			if (LOCKED_BLOCKS.get(i).getFirst().equals(getPositionString(pos))) {
//				LOGGER.info(">>> UNLOCKING BLOCK " + getPositionString(pos) + " AT TIME " + time + " SUCCEEDED");
				LOCKED_BLOCKS.remove(i);
			}
		}
	}
	
	public void pruneLockedBlocks(Long currentTime) {
		for (int i=LOCKED_BLOCKS.size()-1; i >= 0 ; i--){
			if (currentTime > LOCKED_BLOCKS.get(i).getSecond() + LOCK_BLOCKS_EPSILON) {
//				LOGGER.info(">>> PRUNED LOCKED BLOCK PAIR" + LOCKED_BLOCKS.get(i).toString() + " AT " + currentTime);
				LOCKED_BLOCKS.remove(i);
			}
		}
	}

	public void emptyLockedBlocks() {
		LOCKED_BLOCKS = new ArrayList<Pair<String, Long>>();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void hearSpell(LivingEvent event) {
		try {
			PlayerEntity WIZARD = null;
			Entity TARGET = null;
			ItemStack WAND = null;
			String WAND_NAME = null;
			BlockPos POS = null;
			
			TARGET_TYPES TYPE = getTargetType(event);
			switch (TYPE) {
			case IGNORE:
				return;
			case LIVING_THING:
				LivingHurtEvent LHE = (LivingHurtEvent) event;
				WIZARD = (PlayerEntity) LHE.getSource().getImmediateSource();
				TARGET = LHE.getEntity();
				WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
				WAND_NAME = WAND.getDisplayName().getString();
				POS = TARGET.getPosition();
				break;
			case BLOCK:
				PlayerInteractEvent.LeftClickBlock LCB = (PlayerInteractEvent.LeftClickBlock) event;
				WIZARD = (PlayerEntity) LCB.getPlayer();
				TARGET = LCB.getEntity();
				WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
				WAND_NAME = WAND.getDisplayName().getString();
				POS = LCB.getPos();
				break;
			case AIR: // POSITION IS WIZARD'S
				PlayerInteractEvent.LeftClickEmpty LCE = (PlayerInteractEvent.LeftClickEmpty) event;
				WIZARD = (PlayerEntity) LCE.getPlayer();
				TARGET = LCE.getEntity();
				WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
				WAND_NAME = WAND.getDisplayName().getString();
				POS = TARGET.getPosition();
				break;
			}

			if (event.isCancelable()) {
				event.setCanceled(true);
			}
			
			Long TIME = TARGET.getEntityWorld().getGameTime();
			pruneLockedBlocks(TIME);
			if (isBlockLocked(POS)) {
//				LOGGER.info(">>> STRING POSITION ", getPositionString(POS), " IS LOCKED! SKIP!");
				return;
			}

			lockBlock(POS, TIME);

			castSpell(WIZARD, TARGET, TYPE, WAND, WAND_NAME, POS);
			LOGGER.info("");

		} catch (Exception ex) {
			emptyLockedBlocks();
			LOGGER.error("WHOOPS hearSpell " + ex.getMessage(), ex);
		}

	}

	public void castSpell(PlayerEntity WIZARD, Entity TARGET, TARGET_TYPES TYPE, ItemStack WAND, String WAND_NAME,
			BlockPos POS) {
		try {
			LOGGER.info(">>> THE WIZARD <<" + WIZARD.getName().getString() + ">> CAST A SPELL USING A WAND OF <<"
					+ WAND.getItem().toString() + ">> NAMED <<" + WAND_NAME + ">> ON <<" + TYPE + ">> AT POSITION <<"
					+ getPositionString(POS) + ">>");
		} catch (Exception ex) {
			emptyLockedBlocks();
			LOGGER.error("WHOOPS castSpell " + ex.getMessage(), ex);
		}
	}

}
