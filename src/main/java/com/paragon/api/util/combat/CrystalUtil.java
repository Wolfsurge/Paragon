package com.paragon.api.util.combat;

import com.paragon.api.util.Wrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class CrystalUtil implements Wrapper {

    public static float calculate(double posX, double posY, double posZ, Entity entity) {
        double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());

        float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0f * 7.0f * 12.0f + 1.0f);

        double damage = 1.0;

        if (entity instanceof EntityLivingBase) {
            damage = getBlastReduction((EntityLivingBase) entity, calculatedDamage * ((mc.world.getDifficulty().getDifficultyId() == 0) ? 0.0f : ((mc.world.getDifficulty().getDifficultyId() == 2) ? 1.0f : ((mc.world.getDifficulty().getDifficultyId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, entity, posX, posY, posZ, 6.0f, false, true));
        }

        return (float) damage;
    }

    public static float getBlastReduction(EntityLivingBase entityLivingBase, float damage, Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entityLivingBase.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }

            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entityLivingBase.getTotalArmorValue(), (float) entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        return damage;
    }

    public static float calculate(BlockPos pos, Entity entity) {
        return calculate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, entity);
    }
}
