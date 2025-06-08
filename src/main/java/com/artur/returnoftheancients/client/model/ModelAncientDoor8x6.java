package com.artur.returnoftheancients.client.model;
// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelAncientDoor8x6 extends ModelBase {
	private final ModelRenderer arch;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer door;
	private final ModelRenderer door1;

	public ModelAncientDoor8x6() {
		textureWidth = 320;
		textureHeight = 320;

		arch = new ModelRenderer(this);
		arch.setRotationPoint(0.0F, -71.0F, -8.0F);
		arch.cubeList.add(new ModelBox(arch, 80, 148, -4.0F, 5.0F, 0.0F, 8, 88, 4, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 170, 244, 1.0F, 15.0F, 4.0F, 2, 64, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 178, 244, -3.0F, 15.0F, 4.0F, 2, 64, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 194, 244, -3.0F, 15.0F, 122.0F, 2, 64, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 186, 244, 1.0F, 15.0F, 122.0F, 2, 64, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 104, 148, -4.0F, 5.0F, 124.0F, 8, 88, 4, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 176, 148, 3.0F, 79.0F, 0.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 176, 180, -8.0F, 79.0F, 0.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 248, 132, -6.0F, -1.0F, 16.0F, 2, 6, 3, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 260, 152, 4.0F, -1.0F, 16.0F, 2, 6, 3, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 260, 161, 4.0F, -1.0F, 109.0F, 2, 6, 3, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 260, 170, -6.0F, -1.0F, 109.0F, 2, 6, 3, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 202, 244, -8.0F, -1.0F, 0.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 218, 148, -8.0F, -1.0F, 0.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 176, 212, 3.0F, -1.0F, 0.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 218, 212, -8.0F, -1.0F, 112.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 218, 180, 3.0F, -1.0F, 112.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 80, 240, -8.0F, 79.0F, 112.0F, 5, 16, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 128, 236, 3.0F, 79.0F, 112.0F, 5, 16, 16, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(5.0F, 93.0F, 0.0F);
		arch.addChild(cube_r1);
		setRotationAngle(cube_r1, -1.5708F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 64, 148, -4.0F, -124.0F, -2.0F, 2, 120, 2, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 72, 148, -8.0F, -124.0F, -2.0F, 2, 120, 2, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(5.0F, 7.0F, 0.0F);
		arch.addChild(cube_r2);
		setRotationAngle(cube_r2, -1.5708F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 56, 148, -4.0F, -124.0F, -2.0F, 2, 120, 2, 0.0F, false));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 48, 148, -8.0F, -124.0F, -2.0F, 2, 120, 2, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
		arch.addChild(cube_r3);
		setRotationAngle(cube_r3, -1.5708F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 148, -4.0F, -128.0F, -1.0F, 8, 128, 6, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 94.0F, 0.0F);
		arch.addChild(cube_r4);
		setRotationAngle(cube_r4, -1.5708F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 28, 148, -4.0F, -128.0F, -1.0F, 8, 128, 2, 0.0F, false));

		door = new ModelRenderer(this);
		door.setRotationPoint(0.0F, -48.0F, 112.0F);
		door.cubeList.add(new ModelBox(door, 124, 0, -1.0F, -18.0F, -56.0F, 2, 88, 60, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 244, 244, 1.0F, 56.0F, -8.0F, 2, 12, 12, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 248, 72, 1.0F, -16.0F, -8.0F, 2, 8, 12, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 248, 92, -3.0F, -16.0F, -8.0F, 2, 8, 12, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 248, 0, -3.0F, 56.0F, -8.0F, 2, 12, 12, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 152, 148, -3.0F, -16.0F, -56.0F, 2, 84, 4, 0.0F, false));
		door.cubeList.add(new ModelBox(door, 164, 148, 1.0F, -16.0F, -56.0F, 2, 84, 4, 0.0F, false));

		door1 = new ModelRenderer(this);
		door1.setRotationPoint(0.0F, 24.0F, 0.0F);
		door1.cubeList.add(new ModelBox(door1, 0, 0, -1.0F, -90.0F, -4.0F, 2, 88, 60, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 128, 148, -3.0F, -88.0F, 52.0F, 2, 84, 4, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 140, 148, 1.0F, -88.0F, 52.0F, 2, 84, 4, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 248, 24, -3.0F, -16.0F, -4.0F, 2, 12, 12, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 248, 112, -3.0F, -88.0F, -4.0F, 2, 8, 12, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 260, 132, 1.0F, -88.0F, -4.0F, 2, 8, 12, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 248, 48, 1.0F, -16.0F, -4.0F, 2, 12, 12, 0.0F, false));
	}

	public void renderArch() {
		arch.render(1.0F / 16.0F);
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