package net.A1exPrdgc.biotechmod.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BioEnergizedFluxStorage implements Capability.IStorage<IBioEnergizedFlux>
{

	@Nullable
	@Override
	public INBT writeNBT(Capability<IBioEnergizedFlux> capability, IBioEnergizedFlux instance, Direction side){
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("bioenergizedflux", instance.getQaFlux());
		return tag;
	}

	@Override
	public void readNBT(Capability<IBioEnergizedFlux> capability, IBioEnergizedFlux instance, Direction side, INBT nbt){
		int flux = ((CompoundNBT) nbt).getInt("bioenergizedflux");
		instance.setQaFlux(flux);
	}
}
