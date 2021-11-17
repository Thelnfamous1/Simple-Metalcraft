package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.crafting.MetalworkingMenu;
import com.infamous.simple_metalcraft.crafting.MultipleItemRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class MetalworkingScreen extends AbstractContainerScreen<MetalworkingMenu> {
   private static final int SCROLLER_WIDTH = 12;
   private static final int SCROLLER_HEIGHT = 15;
   private static final int RECIPES_COLUMNS = 4;
   private static final int RECIPES_ROWS = 3;
   private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
   private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
   private static final int SCROLLER_FULL_HEIGHT = 54;
   private static final int RECIPES_X = 52;
   private static final int RECIPES_Y = 14;
   public static final int GREEN = 8453920;
   public static final int RED = 16736352;
   private float scrollOffs;
   private boolean scrolling;
   private int startIndex;
   private boolean displayRecipes;
   private final Player player;

   public MetalworkingScreen(MetalworkingMenu menu, Inventory inventory, Component component) {
      super(menu, inventory, component);
      menu.registerUpdateListener(this::containerChanged);
      this.player = inventory.player;
      --this.titleLabelY;
   }

   @Override
   public void render(PoseStack poseStack, int p_99338_, int p_99339_, float p_99340_) {
      super.render(poseStack, p_99338_, p_99339_, p_99340_);
      this.renderTooltip(poseStack, p_99338_, p_99339_);
   }

   @Override
   protected void renderBg(PoseStack poseStack, float p_99329_, int xStart, int yStart) {
      this.renderBackground(poseStack);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, this.getBgLocation());
      int x = this.leftPos;
      int y = this.topPos;
      this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
      int k = (int)(41.0F * this.scrollOffs);
      this.blit(poseStack, x + 119, y + SCROLLER_HEIGHT + k, 176 + (this.isScrollBarActive() ? 0 : SCROLLER_WIDTH), 0, SCROLLER_WIDTH, SCROLLER_HEIGHT);
      int xEnd = this.leftPos + RECIPES_X;
      int yEnd = this.topPos + RECIPES_Y;
      int width = this.startIndex + SCROLLER_WIDTH;
      this.renderButtons(poseStack, xStart, yStart, xEnd, yEnd, width);
      this.renderRecipes(xEnd, yEnd, width);
   }

   protected abstract ResourceLocation getBgLocation();

   @Override
   protected void renderTooltip(PoseStack poseStack, int x, int y) {
      super.renderTooltip(poseStack, x, y);
      if (this.displayRecipes) {
         int i = this.leftPos + RECIPES_X;
         int j = this.topPos + RECIPES_Y;
         int k = this.startIndex + SCROLLER_WIDTH;
         List<? extends MultipleItemRecipe> list = this.menu.getRecipes();

         for(int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
            int i1 = l - this.startIndex;
            int j1 = i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int k1 = j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            if (x >= j1 && x < j1 + RECIPES_IMAGE_SIZE_WIDTH && y >= k1 && y < k1 + RECIPES_IMAGE_SIZE_HEIGHT) {
               this.renderTooltip(poseStack, list.get(l).getResultItem(), x, y);
            }
         }
      }

   }

   private void renderButtons(PoseStack poseStack, int xStart, int yStart, int xEnd, int yEnd, int width) {
      for(int recipeIndex = this.startIndex; recipeIndex < width && recipeIndex < this.menu.getNumRecipes(); ++recipeIndex) {
         int j = recipeIndex - this.startIndex;
         int buttonXStart = xEnd + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
         int l = j / RECIPES_COLUMNS;
         int i1 = yEnd + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
         int buttonYEnd = this.imageHeight;
         if (recipeIndex == this.menu.getSelectedRecipeIndex()) {
            buttonYEnd += RECIPES_IMAGE_SIZE_HEIGHT;
         } else if (xStart >= buttonXStart && yStart >= i1 && xStart < buttonXStart + RECIPES_IMAGE_SIZE_WIDTH && yStart < i1 + RECIPES_IMAGE_SIZE_HEIGHT) {
            buttonYEnd += 36;
         }

         this.blit(poseStack, buttonXStart, i1 - 1, 0, buttonYEnd, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
      }

   }

   private void renderRecipes(int p_99349_, int p_99350_, int p_99351_) {
      List<? extends MultipleItemRecipe> list = this.menu.getRecipes();

      for(int i = this.startIndex; i < p_99351_ && i < this.menu.getNumRecipes(); ++i) {
         int j = i - this.startIndex;
         int k = p_99349_ + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
         int l = j / RECIPES_COLUMNS;
         int i1 = p_99350_ + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
         this.minecraft.getItemRenderer().renderAndDecorateItem(list.get(i).getResultItem(), k, i1);
      }

   }

   @Override
   protected void renderLabels(PoseStack poseStack, int x, int y) {
      RenderSystem.disableBlend();
      super.renderLabels(poseStack, x, y);
      this.renderLevelRequirementLabel(poseStack);
      this.renderXPCostLabel(poseStack);

   }

   private void renderLevelRequirementLabel(PoseStack poseStack) {
      int levelRequirement = this.menu.getLevelRequirement();
      if (levelRequirement > 0) {
         int textColor = GREEN;
         Component component = this.getLevelRequirementComponent(levelRequirement);
         if (!this.menu.meetsLevelRequirement(this.player)) {
            textColor = RED;
         }

         int shadowX = this.imageWidth - 8 - this.font.width(component) - 2;
         int shadowY = this.titleLabelY; // same y position as the menu title
         int fillXStart = shadowX - 2;
         int fillYStart = shadowY - 2;
         int fillXEnd = shadowX + this.font.width(component) + 2;
         int fillYEnd = shadowY + 10;
         fill(poseStack, fillXStart, fillYStart, fillXEnd, fillYEnd, 1325400064);
         this.font.drawShadow(poseStack, component, (float)shadowX, (float)shadowY, textColor);
      }
   }

   private void renderXPCostLabel(PoseStack poseStack) {
      int xpCost = this.menu.getXpCost();
      if (xpCost > 0) {
         int textColor = GREEN;
         Component component = this.getCostComponent(xpCost);
         if (!this.menu.meetsXPCostRequirement(this.player)) {
            textColor = RED;
         }

         int shadowX = this.imageWidth - 8 - this.font.width(component) - 2;
         int shadowY = 69;
         shadowY += 2; // Shifting down by 2px
         int fillXStart = shadowX - 2;
         int fillYStart = shadowY - 2;
         int fillXEnd = shadowX + this.font.width(component) + 2;
         int fillYEnd = shadowY + 10;
         fill(poseStack, fillXStart, fillYStart, fillXEnd, fillYEnd, 1325400064);
         this.font.drawShadow(poseStack, component, (float)shadowX, (float)shadowY, textColor);
      }
   }

   protected abstract TranslatableComponent getLevelRequirementComponent(int levelRequirement);

   protected abstract TranslatableComponent getCostComponent(int xpCost);

   @Override
   public boolean mouseClicked(double p_99318_, double p_99319_, int p_99320_) {
      this.scrolling = false;
      if (this.displayRecipes) {
         int i = this.leftPos + RECIPES_X;
         int j = this.topPos + RECIPES_Y;
         int k = this.startIndex + SCROLLER_WIDTH;

         for(int l = this.startIndex; l < k; ++l) {
            int i1 = l - this.startIndex;
            double d0 = p_99318_ - (double)(i + i1 % 4 * RECIPES_IMAGE_SIZE_WIDTH);
            double d1 = p_99319_ - (double)(j + i1 / 4 * RECIPES_IMAGE_SIZE_HEIGHT);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < (double)RECIPES_IMAGE_SIZE_WIDTH && d1 < (double)RECIPES_IMAGE_SIZE_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, l)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
               return true;
            }
         }

         i = this.leftPos + 119;
         j = this.topPos + 9;
         if (p_99318_ >= (double)i && p_99318_ < (double)(i + SCROLLER_WIDTH) && p_99319_ >= (double)j && p_99319_ < (double)(j + SCROLLER_FULL_HEIGHT)) {
            this.scrolling = true;
         }
      }

      return super.mouseClicked(p_99318_, p_99319_, p_99320_);
   }

   @Override
   public boolean mouseDragged(double p_99322_, double p_99323_, int p_99324_, double p_99325_, double p_99326_) {
      if (this.scrolling && this.isScrollBarActive()) {
         int i = this.topPos + RECIPES_Y;
         int j = i + SCROLLER_FULL_HEIGHT;
         this.scrollOffs = ((float)p_99323_ - (float)i - 7.5F) / ((float)(j - i) - (float)SCROLLER_HEIGHT);
         this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
         this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
         return true;
      } else {
         return super.mouseDragged(p_99322_, p_99323_, p_99324_, p_99325_, p_99326_);
      }
   }

   @Override
   public boolean mouseScrolled(double p_99314_, double p_99315_, double p_99316_) {
      if (this.isScrollBarActive()) {
         int i = this.getOffscreenRows();
         this.scrollOffs = (float)((double)this.scrollOffs - p_99316_ / (double)i);
         this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
         this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D) * RECIPES_COLUMNS;
      }

      return true;
   }

   private boolean isScrollBarActive() {
      return this.displayRecipes && this.menu.getNumRecipes() > SCROLLER_WIDTH;
   }

   protected int getOffscreenRows() {
      return (this.menu.getNumRecipes() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - RECIPES_ROWS;
   }

   private void containerChanged() {
      this.displayRecipes = this.menu.hasInputItem();
      if (!this.displayRecipes) {
         this.scrollOffs = 0.0F;
         this.startIndex = 0;
      }

   }
}