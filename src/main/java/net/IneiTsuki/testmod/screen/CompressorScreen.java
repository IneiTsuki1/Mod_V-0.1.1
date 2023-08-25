package net.IneiTsuki.testmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CompressorScreen extends AbstractContainerScreen<CompressorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(testmod.MOD_ID, "textures/gui/circuit_fabricator2.png");


    public CompressorScreen(CompressorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 195;
    }

    @Override
    protected void renderBg(@NotNull PoseStack ms, float partialTicks, int gx, int gy) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, TEXTURE);
        //int x = (width - imageWidth) / 2;
        //int y = (height - imageHeight) / 2;
        blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.setShaderTexture(0, new ResourceLocation(testmod.MOD_ID, "textures/gui/arrowback.png"));
        blit(ms, this.leftPos + 81, this.topPos + 44, 0, 0, 40, 10, 40, 10);
        RenderSystem.setShaderTexture(0, new ResourceLocation(testmod.MOD_ID, "textures/gui/arrow.png"));
        blit(ms, this.leftPos + 81, this.topPos + 44, 0, 0, menu.getScaledProgress(), 10, 40, 10);
        RenderSystem.disableBlend();
        //renderProgressArrow(poseStack, x, y);
    }

    /*private void renderProgressArrow(PoseStack poseStack, int x, int y) {
        if (menu.isCrafting()) {
            blit(poseStack, x + 12, y + 92, /* this is the location the arrow will be rendered */
    // 77177, 0, 5, /* size of the arrow *77/menu.getScaledProgress());
    //}
    //}*/

    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }
}
