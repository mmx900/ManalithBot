package org.manalith.ircbot.plugin.keyseqconv;

import java.text.ParseException;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class SebeolAutomataEngine implements IAutomataEngine {

	protected boolean enableParseExceptionSyntax;
	protected LetterObject syl;

	public SebeolAutomataEngine() {
	}

	public abstract String changeKeyValToInternalSymbol(final String str);

	@Override
	public abstract boolean isISingleConsonant(String tICon);

	@Override
	public abstract boolean isIDoubleConsonant(String tICon);

	@Override
	public abstract boolean isVowel(String tVow);

	@Override
	public abstract boolean isFConsonant(String tFCon);

	public abstract boolean isSpecialChar(String tChar);

	protected abstract String inputIConsonant(boolean isLastPosition,
			String token);

	protected abstract String inputVowel(boolean isLastPosition, String token);

	protected abstract String inputFConsonant(boolean isLastPosition,
			String token);

	protected abstract String inputSpecialChar(String token);

	protected abstract String inputOtherChar(String token);

	// Example in body :
	// return SebeolSymbol.SebeolSingleLetter.valueOf(keySymbol).value();
	@Override
	public abstract int getSingleCharVal(String keySymbol);

	@Override
	public final void setEnableParsingExceptionSyntax(boolean enable) {
		enableParseExceptionSyntax = enable;
	}

	@Override
	public final boolean isEnableParsingExceptionSyntax() {
		return enableParseExceptionSyntax;
	}

	@Override
	public final String parseKoreanStringToEngSpell(String korean) {
		return "";
	}

	@Override
	public final String parseKeySequenceToKorean(String keySequence)
			throws ParseException, IllegalArgumentException {
		String result = "";

		String tToken = "";
		String tTokenLookahead = "";
		String tTokenLookaheadCombination = "";

		// 기본값은 마지막 위치가 아닌걸로 태깅
		boolean isLastPosition = false;

		if (isEnableParsingExceptionSyntax()
				&& StringUtils.countMatches(keySequence, "\\") % 2 == 1)
			throw new ParseException("Back slashes do not match",
					keySequence.lastIndexOf("\\", 0));

		for (int i = 0; i < keySequence.length(); i++) {
			if (!CharUtils.isAsciiAlpha(keySequence.charAt(i))) {
				if (keySequence.charAt(i) == '\\'
						&& isEnableParsingExceptionSyntax()) {
					if (i < keySequence.length() - 1)
						if (keySequence.charAt(i + 1) == '\\') {
							result += "\\";
							continue;
						}
					i++;
					while (true) {
						if (i + 1 <= keySequence.length() - 1) {
							if (keySequence.charAt(i) == '\\') {
								if (keySequence.charAt(i + 1) == '\\') {
									i++;
									result += '\\';
								} else
									break;
							} else {
								result += keySequence.charAt(i);
							}
						} else {
							if (keySequence.charAt(i) == '\\') {
								break;
							} else {
								result += keySequence.charAt(i);
							}
						}
						i++;
					}
					continue;
				}
			}

			tTokenLookahead = tTokenLookaheadCombination = "";
			tToken = Character.toString(keySequence.charAt(i));

			if (i < keySequence.length() - 1) {
				tTokenLookahead = Character.toString(keySequence.charAt(i + 1));
				tTokenLookaheadCombination = tToken + tTokenLookahead;

				// 마지막 위치 여부 저장
				isLastPosition = (i + 1 == keySequence.length() - 1);

				// 쌍자음
				if (isIDoubleConsonant(changeKeyValToInternalSymbol(tTokenLookaheadCombination))) {
					result += inputIConsonant(isLastPosition,
							tTokenLookaheadCombination);
					if (isLastPosition) {
						syl.initLetter();
						break;
					} else {
						i++;
						continue;
					}
				}
				// 겹모음!
				else if (isVowel(changeKeyValToInternalSymbol(tTokenLookaheadCombination))) {
					result += inputVowel(isLastPosition,
							tTokenLookaheadCombination);
					if (isLastPosition) {
						syl.initLetter();
						break;
					} else {
						i++;
						continue;
					}
				}
				// 겹받침
				else if (isFConsonant(changeKeyValToInternalSymbol(tTokenLookaheadCombination))) {
					result += inputFConsonant(isLastPosition,
							tTokenLookaheadCombination);
					if (isLastPosition) {
						syl.initLetter();
						break;
					} else {
						i++;
						continue;
					}
				}
			}

			// 쌍자음,겹모음 검사에서 통과하지 않았으므로 단자음 단모음 받침 검사
			// 마지막 위치 여부 입력
			isLastPosition = (i == keySequence.length() - 1);

			// 단자음
			if (isISingleConsonant(changeKeyValToInternalSymbol(tToken)))
				result += inputIConsonant(isLastPosition, tToken);

			// 단모음
			else if (isVowel(changeKeyValToInternalSymbol(tToken)))
				result += inputVowel(isLastPosition, tToken);

			// 받침은 키 하나로만 입력
			else if (isFConsonant(changeKeyValToInternalSymbol(tToken)))
				result += inputFConsonant(isLastPosition, tToken);

			else if (isSpecialChar(changeKeyValToInternalSymbol(tToken)))
				result += inputSpecialChar(tToken);
			else
				result += inputOtherChar(tToken);

			// 마지막이면 루프를 깨고 아니면 계속
			if (isLastPosition) {
				syl.initLetter();
				break;
			}
		}

		syl.initLetter();
		return result;
	}

	@Override
	public final String getSingleChar(int charVal) {
		char[] ch = new char[1];
		// single char value starts from 0x1100
		ch[0] = (char) (charVal + 0x1100);

		return new String(ch);
	}

	public final String getSpecialChar(int charVal) {
		char[] ch = new char[1];
		ch[0] = (char) (charVal + 32);

		return new String(ch);
	}

}
