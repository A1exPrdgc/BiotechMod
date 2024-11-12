package net.A1exPrdgc.biotechmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.ExtractorContainer;
import net.A1exPrdgc.biotechmod.tileentity.ExtractorTile;
import net.A1exPrdgc.biotechmod.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ExtractorScreen extends ContainerScreen<ExtractorContainer>
{
	private static final int TANK_SIZE_Y = 47;
	public static final int TANK_SIZE_X = 18;
	private final ResourceLocation GUI = new ResourceLocation(BiotechMod.MOD_ID,
			"textures/gui/extractor_gui.png");

	private int fluidAmount;

	public ExtractorScreen(ExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn){
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y){
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

		FluidTank tank = container.getTileEntity().getTank();
		FluidStack fluid = tank.getFluid();

		this.fluidAmount = tank.getFluidAmount();

		if(this.fluidAmount > 0)
		{
			System.out.println(this.fluidAmount);
			int temp = sizedBar(this.fluidAmount);
			TextureAtlasSprite fluidTexture = Minecraft.getInstance().getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(Util.getFluidTexture(fluid));

			float[] tabcol = Util.intToColor4f(Util.getFluidColor(fluid));

			Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			RenderSystem.color4f( tabcol[0],  tabcol[1],  tabcol[2],  tabcol[3]);
			blit(matrixStack, i + 79,j + 9 + (ExtractorScreen.TANK_SIZE_Y - temp),0 , TANK_SIZE_X, temp, fluidTexture);
		}

	}

	@Override
	protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y){
		super.renderHoveredTooltip(matrixStack, x, y);

		int temp = sizedBar(this.fluidAmount);
		if(x >= guiLeft + 79 && x < guiLeft + 79 + ExtractorScreen.TANK_SIZE_X && y >= guiTop + 9 + (ExtractorScreen.TANK_SIZE_Y - temp) && y < guiTop + 9 + ExtractorScreen.TANK_SIZE_Y){
			this.renderTooltip(matrixStack, new StringTextComponent(this.fluidAmount + "/" + ExtractorTile.CAPACITY), x, y);
		}
	}

	private static int sizedBar(int value)
	{
		return (ExtractorScreen.TANK_SIZE_Y * value) / ExtractorTile.CAPACITY;
	}
}
