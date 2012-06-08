package org.manalith.ircbot.plugin.keyseqconv;

import org.manalith.ircbot.plugin.keyseqconv.exceptions.BackSlashesDoNotMatchException;
import org.manalith.ircbot.plugin.keyseqconv.exceptions.LayoutNotSpecifiedException;

public interface IAutomataEngine {
	public void setEnableParsingExceptionSyntax(boolean enable);
	public boolean isEnableParsingExceptionSyntax();
	
	public String parseKoreanStringToEngSpell(String korean)
			throws BackSlashesDoNotMatchException;
	public String parseKeySequenceToKorean(String keySequence)
			throws BackSlashesDoNotMatchException, LayoutNotSpecifiedException;
}
