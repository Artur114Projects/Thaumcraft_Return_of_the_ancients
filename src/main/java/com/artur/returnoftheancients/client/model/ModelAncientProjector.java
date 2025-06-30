package com.artur.returnoftheancients.client.model;
// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelAncientProjector extends ModelBase {
	private final ModelRenderer bb_main;

	public ModelAncientProjector() {
		textureWidth = 80;
		textureHeight = 80;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -8.0F, -16.0F, -8.0F, 16, 10, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 44, -8.0F, -6.0F, 6.0F, 16, 6, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 36, 44, -8.0F, -6.0F, -8.0F, 16, 6, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 26, -8.0F, -6.0F, -6.0F, 2, 6, 12, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 28, 26, 6.0F, -6.0F, -6.0F, 2, 6, 12, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 52, -6.0F, -6.0F, -6.0F, 12, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 28, 52, -6.0F, -6.0F, 4.0F, 12, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 56, -6.0F, -6.0F, -4.0F, 2, 2, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 20, 56, 4.0F, -6.0F, -4.0F, 2, 2, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 56, 26, -1.0F, -6.0F, -1.0F, 2, 2, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 56, 34, 1.0F, -6.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 56, 37, -2.0F, -6.0F, -1.0F, 1, 1, 2, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 56, 30, -2.0F, -6.0F, 1.0F, 4, 1, 1, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 56, 32, -2.0F, -6.0F, -2.0F, 4, 1, 1, 0.0F, false));
	}

	public void renderAll() {
		bb_main.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}