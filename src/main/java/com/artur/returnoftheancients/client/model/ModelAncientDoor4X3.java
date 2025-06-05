package com.artur.returnoftheancients.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class ModelAncientDoor4X3 extends ModelBase {
	private final ModelRenderer door;
	private final ModelRenderer door1;
	private final ModelRenderer arch;
	private final ModelRenderer arch1;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;

	public ModelAncientDoor4X3() {
		textureWidth = 256;
		textureHeight = 256;

		door = new ModelRenderer(this);
		door.setRotationPoint(0.0F, 24.0F, -5.0F);
		door.cubeList.add(new ModelBox(door, 0, 68, -1.0F, -44.0F, 1.0F, 2, 44, 28, 0.0F, false));

		door1 = new ModelRenderer(this);
		door1.setRotationPoint(0.0F, 24.0F, 41.0F);
		door1.cubeList.add(new ModelBox(door1, 60, 68, -1.0F, -44.0F, -17.0F, 2, 44, 28, 0.0F, false));

		arch = new ModelRenderer(this);
		arch.setRotationPoint(1.0F, 24.0F, 55.0F);
		arch.cubeList.add(new ModelBox(arch, 0, 0, -4.0F, -48.0F, -63.0F, 6, 4, 64, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 120, 68, -4.0F, -44.0F, -63.0F, 6, 44, 4, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 120, 116, -4.0F, -44.0F, -3.0F, 6, 44, 4, 0.0F, false));

		arch1 = new ModelRenderer(this);
		arch1.setRotationPoint(-2.0F, -12.0F, -3.0F);


		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		arch1.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 140, 0.0F, -13.0F, -4.0F, 1, 16, 5, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 140, 0, 3.0F, -13.0F, -4.0F, 1, 16, 5, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, -2.0F, -2.0F);
		arch1.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 24, 140, 0.0F, -10.0F, -4.0F, 1, 10, 3, 0.0F, false));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 32, 140, 3.0F, -10.0F, -4.0F, 1, 10, 3, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, -5.0F, 60.0F);
		arch1.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.7854F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 40, 140, 0.0F, -10.0F, -4.0F, 1, 10, 3, 0.0F, false));
		cube_r3.cubeList.add(new ModelBox(cube_r3, 140, 42, 3.0F, -10.0F, -4.0F, 1, 10, 3, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, -2.0F, 56.0F);
		arch1.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.7854F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 12, 140, 0.0F, -13.0F, -4.0F, 1, 16, 5, 0.0F, false));
		cube_r4.cubeList.add(new ModelBox(cube_r4, 140, 21, 3.0F, -13.0F, -4.0F, 1, 16, 5, 0.0F, false));
	}

	public void renderArch() {
		arch.render(1.0F / 16.0F);
		arch1.render(1.0F / 16.0F);
	}

	public void renderDoor1() {
		door.render(1.0F / 16.0F);
	}

	public void renderDoor2() {
		door1.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}