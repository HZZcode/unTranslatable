package mod.untranslatable.mixin;

import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MutableText.class)
public class MixinTranslatable {
	@Inject(method = "of", at = @At("HEAD"), cancellable = true)
	private static void of(TextContent content, CallbackInfoReturnable<MutableText> cir) {
		if (content instanceof TranslatableTextContent translatable) {
			String key = translatable.getKey();
			StringBuilder builder = new StringBuilder(key);
			boolean b = false;
			Object[] args = translatable.getArgs();
			if (args.length != 0) builder.append("(");
			for (Object object: args) {
				if (b) builder.append(", ");
				b = true;
				builder.append(object);
			}
			if (args.length != 0) builder.append(")");
			cir.setReturnValue(MutableText.of(PlainTextContent.of(builder.toString())));
			cir.cancel();
		}
	}
}