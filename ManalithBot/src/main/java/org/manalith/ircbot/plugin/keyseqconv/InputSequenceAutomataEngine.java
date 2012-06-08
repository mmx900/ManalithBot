package org.manalith.ircbot.plugin.keyseqconv;

public abstract class InputSequenceAutomataEngine implements IAutomataEngine {

	public InputSequenceAutomataEngine() {
		;
	}

	protected abstract boolean isISingleConsonant(String tICon);
	protected abstract boolean isIDoubleConsonant(String tICon);
	protected abstract boolean isVowel(String tVow);
	protected abstract boolean isFConsonant(String tFCon);

	protected abstract int getSingleCharVal ( String keySequence );
	
	protected abstract String getSingleChar(int charVal);

}
