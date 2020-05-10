package spellbinder;

import enchiridion.Incantation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("examplemod")
public class SeizeSpells {

	// These are the event listeners triggered on a desired event

	public SeizeSpells() {
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	public EnchantedBlocks enchantedBlocks = new EnchantedBlocks();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void seizeAir(PlayerInteractEvent.LeftClickEmpty event) {
		MarkType.MARK_TYPE type = MarkType.MARK_TYPE.AIR;
		PlayerEntity wizard = (PlayerEntity) event.getPlayer();
		Entity mark = event.getEntity(); // is wizard
		BlockPos position = wizard.getPosition();
		World world = event.getWorld();
		event.getEntityLiving().getEntityWorld();
		enhanceSpell(wizard, mark, type, position, event, world);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void seizeCreature(LivingHurtEvent event) {
		if (!(event.getSource().getImmediateSource() instanceof PlayerEntity)) {
			return;
		}
		MarkType.MARK_TYPE type = MarkType.MARK_TYPE.CREATURE;
		PlayerEntity wizard = (PlayerEntity) event.getSource().getImmediateSource();
		Entity mark = event.getEntity();
		BlockPos position = mark.getPosition();
		World world = event.getEntityLiving().getEntityWorld();
		enhanceSpell(wizard, mark, type, position, event, world);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void seizeWorld(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getHand() != Hand.MAIN_HAND) {
			return;
		}
		MarkType.MARK_TYPE type = MarkType.MARK_TYPE.WORLD;
		PlayerEntity wizard = (PlayerEntity) event.getPlayer();
		Entity mark = event.getEntity();
		BlockPos position = event.getPos();
		World world = event.getEntityLiving().getEntityWorld();
		enhanceSpell(wizard, mark, type, position, event, world);
	}

	public void enhanceSpell(PlayerEntity wizard, Entity mark, MarkType.MARK_TYPE type, BlockPos position, Event event,
			World world) {

//		Long time = mark.getEntityWorld().getGameTime();
//		enchantedBlocks.pruneEnchantedBlocks(time);
//		if (enchantedBlocks.isBlockEnchanted(position)) {
//			return;
//		}
//		enchantedBlocks.enchantBlock(position, time);
		Item wand = wizard.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();
		String incantationName = wizard.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getDisplayName().getString()
				.toUpperCase();
		if (!Sygaldry.doesSpellExist(incantationName)) {
			return;
		}
		if (event.isCancelable()) {
			event.setCanceled(true);
		}
		Sygaldry.inscribeSpell(wizard, mark, type, wand, incantationName, position, world);

	}

}
