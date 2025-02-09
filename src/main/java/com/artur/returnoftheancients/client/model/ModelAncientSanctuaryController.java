package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelAncientSanctuaryController extends ModelBase {
	private final ModelRenderer columns;
	private final ModelRenderer base;
	private final ModelRenderer door0;
	private final ModelRenderer door1;
	private final ModelRenderer door2;
	private final ModelRenderer door3;

	public ModelAncientSanctuaryController() {
		textureWidth = 64;
		textureHeight = 64;

		columns = new ModelRenderer(this);
		columns.setRotationPoint(6.0F, 20.0F, 6.0F);
		columns.cubeList.add(new ModelBox(columns, 43, 0, -11.0F, -11.0F, -3.0F, 2, 11, 2, 0.0F, false));
		columns.cubeList.add(new ModelBox(columns, 2, 0, -3.0F, -11.0F, -3.0F, 2, 11, 2, 0.0F, false));
		columns.cubeList.add(new ModelBox(columns, 27, 15, -3.0F, -11.0F, -11.0F, 2, 11, 2, 0.0F, false));
		columns.cubeList.add(new ModelBox(columns, 51, 0, -11.0F, -11.0F, -11.0F, 2, 11, 2, 0.0F, false));

		base = new ModelRenderer(this);
		base.setRotationPoint(6.0F, 10.0F, 6.0F);
		base.cubeList.add(new ModelBox(base, 23, 47, -8.0F, 9.0F, -8.0F, 4, 1, 4, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 23, 47, -8.0F, 0.0F, -8.0F, 4, 1, 4, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 0, 0, -13.0F, 13.0F, -13.0F, 14, 1, 14, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 1, 46, -12.0F, 12.0F, -11.0F, 12, 1, 10, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 1, 60, -11.0F, 12.0F, -12.0F, 10, 1, 1, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 1, 58, -11.0F, 12.0F, -1.0F, 10, 1, 1, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 0, 34, -11.0F, 10.0F, -11.0F, 10, 2, 10, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 0, 26, -11.0F, -2.0F, -9.0F, 10, 2, 6, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 10, 18, -9.0F, -2.0F, -3.0F, 6, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 10, 22, -9.0F, -2.0F, -11.0F, 6, 2, 2, 0.0F, false));

		door0 = new ModelRenderer(this);
		door0.setRotationPoint(0.0F, 24.0F, 0.0F);
		door0.cubeList.add(new ModelBox(door0, 56, 40, -4.1F, -14.0F, -4.9F, 1, 10, 3, 0.0F, false));
		door0.cubeList.add(new ModelBox(door0, 56, 40, 3.1F, -14.0F, -4.9F, 1, 10, 3, 0.0F, false));

		door1 = new ModelRenderer(this);
		door1.setRotationPoint(0.0F, 24.0F, 0.0F);
		door1.cubeList.add(new ModelBox(door1, 48, 40, -4.1F, -14.0F, 1.9F, 1, 10, 3, 0.0F, false));
		door1.cubeList.add(new ModelBox(door1, 48, 40, 3.1F, -14.0F, 1.9F, 1, 10, 3, 0.0F, false));

		door2 = new ModelRenderer(this);
		door2.setRotationPoint(0.0F, 24.0F, -1.0F);
		door2.cubeList.add(new ModelBox(door2, 56, 53, -4.9F, -14.0F, 4.1F, 3, 10, 1, 0.0F, false));
		door2.cubeList.add(new ModelBox(door2, 56, 53, -4.9F, -14.0F, -3.1F, 3, 10, 1, 0.0F, false));

		door3 = new ModelRenderer(this);
		door3.setRotationPoint(0.0F, 24.0F, 0.0F);
		door3.cubeList.add(new ModelBox(door3, 48, 53, 1.9F, -14.0F, -4.1F, 3, 10, 1, 0.0F, false));
		door3.cubeList.add(new ModelBox(door3, 48, 53, 1.9F, -14.0F, 3.1F, 3, 10, 1, 0.0F, false));
	}

	public void renderAll() {
		columns.render(1.0F / 16.0F);
		base.render(1.0F / 16.0F);
		door0.render(1.0F / 16.0F);
		door1.render(1.0F / 16.0F);
		door2.render(1.0F / 16.0F);
		door3.render(1.0F / 16.0F);
	}

	public void setDoorProgress(float prevProgress, float progress, float partialTicks) {
		prevProgress = MathHelper.clamp(prevProgress, 0.0F, 1.0F);
		progress = MathHelper.clamp(progress, 0.0F, 1.0F);

		float finalProgress = prevProgress + (progress - prevProgress) * partialTicks;

		float offset = 1.9F / 16.0F;

		door0.offsetZ = offset * finalProgress;
		door1.offsetZ = -(offset * finalProgress);
		door2.offsetX = offset * finalProgress;
		door3.offsetX = -(offset * finalProgress);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}