package net.IneiTsuki.testmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompressorScreen extends AbstractContainerScreen<CompressorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(testmod.MOD_ID, "textures/gui/Compressor1.png");

    public CompressorScreen(CompressorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }



    @Override
    protected void renderBg(PoseStack poseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(poseStack, x, y);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if (menu.isCrafting()) {
            blit(pPoseStack, x + 81, y + 47, 177, 0, menu.getScaledProgress(), 10);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
