package net.A1exPrdgc.biotechmod.container;

import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.A1exPrdgc.biotechmod.util.PacketHandler;
import net.A1exPrdgc.biotechmod.util.UpdateContainerDataPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SqueezerContainer extends Container
{
	private SqueezerTile tileEntity;
	private final PlayerEntity playerEntity;
	private final IItemHandler playerInventory;
	public SqueezerContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player)
	{
		super(ModContainers.SQUEEZER_CONTAINER.get(), windowId);


		this.tileEntity = (SqueezerTile)world.getTileEntity(pos);
		this.playerEntity = player;
		this.playerInventory = new InvWrapper(playerInventory);

		layoutPlayerInventorySlots(8, 86);

		if(this.tileEntity instanceof SqueezerTile)
		{

			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(ih ->
			{
				SlotItemHandler[] tab = new SlotItemHandler[]{
						new SlotItemHandler(ih, 0, 80, 53),  // slot sous flèche
						new SlotItemHandler(ih, 1, 39, 31),  // slot tout à gauche
						new SlotItemHandler(ih, 2, 123, 65), // slot seau
						new SlotItemHandler(ih, 3, 152, 6),  // slot upgrade 1
						new SlotItemHandler(ih, 4, 152, 26), // slot upgrade 2
						new SlotItemHandler(ih, 5, 152, 46), // slot upgrade 3
						new SlotItemHandler(ih, 6, 152, 66)  // slot upgrade 4
				};

				for (int i=0; i < tab.length; i++) {
					addSlot(tab[i]);
				}

			});
		}
	}

	public ResourceLocation getFluidTexture() {
		FluidStack fluidStack = this.tileEntity.getTank().getFluid();

		if (!fluidStack.isEmpty()) {
			return fluidStack.getFluid().getAttributes().getStillTexture();
		}

		return null;
	}

	public int getFluidColor()
	{
		FluidStack fluidstack = this.tileEntity.getTank().getFluid();

		if(!fluidstack.isEmpty())
		{
			return fluidstack.getFluid().getAttributes().getColor();
		}
		return 0;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn){
		return isWithinUsableDistance(IWorldPosCallable.of(
				tileEntity.getWorld(), tileEntity.getPos()), playerIn, ModBlocks.SQUEEZER.get());
	}

	public SqueezerTile getTileEntity(){
		return tileEntity;
	}

	//--------------------Gestion de l'inventaire----------------------
	private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			addSlot(new SlotItemHandler(handler, index, x, y));
			x += dx;
			index++;
		}

		return index;
	}

	private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}

		return index;
	}

	private void layoutPlayerInventorySlots(int leftCol, int topRow) {
		addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}

	//----------------------------------------------------

	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	// THIS YOU HAVE TO DEFINE!
	private static final int TE_INVENTORY_SLOT_COUNT = 7;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot sourceSlot = inventorySlots.get(index);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
					+ TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.out.println("Invalid slotIndex:" + index);
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {
			sourceSlot.putStack(ItemStack.EMPTY);
		} else {
			sourceSlot.onSlotChanged();
		}
		sourceSlot.onTake(playerEntity, sourceStack);
		return copyOfSourceStack;
	}
}
