package satisfy.beachparty.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import satisfy.beachparty.BeachpartyIdentifier;

@SuppressWarnings("unused")
public class RubberRingPelicanModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.of("rubber_ring_pelican"), "main");
	private final ModelPart group;
	private final ModelPart comb;
	private final ModelPart bone;
	public RubberRingPelicanModel(ModelPart root) {
		this.group = root.getChild("group");
		this.comb = root.getChild("comb");
		this.bone = root.getChild("bone");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition group = modelPartData.addOrReplaceChild("group", CubeListBuilder.create().texOffs(7, 0).addBox(-5.5F, -2.5F, -15.5F, 14.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(50, 26).addBox(5.5F, 1.5F, -4.5F, -8.0F, -4.0F, -8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 22.5F, 8.5F));

		PartDefinition comb = modelPartData.addOrReplaceChild("comb", CubeListBuilder.create().texOffs(1, 31).addBox(-2.0F, 3.0F, -1.0F, 4.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(1, 22).addBox(-2.0F, 2.0F, -1.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -17.0F));

		PartDefinition bone = modelPartData.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -10.0F, 3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(18, 18).addBox(0.0F, -7.0F, 7.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -13.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int k) {
		group.render(poseStack, vertexConsumer, i, j, k);
		comb.render(poseStack, vertexConsumer, i, j, k);
		bone.render(poseStack, vertexConsumer, i, j, k);
	}
}