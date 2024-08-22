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
public class BeachHatModel<T extends Entity> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BeachpartyIdentifier.of("beach_hat"), "main");

	private final ModelPart beach_hat;
	public BeachHatModel(ModelPart root) {
		this.beach_hat = root.getChild("beach_hat");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition beach_hat = modelPartData.addOrReplaceChild("beach_hat", CubeListBuilder.create().texOffs(-22, 15).addBox(-11.0F, 0.0F, -11.0F, 22.0F, 0.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, -5.01F, -5.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 39).addBox(-5.25F, -5.6F, -5.25F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(42, 6).addBox(1.1F, -0.25F, -11.0F, 2.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(49, 14).addBox(1.1F, -0.25F, -11.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int k) {
		beach_hat.render(poseStack, vertexConsumer, i, j, k);
	}
}