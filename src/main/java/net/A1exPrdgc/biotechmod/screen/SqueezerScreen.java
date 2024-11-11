package net.A1exPrdgc.biotechmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

public class SqueezerScreen extends ContainerScreen<SqueezerContainer>
{

	public static final int ARROW_SIZE_X = 49;
	public static final int ARROW_SIZE_Y = 14;
	public static final int TANK_SIZE_X = 18;
	public static final int TANK_SIZE_Y = 47;

	private int fluidAmount;
	private int timer;
	private int nbItemInMainSlot;

	private final ResourceLocation GUI = new ResourceLocation(BiotechMod.MOD_ID,
			"textures/gui/squeezer_gui.png");

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
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

		this.nbItemInMainSlot = container.getTileEntity().getCount();                                       // nombre d'item dans le slot 1
		this.fluidAmount = container.getTileEntity().getTank().getFluidAmount();                            // quantité de fluid ans le tank
		this.timer = container.getTileEntity().getTimer();                                                  // timer

		//gère la flèche
		if (this.timer >= 0 && this.nbItemInMainSlot > 1 && this.fluidAmount <= SqueezerTile.CAPACITY)
		{
			this.blit(matrixStack, i + 63, j + 32, 176, 0,
					  sizedArrow(this.timer), SqueezerScreen.ARROW_SIZE_Y);
		}

		//gère le tank
		if (this.fluidAmount > 0)
		{
			int temp = sizedBar(this.fluidAmount);
			this.blit(matrixStack, i + 122,j + 14 + (SqueezerScreen.TANK_SIZE_Y - temp), 177,
					15 + (SqueezerScreen.TANK_SIZE_Y - temp), SqueezerScreen.TANK_SIZE_X, temp);
		}
	}

	@Override
	protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y){
		super.renderHoveredTooltip(matrixStack, x, y);
		int qa = container.getTileEntity().getTank().getFluidAmount();

		int temp = sizedBar(qa);
		if(x >= guiLeft + 122 && x < guiLeft + 122 + SqueezerScreen.TANK_SIZE_X && y >= guiTop + 14 + (SqueezerScreen.TANK_SIZE_Y - temp) && y < guiTop + 14 + TANK_SIZE_Y){
			this.renderTooltip(matrixStack, new StringTextComponent(this.fluidAmount + "/" + SqueezerTile.CAPACITY), x, y);
		}
	}

	private static int sizedBar(int value)
	{
		return (SqueezerScreen.TANK_SIZE_Y * value) / SqueezerTile.CAPACITY;
	}

	public static int sizedArrow(int value)
	{
		return (SqueezerScreen.ARROW_SIZE_X * value) / SqueezerTile.COOKING_TIME_TICK;
	}

}
