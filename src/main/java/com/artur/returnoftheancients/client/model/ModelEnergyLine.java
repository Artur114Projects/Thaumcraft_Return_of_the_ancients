package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumFacing;

public class ModelEnergyLine extends ModelBase {
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer bone;

	public ModelEnergyLine() {
		textureWidth = 16;
		textureHeight = 16;

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 8.0F, 0.0F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-8.0F, 8.0F, 8.0F);
		bone2.addChild(cube_r1);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -1.5F, 2.0F, -1.5F, 3, 6, 3, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -9.5F, -1.5F, 6.5F, 3, 3, 3, 0.0F, false));
	}

	public void renderBase() {
		bone.render(1.0F / 16.0F);
	}

	public void renderBone2(EnumFacing facing) {
		switch (facing) {
			case DOWN:
				this.setRotationAngle(cube_r1, (float) -Math.PI, 0.0F, 0.0F);
				bone2.render(1.0F / 16.0F);
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				break;
			case UP:
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				bone2.render(1.0F / 16.0F);
				break;
			case WEST:
				this.setRotationAngle(cube_r1, (float) (Math.PI / 2), (float) (Math.PI / 2), 0.0F);
				bone2.render(1.0F / 16.0F);
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				break;
			case EAST:
				this.setRotationAngle(cube_r1, (float) (Math.PI / 2), (float) (-Math.PI / 2), 0.0F);
				bone2.render(1.0F / 16.0F);
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				break;
			case SOUTH:
				this.setRotationAngle(cube_r1, (float) (Math.PI / 2), 0.0F, 0.0F);
				bone2.render(1.0F / 16.0F);
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				break;
			case NORTH:
				this.setRotationAngle(cube_r1, (float) (Math.PI / 2), (float) Math.PI, 0.0F);
				bone2.render(1.0F / 16.0F);
				this.setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0F);
				break;
		}
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}