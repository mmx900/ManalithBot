package org.manalith.ircbot.plugin.keyseqconv;

import org.manalith.ircbot.plugin.keyseqconv.symboltable.Sebeol390Symbol;

public class Sebeol390AutomataEngine extends SebeolAutomataEngine {

	public Sebeol390AutomataEngine() {
		try {
			syl = new LetterObject(KeyboardLayout.Sebeol390);
		} catch (IllegalArgumentException e) {
			// Do Nothing.
			// e.printStackTrace();
		}
	}

	@Override
	public String changeKeyValToInternalSymbol(final String str) {
		String result = "";
		String strbuf = "";

		// do not change an order both 2nd and 3rd

		// 1st phase : change number 0 and 1 to _0 and _1 respectivelly
		strbuf = str.replaceAll("\\_", "_0¯").replaceAll("0", "_0")
				.replaceAll("1", "_1").replaceAll("¯", "0");

		// 2nd phase : change alphanumeric char to
		// low line + alphanumeric character.
		strbuf = strbuf.replaceAll("2", "_2").replaceAll("3", "_3")
				.replaceAll("4", "_4").replaceAll("5", "_5")
				.replaceAll("6", "_6").replaceAll("7", "_7")
				// gd: ㅢ , b: ㅜ
				.replaceAll("8", "gd").replaceAll("9", "b");

		// 3rd phase : change special char printed above number to
		// low line + double number.
		strbuf = strbuf.replaceAll("\\!", "_11").replaceAll("\\@", "_22")
				.replaceAll("\\#", "_33").replaceAll("\\$", "_44")
				.replaceAll("\\%", "_55").replaceAll("\\^", "_66")
				.replaceAll("\\&", "_77").replaceAll("\\*", "_88")
				.replaceAll("\\(", "_99").replaceAll("\\)", "_00");

		// 4th phase : replaceable key sequence for Final Consonant combination
		strbuf = strbuf.replace("xq", "V").replaceAll("s\\_33", "E")
				.replaceAll("s\\_1", "S").replaceAll("wx", "_22")
				.replaceAll("wz", "F").replaceAll("w\\_3", "D")
				.replaceAll("wq", "T").replaceAll("wW", "_55")
				.replaceAll("w_1", "R").replaceAll("_3q", "X")
				.replaceAll("qq", "_2");

		// final phase : ` ~ - = + [ ] { } ; ' : " , . / < > ?
		result = strbuf.replaceAll("\\`", "_1_").replaceAll("\\~", "_11_")
				.replaceAll("\\-", "__0").replaceAll("\\=", "___0")
				.replaceAll("\\+", "___00").replaceAll("\\\\", "____0")
				.replaceAll("\\|", "____00").replaceAll("\\[", "_p")
				.replaceAll("\\]", "__p").replaceAll("\\{", "_P")
				.replaceAll("\\}", "__P").replaceAll("\\;", "_l")
				.replaceAll("\\'", "__l").replaceAll("\\:", "_L")
				.replaceAll("\\\"", "__L").replaceAll("\\,", "_m")
				.replaceAll("\\<", "_M").replaceAll("\\.", "__m")
				.replaceAll("\\>", "__M").replaceAll("\\?", "___M")
				// "ㅗ" -> v
				.replaceAll("\\/", "v");

		return result;
	}

