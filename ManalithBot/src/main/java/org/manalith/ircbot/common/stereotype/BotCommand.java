package org.manalith.ircbot.common.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotCommand {
	public enum BotEvent {
		ON_MESSAGE, ON_PRIVATE_MESSAGE
	}

	String[] value();

	int minimumArguments() default 0;

	BotEvent[] listeners() default { BotEvent.ON_MESSAGE };

	boolean stopEvent() default true;
}
