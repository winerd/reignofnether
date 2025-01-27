package com.solegendary.reignofnether.mixin;

import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.building.buildings.neutral.Beacon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin extends BlockEntity {

    public BeaconBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(
            method = "getBeamSections()Ljava/util/List;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getBeamSections(CallbackInfoReturnable<List<BeaconBlockEntity.BeaconBeamSection>> cir) {
        if (level == null || !level.isClientSide())
            return;

        Beacon beacon = BuildingUtils.getBeacon(level.isClientSide());

        if (beacon != null && beacon.getUpgradeLevel() > 0 && worldPosition.equals(beacon.beaconPos)) {
            if (beacon.isBeaconActive()) {
                BeaconBlockEntity.BeaconBeamSection beam = new BeaconBlockEntity.BeaconBeamSection(new float[] { 1.0f, 1.0f, 1.0f });
                cir.setReturnValue(List.of(beam));
            } else {
                cir.setReturnValue(List.of());
            }
        }
    }
}
