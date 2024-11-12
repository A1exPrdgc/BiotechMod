package net.A1exPrdgc.biotechmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.scenario.effect.Color4f;
import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;

import static net.minecraftforge.fml.client.gui.GuiUtils.drawTexturedModalRect;

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
			TextureAtlasSprite fluidTexture = Minecraft.getInstance().getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(container.getFluidTexture());

			float[] tabcol = SqueezerScreen.intToColor4f(container.getFluidColor());

			Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			RenderSystem.color4f( tabcol[0],  tabcol[1],  tabcol[2],  tabcol[3]);

			blit(matrixStack, i + 122,j + 14 + (SqueezerScreen.TANK_SIZE_Y - temp),0 , SqueezerScreen.TANK_SIZE_X, temp, fluidTexture);

		}
	}

	private static float[] intToColor4f(int c)
	{
		float[] res = new float[4];
		Color col = new Color(c);

		res[0] = (float) col.getRed() / 100;
		res[1] = (float) col.getGreen() / 100;
		res[2] = (float) col.getBlue() / 100;
		res[3] = col.getAlpha();

		return res;

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
