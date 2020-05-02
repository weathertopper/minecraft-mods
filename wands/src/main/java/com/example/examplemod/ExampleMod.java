package com.example.examplemod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod {
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public ExampleMod() {
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
			return TARGET_TYPES.BLOCK;
		} else if (event instanceof PlayerInteractEvent.LeftClickEmpty) {
			return TARGET_TYPES.AIR;
		}
		return TARGET_TYPES.IGNORE;
	}

	static boolean LOCKED = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void hearSpell(LivingEvent event) {
		try {
			if (!LOCKED) {
				LOCKED = true;
				PlayerEntity WIZARD = null;
				Entity TARGET = null;
				ItemStack WAND = null;
				String WAND_NAME = null;
				BlockPos POS = null;

				TARGET_TYPES TYPE = getTargetType(event);
				switch (TYPE) {
				case IGNORE:
					LOCKED = false;
					return;
				case LIVING_THING:
					LivingHurtEvent LHE = (LivingHurtEvent) event;
					WIZARD = (PlayerEntity) LHE.getSource().getImmediateSource();
					TARGET = LHE.getEntity();
					WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					WAND_NAME = WAND.getDisplayName().getString();
					POS = TARGET.getPosition();
					break;
				case BLOCK: // TRIGGERS TWICE FOR SOME REASON, AND POSITION IS WIZARD'S
					PlayerInteractEvent.LeftClickBlock LCB = (PlayerInteractEvent.LeftClickBlock) event;
					WIZARD = (PlayerEntity) LCB.getPlayer();
					TARGET = LCB.getEntity();
					WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					WAND_NAME = WAND.getDisplayName().getString();
					POS = TARGET.getPosition();
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

				LOCKED = false;

				LOGGER.info(">>> HEARD SPELL...");
				castSpell(WIZARD, TARGET, TYPE, WAND, WAND_NAME, POS);
				LOGGER.info("");
			}

		} catch (Exception ex) {
			LOCKED = false;
			LOGGER.error("WHOOPS" + ex.getMessage(), ex);
		}

	}

	public void castSpell(PlayerEntity WIZARD, Entity TARGET, TARGET_TYPES TYPE, ItemStack WAND, String WAND_NAME,
			BlockPos POS) {
		LOGGER.info(">>> THE WIZARD <<" + WIZARD.getName().getString() + ">> CAST A SPELL USING A WAND OF <<"
				+ WAND.getItem().toString() + ">> NAMED <<" + WAND_NAME + ">> ON <<" + TYPE + ">> AT POSITION <<"
				+ POS.toString() + ">>");
		// set switch here for all different spells
	}

}
