package org.pircbotx;

import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputRawUTF8;

public class PircBotXUtf8 extends PircBotX {
	private final OutputRawUTF8 outputRaw;

	public PircBotXUtf8(Configuration<? extends PircBotX> configuration) {
		super(configuration);

		outputRaw = new OutputRawUTF8(this);
	}

	@Override
	public OutputRaw sendRaw() {
		return outputRaw;
	}
}
