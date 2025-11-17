package com.artur.returnoftheancients.client.model;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.util.math.MathUtils;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelEnergyLine extends ModelBase {
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer bone;

	public ModelEnergyLine() {
		textureWidth = 16;
		textureHeight = 16;

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-8.0F, 8.0F, 8.0F);
		cube_r1.cubeList.add(new ModelBoxP(cube_r1, 0, 0, -1.5F, 2.0F, -1.5F, 3, 6, 3, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 8.0F, 0.0F);
		bone2.addChild(cube_r1);

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

	private static class ModelBoxP extends ModelBox {
		private final List<TexturedQuad> quadList;

		public ModelBoxP(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta)
		{
			this(renderer, texU, texV, x, y, z, dx, dy, dz, delta, renderer.mirror);
		}

		public ModelBoxP(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
			super(renderer, texU, texV, x, y, z, dx, dy, dz, delta, mirror);
			this.quadList = new ArrayList<>();
			float f = x + (float)dx;
			float f1 = y + (float)dy;
			float f2 = z + (float)dz;
			x = x - delta;
			y = y - delta;
			z = z - delta;
			f = f + delta;
			f1 = f1 + delta;
			f2 = f2 + delta;

			if (mirror)
			{
				float f3 = f;
				f = x;
				x = f3;
			}

			PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
			PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
			PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
			PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
			PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
			PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
			PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
			PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight));
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight));
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight));
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dx, texV, renderer.textureWidth, renderer.textureHeight));
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight));
			this.quadList.add(new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, texU + dz + dx + dz, texV + dz, texU + dz + dx + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight));

			if (mirror) {
				for (TexturedQuad texturedquad : this.quadList) {
					texturedquad.flipFace();
				}
			}
		}

		@SideOnly(Side.CLIENT)
		public void render(BufferBuilder renderer, float scale) {
			int[] arr = new int[] {2, 3};
			int i = 0;
			for (TexturedQuad texturedquad : this.quadList) {
				if (MiscHandler.arrayContains(arr, i)) {
					i++; continue;
				}
				texturedquad.draw(renderer, scale);
				i++;
			}
		}

		public ModelBoxP setBoxName(String name) {
			this.boxName = name;
			return this;
		}
	}
}