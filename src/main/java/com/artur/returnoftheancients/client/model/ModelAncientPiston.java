package com.artur.returnoftheancients.client.model;
// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelAncientPiston extends ModelBase {
	private final ModelRenderer piston;
	private final ModelRenderer base;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;

	public ModelAncientPiston() {
		textureWidth = 128;
		textureHeight = 128;

		piston = new ModelRenderer(this);
		piston.setRotationPoint(5.0F, 14.0F, 0.0F);
		piston.cubeList.add(new ModelBox(piston, 0, 19, -10.0F, -6.0F, -5.0F, 10, 2, 10, 0.0F, false));
		piston.cubeList.add(new ModelBox(piston, 0, 32, -9.0F, -4.0F, -4.0F, 8, 12, 8, 0.0F, false));

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		base.cubeList.add(new ModelBox(base, 3, 2, -7.0F, -2.0F, -7.0F, 14, 2, 14, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 64, 3, 4.0F, -5.0F, -5.0F, 1, 1, 10, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 42, 20, -4.0F, -5.0F, -5.0F, 8, 1, 1, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 42, 24, -4.0F, -5.0F, 4.0F, 8, 1, 1, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 65, 21, -5.0F, -5.0F, -5.0F, 1, 1, 10, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(7.0F, 0.0F, 0.0F);
		base.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.48F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 35, 34, -1.9F, -4.6F, -5.0F, 2, 3, 10, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-7.0F, 0.0F, 0.0F);
		base.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.48F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 35, 50, -0.1F, -4.6F, -5.0F, 2, 3, 10, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, -7.0F);
		base.addChild(cube_r3);
		setRotationAngle(cube_r3, -0.48F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 63, 33, -5.0F, -4.6F, -0.1F, 10, 3, 2, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, 7.0F);
		base.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.48F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 63, 39, -5.0F, -4.6F, -1.9F, 10, 3, 2, 0.0F, false));
	}

	public void renderBase() {
		this.base.render(1.0F / 16.0F);
	}

	public void renderPiston() {
		this.piston.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}