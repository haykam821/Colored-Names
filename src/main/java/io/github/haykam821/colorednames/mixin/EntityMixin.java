package io.github.haykam821.colorednames.mixin;

import java.util.Random;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.MathHelper;

@Mixin({Entity.class, PlayerEntity.class})
public class EntityMixin {
	@Unique
	private static int colorFromUuid(UUID uuid) {
		Random random = new Random(uuid.getMostSignificantBits());
		return MathHelper.hsvToRgb(random.nextFloat(), 0.8f, 0.8f);
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	public void modifyDefaultColor(CallbackInfoReturnable<MutableText> ci) {
		ci.setReturnValue(ci.getReturnValue().styled(style -> {
			if (style.getColor() == null) {
				int color = EntityMixin.colorFromUuid(((Entity) (Object) this).getUuid());
				return style.withColor(TextColor.fromRgb(color));
			} else {
				return style;
			}
		}));
	}
}