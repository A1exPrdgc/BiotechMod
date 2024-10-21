package net.A1exPrdgc.biotechmod.energy;

public interface IBioEnergizedFlux
{
	public boolean consumeEnergy(int quantity);
	public boolean fillEnergy(int maxQuantity, int quantity);
	public void setQaFlux(int quantity);
	public int getQaFlux();
}
