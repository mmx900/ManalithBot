package org.manalith.ircbot.plugin.typeconv;

/*
 org.manalith.ircbot.plugin.typeconv/DobeolAutomataEngine.java
 ManalithBot - An open source IRC bot based on the PircBot Framework.
 Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.typeconv.exceptions.BackSlashesDoNotMatchException;

public class DobeolAutomataEngine {

	private boolean enableParseExceptionSyntax;

	public DobeolAutomataEngine() {
		;
	}

	public void setEnableParsingExceptionSyntax(boolean enable) {
		this.enableParseExceptionSyntax = enable;
	}

	public boolean isEnableParsingExceptionSyntax() {
		return this.enableParseExceptionSyntax;
	}

	private static enum State {
		Null, IConsonant, Vowel, FConsonant, Finish
	};

	public boolean isIConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			DobeolSymbol.DobeolIConsonant check1 = DobeolSymbol.DobeolIConsonant
					.valueOf(tICon); // dummy alloc.
			return true;
		} catch (Exception e)
		// if checked character is abnormal
		{
			return false;
		}
	}

	public boolean isVowel(String tVow) {
		try {
			@SuppressWarnings("unused")
			DobeolSymbol.DobeolVowel check1 = DobeolSymbol.DobeolVowel
					.valueOf(tVow); // dummy alloc.
			return true;
		} catch (Exception e)
		// if checked character is abnormal
		{
			return false;
		}
	}

	public boolean isFConsonant(String tFCon) {
		try {
			@SuppressWarnings("unused")
			DobeolSymbol.DobeolFConsonant check1 = DobeolSymbol.DobeolFConsonant
					.valueOf(tFCon); // dummy alloc.
			return true;
		} catch (Exception e)
		// if checked character is abnormal
		{
			return false;
		}
	}

	public String parseKoreanStringToEngSpell(String korean)
			throws BackSlashesDoNotMatchException {
		String result = "";

		for (int i = 0; i < korean.length(); i++) {
			char ch = korean.charAt(i);

			// System.out.println(charVal);

			if (ch >= '가' && ch <= '힣') {
				byte[] bytes = Character.toString(ch).getBytes();
				int charVal = ((bytes[0] & 0x0f) << 12)
						| ((bytes[1] & 0x3f) << 6) | (bytes[2] & 0x3f);
				charVal -= 0xAC00;
				int initialConsonant = charVal / (21 * 28);
				charVal %= (21 * 28);
				int Vowel = charVal / 28;
				charVal %= 28;
				int finalConsonant = charVal;

				result += DobeolSymbol.DobeolIConsonant.values()[initialConsonant + 1]
						.toString()
						+ DobeolSymbol.DobeolVowel.values()[Vowel + 1]
								.toString();
				if (finalConsonant != 0)
					result += DobeolSymbol.DobeolFConsonant.values()[finalConsonant];
			} else if (ch >= 'ㄱ' && ch <= 'ㅣ') {
				byte[] bytes = Character.toString(ch).getBytes();
				int charVal = ((bytes[0] & 0x0f) << 12)
						| ((bytes[1] & 0x3f) << 6) | (bytes[2] & 0x3f);
				charVal -= 0x3130;
				result += DobeolSymbol.DobeolSingleLetter.values()[charVal - 1];
			} else {
				result += ch;
			}

		}

		return result;
	}

	public String parseKeySequenceToKorean(String keySequence)
			throws BackSlashesDoNotMatchException {
		String result = "";

		State stateFlag = State.Null;

		String tICon = "";
		String tIConLookahead = "";
		String tIConLookaheadCombination = "";
		String tVow = "";
		String tVowLookahead = "";
		String tVowLookaheadCombination = "";
		String tFCon = "";
		String tFConLookahead = "";
		String tFConLookaheadCombination = "";

		DobeolSymbol.DobeolIConsonant init = DobeolSymbol.DobeolIConsonant.nul;
		DobeolSymbol.DobeolVowel vow = DobeolSymbol.DobeolVowel.nul;
		DobeolSymbol.DobeolFConsonant fin = DobeolSymbol.DobeolFConsonant.nul;

		if (this.isEnableParsingExceptionSyntax()
				&& StringUtils.countMatches(keySequence, "\\") % 2 == 1)
			throw new BackSlashesDoNotMatchException();

		for (int i = 0; i < keySequence.length(); i++) {
			if (stateFlag.equals(State.Null) || stateFlag.equals(State.Finish)) {
				stateFlag = State.IConsonant;
			}

			if (!CharUtils.isAsciiAlpha(keySequence.charAt(i))) {
				if (keySequence.charAt(i) == '\\'
						&& this.isEnableParsingExceptionSyntax()) {
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
				} else {
					result += keySequence.charAt(i);
				}
				continue;
			}
			// 초성 (자음, 쌍자음)
			if (stateFlag.equals(State.IConsonant)) {
				// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
				tICon = this.hasTwoSymbolinOneKey(keySequence.charAt(i)) ? Character
						.toString(keySequence.charAt(i)) : Character.toString(
						keySequence.charAt(i)).toLowerCase();
				// 다음 키 시퀀스랑 조합 분석하기 위한 할당
				if (i < keySequence.length() - 1) {
					// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
					tIConLookahead = this.hasTwoSymbolinOneKey(keySequence
							.charAt(i + 1)) ? Character.toString(keySequence
							.charAt(i + 1)) : Character.toString(
							keySequence.charAt(i + 1)).toLowerCase();
					tIConLookaheadCombination = tICon + tIConLookahead;
				}

				// 받침자음은 첫자음을 포함하며, 수가 더 많음.
				if (this.isFConsonant(tIConLookaheadCombination)) {

					// 2 step - lookahead가 가능하면 try
					if (i + 2 <= keySequence.length() - 1) {
						String lookOverTwoStep = this
								.hasTwoSymbolinOneKey(keySequence.charAt(i + 2)) ? Character
								.toString(keySequence.charAt(i + 2))
								: Character.toString(keySequence.charAt(i + 2))
										.toLowerCase();

						// 자음 두번 입력 후, 자음 혹은 특수문자, 공백, 숫자
						if (this.isIConsonant(lookOverTwoStep)
								|| !CharUtils.isAsciiAlpha(lookOverTwoStep
										.charAt(0))) {
							result += this
									.getSingleLetter(tIConLookaheadCombination);
							tICon = tIConLookahead = tIConLookaheadCombination = "";
							i++;
						}
						// 자음 두번 입력 후 모음
						else if (this.isVowel(lookOverTwoStep)) {
							result += this.getSingleLetter(tICon);
							tICon = tIConLookahead = tIConLookaheadCombination = "";
							continue;
						}

					}
					// 문장의 마지막에 (받침용) 겹자음 입력했을 경우 출력 후 종료
					else {
						result += this
								.getSingleLetter(tIConLookaheadCombination);
						tICon = tIConLookahead = tIConLookaheadCombination = "";
						stateFlag = State.Null;
						break;
					}
				}
				// 쌍자음, 단자음인 경우 ( (받침용) 겹자음 제외)
				else {
					// 자음이면 대기 슬롯에 넣음
					if (this.isIConsonant(tICon)) {
						init = DobeolSymbol.DobeolIConsonant.valueOf(tICon);
					}
					// 모음이면 독립모음을 찍기 위해 모음 단계부터 다시 처리
					else if (this.isVowel(tICon)) {
						i--;
						stateFlag = State.Vowel;
						continue;
					}

					// 문장의 마지막에 단일 자음 입력했을 경우 출력 후 종료
					if (i == keySequence.length() - 1) {
						result += this.getSingleLetter(tICon);
						tICon = tIConLookahead = tIConLookaheadCombination = "";
						init = DobeolSymbol.DobeolIConsonant.nul;
						stateFlag = State.Null;

						break;
					}

					// 현재 자음이고 다음 글자가 모음이면 모음을 확인하기 위해 계속 진행
					if (this.isVowel(tIConLookahead)) {
						stateFlag = State.Vowel;
						continue;
					}
					// 초성 자음이 뒤따라 오는 경우
					else {
						result += this.getSingleLetter(tICon);
						tVow = tVowLookahead = tVowLookaheadCombination = "";
						init = DobeolSymbol.DobeolIConsonant.nul;
					}
				}
			}
			// 중성 (모음)
			else if (stateFlag.equals(State.Vowel)) {
				// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
				tVow = this.hasTwoSymbolinOneKey(keySequence.charAt(i)) ? Character
						.toString(keySequence.charAt(i)) : Character.toString(
						keySequence.charAt(i)).toLowerCase();
				// 다음 키 시퀀스랑 조합 분석하기 위한 할당
				if (i < keySequence.length() - 1) {
					// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
					tVowLookahead = this.hasTwoSymbolinOneKey(keySequence
							.charAt(i + 1)) ? Character.toString(keySequence
							.charAt(i + 1)) : Character.toString(
							keySequence.charAt(i + 1)).toLowerCase();
					tVowLookaheadCombination = tVow + tVowLookahead;
				}

				// 겹모음인 경우?
				if (this.isVowel(tVowLookaheadCombination)) {
					vow = DobeolSymbol.DobeolVowel
							.valueOf(tVowLookaheadCombination);

					// 2 step - lookahead가 가능하면 try
					if (i + 2 <= keySequence.length() - 1) {
						// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
						String lookOverTwoStep = this
								.hasTwoSymbolinOneKey(keySequence.charAt(i + 2)) ? Character
								.toString(keySequence.charAt(i + 2))
								: Character.toString(keySequence.charAt(i + 2))
										.toLowerCase();

						i++;
						// 겹모음에 모음이 또 따라오면 현재 글자는 완성, 다음 모음은 독립적인 존재.
						// 다음에 오는 자음은 받침에 쓸 수 없을 경우 이 과정을 밟는다
						if (this.isVowel(lookOverTwoStep)
								|| (this.isIConsonant(lookOverTwoStep) && !this
										.isFConsonant(lookOverTwoStep))) {
							tVow = tVowLookahead = tVowLookaheadCombination = "";
							stateFlag = State.Finish;
						}
						// 겹모음에 받침이 따라오는 경우 받침을 찾을 차례, ex: 왠
						else if (this.isFConsonant(lookOverTwoStep)) {
							if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
								result += this
										.getSingleLetter(tVowLookaheadCombination);
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.Null;
							} else {
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.FConsonant;
								continue;
							}
						} else if (!CharUtils.isAsciiAlpha(lookOverTwoStep
								.charAt(0))) {
							if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
								result += this
										.getSingleLetter(tVowLookaheadCombination);
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								vow = DobeolSymbol.DobeolVowel.nul;
								stateFlag = State.Null;
							} else {
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.Finish;
							}
						}
					}
					// 다음 문자 밖에 볼 수 없을 경우 (시퀀스의 마지막)
					else {
						// 자음이 대기 슬롯에 없으면 독립 모음 출력
						if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
							result += this
									.getSingleLetter(tVowLookaheadCombination);
							i++;
							tVow = tVowLookahead = tVowLookaheadCombination = "";
							stateFlag = State.Null;
						} else {
							stateFlag = State.Finish;
						}

						// 포인터가 시퀀스의 마지막에 있으면 종료
						if (i == keySequence.length() - 1)
							break;
					}
				}
				// 겹모음이 아닌 경우, ㅏ ㅐ ㅑ ㅒ ㅓ ㅖ ㅕ ㅖ ㅗ ㅛ ㅜ ㅠ ㅡ ㅣ
				else {
					// 현재 키 시퀀스가 모음에 해당해야 함
					if (this.isVowel(tVow)) {
						// 모음을 대기 슬롯에 넣는다.
						if (vow.equals(DobeolSymbol.DobeolVowel.nul)) {
							vow = DobeolSymbol.DobeolVowel.valueOf(tVow);
						}

						// 키 시퀀스의 마지막이라면
						if (i == keySequence.length() - 1) {
							// 자음이 존재하지 않는다면 독립 모음 입력
							if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
								result += this.getSingleLetter(tVow);
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.Null;
							}
							// 자음이 있으면 받침이 없는 완전한 글자 완성
							else {
								stateFlag = State.Finish;
							}
							break;
						}

						// 2벌식 초성 중성 종성은 영문글자의 위치 영역에 있으므로
						// 영문자가 아닌 문자를 별개문자 혹은 delimiter로 취급
						// 뒤에 공백, 숫자, 특수문자 따라오는 경우.
						if (!CharUtils.isAsciiAlpha(tVowLookahead.charAt(0))) {

							// 초성이 없다면 독립 모음 입력.
							if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
								result += this.getSingleLetter(tVow);
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								vow = DobeolSymbol.DobeolVowel.nul;
								stateFlag = State.IConsonant;
								continue;
							} else
								stateFlag = State.Finish;
						}

						// *// 접근이 안되는 코드인듯. 주석처리
						// 자음이 입력되지 않았을 경우
						if (init.equals(DobeolSymbol.DobeolIConsonant.nul)) {
							// 독립 모음
							result += this.getSingleLetter(tVow);
							vow = DobeolSymbol.DobeolVowel.nul;

							// 다음이 자음이면 자음으로
							if (this.isIConsonant(tVowLookahead)) {
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.IConsonant;
							}
							// 모음이면 모음으로 검색모드 전환
							else if (this.isVowel(tVowLookahead)) {
								tVow = tVowLookahead = tVowLookaheadCombination = "";
								stateFlag = State.Vowel;
							}
							continue;
						} else {
							// 자음이 입력되었을 때 모음 다음 모음 오는 경우, 예: 거ㅣ
							// ㄸ 과 같은 자음은 받침으로 쓰이지 않는다.
							if (this.isVowel(tVowLookahead)
									|| (this.isIConsonant(tVowLookahead) && !this
											.isFConsonant(tVowLookahead)))
								stateFlag = State.Finish;
							// 자음 + 모음 + 받침 : good!
							else if (this.isFConsonant(tVowLookahead))
								stateFlag = State.FConsonant;

							tVow = tVowLookahead = tVowLookaheadCombination = "";
						}
					}
				}
			}
			// 종성
			else if (stateFlag.equals(State.FConsonant)) {
				// 대 소문자에 따라 값이 바뀌는 키라면 그냥 넣어주고 아니면 소문자로 바꿔준다
				tFCon = this.hasTwoSymbolinOneKey(keySequence.charAt(i)) ? Character
						.toString(keySequence.charAt(i)) : Character.toString(
						keySequence.charAt(i)).toLowerCase();
				// 다음 키 시퀀스랑 조합 분석하기 위한 할당
				if (i < keySequence.length() - 1) {
					tFConLookahead = this.hasTwoSymbolinOneKey(keySequence
							.charAt(i + 1)) ? Character.toString(keySequence
							.charAt(i + 1)) : Character.toString(
							keySequence.charAt(i + 1)).toLowerCase();
					tFConLookaheadCombination = tFCon + tFConLookahead;
				}

				stateFlag = State.Finish; // 받침이 나오면 한 글자 완성이 끝남.

				// 받침용 겹모음이라면?
				if (this.isFConsonant(tFConLookaheadCombination)) {

					// 2 step - lookahead가 가능하면 try
					if (i + 2 <= keySequence.length() - 1) {
						String lookOverTwoStep = this
								.hasTwoSymbolinOneKey(keySequence.charAt(i)) ? Character
								.toString(keySequence.charAt(i + 2))
								: Character.toString(keySequence.charAt(i + 2))
										.toLowerCase();

						// (받침용) 겹자음에 자음이 뒤따라오는 모양새
						if (this.isIConsonant(lookOverTwoStep)
								|| !CharUtils.isAsciiAlpha(lookOverTwoStep
										.charAt(0))) {
							// 받침을 대기 슬롯에 넣는다. 겹자음이므로 키 시퀀스를 하나 건너뛴다
							fin = DobeolSymbol.DobeolFConsonant
									.valueOf(tFConLookaheadCombination);
							i++;
							// 단자음 받침일 수도 있다. 받침, 자음 + 모음
						} else if (this.isVowel(lookOverTwoStep)) {
							fin = DobeolSymbol.DobeolFConsonant.valueOf(tFCon);
						}

						tFCon = tFConLookahead = tFConLookaheadCombination = "";
					} else {
						// 키 시퀀스의 마지막이라면 대기 슬롯 받침자리에 받침을 채우고 끝낸다.
						if (this.isFConsonant(tFConLookaheadCombination)) {
							fin = DobeolSymbol.DobeolFConsonant
									.valueOf(tFConLookaheadCombination);
						}

						tFCon = tFConLookahead = tFConLookaheadCombination = "";
						break;
					}

				} else {
					// 단자음 받침이나 쌍자음 받침을 슬롯에 넣는다.
					if (this.isFConsonant(tFCon))
						fin = DobeolSymbol.DobeolFConsonant.valueOf(tFCon);

					// 키 시퀀스의 끝이라면 끝낸다.
					if (i == keySequence.length() - 1)
						break;

					// 다음 글자가 모음이면 받침으로 간주하지 않고 backtracking.
					// 대기 슬롯의 받침 자리를 비워둔다
					if (this.isVowel(tFConLookahead)) {
						fin = DobeolSymbol.DobeolFConsonant.nul;
						tFCon = tFConLookahead = tFConLookaheadCombination = "";
						i--;
						stateFlag = State.Finish;
					}
				}
			}

			// 한 글자가 완성되었으니 대기 슬롯에서 글자를 빼내어 결과 스트링에 붙여준다
			if (stateFlag == State.Finish) {
				result += this.getSyllableChar(init, vow, fin);
				init = DobeolSymbol.DobeolIConsonant.nul;
				vow = DobeolSymbol.DobeolVowel.nul;
				fin = DobeolSymbol.DobeolFConsonant.nul;
			}
		}

		// 마무리.
		if (stateFlag == State.Finish)
			result += this.getSyllableChar(init, vow, fin);

		return result;
	}

	private String getSingleLetter(String keySequence) {
		char[] ch = new char[1];
		ch[0] = (char) (DobeolSymbol.DobeolSingleLetter.valueOf(keySequence)
				.value() + 0x3130);

		return new String(ch);
	}

	private String getSyllableChar(DobeolSymbol.DobeolIConsonant init,
			DobeolSymbol.DobeolVowel vow, DobeolSymbol.DobeolFConsonant fin) {
		char[] ch = new char[1];
		ch[0] = (char) ((init.value() * 21 * 28 + vow.value() * 28 + fin
				.value()) + 0xAC00);

		return new String(ch);
	}

	private boolean hasTwoSymbolinOneKey(char ch) {
		return ((ch == 'q' || ch == 'Q') || (ch == 'w' || ch == 'W')
				|| (ch == 'e' || ch == 'E') || (ch == 'r' || ch == 'R') || (ch == 't' || ch == 'T'))
				|| ((ch == 'o' || ch == 'O') || (ch == 'p' || ch == 'P'));
	}
}
