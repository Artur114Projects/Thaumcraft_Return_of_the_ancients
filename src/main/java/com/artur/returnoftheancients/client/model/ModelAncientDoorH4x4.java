package com.artur.returnoftheancients.client.model;
// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelAncientDoorH4x4 extends ModelBase {
	private final ModelRenderer arch;
	private final ModelRenderer door;
	private final ModelRenderer door1;

	public ModelAncientDoorH4x4() {
		textureWidth = 256;
		textureHeight = 256;

		arch = new ModelRenderer(this);
		arch.setRotationPoint(0.0F, 24.0F, 0.0F);
		arch.cubeList.add(new ModelBox(arch, 136, 136, -8.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 169, -8.0F, -16.0F, -8.0F, 16, 3, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 144, -8.0F, -9.0F, 40.0F, 16, 9, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 64, 169, -8.0F, -16.0F, 40.0F, 16, 3, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 64, 144, -56.0F, -9.0F, 40.0F, 16, 9, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 180, 0, -56.0F, -16.0F, 40.0F, 16, 3, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 128, 161, -56.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 180, 19, -56.0F, -16.0F, -8.0F, 16, 3, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 68, 6.0F, -16.0F, 8.0F, 2, 10, 32, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 136, 190, 6.0F, -13.0F, -8.0F, 2, 4, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 172, 190, 6.0F, -13.0F, 40.0F, 2, 4, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 192, -56.0F, -13.0F, 40.0F, 2, 4, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 192, 161, -56.0F, -13.0F, -8.0F, 2, 4, 16, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 68, 68, -56.0F, -16.0F, 8.0F, 2, 10, 32, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 180, 38, -40.0F, -16.0F, 54.0F, 32, 10, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 180, 50, -40.0F, -16.0F, -8.0F, 32, 10, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 110, 4.0F, -9.0F, 8.0F, 2, 2, 32, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 68, 110, -54.0F, -9.0F, 8.0F, 2, 2, 32, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 180, 62, -40.0F, -9.0F, -6.0F, 32, 2, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 128, 186, -40.0F, -15.0F, -6.0F, 32, 2, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 0, 188, -40.0F, -9.0F, 52.0F, 32, 2, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 68, 190, -40.0F, -15.0F, 52.0F, 32, 2, 2, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 136, 68, 4.0F, -15.0F, 8.0F, 2, 2, 32, 0.0F, false));
		arch.cubeList.add(new ModelBox(arch, 136, 102, -54.0F, -15.0F, 8.0F, 2, 2, 32, 0.0F, false));

		door = new ModelRenderer(this);
		door.setRotationPoint(0.0F, 24.0F, 0.0F);
		door.cubeList.add(new ModelBox(door, 0, 0, -54.0F, -13.0F, 24.0F, 60, 4, 30, 0.0F, false));

		door1 = new ModelRenderer(this);
		door1.setRotationPoint(0.0F, 24.0F, 0.0F);
		door1.cubeList.add(new ModelBox(door1, 0, 34, -54.0F, -13.0F, -6.0F, 60, 4, 30, 0.0F, false));
	}

	public void renderArch() {
		arch.render(1.0F / 16.0F);
	}

	public void renderDoor1() {
		door1.render(1.0F / 16.0F);
	}

	public void renderDoor2() {
		door.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}