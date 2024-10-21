package net.A1exPrdgc.biotechmod.energy;

import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BioEnergizedFlux implements IBioEnergizedFlux
{
	private int bioEnergizedFlux;
	@Override
	public boolean consumeEnergy(int quantity){
		if(this.bioEnergizedFlux > quantity)
		{
			bioEnergizedFlux -= quantity;
			return true;
		}
		return false;
	}

	@Override
	public boolean fillEnergy(int maxQuantity, int quantity){
		if(this.bioEnergizedFlux + quantity < maxQuantity)
		{
			this.bioEnergizedFlux += quantity;
			return true;
		}
		return false;
	}


	@Override
	public void setQaFlux(int quantity){
		this.bioEnergizedFlux = quantity;
	}

	@Override
	public int getQaFlux(){
		return this.bioEnergizedFlux;
	}
}
