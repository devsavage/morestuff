package io.savagedev.morestuff.common.blocks.infuser;

/*
 * GuiInfuser.java
 * Copyright (C) 2018 Savage - github.com/devsavage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.Reference;
import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiInfuser extends GuiContainer
{
    private TileEntityInfuser tileEntityInfuser;

    public GuiInfuser(InventoryPlayer inventoryPlayer, TileEntityInfuser tileEntityInfuser) {
        super(new ContainerInfuser(inventoryPlayer, tileEntityInfuser));
        this.tileEntityInfuser = tileEntityInfuser;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_DOMAIN + Names.Gui.infuserResourceLocation));

        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;

        drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        int bufferScale = this.tileEntityInfuser.getFuelStored(39);
        drawTexturedModalRect(xStart + 10, yStart + 57 - bufferScale + 1, 0, 206 - bufferScale, 12, bufferScale);

        if (TileEntityInfuser.isInfusing(this.tileEntityInfuser)) {
            int infusionProgress = getInfuseProgressScaled(24);
            drawTexturedModalRect(xStart + 93, yStart + 36, 176, 14, infusionProgress + 1, 16);
        }
    }

    private int getInfuseProgressScaled(int pixels) {
        int INFUSE_TIME = this.tileEntityInfuser.getField(0);
        int TOTAL_INFUSE_TIME = this.tileEntityInfuser.getField(1);

        return TOTAL_INFUSE_TIME != 0 && INFUSE_TIME != 0 ? INFUSE_TIME * pixels / TOTAL_INFUSE_TIME : 0;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String containerName = this.tileEntityInfuser.getDisplayName().getFormattedText();

        this.fontRendererObj.drawString(containerName, this.xSize / 2 - this.fontRendererObj.getStringWidth(containerName) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), this.xSize - 58, this.ySize - 96 + 2, 4210752);
    }
}
