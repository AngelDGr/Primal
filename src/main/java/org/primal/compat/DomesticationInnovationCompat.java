package org.primal.compat;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.github.alexthe668.domesticationinnovation.client.ClientProxy;
import com.github.alexthe668.domesticationinnovation.client.model.BlazingBarModel;
import com.github.alexthe668.domesticationinnovation.client.model.ShadowHandModel;
import com.github.alexthe668.domesticationinnovation.client.render.DIRenderTypes;
import com.github.alexthe668.domesticationinnovation.client.render.TextureSizer;
import com.github.alexthe668.domesticationinnovation.server.enchantment.DIEnchantmentRegistry;
import com.github.alexthe668.domesticationinnovation.server.entity.TameableUtils;
import com.github.alexthe668.domesticationinnovation.server.item.DIItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeRenderTypes;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class DomesticationInnovationCompat {

    public static<T extends Entity & GeoAnimatable> void addEnchantmentsLayer(GeoEntityRenderer<T> entityRendererIn){
        entityRendererIn.addRenderLayer(new DomesticationInnovationCompat.LayerPetOverlays<>(entityRendererIn, null));
    }

    public static<T extends Entity, M extends GeoAnimatable> void addEnchantmentsLayer(GeoReplacedEntityRenderer<T, M> entityRendererIn){
        entityRendererIn.addRenderLayer(new DomesticationInnovationCompat.LayerPetOverlays<>(entityRendererIn, entityRendererIn));
    }

    public static class LayerPetOverlays<T extends GeoAnimatable, J extends Entity, M extends GeoAnimatable> extends GeoRenderLayer<T> {
        private static final ItemStack MAGNET;
        private static final int CLOUD_COUNT = 14;
        private static final Vec3[] CLOUD_OFFSETS;
        private static final Vec3[] CLOUD_SCALES;
        private static final ShadowHandModel SHADOW_HAND_MODEL;
        private static final BlazingBarModel BLAZING_BAR_MODEL;
        private static final ResourceLocation BLAZE_TEXTURE;
        private static final ResourceLocation AURA_TEXTURE;
        private static final Map<ResourceLocation, Integer> MODELS_TO_XSIZE;
        private static final Map<ResourceLocation, Integer> MODELS_TO_YSIZE;
        private final LightningRender lightningRender = new LightningRender();
        private final LightningBoltData.BoltRenderInfo healthBoltData = new LightningBoltData.BoltRenderInfo(0.3F, 0.0F, 0.0F, 0.0F, new Vector4f(0.4F, 0.0F, 0.0F, 0.4F), 0.2F);
        private final LightningBoltData.BoltRenderInfo shadowBoltData = new LightningBoltData.BoltRenderInfo(0.3F, 0.0F, 0.0F, 0.0F, new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), 0.2F);
        private final GeoReplacedEntityRenderer<J, M> geoReplacedEntityRenderer;

        public LayerPetOverlays(GeoRenderer<T> entityRendererIn, GeoReplacedEntityRenderer<J, M> geoReplacedEntityRenderer) {
            super(entityRendererIn);
            this.geoReplacedEntityRenderer = geoReplacedEntityRenderer;
        }

        private static void addVertexPairAlex(VertexConsumer p_174308_, Matrix4f p_174309_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
            float f = (float)p_174321_ / 8.0F;
            int i = (int) Mth.lerp(f, (float)p_174313_, (float)p_174314_);
            int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
            int k = LightTexture.pack(i, j);
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            float f5 = p_174310_ * f;
            float f6 = p_174311_ < 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
            float f7 = p_174312_ * f;
            p_174308_.vertex(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
            p_174308_.vertex(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        }

        public static <E extends Entity> void renderShadowString(Entity from, Vec3 fromVec, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Vec3 to) {
            poseStack.pushPose();
            double d3 = fromVec.x;
            double d4 = fromVec.y;
            double d5 = fromVec.z;
            poseStack.translate(d3, d4, d5);
            float f = (float)(to.x - d3);
            float f1 = (float)(to.y - d4);
            float f2 = (float)(to.z - d5);
            float f3 = 0.025F;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
            Matrix4f matrix4f = poseStack.last().pose();
            BlockPos blockpos = BlockPos.containing(fromVec);
            BlockPos blockpos1 = BlockPos.containing(to);
            int i = 0;
            int j = from.level().getBrightness(LightLayer.BLOCK, blockpos1);
            int k = from.level().getBrightness(LightLayer.SKY, blockpos);
            int l = from.level().getBrightness(LightLayer.SKY, blockpos1);

            for(int i1 = 0; i1 <= 8; ++i1) {
                float width = 0.05F - (float)i1 / 8.0F * 0.025F;
                addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.2F, width, width, width, i1, false);
            }

            poseStack.popPose();
        }

        private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_, float alpha) {
            p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.5F, 0.0F).color(255.0F, 255.0F, 255.0F, alpha * 255.0F).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void render(PoseStack matrixStackIn, T geo, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferIn, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
            var entity = geoReplacedEntityRenderer!=null? geoReplacedEntityRenderer.getCurrentEntity(): geo instanceof Entity casted? casted: null;
            if(entity!=null)
                if (TameableUtils.couldBeTamed(entity)) {
                    LivingEntity living = (LivingEntity)entity;
                    float f = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
                    float realAge = (float)living.tickCount + partialTicks;
                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.IMMUNITY_FRAME) && TameableUtils.getImmuneTime((LivingEntity)entity) > 0) {
                        VertexConsumer ivertexbuilder = bufferIn.getBuffer(DIRenderTypes.IFRAME_GLINT);
                        float alpha = 0.5F;
                        matrixStackIn.pushPose();

                        if(geoReplacedEntityRenderer!=null)
                            this.geoReplacedEntityRenderer.reRender(bakedModel, matrixStackIn, bufferIn, (M) geo, renderType, ivertexbuilder, partialTicks, packedLightIn,
                                    LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
                        else
                            this.renderer.reRender(bakedModel, matrixStackIn, bufferIn, geo, renderType, ivertexbuilder, partialTicks, packedLightIn,
                                    LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);

                        matrixStackIn.popPose();
                    }

                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.MAGNETIC)) {
                        Entity suck = TameableUtils.getPetAttackTarget(living);
                        if (suck != null) {
                            double d0 = Mth.lerp(partialTicks, suck.xo, suck.getX()) - Mth.lerp(partialTicks, living.xo, living.getX());
                            double d1 = Mth.lerp(partialTicks, suck.yo, suck.getY()) - Mth.lerp(partialTicks, living.yo, living.getY());
                            double d2 = Mth.lerp(partialTicks, suck.zo, suck.getZ()) - Mth.lerp(partialTicks, living.zo, living.getZ());
                            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                            double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                            float f1 = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                            float f2 = (float)(-(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                            matrixStackIn.pushPose();
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f1-180));
                            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-f2));
                            matrixStackIn.pushPose();
                            float bob1 = (float)Math.sin(realAge * 0.5F) * 0.05F;
                            float bob2 = (float)Math.sin(realAge * 0.3F) * 0.09F - 0.03F;
                            float bob3 = (float)Math.cos(realAge * 0.1F) * 0.05F;
                            matrixStackIn.translate(bob1, 1.25F - entity.getBbHeight() * 0.5F - bob2, -entity.getBbWidth() - 0.125F - bob3);
                            matrixStackIn.mulPose(Axis.XN.rotationDegrees(90.0F));
                            matrixStackIn.scale(1.6F, 1.6F, 3.0F);
                            Minecraft.getInstance().getItemRenderer().renderStatic(MAGNET, ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.level(), entity.getId());
                            matrixStackIn.popPose();
                            matrixStackIn.popPose();
                        }
                    }

                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.HEALTH_SIPHON)) {
                        Entity owner = TameableUtils.getOwnerOf(living);
                        if (owner != null && owner.isAlive() && owner.distanceTo(living) < 100.0F) {
                            float x = (float)Mth.lerp(partialTicks, entity.xo, entity.getX());
                            float y = (float)Mth.lerp(partialTicks, entity.yo, entity.getY());
                            float z = (float)Mth.lerp(partialTicks, entity.zo, entity.getZ());
                            if (living.hurtTime > 0 && living.hurtTime == living.hurtDuration - 1) {
                                float height = entity.getBbHeight() * 0.8F;
                                float ownerHeight = owner.getBbHeight() * 0.6F;
                                LightningBoltData bolt = (new LightningBoltData(this.healthBoltData, new Vec3(x, y + height, z), owner.position().add(0.0F, ownerHeight, 0.0F), 3)).size(0.5F).lifespan(5).spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                                this.lightningRender.update(living, bolt, partialTicks);
                            }

                            matrixStackIn.pushPose();
                            matrixStackIn.translate(-x, -y, -z);
                            this.lightningRender.render(partialTicks, matrixStackIn, bufferIn);
                            matrixStackIn.popPose();
                        }
                    }

                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.VOID_CLOUD) && !living.isInWaterOrBubble() && !living.onGround() && TameableUtils.getFallDistance(living) >= 3.0F) {
                        matrixStackIn.pushPose();
                        matrixStackIn.translate(0.4F, -1.25F + entity.getBbHeight(), -0.2F);
                        matrixStackIn.mulPose(Axis.YN.rotationDegrees(f));
                        matrixStackIn.mulPose(Axis.XN.rotationDegrees(180.0F));

                        for(int i = 0; i < 14; ++i) {
                            float xSin = (float)Math.sin(realAge * 0.05F + (float)i * 2.0F) * 0.1F;
                            float ySin = (float)Math.cos(realAge * 0.05F + (float)i * 2.0F) * 0.1F;
                            float zSin = (float)Math.sin(realAge * 0.05F + (float)i * 2.0F - 2.0F) * 0.1F;
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(CLOUD_OFFSETS[i].x + (double)xSin, CLOUD_OFFSETS[i].y + (double)ySin, CLOUD_OFFSETS[i].z + (double)zSin);
                            matrixStackIn.scale((float)CLOUD_SCALES[i].x + xSin, (float)CLOUD_SCALES[i].y + ySin, (float)CLOUD_SCALES[i].z + xSin);
                            this.renderVoidCloudCube(entity, matrixStackIn, bufferIn.getBuffer(DIRenderTypes.VOID_CLOUD));
                            matrixStackIn.popPose();
                        }

                        matrixStackIn.popPose();
                    }

                    if (TameableUtils.isZombiePet(living)) {
                        ResourceLocation mobTexture = renderer.getTextureLocation(geo);
                        Pair<Integer, Integer> xandyDimensions = TextureSizer.getTextureWidth(mobTexture);
                        VertexConsumer zombieBuffer = bufferIn.getBuffer(DIRenderTypes.getZombieOverlay(mobTexture, xandyDimensions.getFirst(), xandyDimensions.getSecond()));
                        float alpha = 0.1F;
                        matrixStackIn.pushPose();
                        if(geoReplacedEntityRenderer!=null)
                            this.geoReplacedEntityRenderer.reRender(bakedModel, matrixStackIn, bufferIn, (M) geo, renderType, zombieBuffer, partialTicks, packedLightIn,
                                    LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 0.0F, 0.5F, 0.0F, alpha);
                        else
                            this.renderer.reRender(bakedModel, matrixStackIn, bufferIn, geo, renderType, zombieBuffer, partialTicks, packedLightIn,
                                    LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 0.0F, 0.5F, 0.0F, alpha);
                        matrixStackIn.popPose();
                    }

                    int shadowHandCount = TameableUtils.getEnchantLevel(living, DIEnchantmentRegistry.SHADOW_HANDS);
                    if (shadowHandCount > 0) {
                        matrixStackIn.pushPose();
                        Entity punching = TameableUtils.getPetAttackTarget(living);
                        double d0 = 0.0F;
                        double d1 = 0.0F;
                        double d2 = 0.0F;
                        if (punching != null) {
                            d0 = Mth.lerp(partialTicks, punching.xo, punching.getX()) - Mth.lerp(partialTicks, living.xo, living.getX());
                            d1 = Mth.lerp(partialTicks, punching.yo, punching.getY()) - Mth.lerp(partialTicks, living.yo, living.getY());
                            d2 = Mth.lerp(partialTicks, punching.zo, punching.getZ()) - Mth.lerp(partialTicks, living.zo, living.getZ());
                        } else {
                            matrixStackIn.mulPose(Axis.YN.rotationDegrees(f-180));
                        }

                        double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                        double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                        float f1 = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                        float f2 = (float)(-(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));

                        for(int i = 0; i < shadowHandCount; ++i) {
                            float punch = this.getPunchFor(living, i, partialTicks) / 10.0F;
                            VertexConsumer vertexconsumer = bufferIn.getBuffer(DIRenderTypes.SHADOW_HAND_ENTITY);
                            float xSpread = shadowHandCount <= 1 ? 0.0F : (float)i / (float)(shadowHandCount - 1) - 0.5F;
                            float xOffset = shadowHandCount <= 1 ? 0.0F : Mth.sin((float)((double)xSpread * Math.PI)) * 1.8F;
                            float zOffset = shadowHandCount <= 1 ? 0.25F : Mth.cos((float)((double)xSpread * Math.PI)) * 1.8F;
                            float yOffset = -zOffset * 0.3F;
                            Vec3 fromPos = new Vec3(0.0F, 1.25F - entity.getBbHeight() * 0.5F, -entity.getBbWidth() * 0.3F);
                            Vec3 handTranslate = new Vec3(xOffset * entity.getBbWidth(), (double)(1.0F + yOffset) + Math.sin(realAge * 0.2F + (float)i * 1.5F) * (double)0.15F, zOffset * entity.getBbWidth());
                            if (punching != null) {
                                Vec3 vec3 = (new Vec3(d0, d1, d2)).scale(punch);
                                handTranslate = handTranslate.add(vec3);
                            }

                            matrixStackIn.pushPose();

                            matrixStackIn.pushPose();
                            matrixStackIn.translate(handTranslate.x, handTranslate.y, handTranslate.z);
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(0.0F, -1.15F, -0.15F);
                            SHADOW_HAND_MODEL.animateShadowHand(punch, i, shadowHandCount, realAge);
                            SHADOW_HAND_MODEL.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
                            matrixStackIn.popPose();
                            matrixStackIn.popPose();
                            matrixStackIn.pushPose();
                            renderShadowString(entity, fromPos, partialTicks, matrixStackIn, bufferIn, handTranslate);
                            matrixStackIn.popPose();
                            matrixStackIn.popPose();
                        }
                        matrixStackIn.popPose();
                    }

                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.BLAZING_PROTECTION)) {
                        int bars = TameableUtils.getBlazingProtectionBars(living);
                        float f1 = realAge * 7.0F;
                        float seperation = 360.0F / ((float)TameableUtils.getEnchantLevel(living, DIEnchantmentRegistry.BLAZING_PROTECTION) * 2.0F);
                        VertexConsumer vertexconsumer = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(BLAZE_TEXTURE));
                        matrixStackIn.pushPose();
                        matrixStackIn.mulPose(Axis.YN.rotationDegrees(f));

                        for(int i = 0; i < bars; ++i) {
                            f1 += seperation;
                            matrixStackIn.pushPose();
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
                            float bob = (float)Math.sin((double)(realAge * 0.6F) + Math.toRadians(seperation * (float)i)) * 0.15F - 0.07F;
                            matrixStackIn.translate(0.0F, 0.4F - entity.getBbHeight() * 0.5F - bob, -entity.getBbWidth() - 0.2F);
                            BLAZING_BAR_MODEL.animateBar(f1);
                            BLAZING_BAR_MODEL.renderToBuffer(matrixStackIn, vertexconsumer, 240, LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
                            matrixStackIn.popPose();
                        }

                        matrixStackIn.popPose();
                    }

                    if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.HEALING_AURA)) {
                        int t = TameableUtils.getHealingAuraTime(living);
                        if (t > 0) {
                            float time = t > 20 ? 200.0F - Math.max(180.0F, (float)t + partialTicks) : (float)t - partialTicks;
                            float pulse = 0.9F + (float)(Math.sin(realAge * 0.08F) * (double)0.1F + (double)0.1F);
                            float healscale = Math.min(time, 20.0F) / 20.0F * 2.2F * pulse;
                            matrixStackIn.pushPose();
                            matrixStackIn.translate(0.0F, 1.8F - entity.getBbHeight() * 0.5F, 0.0F);
                            matrixStackIn.mulPose(Axis.YN.rotationDegrees(f));
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(realAge * 3.0F));
                            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                            matrixStackIn.scale(3.0F * healscale, 3.0F * healscale, 3.0F * healscale);
                            VertexConsumer vertexconsumer = bufferIn.getBuffer(DIRenderTypes.HEALING_AURA);
                            PoseStack.Pose posestack$pose = matrixStackIn.last();
                            Matrix4f matrix4f = posestack$pose.pose();
                            Matrix3f matrix3f = posestack$pose.normal();
                            vertex(vertexconsumer, matrix4f, matrix3f, 240, 0.0F, 0, 0, 1, 1.0F);
                            vertex(vertexconsumer, matrix4f, matrix3f, 240, 1.0F, 0, 1, 1, 1.0F);
                            vertex(vertexconsumer, matrix4f, matrix3f, 240, 1.0F, 1, 1, 0, 1.0F);
                            vertex(vertexconsumer, matrix4f, matrix3f, 240, 0.0F, 1, 0, 0, 1.0F);
                            matrixStackIn.popPose();
                        }
                    }
                }
        }

        private float getPunchFor(LivingEntity living, int i, float partialTicks) {
            int[] arr = TameableUtils.getShadowPunchTimes(living);
            if (arr.length > i) {
                if (ClientProxy.shadowPunchRenderData.containsKey(living) && ClientProxy.shadowPunchRenderData.get(living).length > i) {
                    int[] prevArr = ClientProxy.shadowPunchRenderData.get(living);
                    return (float)prevArr[i] + (float)(arr[i] - prevArr[i]) * partialTicks;
                } else {
                    return (float)arr[i];
                }
            } else {
                return 0.0F;
            }
        }

        private void renderVoidCloudCube(Entity entity, PoseStack poseStack, VertexConsumer consumer) {
            Matrix4f cubeAt = poseStack.last().pose();
            this.renderVoidCloudFace(entity, cubeAt, consumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
            this.renderVoidCloudFace(entity, cubeAt, consumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
            this.renderVoidCloudFace(entity, cubeAt, consumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
            this.renderVoidCloudFace(entity, cubeAt, consumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
            this.renderVoidCloudFace(entity, cubeAt, consumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
            this.renderVoidCloudFace(entity, cubeAt, consumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
        }

        private void renderVoidCloudFace(Entity entity, Matrix4f p_173696_, VertexConsumer p_173697_, float p_173698_, float p_173699_, float p_173700_, float p_173701_, float p_173702_, float p_173703_, float p_173704_, float p_173705_, Direction p_173706_) {
            p_173697_.vertex(p_173696_, p_173698_, p_173700_, p_173702_).overlayCoords(240).endVertex();
            p_173697_.vertex(p_173696_, p_173699_, p_173700_, p_173703_).overlayCoords(240).endVertex();
            p_173697_.vertex(p_173696_, p_173699_, p_173701_, p_173704_).overlayCoords(240).endVertex();
            p_173697_.vertex(p_173696_, p_173698_, p_173701_, p_173705_).overlayCoords(240).endVertex();
        }

        static {
            MAGNET = new ItemStack(DIItemRegistry.MAGNET.get());
            CLOUD_OFFSETS = new Vec3[14];
            CLOUD_SCALES = new Vec3[14];
            SHADOW_HAND_MODEL = new ShadowHandModel();
            BLAZING_BAR_MODEL = new BlazingBarModel();
            BLAZE_TEXTURE = ResourceLocation.parse("textures/entity/blaze.png");
            AURA_TEXTURE = ResourceLocation.fromNamespaceAndPath("domesticationinnovation", "textures/healing_aura.png");
            MODELS_TO_XSIZE = new HashMap<>();
            MODELS_TO_YSIZE = new HashMap<>();
            Random random = new Random(500L);

            for(int i = 0; i < 14; ++i) {
                CLOUD_OFFSETS[i] = (new Vec3(random.nextFloat() - 0.5F, 0.2F * (random.nextFloat() - 0.5F), random.nextFloat() - 0.5F)).scale(1.2F);
                CLOUD_SCALES[i] = new Vec3(0.6F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F);
            }

        }
    }

}
