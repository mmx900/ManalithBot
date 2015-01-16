package org.manalith.ircbot.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.manalith.ircbot.annotation.Option;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.common.stereotype.BotCommand.BotEvent;
import org.manalith.ircbot.resources.MessageEvent;

/**
 * 어노테이션(@BotCommand) 기반 명령을 위한 메서드 래퍼
 */
public class Command {
	private Plugin plugin;
	private Method method;
	private BotCommand meta;
	private String[] aliases;
	private String[] params;
	private int minimumArguments;

	public Command(Method method, Plugin plugin) {
		this.method = method;
		this.plugin = plugin;
		meta = method.getAnnotation(BotCommand.class);
		aliases = ArrayUtils.add(meta.value(), method.getName().toLowerCase());

		for (int i = 0; i < aliases.length; i++) {
			aliases[i] = EventDispatcher.COMMAND_PREFIX + aliases[i];
		}

		Class<?>[] types = method.getParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();
		List<String> params = new ArrayList<>();

		for (int i = 0; i < types.length; i++) {
			Annotation[] ann = annotations[i];
			Class<?> type = types[i];

			if (type == MessageEvent.class)
				continue;

			String name = null;
			boolean isRequired = false;

			if (type != Optional.class) {
				minimumArguments++;
				isRequired = true;
			}

			for (Annotation a : ann) {
				if (a instanceof Option)
					name = ((Option) a).name();
			}

			if (name == null)
				name = "arg" + i;

			if (isRequired)
				name = "*" + name;

			name = "[" + name + "]";

			params.add(name);
		}

		this.params = params.toArray(new String[] {});
	}

	public BotCommand getMeta() {
		return meta;
	}

	public String[] getAliases() {
		return aliases;
	}

	public BotEvent[] getListeners() {
		return meta.listeners();
	}

	public boolean matches(String alias, BotEvent event) {
		return ArrayUtils.contains(getListeners(), event) && ArrayUtils.contains(aliases, alias);

	}

	public String getUsage() {
		return "사용법 : " + StringUtils.join(aliases, '|') + " " + StringUtils.join(params, ' ');
	}

	public String execute(MessageEvent event, String[] params)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (params.length < minimumArguments)
			return getUsage();

		String result = null;
		switch (method.getParameterTypes().length) {
		case 0:
			result = (String) method.invoke(plugin);
			break;
		case 1: {
			Class<?> type = method.getParameterTypes()[0];

			if (type == MessageEvent.class)
				result = (String) method.invoke(plugin, event);
			else if (type.isArray())
				result = (String) method.invoke(plugin, (Object) params);
			else
				result = (String) method.invoke(plugin,
						StringUtils.join(params, " "));

			break;
		}
		default:
			// event, param
			// event, params[]
			// event, param, params[]
			// param, param
			// param, param, params[]
			// ...

			List<Class<?>> args = Arrays.asList(method.getParameterTypes());
			Iterator<Class<?>> ai = args.iterator();

			List<String> inputs = Arrays.asList(params);
			ListIterator<String> ii = inputs.listIterator();

			List<Object> newParams = new ArrayList<>();

			while (ai.hasNext()) {
				Class<?> arg = ai.next();

				if (arg == MessageEvent.class) {
					newParams.add(event);
				} else {
					if (!ai.hasNext() && ii.hasNext()) {
						List<String> more = new ArrayList<>();

						while (ii.hasNext()) {
							more.add(ii.next());
						}

						if (arg.isArray()) {
							newParams.add(more.toArray(new String[] {}));
						} else {
							newParams.add(StringUtils.join(more, ' '));
						}
					} else {
						newParams.add(ii.next());
					}
				}
			}

			// result = (String) method.invoke(plugin, event,
			// method.getParameterTypes()[1].isArray() ? params
			// : StringUtils.join(params));
			result = (String) method.invoke(plugin, newParams.toArray());
		}

		if (StringUtils.isNotBlank(result))
			return result;

		event.setExecuted(meta.stopEvent());

		return "";
	}
}
