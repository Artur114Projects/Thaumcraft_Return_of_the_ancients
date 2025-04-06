package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelEnergyLine extends ModelBase {
	private final ModelRenderer bone2;
	private final ModelRenderer bone;

	public ModelEnergyLine() {
		textureWidth = 16;
		textureHeight = 16;

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-6.0F, 24.0F, 6.0F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, 4.3F, -5.9F, -7.7F, 3, 5, 3, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 16.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -1.7F, -1.7F, -1.7F, 3, 3, 3, 0.0F, false));
	}


	public void render(int index) {
		if (index == 0) {
			bone.render(1.0F / 16.0F);
		} else if (index == 1) {
			bone2.render(1.0F / 16.0F);
		}
	}
}