package net.A1exPrdgc.biotechmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.ExtractorContainer;
import net.A1exPrdgc.biotechmod.tileentity.ExtractorTile;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class ExtractorScreen extends ContainerScreen<ExtractorContainer>
{
	private static final int TANK_SIZE_Y = 47;
	public static final int TANK_SIZE_X = 18;
	private final ResourceLocation GUI = new ResourceLocation(BiotechMod.MOD_ID,
			"textures/gui/extractor_gui.png");

	private Rectangle fluidBar = new Rectangle(122, 14, 18, 47);

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

		if(container.getDataArray().get(0) > 0)
		{
			int temp = sizedBar(container.getDataArray().get(0));
			this.blit(matrixStack, i + 79,j + 9 + (ExtractorScreen.TANK_SIZE_Y - temp), 177,
					1 + (ExtractorScreen.TANK_SIZE_Y - temp), TANK_SIZE_X, temp);
		}

	}

	private static int sizedBar(int value)
	{
		return (ExtractorScreen.TANK_SIZE_Y * value) / ExtractorTile.CAPACITY;
	}
}
