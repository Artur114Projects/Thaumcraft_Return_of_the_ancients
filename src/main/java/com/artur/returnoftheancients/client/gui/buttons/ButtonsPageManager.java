package com.artur.returnoftheancients.client.gui.buttons;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class ButtonsPageManager {

    private final Map<Integer, int[]> buttonsOnPage = new HashMap<>();
    private final List<ButtonPage> buttonsPage = new ArrayList<>();
    private final List<GuiButton> buttons;
    public final int startButtonID;
    public final int pagesCount;
    private int currentPage = -1;

    public ButtonsPageManager(List<GuiButton> buttonList, int startX, int startY, int startButtonID, int pagesCount, int size, float scale, char orientation, ResourceLocation baseTexture, ResourceLocation[] buttonIcons, String[] hoveredTexts) {
        this.startButtonID = startButtonID;
        this.pagesCount = pagesCount;
        this.buttons = buttonList;
        size = (int) (size * scale);
        String[] compileHoveredTexts = Arrays.copyOf(hoveredTexts, pagesCount);
        ResourceLocation[] compileButtonIcons = Arrays.copyOf(buttonIcons, pagesCount);
        for (int i = 0; i != pagesCount; i++) {
            int localX = startX;
            int localY = startY;
            if (orientation == 'x') {
                localX += size * i;
            } else if (orientation == 'y') {
                localY += size * i;
            }
            ButtonPage button = new ButtonPage(startButtonID + i, localX, localY, scale, baseTexture, compileButtonIcons[i], compileHoveredTexts[i]);
            buttonsPage.add(button);
            buttonList.add(button);
        }
        buttonsPage.get(0).enabled = false;
    }

    public void putButtonsOnPage(int page, int... buttons) {
        buttonsOnPage.put(page, buttons);
    }

    public void update() {
        boolean flag = false;
        for (ButtonPage buttonPage : buttonsPage) {
            if (!buttonPage.enabled) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            buttonsPage.get(0).enabled = false;
            changePage(0);
        }
    }

    public void onPageChange(int page, int pageIn) {
        enableButtons(buttonsOnPage.get(pageIn));
    }

    private void enableButtons(int... buttonIDs) {
        for (GuiButton button : buttons) {
            if (button.id >= startButtonID) {
                continue;
            }
            if (buttonIDs != null) {
                button.visible = Arrays.stream(buttonIDs).anyMatch(id -> Objects.equals(id, button.id));
            } else {
                button.visible = false;
            }
        }
    }


    public void changePage(int pageIn) {
        if (pageIn > pagesCount || currentPage == pageIn) {
            return;
        }
        onPageChange(currentPage, pageIn);
        currentPage = pageIn;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private void actionPerformed(int id) {
        for (ButtonPage buttonPage : buttonsPage) {
            buttonPage.enabled = buttonPage.id != id;
        }
    }

    public void renderHoveredText(GuiScreen screen, int mouseX, int mouseY) {
        for (ButtonPage buttonPage : buttonsPage) {
            buttonPage.renderHoveredText(screen, mouseX, mouseY);
        }
    }

    public void processButtonActionPerformed(GuiButton button) {
        if (button.id >= startButtonID) {
            changePage(button.id - startButtonID);
            actionPerformed(button.id);
        }
    }
}
