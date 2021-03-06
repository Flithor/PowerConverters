package covers1624.powerconverters.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import covers1624.powerconverters.util.LogHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class InfoHandler implements IUsageHandler, ICraftingHandler {

    private static final int WIDTH = 166;
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static int color = -12566464;
    ItemStack displayItem;
    boolean precise = false;
    String id;
    String name;
    String[] info;
    public boolean checkedOrder = false;
    int noLinesPerPage = 12;
    public final String suffix = ".documentation";

    public InfoHandler() {
        this.displayItem = null;
    }

    public InfoHandler(ItemStack item) {
        if (!StatCollector.canTranslate(item.getUnlocalizedName() + suffix) && !StatCollector.canTranslate(item.getUnlocalizedName() + suffix + ".0")) {
            this.id = item.getItem().getUnlocalizedName();
            this.name = StatCollector.translateToLocal(item.getItem().getUnlocalizedName());
            this.precise = false;
        } else {
            this.id = item.getUnlocalizedName();
            this.name = StatCollector.translateToLocal(item.getUnlocalizedName());
            this.precise = true;
        }
        if (StatCollector.canTranslate(this.id + suffix)) {
            List<String> list = this.splitString(StatCollector.translateToLocal(this.id + suffix));
            this.info = list.toArray(new String[list.size()]);
        } else {
            ArrayList<String> temp = new ArrayList<String>();
            for (int i = 0; StatCollector.canTranslate(this.id + suffix + "." + i); ++i) {
                String a = StatCollector.translateToLocal(this.id + suffix + "." + i);
                temp.addAll(this.splitString(a));
            }
            this.info = temp.toArray(new String[temp.size()]);
        }
        this.displayItem = item.copy();
        this.displayItem.stackSize = 1;
    }

    @Override
    public void drawBackground(int arg0) {
        // Skip
    }

    @Override
    public void drawForeground(int recipe) {
        // Draw the text
        @SuppressWarnings("unchecked")
        List<String> text = fontRenderer.listFormattedStringToWidth(this.info[recipe], WIDTH - 8);
        for (int i = 0; i < text.size(); i++) {
            String toDraw = (String) text.get(i);
            GuiDraw.drawString(toDraw, WIDTH / 2 - GuiDraw.getStringWidth(toDraw) / 2, 18 + i * 8, color, false);
        }
    }

    public List<String> splitString(String input) {
        ArrayList<String> list = new ArrayList<String>();
        @SuppressWarnings("unchecked")
        List<String> page = fontRenderer.listFormattedStringToWidth(input, WIDTH - 8);

        if (page.size() < this.noLinesPerPage) {
            list.add(input);
        } else {
            String temp = "";
            for (int i = 0; i < page.size(); i++) {
                temp = temp + page.get(i) + " ";
                if (i > 0 && i % this.noLinesPerPage == 0) {
                    String temp2 = temp.trim();
                    list.add(temp2);
                    temp = "";
                }
            }

            temp = temp.trim();
            if (!"".equals(temp)) {
                list.add(temp);
            }
        }
        return list;
    }

    @Override
    public List<PositionedStack> getIngredientStacks(int arg0) {
        return new ArrayList<PositionedStack>();
    }

    @Override
    public List<PositionedStack> getOtherStacks(int arg0) {
        return new ArrayList<PositionedStack>();
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer arg0, int arg1) {
        return null;
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int arg1) {
        return null;
    }

    @Override
    public String getRecipeName() {
        if (this.displayItem == null) {
            return "Documentation";
        } else {
            String s = Item.itemRegistry.getNameForObject(this.displayItem.getItem());
            String modid = s.split(":")[0];

            if ("minecraft".equals(modid)) {
                return "Minecraft";
            } else {
                ModContainer selectMod = (ModContainer) Loader.instance().getIndexedModList().get(modid);
                return selectMod == null ? modid : (!selectMod.getMetadata().autogenerated ? selectMod.getMetadata().name : selectMod.getName());
            }
        }
    }

    @Override
    public PositionedStack getResultStack(int arg0) {
        return new PositionedStack(this.displayItem, WIDTH / 2 - 9, 0, false);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        return currenttip;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        return currenttip;
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }

    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        return false;
    }

    @Override
    public int numRecipes() {
        return this.displayItem != null && this.info != null ? this.info.length : 0;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    public boolean isValidItem(ItemStack item) {
        boolean flag = false;
        LogHelper.info(item.getUnlocalizedName() + suffix);
        // LogHelper.info(item.getItem().getUnlocalizedName() + suffix);
        // LogHelper.info(item.getUnlocalizedName() + suffix + ".0");
        // LogHelper.info(item.getItem().getUnlocalizedName() + suffix + ".0");
        if (StatCollector.canTranslate(item.getUnlocalizedName() + suffix)) {
            flag = true;
        } else if (StatCollector.canTranslate(item.getItem().getUnlocalizedName() + suffix)) {
            flag = true;
        } else if (StatCollector.canTranslate(item.getUnlocalizedName() + suffix + ".0")) {
            flag = true;
        } else if (StatCollector.canTranslate(item.getItem().getUnlocalizedName() + suffix + ".0")) {
            flag = true;
        }
        return flag;
    }

    @Override
    public IUsageHandler getUsageHandler(String inputId, Object... ingredients) {
        if (!inputId.equals("item")) {
            return this;
        } else {
            Object[] ingredientsArray = ingredients;
            int ingredientsLength = ingredients.length;

            for (int i = 0; i < ingredientsLength; i++) {
                Object ingredient = ingredientsArray[i];

                if (ingredient instanceof ItemStack && this.isValidItem((ItemStack) ingredient)) {
                    return new InfoHandler((ItemStack) ingredient);
                }
            }
            return this;
        }
    }

    @Override
    public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
        if (!outputId.equals("item")) {
            return this;
        } else {
            Object[] ingredientsArray = results;
            int ingredientsLength = results.length;

            for (int i = 0; i < ingredientsLength; i++) {
                Object ingredient = ingredientsArray[i];

                if (ingredient instanceof ItemStack && this.isValidItem((ItemStack) ingredient)) {
                    return new InfoHandler((ItemStack) ingredient);
                }
            }
            return this;
        }
    }

}