package org.manalith.ircbot.plugin.keyseqconv;

import org.manalith.ircbot.plugin.keyseqconv.symboltable.SebeolFinalSymbol;

public class SebeolFinalAutomataEngine extends SebeolAutomataEngine {

	public SebeolFinalAutomataEngine() {
		try {
			syl = new LetterObject(KeyboardLayout.SebeolFinal);
		} catch (IllegalArgumentException e) {
			// Do Nothing.
			// e.printStackTrace();
		}
	}

	public String changeKeyValToInternalSymbol(final String str) {
		String result = "";
		String strbuf = "";

		// do not change an order both 2nd and 3rd

		// 1st phase : change number 0 and 1 to _0 and _1 respectivelly
		strbuf = str.replaceAll("\\_", "_0¯").replaceAll("0", "_0")
				.replaceAll("1", "_1").replaceAll("¯", "0");

		// 2nd phase : change special char with another special char
		// to low lines + alphanumeric characters.
		strbuf = strbuf.replaceAll("2", "_2").replaceAll("3", "_3")
				.replaceAll("4", "_4").replaceAll("5", "_5")
				.replaceAll("6", "_6").replaceAll("7", "_7")
				.replaceAll("8", "gd").replaceAll("9", "b");

		// 3rd phase : change special char printed above number to low line +
		// double number.
		strbuf = strbuf.replaceAll("\\!", "_11").replaceAll("\\@", "_22")
				.replaceAll("\\#", "_33").replaceAll("\\$", "_44")
				.replaceAll("\\%", "_55").replaceAll("\\^", "_66")
				.replaceAll("\\&", "_77").replaceAll("\\*", "_88")
				.replaceAll("\\(", "_99").replaceAll("\\)", "_00");

		// 4th phase : replaceable key sequence for Final Consonant combination
		strbuf = strbuf.replaceAll("xx", "F").replaceAll("s\\_1", "S")
				.replaceAll("wx", "D").replaceAll("wz", "C")
				.replaceAll("w\\_1", "V").replaceAll("\\_3q", "X");

		// final phase : ` ~ - = + [ ] { } ; ' : " , . / < > ?
		result = strbuf.replaceAll("\\`", "_1_").replaceAll("\\~", "_11_")
				.replaceAll("\\-", "__0").replaceAll("\\=", "___0")
				.replaceAll("\\+", "___00").replaceAll("\\\\", "____0")
				.replaceAll("\\|", "____00").replaceAll("\\[", "_p")
				.replaceAll("\\]", "__p").replaceAll("\\{", "_P")
				.replaceAll("\\}", "__P").replaceAll("\\;", "_l")
				.replaceAll("\\'", "__l").replaceAll("\\:", "_L")
				.replaceAll("\\\"", "__L").replaceAll("[\\,\\<]", "_m")
				.replaceAll("[\\.\\>]", "__m").replaceAll("\\/", "v")
				.replaceAll("\\?", "___M");

		return result;
	}

