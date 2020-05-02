package com.weathertopper.test001;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("test001")
public class Test001
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Test001() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        try {
            Entity attacker = event.getSource().getImmediateSource();
            if (attacker instanceof PlayerEntity) {
                Entity injured = event.getEntity();
                DamageSource injury = event.getSource();

                LOGGER.info(">>> " + attacker.getName().getString() +
                        " hurt " + injured.getName().getString() +
                        " with " + injury.getDamageType());

                World world = event.getEntityLiving().getEntityWorld();
                PlayerEntity player = (PlayerEntity) attacker;
                ItemStack mainHandIemStack = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);

                if (mainHandIemStack.getItem().equals(Items.GOLDEN_AXE)) {

                    // Summon lightning
                	world.addEntity(new LightningBoltEntity(
                            world,
                            (double) event.getEntity().getPosition().getX(),
                            event.getEntity().getPosition().getY(),
                            event.getEntity().getPosition().getZ(),
                            false));

//                    // Make an explosion
//                    world.createExplosion(
//                            event.getEntity(),
//                            injury, (double) event.getEntity().getPosition().getX(),
//                            (double) event.getEntity().getPosition().getY(),
//                            (double) event.getEntity().getPosition().getZ(),
//                            8, true, null);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
