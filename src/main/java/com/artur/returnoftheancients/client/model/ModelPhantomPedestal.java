package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPhantomPedestal extends ModelBase {
	private final ModelRenderer clot;
	private final ModelRenderer bb_main;

	public ModelPhantomPedestal() {
		textureWidth = 16;
		textureHeight = 16;

		clot = new ModelRenderer(this);
		clot.setRotationPoint(0.0F, 1.0F, 0.0F);
		clot.cubeList.add(new ModelBox(clot, -3, -2, -2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F, false));

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -8.0F, -4.0F, -8.0F, 16, 4, 16, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -12.0F, -4.0F, 8, 8, 8, 0.0F, false));
		bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -16.0F, -6.0F, 12, 4, 12, 0.0F, false));
	}

	public void renderBase() {
		bb_main.render(1.0F / 16.0F);
	}

	public void renderClot() {
		clot.render(1.0F / 16.0F);
	}

	public void setClotRotationAngle(float x, float y, float z) {
		this.setRotationAngle(this.clot, x, y, z);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}