	@Override
	public boolean isISingleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			Sebeol390Symbol.SebeolISingleConsonant check1 = Sebeol390Symbol.SebeolISingleConsonant
					.valueOf(tICon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isIDoubleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			Sebeol390Symbol.SebeolIDoubleConsonant check1 = Sebeol390Symbol.SebeolIDoubleConsonant
					.valueOf(tICon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isVowel(String tVow) {
		try {
			@SuppressWarnings("unused")
			Sebeol390Symbol.SebeolVowel check1 = Sebeol390Symbol.SebeolVowel
					.valueOf(tVow);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isFConsonant(String tFCon) {
		try {
			@SuppressWarnings("unused")
			Sebeol390Symbol.SebeolFConsonant check1 = Sebeol390Symbol.SebeolFConsonant
					.valueOf(tFCon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isSpecialChar(String tChar) {
		try {
			@SuppressWarnings("unused")
			Sebeol390Symbol.SebeolSpecialChar check1 = Sebeol390Symbol.SebeolSpecialChar
					.valueOf(tChar);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected String inputIConsonant(boolean isLastPosition, String token) {
		String result = "";

		// 초성 유무 상태 저장
		boolean beingIConsonant = (syl.getIConsonantValue() != Sebeol390Symbol.SebeolIConsonant.nul
				.value());

		// 초성이 없으면 초성을 넣는다
		if (!beingIConsonant)
			syl.setIConsonant(changeKeyValToInternalSymbol(token));

		// 마지막 입력도 아니고 초성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if (!(!isLastPosition && !beingIConsonant)) {
			// 중성 있는 완성 글자 (최소한 초성+중성)
			if (syl.isCompleteSyllable())
				result += syl.getLetter();

			// 초성은 있고 중성이 없다?!
			else {
				Sebeol390Symbol.SebeolIConsonant init = Sebeol390Symbol.SebeolIConsonant
						.valueOf(syl.getIConsonantKeySymbol());
				Sebeol390Symbol.SebeolFConsonant fin = Sebeol390Symbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 받침이 있으면
				if (syl.isAssignedFConstantFirst())
					result += getSingleChar(getSingleCharVal(fin.toString()))
							+ getSingleChar(getSingleCharVal(init.toString()));
				// 초성만 있다.
				else
					result += getSingleChar(getSingleCharVal(init.toString()));
			}
		}

		// 초성이 있으면
		if (beingIConsonant) {
			// 마지막 입력이면
			if (isLastPosition)
				// 출력
				result += getSingleChar(getSingleCharVal(changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 초기화 하고 슬롯에 넣는다.
				syl.initLetter();
				syl.setIConsonant(changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	@Override
	protected String inputVowel(boolean isLastPosition, String token) {
		String result = "";

		// 중성 유무 상태 저장
		boolean beingVowel = (syl.getVowelValue() != Sebeol390Symbol.SebeolVowel.nul
				.value());

		// 중성이 없으면 중성을 넣는다
		if (!beingVowel)
			syl.setVowel(changeKeyValToInternalSymbol(token));

		// 마지막 입력도 아니고 중성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if (!(!isLastPosition && !beingVowel)) {
			// 모음이 이미 있으면서 완성체인가?
			if (syl.isCompleteSyllable())
				result += syl.getLetter();
			// 자음이 없어서 완성체가 아닌 경우
			else {
				Sebeol390Symbol.SebeolVowel vow = Sebeol390Symbol.SebeolVowel
						.valueOf(syl.getVowelKeySymbol());
				Sebeol390Symbol.SebeolFConsonant fin = Sebeol390Symbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 받침이 있으면
				if (syl.isAssignedFConstantFirst())
					result += getSingleChar(getSingleCharVal(fin.toString()))
							+ getSingleChar(getSingleCharVal(vow.toString()));

				// 받침이 없으면
				else
					result += getSingleChar(getSingleCharVal(vow.toString()));

			}
		}

		// 모음 존재
		if (beingVowel) {
			// 마지막 입력이면
			if (isLastPosition)
				// 출력
				result += getSingleChar(getSingleCharVal(changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 초기화 하고 슬롯에 넣는다
				syl.initLetter();
				syl.setVowel(changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	@Override
	protected String inputFConsonant(boolean isLastPosition, String token) {
		String result = "";

		boolean beingFConsonant = (syl.getFConsonantValue() != Sebeol390Symbol.SebeolFConsonant.nul
				.value());

		// 종성이 슬롯에 없으면 종성을 일단 넣는다.
		if (!beingFConsonant)
			syl.setFConsonant(changeKeyValToInternalSymbol(token));

		if (!(!isLastPosition && !beingFConsonant)) {
			// 완성 글자 초+중+종
			if (syl.isCompleteSyllable())
				result += syl.getLetter();

			// 초성 빠져 있고 종성 있으면 (초성이 있고 없고.)
			else {
				Sebeol390Symbol.SebeolIConsonant init = Sebeol390Symbol.SebeolIConsonant
						.valueOf(syl.getIConsonantKeySymbol());
				Sebeol390Symbol.SebeolVowel vow = Sebeol390Symbol.SebeolVowel
						.valueOf(syl.getVowelKeySymbol());
				Sebeol390Symbol.SebeolFConsonant fin = Sebeol390Symbol.SebeolFConsonant
						.valueOf(syl.getFConsonantKeySymbol());

				// 초성이 있으면 (초성 -> 종성)
				if (init != Sebeol390Symbol.SebeolIConsonant.nul)
					result += getSingleChar(getSingleCharVal(init.toString()));
				// 중성이 있으면 (중성 -> 종성)
				if (vow != Sebeol390Symbol.SebeolVowel.nul)
					result += getSingleChar(getSingleCharVal(vow.toString()));

				result += getSingleChar(getSingleCharVal(fin.toString()));
			}
		}

		// 종성이 슬롯에 있을때
		if (beingFConsonant) {
			// 마지막 잉여 종성 입력이면
			if (isLastPosition)
				// 출력
				result += getSingleChar(getSingleCharVal(changeKeyValToInternalSymbol(token)));
			// 아니면
			else {
				// 종성이 이미 있으므로 슬롯을 초기화 하고 넣는다.
				syl.initLetter();
				syl.setFConsonant(changeKeyValToInternalSymbol(token));
			}
		}

		return result;
	}

	@Override
	protected String inputSpecialChar(String token) {
		String result = "";
		String internalSymbol = changeKeyValToInternalSymbol(token);
		Sebeol390Symbol.SebeolSpecialChar spec = Sebeol390Symbol.SebeolSpecialChar
				.valueOf(internalSymbol);

		if (syl.isCompleteSyllable()) {
			result += syl.getLetter();
		}
		// 자음이나 모음이 없다.
		else {
			Sebeol390Symbol.SebeolIConsonant init = Sebeol390Symbol.SebeolIConsonant
					.valueOf(syl.getIConsonantKeySymbol());
			Sebeol390Symbol.SebeolVowel vow = Sebeol390Symbol.SebeolVowel
					.valueOf(syl.getVowelKeySymbol());
			Sebeol390Symbol.SebeolFConsonant fin = Sebeol390Symbol.SebeolFConsonant
					.valueOf(syl.getFConsonantKeySymbol());

			// 받침이 있으면
			if (syl.isAssignedFConstantFirst()) {
				result += getSingleChar(getSingleCharVal(fin.toString()));
				// 초성이 있으면
				if (init != Sebeol390Symbol.SebeolIConsonant.nul)
					result += getSingleChar(getSingleCharVal(init.toString()));
				if (vow != Sebeol390Symbol.SebeolVowel.nul)
					result += getSingleChar(getSingleCharVal(vow.toString()));
			}
			// 초성만 있거나 중성만 있거나 아무것도 없다?
			else {
				if (init != Sebeol390Symbol.SebeolIConsonant.nul)
					result += getSingleChar(getSingleCharVal(init.toString()));
				if (vow != Sebeol390Symbol.SebeolVowel.nul)
					result += getSingleChar(getSingleCharVal(vow.toString()));
				if (fin != Sebeol390Symbol.SebeolFConsonant.nul)
					result += getSingleChar(getSingleCharVal(fin.toString()));

			}

		}

		result += getSpecialChar(spec.value());
		syl.initLetter();
		return result;
	}

	@Override
	protected String inputOtherChar(String token) {
		String result = "";

		if (syl.isCompleteSyllable()) {
			result += syl.getLetter();
		} else {
			Sebeol390Symbol.SebeolIConsonant init = Sebeol390Symbol.SebeolIConsonant
					.valueOf(syl.getIConsonantKeySymbol());
			Sebeol390Symbol.SebeolVowel vow = Sebeol390Symbol.SebeolVowel
					.valueOf(syl.getVowelKeySymbol());
			Sebeol390Symbol.SebeolFConsonant fin = Sebeol390Symbol.SebeolFConsonant
					.valueOf(syl.getFConsonantKeySymbol());

			// 받침이 있으면
			if (syl.isAssignedFConstantFirst()) {
				result += getSingleChar(getSingleCharVal(fin.toString()));
				// 초성이 있으면
				if (init != Sebeol390Symbol.SebeolIConsonant.nul)
					result += getSingleChar(getSingleCharVal(init.toString()));
				if (vow != Sebeol390Symbol.SebeolVowel.nul)
					result += getSingleChar(getSingleCharVal(vow.toString()));
			}
			// 초성만 있거나 아무것도 없다?
			else {
				if (init != Sebeol390Symbol.SebeolIConsonant.nul)
					result += getSingleChar(getSingleCharVal(init.toString()));
				if (vow != Sebeol390Symbol.SebeolVowel.nul)
					result += getSingleChar(getSingleCharVal(vow.toString()));
				if (fin != Sebeol390Symbol.SebeolFConsonant.nul)
					result += getSingleChar(getSingleCharVal(fin.toString()));
			}
		}

		result += token;
		syl.initLetter();
		return result;
	}

	@Override
	public int getSingleCharVal(String keySymbol) {
		return Sebeol390Symbol.SebeolSingleLetter.valueOf(keySymbol).value();
	}
}
