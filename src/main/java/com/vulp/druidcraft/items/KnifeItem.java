package com.vulp.druidcraft.items;

import com.vulp.druidcraft.api.IKnifeable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class KnifeItem extends Item {

    public KnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.PASS;

        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof IKnifeable) {
            ActionResultType result = ((IKnifeable) state.getBlock()).onKnife(context);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        if (player.isSneaking() && state.has(BlockStateProperties.HORIZONTAL_FACING)) {
            BlockState state1 = cycleProperty(state, BlockStateProperties.HORIZONTAL_FACING);
            world.setBlockState(pos, state1, 18);
            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(context);
    }

    private static <T extends Comparable<T>> BlockState cycleProperty(BlockState state, IProperty<T> propertyIn) {
        return state.with(propertyIn, getAdjacentValue(propertyIn.getAllowedValues(), state.get(propertyIn)));
    }

    private static <T> T getAdjacentValue(Iterable<T> p_195959_0_, @Nullable T p_195959_1_) {
        return Util.getElementAfter(p_195959_0_, p_195959_1_);
    }
}
