package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModelIncinerator extends ModelBase {
	private final List<ModelRenderer> lavaRenders = new ArrayList<>();

	private final ModelRenderer base;
	private final ModelRenderer jetBase;
	private final ModelRenderer jet;
	private final ModelRenderer glass;
	private ModelRenderer lava;

	public ModelIncinerator() {
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this);
		base.setRotationPoint(0.0F, 24.0F, 0.0F);
		base.cubeList.add(new ModelBox(base, 0, 0, -8.0F, -2.0F, -8.0F, 16, 2, 16, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 38, 61, -4.0F, -3.7F, 5.0F, 8, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 61, 51, -4.0F, -3.7F, -7.0F, 8, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 0, 39, 5.0F, -3.7F, -4.0F, 2, 2, 8, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 70, 35, 4.0F, -3.7F, -6.0F, 2, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 60, 71, 4.0F, -3.7F, 4.0F, 2, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 74, 0, -6.0F, -3.7F, 4.0F, 2, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 74, 5, -6.0F, -3.7F, -6.0F, 2, 2, 2, 0.0F, false));
		base.cubeList.add(new ModelBox(base, 21, 43, -7.0F, -3.7F, -4.0F, 2, 2, 8, 0.0F, false));

		jetBase = new ModelRenderer(this);
		jetBase.setRotationPoint(0.0F, 19.0F, 0.0F);
		jetBase.cubeList.add(new ModelBox(jetBase, 33, 19, -4.0F, -10.0F, -5.0F, 8, 1, 10, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 0, 50, -5.0F, -10.0F, -4.0F, 1, 1, 8, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 42, 51, 4.0F, -10.0F, -4.0F, 1, 1, 8, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 0, 60, 4.0F, 2.0F, -4.0F, 1, 1, 8, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 33, 31, -4.0F, 2.0F, -5.0F, 8, 1, 10, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 19, 54, -5.0F, 2.0F, -4.0F, 1, 1, 8, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 61, 56, -5.0F, -9.0F, -4.0F, 1, 11, 3, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 19, 64, -5.0F, -9.0F, 1.0F, 1, 11, 3, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 37, 66, -4.0F, -9.0F, 4.0F, 3, 11, 1, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 46, 66, 1.0F, -9.0F, 4.0F, 3, 11, 1, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 9, 70, 1.0F, -9.0F, -5.0F, 3, 11, 1, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 0, 70, -4.0F, -9.0F, -5.0F, 3, 11, 1, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 65, 0, 4.0F, -9.0F, -4.0F, 1, 11, 3, 0.0F, false));
		jetBase.cubeList.add(new ModelBox(jetBase, 28, 64, 4.0F, -9.0F, 1.0F, 1, 11, 3, 0.0F, false));

		jet = new ModelRenderer(this);
		jet.setRotationPoint(0.0F, 7.5F, 0.0F);
		jet.cubeList.add(new ModelBox(jet, 42, 43, -2.0F, 0.5F, -3.0F, 4, 1, 6, 0.0F, false));
		jet.cubeList.add(new ModelBox(jet, 63, 43, 2.0F, 0.5F, -2.0F, 1, 1, 4, 0.0F, false));
		jet.cubeList.add(new ModelBox(jet, 70, 29, -3.0F, 0.5F, -2.0F, 1, 1, 4, 0.0F, false));

		glass = new ModelRenderer(this);
		glass.setRotationPoint(0.0F, 17.0F, 0.0F);
		glass.cubeList.add(new ModelBox(glass, 70, 56, -1.0F, -7.0F, -4.6F, 2, 11, 0, 0.0F, false));
		glass.cubeList.add(new ModelBox(glass, 70, 68, -1.0F, -7.0F, 4.6F, 2, 11, 0, 0.0F, false));
		glass.cubeList.add(new ModelBox(glass, 75, 66, -4.6F, -7.0F, -1.0F, 0, 11, 2, 0.0F, false));
		glass.cubeList.add(new ModelBox(glass, 75, 54, 4.6F, -7.0F, -1.0F, 0, 11, 2, 0.0F, false));

		lava = new ModelRenderer(this);
		lava.setRotationPoint(0.0F, 23.0F, 0.0F);
		lava.cubeList.add(new ModelBox(lava, 0, 19, -4.0F, -13.0F, -4.0F, 8, 11, 8, 0.0F, false));

		this.compileRenders();
	}

	private void compileRenders() {
		this.lava.textureWidth = 32;
		this.lava.textureHeight = 320;

		for (int i = 0; i < 320; i += 11) {
			ModelRenderer lava = new ModelRenderer(this).setTextureSize(32, 320);
			lava.setRotationPoint(0.0F, 23.0F, 0.0F);
			lava.cubeList.add(new ModelBox(lava, 0, i, -4.0F, -13.0F, -4.0F, 8, 11, 8, 0.0F, false));
			this.lavaRenders.add(lava);
		}
	}

	public void setLavaBoxIndex(int index) {
		this.lava = this.lavaRenders.get(index & (this.lavaRenders.size() - 1));
	}

	public void renderBase() {
		base.render(1.0F / 16.0F);
	}

	public void renderJetBase() {
		jetBase.render(1.0F / 16.0F);
	}

	public void renderJet() {
		jet.render(1.0F / 16.0F);
	}

	public void renderGlass() {
		glass.render(1.0F / 16.0F);
	}

	public void renderLava() {
		lava.render(1.0F / 16.0F);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}