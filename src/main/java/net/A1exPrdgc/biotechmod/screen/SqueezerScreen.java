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

import java.awt.*;

public class SqueezerScreen extends ContainerScreen<SqueezerContainer>
{

	public static final int ARROW_SIZE_X = 49;
	public static final int ARROW_SIZE_Y = 14;
	public static final int TANK_SIZE_X = 18;
	public static final int TANK_SIZE_Y = 47;

	private final ResourceLocation GUI = new ResourceLocation(BiotechMod.MOD_ID,
			"textures/gui/squeezer_gui.png");

	private Rectangle fluidBar = new Rectangle(122, 14, 18, 47);

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

		//gère la flèche
		if (container.getDataArray().get(2) >= 0 &&
			container.getDataArray().get(0) > 1 &&
			container.getDataArray().get(1) + 250 <= SqueezerTile.CAPACITY)
		{
			this.blit(matrixStack, i + 63, j + 32, 176, 0,
					  sizedArrow(container.getDataArray().get(2)), SqueezerScreen.ARROW_SIZE_Y);
		}

		//gère le tank
		if (container.getDataArray().get(1) > 0)
		{
			int temp = sizedBar(container.getDataArray().get(1));
			this.blit(matrixStack, i + 122,j + 14 + (SqueezerScreen.TANK_SIZE_Y - temp), 177,
					15 + (SqueezerScreen.TANK_SIZE_Y - temp), SqueezerScreen.TANK_SIZE_X, temp);
		}

		//condition pour le lancement de la fabrication (animation flèche)
	}

	/*@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y){
		this.drawString(matrixStack, this.font, container.getDataArray().get(0) + " : " + container.getDataArray().get(1), 10, 84, 65);
	}*/

	private static int sizedBar(int value)
	{
		return (SqueezerScreen.TANK_SIZE_Y * value) / SqueezerTile.CAPACITY;
	}

	public static int sizedArrow(int value)
	{
		return (SqueezerScreen.ARROW_SIZE_X * value) / SqueezerTile.COOKING_TIME_TICK;
	}

}
