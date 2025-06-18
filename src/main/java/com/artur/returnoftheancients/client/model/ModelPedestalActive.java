package com.artur.returnoftheancients.client.model;
// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelPedestalActive extends ModelBase {
	private final ModelRenderer bb_main;
	private final ModelRenderer cube_r1;

	public ModelPedestalActive() {
		textureWidth = 64;
		textureHeight = 64;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 40, 0, 0.0F, -17.0F, -4.0F, 4, 3, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 16, -7.0F, -3.0F, -7.0F, 14, 3, 14, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 1, 33, -4.0F, -11.0F, -4.0F, 8, 8, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 34, 33, -3.0F, -14.0F, -4.0F, 7, 3, 8, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(1.0F, -14.0F, -6.0F);
		bb_main.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.7854F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -7.2F, -3.0F, 0.0F, 12, 3, 12, 0.0F, false));
	}

	public void renderAll() {
		this.bb_main.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}