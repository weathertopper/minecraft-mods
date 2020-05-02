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

	public boolean leftClickEvent(LivingEvent event) {
		return (event instanceof LivingHurtEvent) || (event instanceof PlayerInteractEvent.LeftClickEmpty)
				|| (event instanceof PlayerInteractEvent.LeftClickBlock);
	}

	public enum EVENT_TYPE {
		LIVING_THING, BLOCK, AIR, IGNORE
	}

	public EVENT_TYPE getEventType(LivingEvent event) {
		if (event instanceof LivingHurtEvent) {
			if (((LivingHurtEvent) event).getSource().getImmediateSource() instanceof PlayerEntity) {
				return EVENT_TYPE.LIVING_THING;
			}
		} else if (event instanceof PlayerInteractEvent.LeftClickBlock) {
			return EVENT_TYPE.BLOCK;
		} else if (event instanceof PlayerInteractEvent.LeftClickEmpty) {
			return EVENT_TYPE.AIR;
		}
		return EVENT_TYPE.IGNORE;
	}

	static boolean LOCKED = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUse(LivingEvent event) {
		try {
			if (!LOCKED) {
				LOCKED = true;

				PlayerEntity WIZARD = null;
				Entity TARGET = null;
				ItemStack WAND = null;
				String WAND_NAME = null;
				BlockPos POS = null;

				EVENT_TYPE TYPE = getEventType(event);
				switch (TYPE) {
				case IGNORE:
					LOCKED = false;
					return;
				case LIVING_THING:
//					LOGGER.info(">>> IT'S A HIT!");
					LivingHurtEvent LHE = (LivingHurtEvent) event;
					WIZARD = (PlayerEntity) LHE.getSource().getImmediateSource();
					TARGET = LHE.getEntity();
					WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					WAND_NAME = WAND.getDisplayName().getString();
					POS = TARGET.getPosition();
					break;
				case BLOCK: // TRIGGERS TWICE FOR SOME REASON
//					LOGGER.info(">>> IT'S A BLOCK!");
					PlayerInteractEvent LCB = (PlayerInteractEvent.LeftClickBlock) event;
					WIZARD = (PlayerEntity) LCB.getPlayer();
					TARGET = LCB.getEntity();
					WAND = WIZARD.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
					WAND_NAME = WAND.getDisplayName().getString();
					POS = TARGET.getPosition();
					break;
				case AIR:
//					LOGGER.info(">>> NOTHING BUT AIR!");
					PlayerInteractEvent LCE = (PlayerInteractEvent.LeftClickEmpty) event;
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
//				LOGGER.info(">>> PAST SWITCH!");

				LOGGER.info(">>> THE WIZARD <<" + WIZARD.getName().getString() + ">> CAST A SPELL USING A WAND OF <<"
						+ WAND.getItem().toString() + ">> NAMED <<" + WAND_NAME + ">> ON <<" + TYPE
						+ ">> AT POSITION <<" + POS.toString() + ">>");
				LOGGER.info("");
			}

		} catch (Exception ex) {
			LOCKED = false;
			LOGGER.error("WHOOPS" + ex.getMessage(), ex);
		}

	}

}
