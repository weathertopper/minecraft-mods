package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public ArrayList<BlockPos> getHouseBlockPos(Vec3d pos){
        int yPos = (int) pos.getY();

        int xWallA = (int) pos.getX() + 5;
        int xWallB = (int) pos.getX() - 5;
        int zWallA = (int) pos.getZ() + 5;
        int zWallB = (int) pos.getZ() - 5;

        ArrayList<BlockPos> houseBlockPos = new ArrayList<BlockPos>();
        for (int j = yPos;  j < yPos + 10; j++) {
            for (int i = xWallB; i < xWallA; i++){
                houseBlockPos.add(new BlockPos(i, j , zWallA));
                houseBlockPos.add(new BlockPos(i, j , zWallB));
            }

            for (int i = zWallB; i < zWallA; i++){
                houseBlockPos.add(new BlockPos(xWallA, j , i));
                houseBlockPos.add(new BlockPos(xWallB, j , i));
            }
        }

        return houseBlockPos;
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // https://github.com/Mojang/brigadier
        event.getCommandDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal("babel")
                        .executes(c -> {
                            Vec3d pos = c.getSource().getPos();
                            ArrayList<BlockPos> wallBlocks = getHouseBlockPos(pos);
                            BlockState diamondBlock = Blocks.DIAMOND_BLOCK.getDefaultState();
                            World world = c.getSource().getWorld();
                            for (int i = 0; i < wallBlocks.size(); i++){
                                BlockPos bp = wallBlocks.get(i);
                                LOGGER.info("i: " + i + " bp: "+bp.toString());
                                world.setBlockState(bp, diamondBlock);
                            }

                            LOGGER.info("x: "+ pos.x + "y: "+ pos.y + "z: " + pos.z);
                            return 1;
                        }));
    }
}
