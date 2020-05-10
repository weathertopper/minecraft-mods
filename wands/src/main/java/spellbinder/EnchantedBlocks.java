package spellbinder;

import java.util.ArrayList;

import org.apache.commons.math3.util.Pair;

import net.minecraft.util.math.BlockPos;


// strictly to limit events captured at one time

public class EnchantedBlocks {
	
	static Long ENCHANT_BLOCK_EPSILON = new Long(5);
	ArrayList<Pair<String,Long>> enchantedBlocks;
	
	public EnchantedBlocks() {
		this.enchantedBlocks = new ArrayList<Pair<String,Long>>();
	}
	
	public boolean isBlockEnchanted(BlockPos pos) {
		for (int i=this.enchantedBlocks.size()-1; i >= 0 ; i--){
			if (stringifyPosition(pos).equals(this.enchantedBlocks.get(i).getFirst())) {
				return true;
			}
		}
		return false;
	}
	
	public void disenchantAllBlocks() {
		this.enchantedBlocks = new ArrayList<Pair<String,Long>>();
	}
	

	public static Pair<String, Long> createEnchantedBlock(BlockPos pos, Long time) {
		return new Pair<String, Long> (stringifyPosition(pos), time); 
	}
	
	public static String stringifyPosition(BlockPos pos) {
		return String.format("(%o,%o,%o)", pos.getX(), pos.getY(), pos.getZ());
	}

	public void enchantBlock(BlockPos pos, Long time) {
		this.enchantedBlocks.add(createEnchantedBlock(pos, time));
	}

	public void disenchantBlock(BlockPos pos, Long time) {
		for (int i=this.enchantedBlocks.size()-1; i >= 0 ; i--){
			if (this.enchantedBlocks.get(i).getFirst().equals(stringifyPosition(pos))) {
				this.enchantedBlocks.remove(i);
			}
		}
	}
	
	public void pruneEnchantedBlocks(Long currentTime) {
		for (int i=this.enchantedBlocks.size()-1; i >= 0 ; i--){
			if (currentTime > this.enchantedBlocks.get(i).getSecond() + ENCHANT_BLOCK_EPSILON) {
				this.enchantedBlocks.remove(i);
			}
		}
	}



}
