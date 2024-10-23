package net.A1exPrdgc.biotechmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.awt.*;

public class SqueezerScreen extends ContainerScreen<SqueezerContainer>
{
	private final ResourceLocation GUI = new ResourceLocation(BiotechMod.MOD_ID,
			"textures/gui/squeezer_gui.png");

	protected Rectangle fluidbar = new Rectangle(122, 14, 18, 47);

	public SqueezerScreen(SqueezerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){

		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}



	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
	{

		this.minecraft.getTextureManager().bindTexture(GUI);
		int i = this.guiLeft;
		int j = this.guiTop;

		final SqueezerTile squeezertile = container.tileEntity;
		System.out.println(container);
		if(squeezertile.getTank().getFluidInTank(1).getAmount() > 0)
		{
			System.out.println("phase 1");
			Fluid fluid = squeezertile.tank.getFluid().getFluid();
			TextureAtlasSprite fluidTexture1 = (TextureAtlasSprite)(minecraft.getAtlasSpriteGetter(fluid.getRegistryName()));
			this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			float fluidPercentage = (float) squeezertile.tank.getFluidAmount() / (float) squeezertile.tank.getCapacity();
			int fluidHeight = (int) Math.ceil(fluidPercentage * (float) fluidbar.height);
			this.blit(matrixStack, fluidbar.x + i, fluidbar.y + j + (fluidbar.height - fluidbar.width),0, fluidbar.width, fluidHeight, fluidTexture1);
			System.out.println("blited");

		}

		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
		//condition pour le lancement de la fabrication (animation flèche)
	}

	private int getFluidInTank(SqueezerTile squeezertile) {

		/*
		 * First getFluid() -> returns the fluidStack
		 * Second getFluid() -> returns the fluid
		 */
		if(squeezertile.getTank().getFluid().isEmpty())
			return 0;

		return squeezertile.getTank().getFluidAmount()/squeezertile.getTank().getCapacity() * 74;
	}
}
