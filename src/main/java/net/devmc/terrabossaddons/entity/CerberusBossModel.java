package net.devmc.terrabossaddons.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CerberusBossModel<T extends CerberusBoss> extends SinglePartEntityModel<T> {
	final ModelPart cerberus;
	private final ModelPart body;
	private final ModelPart neck1;
	private final ModelPart head1;
	//private final ModelPart jaws1;
	private final ModelPart neck2;
	private final ModelPart head2;
	//private final ModelPart jaws2;
	private final ModelPart neck3;
	private final ModelPart head3;
	//private final ModelPart jaws3;
	//private final ModelPart tail1;
	//private final ModelPart tail2;
	//private final ModelPart tail3;
	//private final ModelPart tail4;
	//private final ModelPart tail5;
	//private final ModelPart left_leg;
	//private final ModelPart left_foreleg2;
	//private final ModelPart left_foreleg1;
	//private final ModelPart left_foot;
	//private final ModelPart right_leg;
	//private final ModelPart right_foreleg2;
	//private final ModelPart right_foreleg1;
	//private final ModelPart right_foot;
	//private final ModelPart left_arm;
	//private final ModelPart left_forearm2;
	//private final ModelPart left_forearm1;
	//private final ModelPart left_hand;
	//private final ModelPart right_arm;
	//private final ModelPart right_forearm2;
	//private final ModelPart right_forearm1;
	//private final ModelPart right_hand;

	public CerberusBossModel(ModelPart root) {
		this.cerberus = root.getChild("main");
		this.body = this.cerberus.getChild("body");
		this.neck1 = this.body.getChild("neck1");
		this.head1 = this.neck1.getChild("head1");
		//this.jaws1 = this.head1.getChild("jaws1");
		this.neck2 = this.body.getChild("neck2");
		this.head2 = this.neck2.getChild("head2");
		//this.jaws2 = this.head2.getChild("jaws2");
		this.neck3 = this.body.getChild("neck3");
		this.head3 = this.neck3.getChild("head3");
		//this.jaws3 = this.head3.getChild("jaws3");
		//this.tail1 = this.body.getChild("tail1");
		//this.tail2 = this.tail1.getChild("tail2");
		//this.tail3 = this.tail2.getChild("tail3");
		//this.tail4 = this.tail3.getChild("tail4");
		//this.tail5 = this.tail4.getChild("tail5");
		//this.left_leg = this.main.getChild("left_leg");
		//this.left_foreleg2 = this.left_leg.getChild("left_foreleg2");
		//this.left_foreleg1 = this.left_foreleg2.getChild("left_foreleg1");
		//this.left_foot = this.left_leg.getChild("left_foot");
		//this.right_leg = this.main.getChild("right_leg");
		//this.right_foreleg2 = this.right_leg.getChild("right_foreleg2");
		//this.right_foreleg1 = this.right_foreleg2.getChild("right_foreleg1");
		//this.right_foot = this.right_leg.getChild("right_foot");
		//this.left_arm = this.main.getChild("left_arm");
		//this.left_forearm2 = this.left_arm.getChild("left_forearm2");
		//this.left_forearm1 = this.left_forearm2.getChild("left_forearm1");
		//this.left_hand = this.left_arm.getChild("left_hand");
		//this.right_arm = this.main.getChild("right_arm");
		//this.right_forearm2 = this.right_arm.getChild("right_forearm2");
		//this.right_forearm1 = this.right_forearm2.getChild("right_forearm1");
		//this.right_hand = this.right_arm.getChild("right_hand");
	}

	@Override
	public void setAngles(CerberusBoss entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		setHeadAngles(netHeadYaw, headPitch);

		if (entity.isAttacking()) {
			if (entity.isRushing()) this.animateMovement(CerberusBoss.CERBERUS_RUNNING, limbSwing, limbSwingAmount, 2F, 2.5F);
			else this.animateMovement(entity.getAttack() == 1 ? CerberusBoss.CERBERUS_ATTACK1 : entity.getAttack() == 2 ? CerberusBoss.CERBERUS_ATTACK2 : CerberusBoss.CERBERUS_ATTACK3, limbSwing, limbSwingAmount, 2F, 2.5F);
		}
		else {
			if (entity.isSprinting())
				this.animateMovement(CerberusBoss.CERBERUS_RUNNING, limbSwing, limbSwingAmount, 2F, 2.5F);
			else if (entity.isWalking())
				this.animateMovement(CerberusBoss.CERBERUS_WALKING, limbSwing, limbSwingAmount, 2F, 2.5F);
		}

		this.updateAnimation(entity.idleAnimationState, CerberusBoss.CERBERUS_IDLE, ageInTicks, 1F);
	}

	private void setHeadAngles(float headYaw, float headPitch) {
		headYaw = MathHelper.clamp(headYaw, -30, 30);
		headPitch = MathHelper.clamp(headPitch, -25, 45);

		this.head1.yaw = headYaw * 0.017453292F;
		this.head1.pitch = headPitch * 0.017453292F;

		this.head2.yaw = headYaw * 0.017453292F;
		this.head2.pitch = headPitch * 0.017453292F;

		this.head3.yaw = headYaw * 0.017453292F;
		this.head3.pitch = headPitch * 0.017453292F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		matrices.scale(2, 2, 2);
		cerberus.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return cerberus;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = main.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-14.5F, -14.6857F, -33.5161F, 29.0F, 31.0F, 41.0F, new Dilation(0.0F))
		.uv(80, 31).mirrored().cuboid(0.0F, -22.6857F, -33.5161F, 0.0F, 8.0F, 41.0F, new Dilation(0.0F)).mirrored(false)
		.uv(0, 10).mirrored().cuboid(0.0F, -21.6857F, 14.4839F, 0.0F, 8.0F, 19.0F, new Dilation(0.0F)).mirrored(false)
		.uv(80, 85).mirrored().cuboid(0.0F, -18.6857F, 7.4839F, 0.0F, 8.0F, 7.0F, new Dilation(0.0F)).mirrored(false)
		.uv(0, 72).cuboid(-13.0F, -10.6857F, 3.4839F, 26.0F, 25.0F, 28.0F, new Dilation(0.0F))
		.uv(99, 0).cuboid(-16.0F, -13.6857F, 14.4839F, 32.0F, 22.0F, 19.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -36.3143F, 15.5161F));

		ModelPartData neck1 = body.addChild("neck1", ModelPartBuilder.create(), ModelTransform.pivot(9.6342F, -8.2094F, -23.6254F));

		ModelPartData cube_r1 = neck1.addChild("cube_r1", ModelPartBuilder.create().uv(158, 111).cuboid(-8.625F, -11.25F, 2.075F, 15.0F, 19.0F, 12.0F, new Dilation(0.0F))
		.uv(139, 150).cuboid(-9.625F, -5.25F, 1.075F, 17.0F, 14.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(3.2439F, -3.3774F, -7.5081F, -0.6865F, -0.4116F, 0.1539F));

		ModelPartData cube_r2 = neck1.addChild("cube_r2", ModelPartBuilder.create().uv(80, 80).cuboid(-7.5F, -11.25F, -10.0F, 15.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 226).cuboid(-12.5F, -16.25F, -7.0F, 26.0F, 30.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 192).cuboid(-8.5F, -5.25F, -10.0F, 17.0F, 14.0F, 6.0F, new Dilation(0.0F))
		.uv(47, 149).cuboid(-6.5F, -10.25F, -12.0F, 13.0F, 17.0F, 15.0F, new Dilation(0.0F))
		.uv(140, 41).cuboid(-7.5F, -4.25F, -13.0F, 15.0F, 12.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(1.389F, -2.3342F, -6.4873F, -0.674F, -0.3742F, 0.1212F));

		ModelPartData cube_r3 = neck1.addChild("cube_r3", ModelPartBuilder.create().uv(103, 164).cuboid(-5.5F, -8.75F, -9.0F, 10.0F, 14.0F, 15.0F, new Dilation(0.0F))
		.uv(0, 166).cuboid(-6.5F, -2.75F, -8.0F, 12.0F, 9.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(6.1736F, -9.9798F, -16.9893F, -0.4733F, -0.2743F, 0.0393F));

		ModelPartData head1 = neck1.addChild("head1", ModelPartBuilder.create().uv(182, 0).cuboid(-5.5F, -0.6904F, -27.4928F, 11.0F, 6.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 125).cuboid(-7.5F, -8.6904F, -15.4928F, 15.0F, 17.0F, 16.0F, new Dilation(0.0F))
		.uv(45, 181).cuboid(-8.5F, 1.3096F, -16.4928F, 17.0F, 8.0F, 9.0F, new Dilation(0.0F))
		.uv(124, 197).cuboid(-4.0F, 5.3096F, -25.9928F, 8.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(189, 75).cuboid(-6.5F, 0.3096F, -20.4928F, 13.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(7.2408F, -13.9609F, -19.3229F));

		ModelPartData cube_r4 = head1.addChild("cube_r4", ModelPartBuilder.create().uv(99, 0).cuboid(-2.0F, 1.0F, -2.5F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(136, 123).cuboid(-3.0F, -2.0F, -2.5F, 6.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0783F, -26.6809F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r5 = head1.addChild("cube_r5", ModelPartBuilder.create().uv(0, 72).cuboid(14.8576F, -7.9304F, 5.414F, 7.0F, 6.0F, 7.0F, new Dilation(0.0F))
		.uv(191, 57).cuboid(15.8576F, -6.9304F, -1.086F, 5.0F, 5.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(-18.3576F, 5.4506F, -24.102F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r6 = head1.addChild("cube_r6", ModelPartBuilder.create().uv(99, 6).mirrored().cuboid(-4.0F, -5.0F, 0.0F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-7.5F, -8.6904F, -8.4928F, 0.0291F, 0.2596F, -1.0253F));

		ModelPartData cube_r7 = head1.addChild("cube_r7", ModelPartBuilder.create().uv(99, 6).cuboid(-1.0F, -5.0F, 0.0F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(7.5F, -8.6904F, -8.4928F, 0.0291F, -0.2596F, 1.0253F));

		ModelPartData jaws1 = head1.addChild("jaws1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.6447F, -15.8386F));

		ModelPartData cube_r8 = jaws1.addChild("cube_r8", ModelPartBuilder.create().uv(157, 215).cuboid(-5.0F, -0.5F, -0.5F, 10.0F, 4.0F, 6.0F, new Dilation(0.0F))
		.uv(200, 103).cuboid(-3.5F, -2.0F, -4.5F, 7.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(170, 96).cuboid(-4.5F, 0.0F, -5.5F, 9.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.3351F, -5.1542F, 0.3054F, 0.0F, 0.0F));

		ModelPartData neck2 = body.addChild("neck2", ModelPartBuilder.create(), ModelTransform.of(-4.8532F, -6.1867F, -23.9336F, 0.2829F, 0.0831F, -0.0989F));

		ModelPartData cube_r9 = neck2.addChild("cube_r9", ModelPartBuilder.create().uv(158, 111).mirrored().cuboid(-9.3945F, -21.654F, -6.2637F, 15.0F, 19.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
		.uv(139, 150).mirrored().cuboid(-10.3945F, -15.654F, -7.2637F, 17.0F, 14.0F, 14.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0214F, 5.7964F, -12.4505F, -0.861F, 0.4116F, -0.1539F));

		ModelPartData cube_r10 = neck2.addChild("cube_r10", ModelPartBuilder.create().uv(80, 80).cuboid(-9.309F, -22.625F, -8.1731F, 15.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 226).cuboid(-14.309F, -27.625F, -5.1731F, 26.0F, 30.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 192).cuboid(-10.309F, -16.625F, -8.1731F, 17.0F, 14.0F, 6.0F, new Dilation(0.0F))
		.uv(47, 149).mirrored().cuboid(-8.309F, -21.625F, -10.1731F, 13.0F, 17.0F, 15.0F, new Dilation(0.0F)).mirrored(false)
		.uv(140, 41).mirrored().cuboid(-9.309F, -15.625F, -11.1731F, 15.0F, 12.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-5.439F, 2.3229F, -16.7037F, -0.674F, 0.3742F, -0.1212F));

		ModelPartData cube_r11 = neck2.addChild("cube_r11", ModelPartBuilder.create().uv(103, 164).mirrored().cuboid(-6.064F, -19.5978F, -5.0152F, 10.0F, 14.0F, 15.0F, new Dilation(0.0F)).mirrored(false)
		.uv(0, 166).mirrored().cuboid(-7.064F, -13.5978F, -4.0152F, 12.0F, 9.0F, 15.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-10.2236F, -5.3228F, -27.2057F, -0.4733F, 0.2743F, -0.0393F));

		ModelPartData head2 = neck2.addChild("head2", ModelPartBuilder.create().uv(182, 0).cuboid(-5.5F, -0.6904F, -27.4928F, 11.0F, 6.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 125).cuboid(-7.5F, -8.6904F, -15.4928F, 15.0F, 17.0F, 16.0F, new Dilation(0.0F))
		.uv(45, 181).cuboid(-8.5F, 1.3096F, -16.4928F, 17.0F, 8.0F, 9.0F, new Dilation(0.0F))
		.uv(124, 197).cuboid(-4.0F, 5.3096F, -25.9928F, 8.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(189, 75).cuboid(-6.5F, 0.3096F, -20.4928F, 13.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-10.805F, -17.1677F, -20.9415F));

		ModelPartData cube_r12 = head2.addChild("cube_r12", ModelPartBuilder.create().uv(99, 0).cuboid(-3.5142F, -2.449F, 8.7689F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(136, 123).cuboid(-4.5142F, -5.449F, 8.7689F, 6.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.5142F, 7.1735F, -36.0906F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r13 = head2.addChild("cube_r13", ModelPartBuilder.create().uv(0, 72).cuboid(-22.2631F, -0.9919F, -4.5245F, 7.0F, 6.0F, 7.0F, new Dilation(0.0F))
		.uv(191, 57).cuboid(-21.2631F, 0.0081F, -11.0245F, 5.0F, 5.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(18.7631F, -4.4686F, -17.1359F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r14 = head2.addChild("cube_r14", ModelPartBuilder.create().uv(99, 6).cuboid(-5.047F, -9.2029F, 10.0974F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(7.0142F, -0.8265F, -17.0906F, 0.0291F, -0.2596F, 1.0253F));

		ModelPartData cube_r15 = head2.addChild("cube_r15", ModelPartBuilder.create().uv(99, 6).mirrored().cuboid(0.5342F, -8.369F, 10.2025F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-7.9858F, -0.8265F, -17.0906F, 0.0291F, 0.2596F, -1.0253F));

		ModelPartData jaws2 = head2.addChild("jaws2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.6447F, -15.8386F));

		ModelPartData cube_r16 = jaws2.addChild("cube_r16", ModelPartBuilder.create().uv(157, 215).cuboid(-4.5142F, -5.4145F, 10.0646F, 10.0F, 4.0F, 6.0F, new Dilation(0.0F))
		.uv(200, 103).cuboid(-3.0142F, -6.9145F, 6.0646F, 7.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(170, 96).cuboid(-4.0142F, -4.9145F, 5.0646F, 9.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-0.4858F, 7.5288F, -13.752F, 0.3054F, 0.0F, 0.0F));

		ModelPartData neck3 = body.addChild("neck3", ModelPartBuilder.create(), ModelTransform.of(3.0218F, 2.8133F, -31.9336F, 0.0538F, 0.0208F, 0.1105F));

		ModelPartData cube_r17 = neck3.addChild("cube_r17", ModelPartBuilder.create().uv(158, 111).cuboid(-7.4994F, -20.2606F, 7.3919F, 15.0F, 19.0F, 12.0F, new Dilation(0.0F))
		.uv(53, 228).cuboid(-8.4994F, -14.2606F, 6.3919F, 17.0F, 14.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(-0.8984F, 9.6421F, -18.6146F, -0.0845F, -0.0716F, -0.033F));

		ModelPartData cube_r18 = neck3.addChild("cube_r18", ModelPartBuilder.create().uv(80, 80).cuboid(-8.7058F, -22.4042F, 0.6482F, 15.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 226).cuboid(-13.7058F, -27.4042F, 3.6482F, 26.0F, 30.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 192).cuboid(-9.7058F, -16.4042F, 0.6482F, 17.0F, 14.0F, 6.0F, new Dilation(0.0F))
		.uv(47, 149).cuboid(-7.7058F, -21.4042F, -1.3518F, 13.0F, 17.0F, 15.0F, new Dilation(0.0F))
		.uv(140, 41).cuboid(-8.7058F, -15.4042F, -2.3518F, 15.0F, 12.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(-1.1188F, 7.0534F, -23.3732F, -0.381F, 0.0234F, -0.0234F));

		ModelPartData cube_r19 = neck3.addChild("cube_r19", ModelPartBuilder.create().uv(103, 164).cuboid(-6.9942F, -23.4968F, -9.4797F, 10.0F, 14.0F, 15.0F, new Dilation(0.0F))
		.uv(0, 166).cuboid(-7.9942F, -17.4968F, -8.4797F, 12.0F, 9.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.4213F, 5.8797F, -29.3314F, -0.7162F, 0.1028F, -0.1244F));

		ModelPartData head3 = neck3.addChild("head3", ModelPartBuilder.create().uv(182, 0).cuboid(-5.5F, -0.6904F, -27.4928F, 11.0F, 6.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 125).cuboid(-7.5F, -8.6904F, -15.4928F, 15.0F, 17.0F, 16.0F, new Dilation(0.0F))
		.uv(45, 181).cuboid(-8.5F, 1.3096F, -16.4928F, 17.0F, 8.0F, 9.0F, new Dilation(0.0F))
		.uv(124, 197).cuboid(-4.0F, 5.3096F, -25.9928F, 8.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(189, 75).cuboid(-6.5F, 0.3096F, -20.4928F, 13.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0298F, -13.0527F, -20.6975F));

		ModelPartData cube_r20 = head3.addChild("cube_r20", ModelPartBuilder.create().uv(99, 0).cuboid(-5.2278F, -4.8004F, 7.394F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(136, 123).cuboid(-6.2278F, -7.8004F, 7.394F, 6.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.2278F, 8.9128F, -33.9944F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r21 = head3.addChild("cube_r21", ModelPartBuilder.create().uv(191, 57).cuboid(-0.6318F, 1.8254F, -15.575F, 5.0F, 5.0F, 13.0F, new Dilation(0.0F))
		.uv(0, 72).cuboid(-1.6318F, 0.8254F, -9.075F, 7.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-1.8682F, -7.7326F, -13.4814F, 0.3491F, 0.0F, 0.0F));

		ModelPartData cube_r22 = head3.addChild("cube_r22", ModelPartBuilder.create().uv(99, 6).cuboid(-7.8814F, -8.6819F, 8.6654F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.7278F, 0.9128F, -14.9944F, 0.0291F, -0.2596F, 1.0253F));

		ModelPartData cube_r23 = head3.addChild("cube_r23", ModelPartBuilder.create().uv(99, 6).mirrored().cuboid(1.6501F, -10.7898F, 8.3997F, 5.0F, 8.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-6.2722F, 0.9128F, -14.9944F, 0.0291F, 0.2596F, -1.0253F));

		ModelPartData jaws3 = head3.addChild("jaws3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.6447F, -15.8386F));

		ModelPartData cube_r24 = jaws3.addChild("cube_r24", ModelPartBuilder.create().uv(157, 215).cuboid(-6.2278F, -7.7037F, 8.5884F, 10.0F, 4.0F, 6.0F, new Dilation(0.0F))
		.uv(170, 96).cuboid(-5.7278F, -7.2037F, 3.5884F, 9.0F, 3.0F, 11.0F, new Dilation(0.0F))
		.uv(200, 103).cuboid(-4.7278F, -9.2037F, 4.5884F, 7.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(1.2278F, 9.2681F, -11.6558F, 0.3054F, 0.0F, 0.0F));

		ModelPartData tail1 = body.addChild("tail1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -13.0F, 33.0F));

		ModelPartData cube_r25 = tail1.addChild("cube_r25", ModelPartBuilder.create().uv(153, 70).cuboid(-5.0F, -5.647F, -14.4148F, 10.0F, 10.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 13.559F, 7.4601F, -0.6981F, 0.0F, 0.0F));

		ModelPartData tail2 = tail1.addChild("tail2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.925F, 11.5F));

		ModelPartData cube_r26 = tail2.addChild("cube_r26", ModelPartBuilder.create().uv(187, 27).cuboid(-4.0F, -5.0353F, 0.3637F, 8.0F, 8.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.509F, -4.2149F, -0.9599F, 0.0F, 0.0F));

		ModelPartData tail3 = tail2.addChild("tail3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 15.075F, 2.175F));

		ModelPartData cube_r27 = tail3.addChild("cube_r27", ModelPartBuilder.create().uv(187, 164).cuboid(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.3937F, 2.6969F, -0.6545F, 0.0F, 0.0F));

		ModelPartData tail4 = tail3.addChild("tail4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 6.5202F, 12.3355F));

		ModelPartData cube_r28 = tail4.addChild("cube_r28", ModelPartBuilder.create().uv(46, 198).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData tail5 = tail4.addChild("tail5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.4798F, 10.9895F));

		ModelPartData cube_r29 = tail5.addChild("cube_r29", ModelPartBuilder.create().uv(65, 201).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

		ModelPartData left_leg = main.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(20.065F, -3.0F, 46.4769F));

		ModelPartData left_foreleg2 = left_leg.addChild("left_foreleg2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r30 = left_foreleg2.addChild("cube_r30", ModelPartBuilder.create().uv(0, 0).cuboid(-5.525F, -13.32F, -5.17F, 10.0F, 19.0F, 10.0F, new Dilation(0.0F))
		.uv(197, 184).cuboid(-4.525F, 5.68F, -4.17F, 8.0F, 11.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -14.282F, -11.0F, 0.7097F, -0.1051F, -0.0154F));

		ModelPartData left_foreleg1 = left_foreleg2.addChild("left_foreleg1", ModelPartBuilder.create(), ModelTransform.pivot(0.9855F, -24.9677F, -19.9037F));

		ModelPartData cube_r31 = left_foreleg1.addChild("cube_r31", ModelPartBuilder.create().uv(91, 123).cuboid(-7.0F, -12.0F, -8.5F, 14.0F, 24.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -5.0F, 7.0F, -0.4422F, -0.158F, -0.4056F));

		ModelPartData left_foot = left_leg.addChild("left_foot", ModelPartBuilder.create().uv(187, 142).cuboid(-7.065F, -3.8912F, -9.585F, 11.0F, 7.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r32 = left_foot.addChild("cube_r32", ModelPartBuilder.create().uv(0, 85).cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-6.065F, 0.2929F, -11.4643F, 0.3491F, 0.3054F, 0.0F));

		ModelPartData cube_r33 = left_foot.addChild("cube_r33", ModelPartBuilder.create().uv(0, 85).cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(2.935F, 0.2929F, -11.4643F, 0.3491F, -0.3054F, 0.0F));

		ModelPartData cube_r34 = left_foot.addChild("cube_r34", ModelPartBuilder.create().uv(199, 86).cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(-1.065F, 0.2929F, -13.4643F, 0.3491F, 0.0436F, 0.0F));

		ModelPartData cube_r35 = left_foot.addChild("cube_r35", ModelPartBuilder.create().uv(236, 208).cuboid(-1.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-8.3786F, 2.516F, -17.1393F, 0.3679F, -0.4346F, -0.2699F));

		ModelPartData cube_r36 = left_foot.addChild("cube_r36", ModelPartBuilder.create().uv(236, 208).cuboid(-3.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.831F, 2.516F, -19.5447F, 0.495F, 0.768F, 0.3429F));

		ModelPartData cube_r37 = left_foot.addChild("cube_r37", ModelPartBuilder.create().uv(236, 208).cuboid(-3.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(5.2486F, 2.516F, -17.1393F, 0.3679F, 0.4346F, 0.2699F));

		ModelPartData right_leg = main.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-20.065F, -3.0F, 46.4769F));

		ModelPartData right_foreleg2 = right_leg.addChild("right_foreleg2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r38 = right_foreleg2.addChild("cube_r38", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-4.475F, -13.32F, -5.17F, 10.0F, 19.0F, 10.0F, new Dilation(0.0F)).mirrored(false)
		.uv(197, 184).mirrored().cuboid(-3.475F, 5.68F, -4.17F, 8.0F, 11.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -14.282F, -11.0F, 0.7097F, 0.1051F, 0.0154F));

		ModelPartData right_foreleg1 = right_foreleg2.addChild("right_foreleg1", ModelPartBuilder.create(), ModelTransform.pivot(-0.9855F, -24.9677F, -19.9037F));

		ModelPartData cube_r39 = right_foreleg1.addChild("cube_r39", ModelPartBuilder.create().uv(91, 123).mirrored().cuboid(-7.0F, -12.0F, -8.5F, 14.0F, 24.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.0F, -5.0F, 7.0F, -0.4422F, 0.158F, 0.4056F));

		ModelPartData right_foot = right_leg.addChild("right_foot", ModelPartBuilder.create().uv(187, 142).mirrored().cuboid(-3.935F, -3.8912F, -9.585F, 11.0F, 7.0F, 13.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r40 = right_foot.addChild("cube_r40", ModelPartBuilder.create().uv(0, 85).mirrored().cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(6.065F, 0.2929F, -11.4643F, 0.3491F, -0.3054F, 0.0F));

		ModelPartData cube_r41 = right_foot.addChild("cube_r41", ModelPartBuilder.create().uv(0, 85).mirrored().cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.935F, 0.2929F, -11.4643F, 0.3491F, 0.3054F, 0.0F));

		ModelPartData cube_r42 = right_foot.addChild("cube_r42", ModelPartBuilder.create().uv(199, 86).mirrored().cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.065F, 0.2929F, -13.4643F, 0.3491F, -0.0436F, 0.0F));

		ModelPartData cube_r43 = right_foot.addChild("cube_r43", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-3.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(8.3786F, 2.516F, -17.1393F, 0.3679F, 0.4346F, 0.2699F));

		ModelPartData cube_r44 = right_foot.addChild("cube_r44", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-1.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.831F, 2.516F, -19.5447F, 0.495F, -0.768F, -0.3429F));

		ModelPartData cube_r45 = right_foot.addChild("cube_r45", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-1.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-5.2486F, 2.516F, -17.1393F, 0.3679F, -0.4346F, -0.2699F));

		ModelPartData left_arm = main.addChild("left_arm", ModelPartBuilder.create(), ModelTransform.pivot(16.0F, -3.0F, -15.3624F));

		ModelPartData left_forearm2 = left_arm.addChild("left_forearm2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r46 = left_forearm2.addChild("cube_r46", ModelPartBuilder.create().uv(153, 178).cuboid(-4.5F, -19.0F, -7.0F, 11.0F, 18.0F, 11.0F, new Dilation(0.0F))
		.uv(88, 193).cuboid(-3.5F, -1.0F, -6.0F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, -5.7856F, 5.8394F, -0.6981F, 0.0F, 0.0F));

		ModelPartData left_forearm1 = left_forearm2.addChild("left_forearm1", ModelPartBuilder.create(), ModelTransform.pivot(3.3122F, -19.8638F, 19.9084F));

		ModelPartData cube_r47 = left_forearm1.addChild("cube_r47", ModelPartBuilder.create().uv(108, 80).cuboid(-7.0F, -13.0F, -8.5F, 14.0F, 26.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, -9.0F, 0.4185F, 0.1274F, -0.2783F));

		ModelPartData left_hand = left_arm.addChild("left_hand", ModelPartBuilder.create().uv(187, 142).cuboid(-1.0F, -3.8912F, -9.1207F, 11.0F, 7.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r48 = left_hand.addChild("cube_r48", ModelPartBuilder.create().uv(236, 208).cuboid(-3.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(11.3136F, 2.516F, -16.675F, 0.3679F, 0.4346F, 0.2699F));

		ModelPartData cube_r49 = left_hand.addChild("cube_r49", ModelPartBuilder.create().uv(236, 208).cuboid(-3.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(4.234F, 2.516F, -19.0804F, 0.495F, 0.768F, 0.3429F));

		ModelPartData cube_r50 = left_hand.addChild("cube_r50", ModelPartBuilder.create().uv(236, 208).cuboid(-2.5F, -3.0F, -2.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-1.8625F, 2.5021F, -15.2445F, 0.6591F, 1.0091F, 0.4713F));

		ModelPartData cube_r51 = left_hand.addChild("cube_r51", ModelPartBuilder.create().uv(199, 86).cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 0.2929F, -13.0F, 0.3491F, 0.0436F, 0.0F));

		ModelPartData cube_r52 = left_hand.addChild("cube_r52", ModelPartBuilder.create().uv(0, 85).cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, 0.2929F, -11.0F, 0.3491F, -0.3054F, 0.0F));

		ModelPartData cube_r53 = left_hand.addChild("cube_r53", ModelPartBuilder.create().uv(0, 85).mirrored().cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.2929F, -11.0F, 0.3491F, 0.3054F, 0.0F));

		ModelPartData right_arm = main.addChild("right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-16.0F, -3.0F, -15.3624F));

		ModelPartData right_forearm2 = right_arm.addChild("right_forearm2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r54 = right_forearm2.addChild("cube_r54", ModelPartBuilder.create().uv(153, 178).mirrored().cuboid(-6.5F, -19.0F, -7.0F, 11.0F, 18.0F, 11.0F, new Dilation(0.0F)).mirrored(false)
		.uv(88, 193).mirrored().cuboid(-5.5F, -1.0F, -6.0F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.5F, -5.7856F, 5.8394F, -0.6981F, 0.0F, 0.0F));

		ModelPartData right_forearm1 = right_forearm2.addChild("right_forearm1", ModelPartBuilder.create(), ModelTransform.pivot(-3.3122F, -19.8638F, 19.9084F));

		ModelPartData cube_r55 = right_forearm1.addChild("cube_r55", ModelPartBuilder.create().uv(108, 80).mirrored().cuboid(-7.0F, -13.0F, -8.5F, 14.0F, 26.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -7.0F, -9.0F, 0.4185F, -0.1274F, 0.2783F));

		ModelPartData right_hand = right_arm.addChild("right_hand", ModelPartBuilder.create().uv(187, 142).mirrored().cuboid(-10.0F, -3.8912F, -9.1207F, 11.0F, 7.0F, 13.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r56 = right_hand.addChild("cube_r56", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-1.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-11.3136F, 2.516F, -16.675F, 0.3679F, -0.4346F, -0.2699F));

		ModelPartData cube_r57 = right_hand.addChild("cube_r57", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-1.5F, -2.5F, -1.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.234F, 2.516F, -19.0804F, 0.495F, -0.768F, -0.3429F));

		ModelPartData cube_r58 = right_hand.addChild("cube_r58", ModelPartBuilder.create().uv(236, 208).mirrored().cuboid(-2.5F, -3.0F, -2.5F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.8625F, 2.5021F, -15.2445F, 0.6591F, -1.0091F, -0.4713F));

		ModelPartData cube_r59 = right_hand.addChild("cube_r59", ModelPartBuilder.create().uv(199, 86).mirrored().cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-5.0F, 0.2929F, -13.0F, 0.3491F, -0.0436F, 0.0F));

		ModelPartData cube_r60 = right_hand.addChild("cube_r60", ModelPartBuilder.create().uv(0, 85).mirrored().cuboid(-3.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-9.0F, 0.2929F, -11.0F, 0.3491F, 0.3054F, 0.0F));

		ModelPartData cube_r61 = right_hand.addChild("cube_r61", ModelPartBuilder.create().uv(0, 85).cuboid(-2.0F, -3.5F, -4.0F, 5.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2929F, -11.0F, 0.3491F, -0.3054F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
}
