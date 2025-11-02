package com.artur.returnoftheancients.structurebuilder;

import com.artur.returnoftheancients.structurebuilder.interf.IBuildProperties;
import com.artur.returnoftheancients.structurebuilder.interf.IStructureBuilder;
import com.artur.returnoftheancients.util.interfaces.Function2;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;


public class BuildRequest implements IBuildProperties {
    protected final IStructureBuilder builder;
    protected final BlockPos pos;
    protected final World world;

    protected boolean isNeedProtect = false;
    protected boolean isIgnoreAir = false;
    protected Function<IBlockState, Boolean> isUseEBSHook = null;
    protected boolean isNeedMarkRenderUpdate = true;
    protected Function<IBlockState, IBlockState> blockStateHook = null;
    protected Function2<IBlockState, BlockPos, Boolean> blockProtectHook = null;
    protected boolean isPosAsXZCenter = false;
    protected boolean isNeedLoadLightMap = true;

    protected BuildRequest(World world, BlockPos pos, IStructureBuilder builder) {
        this.builder = builder;
        this.world = world;
        this.pos = pos;
    }

    public void build() {
        this.builder.build(world, pos, this);
    }

    @Override
    public boolean isIgnoreAir() {
        return this.isIgnoreAir;
    }
    public BuildRequest setIgnoreAir() {
        this.isIgnoreAir = true;
        return this;
    }

    @Override
    public boolean isNeedProtect() {
        return this.isNeedProtect;
    }
    public BuildRequest setNeedProtect() {
        this.isNeedProtect = true;
        return this;
    }
    public BuildRequest setNeedProtect(boolean state) {
        this.isNeedProtect = state;
        return this;
    }

    @Override
    public boolean isUseEBSHook(IBlockState state) {
        if (this.isUseEBSHook == null) {
            return true;
        }
        return this.isUseEBSHook.apply(state);
    }
    public BuildRequest addIsUseEBSHook(Function<IBlockState, Boolean> hook) {
        this.isUseEBSHook = hook;
        return this;
    }

    @Override
    public boolean isNeedMarkRenderUpdate() {
        return this.isNeedMarkRenderUpdate;
    }
    public BuildRequest setNotNeedMarkRenderUpdate() {
        this.isNeedMarkRenderUpdate = false;
        return this;
    }

    @Override
    public IBlockState blockStateHook(IBlockState state) {
        if (this.blockStateHook == null) {
            return state;
        }
        return this.blockStateHook.apply(state);
    }
    public BuildRequest addBlockStateHook(Function<IBlockState, IBlockState> hook) {
        this.blockStateHook = hook;
        return this;
    }

    @Override
    public boolean blockProtectHook(IBlockState state, BlockPos pos) {
        if (this.blockProtectHook == null) {
            return true;
        }
        return this.blockProtectHook.apply(state, pos);
    }
    public BuildRequest addBlockProtectHook(Function2<IBlockState, BlockPos, Boolean> hook) {
        this.blockProtectHook = hook;
        return this;
    }

    @Override
    public boolean isPosAsXZCenter() {
        return this.isPosAsXZCenter;
    }
    public BuildRequest setPosAsXZCenter() {
        this.isPosAsXZCenter = true;
        return this;
    }

    @Override
    public boolean isNeedLoadLightMap() {
        return isNeedLoadLightMap;
    }
    public BuildRequest setNotNeedLoadLightMap() {
        this.isNeedLoadLightMap = false;
        return this;
    }
}
