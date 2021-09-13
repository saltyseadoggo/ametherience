package io.github.saltyseadoggo.mixin;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AmethystClusterBlock.class)
public class AmethystClusterMixin extends AmethystBlock {
	public AmethystClusterMixin(Settings settings) {
		super(settings);
	}
		//This method doesn't exist in AmethystClusterBlock, so we don't need to inject; instead, we override the method from the classes it extends,
		//which includes Block, which has this method
	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, state, blockEntity, stack);
			//If this is the client, the block is a small/medium/large amethyst bud or the player has silk touch, do nothing
		if (world instanceof ServerWorld && state.isOf(Blocks.AMETHYST_CLUSTER) && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
				//If the player's held item is in the item tag minecraft:cluster_max_harvestables, drop more experience than otherwise
				//The 2-5 experience is the same experience yield as nether quartz ore, defined in Blocks.class where it is registered
				//This section follows the format variable = true/false question ? set to this if true : set to this if false
			int i = stack.isIn(ItemTags.CLUSTER_MAX_HARVESTABLES) ? 2 + world.random.nextInt(5) : 1 + world.random.nextInt(3);
				//Drop the experience!
			this.dropExperience((ServerWorld)world, pos, i);
		}
	}
}
