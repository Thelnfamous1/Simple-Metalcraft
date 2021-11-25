package com.infamous.simple_metalcraft.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class SMFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T>{
   private final ResourceLocation texture;

   public SMFurnaceScreen(T furnaceMenu, Inventory inventory, Component component, ResourceLocation location) {
      super(furnaceMenu, inventory, component);
      this.texture = location;
   }

   public void init() {
      super.init();
      this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
   }

   public void render(PoseStack poseStack, int p_97859_, int p_97860_, float p_97861_) {
      this.renderBackground(poseStack);
      super.render(poseStack, p_97859_, p_97860_, p_97861_);
      this.renderTooltip(poseStack, p_97859_, p_97860_);
   }

   protected void renderBg(PoseStack poseStack, float p_97854_, int p_97855_, int p_97856_) {
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, this.texture);
      int i = this.leftPos;
      int j = this.topPos;
      this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
      if (this.menu.isLit()) {
         int k = this.menu.getLitProgress();
         this.blit(poseStack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
      }

      int l = this.menu.getBurnProgress();
      this.blit(poseStack, i + 79, j + 34, 176, 14, l + 1, 16);
   }
}