	public boolean isISingleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			SebeolFinalSymbol.SebeolISingleConsonant check1 = SebeolFinalSymbol.SebeolISingleConsonant
					.valueOf(tICon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isIDoubleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			SebeolFinalSymbol.SebeolIDoubleConsonant check1 = SebeolFinalSymbol.SebeolIDoubleConsonant
					.valueOf(tICon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isVowel(String tVow) {
		try {
			@SuppressWarnings("unused")
			SebeolFinalSymbol.SebeolVowel check1 = SebeolFinalSymbol.SebeolVowel
					.valueOf(tVow);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isFConsonant(String tFCon) {
		try {
			@SuppressWarnings("unused")
			SebeolFinalSymbol.SebeolFConsonant check1 = SebeolFinalSymbol.SebeolFConsonant
					.valueOf(tFCon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isSpecialChar(String tChar) {
		try {
			@SuppressWarnings("unused")
			SebeolFinalSymbol.SebeolSpecialChar check1 = SebeolFinalSymbol.SebeolSpecialChar
					.valueOf(tChar);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected String inputIConsonant(boolean isLastPosition, String token) {
		String result = "";

		// 초성 유무 상태 저장
		boolean beingIConsonant = (syl.getIConsonantValue() != SebeolFinalSymbol.SebeolIConsonant.nul
				.value());

		// 초성이 없으면 초성을 넣는다
		if (!beingIConsonant)
			syl.setIConsonant(this.changeKeyValToInternalSymbol(token));

		// 마지막 입력도 아니고 초성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if (!(!isLastPosition && !beingIConsonant)) {
			// 중성 있는 완성 글자 (최소한 초성+중성)
			if (syl.isCompleteSyllable())
				result += syl.getLetter();

			// 초성은 있고 중성이 없다?!
			else {
				SebeolFinalSymbol.SebeolIConsonant init = SebeolFinalSymbol.SebeolIConsonant
						.valueOf(syl.getIConsonantKeySymbol());
				SebeolFinalSymbol.SebeolFConsonant fin = SebeolFinalSymbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 받침이 있으면
				if (syl.isAssignedFConstantFirst())
					result += this.getSingleChar(this.getSingleCharVal(fin
							.toString()))
							+ this.getSingleChar(this.getSingleCharVal(init
									.toString()));
				// 초성만 있다.
				else
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
			}
		}

		// 초성이 있으면
		if (beingIConsonant) {
			// 마지막 입력이면
			if (isLastPosition)
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(this
						.changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 초기화 하고 슬롯에 넣는다.
				syl.initLetter();
				syl.setIConsonant(this.changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	protected String inputVowel(boolean isLastPosition, String token) {
		String result = "";

		// 중성 유무 상태 저장
		boolean beingVowel = (syl.getVowelValue() != SebeolFinalSymbol.SebeolVowel.nul
				.value());

		// 중성이 없으면 중성을 넣는다
		if (!beingVowel)
			syl.setVowel(this.changeKeyValToInternalSymbol(token));

		// 마지막 입력도 아니고 중성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if (!(!isLastPosition && !beingVowel)) {
			// 모음이 이미 있으면서 완성체인가?
			if (syl.isCompleteSyllable())
				result += syl.getLetter();
			// 자음이 없어서 완성체가 아닌 경우
			else {
				SebeolFinalSymbol.SebeolVowel vow = SebeolFinalSymbol.SebeolVowel
						.valueOf(syl.getVowelKeySymbol());
				SebeolFinalSymbol.SebeolFConsonant fin = SebeolFinalSymbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 받침이 있으면
				if (syl.isAssignedFConstantFirst())
					result += this.getSingleChar(this.getSingleCharVal(fin
							.toString()))
							+ this.getSingleChar(this.getSingleCharVal(vow
									.toString()));

				// 받침이 없으면
				else
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));

			}
		}

		// 모음 존재
		if (beingVowel) {
			// 마지막 입력이면
			if (isLastPosition)
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(this
						.changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 초기화 하고 슬롯에 넣는다
				syl.initLetter();
				syl.setVowel(this.changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	protected String inputFConsonant(boolean isLastPosition, String token) {
		String result = "";

		boolean beingFConsonant = (syl.getFConsonantValue() != SebeolFinalSymbol.SebeolFConsonant.nul
				.value());

		// 종성이 슬롯에 없으면 종성을 일단 넣는다.
		if (!beingFConsonant)
			syl.setFConsonant(this.changeKeyValToInternalSymbol(token));

		if (!(!isLastPosition && !beingFConsonant)) {
			// 완성 글자 초+중+종
			if (syl.isCompleteSyllable())
				result += syl.getLetter();

			// 초성 빠져 있고 종성 있으면 (초성이 있고 없고.)
			else {
				SebeolFinalSymbol.SebeolIConsonant init = SebeolFinalSymbol.SebeolIConsonant
						.valueOf(syl.getIConsonantKeySymbol());
				SebeolFinalSymbol.SebeolVowel vow = SebeolFinalSymbol.SebeolVowel
						.valueOf(syl.getVowelKeySymbol());
				SebeolFinalSymbol.SebeolFConsonant fin = SebeolFinalSymbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 초성이 있으면 (초성 -> 종성)
				if (init != SebeolFinalSymbol.SebeolIConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
				// 중성이 있으면 (중성 -> 종성)
				if (vow != SebeolFinalSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));

				result += this.getSingleChar(this.getSingleCharVal(fin
						.toString()));
			}
		}

		// 종성이 슬롯에 있을때
		if (beingFConsonant) {
			// 마지막 잉여 종성 입력이면
			if (isLastPosition)
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(this
						.changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 종성이 이미 있으므로 슬롯을 초기화 하고 넣는다.
				syl.initLetter();
				syl.setFConsonant(this.changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	protected String inputSpecialChar(String token) {
		String result = "";
		String internalSymbol = this.changeKeyValToInternalSymbol(token);
		SebeolFinalSymbol.SebeolSpecialChar spec = SebeolFinalSymbol.SebeolSpecialChar
				.valueOf(internalSymbol);

		if (syl.isCompleteSyllable()) {
			result += syl.getLetter();
		}
		// 자음이나 모음이 없다.
		else {
			SebeolFinalSymbol.SebeolIConsonant init = SebeolFinalSymbol.SebeolIConsonant
					.valueOf(syl.getIConsonantKeySymbol());
			SebeolFinalSymbol.SebeolVowel vow = SebeolFinalSymbol.SebeolVowel
					.valueOf(syl.getVowelKeySymbol());
			SebeolFinalSymbol.SebeolFConsonant fin = SebeolFinalSymbol.SebeolFConsonant
					.valueOf(syl.getFConsonantKeySymbol());

			// 받침이 있으면
			if (syl.isAssignedFConstantFirst()) {
				result += this.getSingleChar(this.getSingleCharVal(fin
						.toString()));
				// 초성이 있으면
				if (init != SebeolFinalSymbol.SebeolIConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
				if (vow != SebeolFinalSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));
			}
			// 초성만 있거나 중성만 있거나 아무것도 없다?
			else {
				if (init != SebeolFinalSymbol.SebeolIConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
				if (vow != SebeolFinalSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));
				if (fin != SebeolFinalSymbol.SebeolFConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(fin
							.toString()));

			}

		}

		result += this.getSpecialChar(spec.value());
		syl.initLetter();
		return result;
	}

	protected String inputOtherChar(String token) {
		String result = "";

		if (syl.isCompleteSyllable()) {
			result += syl.getLetter();
		} else {
			SebeolFinalSymbol.SebeolIConsonant init = SebeolFinalSymbol.SebeolIConsonant
					.valueOf(syl.getIConsonantKeySymbol());
			SebeolFinalSymbol.SebeolVowel vow = SebeolFinalSymbol.SebeolVowel
					.valueOf(syl.getVowelKeySymbol());
			SebeolFinalSymbol.SebeolFConsonant fin = SebeolFinalSymbol.SebeolFConsonant
					.valueOf(syl.getFConsonantKeySymbol());

			// 받침이 있으면
			if (syl.isAssignedFConstantFirst()) {
				result += this.getSingleChar(this.getSingleCharVal(fin
						.toString()));
				// 초성이 있으면
				if (init != SebeolFinalSymbol.SebeolIConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
				if (vow != SebeolFinalSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));
			}
			// 초성만 있거나 아무것도 없다?
			else {
				if (init != SebeolFinalSymbol.SebeolIConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(init
							.toString()));
				if (vow != SebeolFinalSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow
							.toString()));
				if (fin != SebeolFinalSymbol.SebeolFConsonant.nul)
					result += this.getSingleChar(this.getSingleCharVal(fin
							.toString()));
			}
		}

		result += token;
		syl.initLetter();
		return result;
	}

	@Override
	public int getSingleCharVal(String keySymbol) {
		return SebeolFinalSymbol.SebeolSingleLetter.valueOf(keySymbol).value();
	}
}
