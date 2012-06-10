/*
 	org.manalith.ircbot.plugin.keyseqconv/DubeolAutomataEngine.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.keyseqconv;

import java.text.ParseException;
import java.util.IllegalFormatException;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.keyseqconv.symboltable.DubeolSymbol;

public class DubeolAutomataEngine implements IAutomataEngine {
	
	private enum LetterState {
		Null, IConsonant, Vowel, FConsonant, Finish
	}

	private boolean enableParseExceptionSyntax;

	public DubeolAutomataEngine() {

	}

	public void setEnableParsingExceptionSyntax(boolean enable) {
		this.enableParseExceptionSyntax = enable;
	}

	public boolean isEnableParsingExceptionSyntax() {
		return this.enableParseExceptionSyntax;
	}

	public boolean isISingleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			DubeolSymbol.DubeolISingleConsonant check1 = DubeolSymbol.DubeolISingleConsonant
					.valueOf(tICon); // dummy alloc.
			return true;
		} catch (Exception e)
		// if checked character is abnormal
		{
			return false;
		}
	}

	public boolean isIDoubleConsonant(String tICon) {
		try {
			@SuppressWarnings("unused")
			DubeolSymbol.DubeolIDoubleConsonant check1 = DubeolSymbol.DubeolIDoubleConsonant
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
			DubeolSymbol.DubeolVowel check1 = DubeolSymbol.DubeolVowel
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
			DubeolSymbol.DubeolFConsonant check1 = DubeolSymbol.DubeolFConsonant
					.valueOf(tFCon); // dummy alloc.
			return true;
		} catch (Exception e)
		// if checked character is abnormal
		{
			return false;
		}
	}

	public String parseKoreanStringToEngSpell(String korean)
			throws IllegalFormatException {
		String result = "";

		for (int i = 0; i < korean.length(); i++) {
			int ch = korean.charAt(i);
			if (((char) ch) >= '가' && ((char) ch) <= '힣') {
				ch -= 0xAC00;
				int initialConsonant = ch / (21 * 28);
				ch %= (21 * 28);
				int Vowel = ch / 28;
				ch %= 28;
				int finalConsonant = ch;

				result += DubeolSymbol.DubeolIConsonant.values()[initialConsonant + 1]
						.toString()
						+ DubeolSymbol.DubeolVowel.values()[Vowel + 1]
								.toString();

				if (finalConsonant != 0)
					result += DubeolSymbol.DubeolFConsonant.values()[finalConsonant];
			} else if (ch >= 'ㄱ' && ch <= 'ㅣ') {
				ch -= 0x3130;
				result += DubeolSymbol.DubeolSingleLetter.values()[ch - 1];
			} else {
				result += (char) ch;
			}

		}

		return result;
	}

	public String parseKeySequenceToKorean(String keySequence)
			throws ParseException, IllegalArgumentException {
		String result = "";

		LetterState stateFlag = LetterState.Null;

		String tICon = "";
		String tIConLookahead = "";
		String tIConLookaheadCombination = "";
		String tVow = "";
		String tVowLookahead = "";
		String tVowLookaheadCombination = "";
		String tFCon = "";
		String tFConLookahead = "";
		String tFConLookaheadCombination = "";

		LetterObject syl = new LetterObject(KeyboardLayout.Dubeol);

		if (this.isEnableParsingExceptionSyntax()
				&& StringUtils.countMatches(keySequence, "\\") % 2 == 1)
			
			throw new ParseException("Back slashes do not match",keySequence.lastIndexOf("\\", 0));

		for (int i = 0; i < keySequence.length(); i++) {
			if (stateFlag.equals(LetterState.Null)
					|| stateFlag.equals(LetterState.Finish)) {
				stateFlag = LetterState.IConsonant;
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
			if (stateFlag.equals(LetterState.IConsonant)) {
				// 일단 초기화
				tIConLookahead = tIConLookaheadCombination = "";

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
				// (ㄸ와 ㅃ,ㅉ 같은 경우 제외)
				if (this.isFConsonant(tIConLookaheadCombination)) {

					// 2 step - lookahead가 가능하면 try
					if (i + 2 <= keySequence.length() - 1) {
						String lookOverTwoStep = this
								.hasTwoSymbolinOneKey(keySequence.charAt(i + 2)) ? Character
								.toString(keySequence.charAt(i + 2))
								: Character.toString(keySequence.charAt(i + 2))
										.toLowerCase();

						// 자음 두번 입력 후, 자음 혹은 특수문자, 공백, 숫자
						if (this.isISingleConsonant(lookOverTwoStep)
								|| this.isIDoubleConsonant(lookOverTwoStep)
								|| !CharUtils.isAsciiAlpha(lookOverTwoStep
										.charAt(0))) {

							result += this
									.getSingleChar(this
											.getSingleCharVal(tIConLookaheadCombination));

							i++;
						}
						// 자음 두번 입력 후 모음
						else if (this.isVowel(lookOverTwoStep)) {
							// 받침자음의 앞 자는 버리고 뒤 자를 살린다
							result += this.getSingleChar(this
									.getSingleCharVal(tICon));
							continue;
						}

					}
					// 문장의 마지막에 (받침용) 겹자음 입력했을 경우 출력 후 종료
					else {
						result += this.getSingleChar(this
								.getSingleCharVal(tIConLookaheadCombination));
						stateFlag = LetterState.Null;
						break;
					}
				}
				// 쌍자음, 단자음인 경우 ( (받침용) 겹자음 제외)
				else {
					// 자음이면 대기 슬롯에 넣음
					if (this.isISingleConsonant(tICon)
							|| this.isIDoubleConsonant(tICon)) {
						syl.setIConsonant(tICon);
						// init = DubeolSymbol.DubeolIConsonant.valueOf(tICon);
					}
					// 모음이면 독립모음을 찍기 위해 모음 단계부터 다시 처리
					else if (this.isVowel(tICon)) {
						stateFlag = LetterState.Vowel;
						i--;
						continue;
					}

					// 문장의 마지막에 단일 자음 입력했을 경우 출력 후 종료
					if (i == keySequence.length() - 1) {

						result += this.getSingleChar(this
								.getSingleCharVal(tICon));
						syl.initLetter();
						stateFlag = LetterState.Null;
						break;
					}

					// 현재 자음이고 다음 글자가 모음이면 모음을 확인하기 위해 계속 진행
					if (this.isVowel(tIConLookahead)) {
						stateFlag = LetterState.Vowel;
						continue;
					}
					// 초성 자음이 뒤따라 오는 경우
					else {

						result += this.getSingleChar(this
								.getSingleCharVal(tICon));
						syl.initLetter();
					}
				}
			}
			// 중성 (모음)
			else if (stateFlag.equals(LetterState.Vowel)) {
				// 일단 초기화
				tVowLookahead = tVowLookaheadCombination = "";

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
					syl.setVowel(tVowLookaheadCombination);
					// vow =
					// DubeolSymbol.DubeolVowel.valueOf(tVowLookaheadCombination);

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
								|| ((this.isISingleConsonant(lookOverTwoStep) || this
										.isIDoubleConsonant(lookOverTwoStep)) && !this
										.isFConsonant(lookOverTwoStep))) {
							stateFlag = LetterState.Finish;
						}
						// 겹모음에 받침이 따라오는 경우 받침을 찾을 차례, ex: 왠
						else if (this.isFConsonant(lookOverTwoStep)) {
							if (!syl.isCompleteSyllable()) {

								result += this
										.getSingleChar(this
												.getSingleCharVal(tVowLookaheadCombination));
								stateFlag = LetterState.Null;
							} else {
								stateFlag = LetterState.FConsonant;
								continue;
							}
						}
						// 빈 칸 혹은 특수문자
						else if (!CharUtils.isAsciiAlpha(lookOverTwoStep
								.charAt(0))) {
							if (!syl.isCompleteSyllable()) {
								result += this
										.getSingleChar(this
												.getSingleCharVal(tVowLookaheadCombination));
								syl.initLetter();
								stateFlag = LetterState.Null;
							} else
								stateFlag = LetterState.Finish;
						}
					}
					// 다음 문자 밖에 볼 수 없을 경우 (시퀀스의 마지막)
					else {
						// 자음이 대기 슬롯에 없으면 독립 모음 출력
						if (!syl.isCompleteSyllable()) {
							result += this
									.getSingleChar(this
											.getSingleCharVal(tVowLookaheadCombination));
							syl.initLetter();
							i++;
							stateFlag = LetterState.Null;
						} else {
							stateFlag = LetterState.Finish;
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
						if (!syl.isCompleteSyllable())
							syl.setVowel(tVow);

						// 키 시퀀스의 마지막이라면
						if (i == keySequence.length() - 1) {
							// 자음이 존재하지 않는다면 독립 모음 입력
							if (!syl.isCompleteSyllable()) {

								result += this.getSingleChar(this
										.getSingleCharVal(tVow));
								syl.initLetter();
								stateFlag = LetterState.Null;
							}
							// 자음이 있으면 받침이 없는 완전한 글자 완성
							else {
								stateFlag = LetterState.Finish;
							}
							break;
						}

						// 2벌식 초성 중성 종성은 영문글자의 위치 영역에 있으므로
						// 영문자가 아닌 문자를 별개문자 혹은 delimiter로 취급
						// 뒤에 공백, 숫자, 특수문자 따라오는 경우.
						if (!CharUtils.isAsciiAlpha(tVowLookahead.charAt(0))) {

							// 초성이 없다면 독립 모음 입력.
							if (!syl.isCompleteSyllable()) {

								result += this.getSingleChar(this
										.getSingleCharVal(tVow));
								syl.initLetter();
								stateFlag = LetterState.IConsonant;
								continue;
							} else
								stateFlag = LetterState.Finish;
						}

						// *// 접근이 안되는 코드인듯. 주석처리
						// 자음이 입력되지 않았을 경우
						if (!syl.isCompleteSyllable()) {
							// 독립 모음
							result += this.getSingleChar(this
									.getSingleCharVal(tVow));
							syl.initLetter();

							// 다음이 자음이면 자음으로
							if (this.isISingleConsonant(tVowLookahead)
									|| this.isIDoubleConsonant(tVowLookahead))
								stateFlag = LetterState.IConsonant;
							// 모음이면 모음으로 검색모드 전환
							else if (this.isVowel(tVowLookahead))
								stateFlag = LetterState.Vowel;

							continue;
						} else {
							// 자음 + 모음 + 받침 : good!
							if (this.isFConsonant(tVowLookahead))
								stateFlag = LetterState.FConsonant;
							// 자음이 입력되었을 때 모음 다음 모음 오는 경우, 예: 거ㅣ
							// ㄸ 과 같은 자음은 받침으로 쓰이지 않는다.
							else
								stateFlag = LetterState.Finish;
						}
					}
				}
			}
			// 종성
			else if (stateFlag.equals(LetterState.FConsonant)) {
				// 일단 초기화
				tFConLookahead = tFConLookaheadCombination = "";

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

				stateFlag = LetterState.Finish; // 받침이 나오면 한 글자 완성이 끝남.

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
						if (this.isISingleConsonant(lookOverTwoStep)
								|| this.isIDoubleConsonant(lookOverTwoStep)
								|| !CharUtils.isAsciiAlpha(lookOverTwoStep
										.charAt(0))) {
							// 받침을 대기 슬롯에 넣는다. 겹자음이므로 키 시퀀스를 하나 건너뛴다
							syl.setFConsonant(tFConLookaheadCombination);
							i++;
							// 단자음 받침일 수도 있다. 받침, 자음 + 모음
						} else if (this.isVowel(lookOverTwoStep))
							syl.setFConsonant(tFCon);

					} else {
						// 키 시퀀스의 마지막이라면 대기 슬롯 받침자리에 받침을 채우고 끝낸다.
						if (this.isFConsonant(tFConLookaheadCombination)) {
							syl.setFConsonant(tFConLookaheadCombination);
						}

						break;
					}

				} else {
					// 단자음 받침이나 쌍자음 받침을 슬롯에 넣는다.
					if (this.isFConsonant(tFCon))
						syl.setFConsonant(tFCon);

					// 키 시퀀스의 끝이라면 끝낸다.
					if (i == keySequence.length() - 1)
						break;

					// 다음 글자가 모음이면 받침으로 간주하지 않고 backtracking.
					// 대기 슬롯의 받침 자리를 비워둔다
					if (this.isVowel(tFConLookahead)) {
						syl.setFConsonant("nul");
						stateFlag = LetterState.Finish;
						i--;
					}
				}
			}

			// 한 글자가 완성되었으니 대기 슬롯에서 글자를 빼내어 결과 스트링에 붙여준다
			if (stateFlag == LetterState.Finish) {
				result += syl.getLetter();
				syl.initLetter();
			}
		}

		// 마무리.
		if (stateFlag == LetterState.Finish)
			result += syl.getLetter();

		return result;
	}

	private boolean hasTwoSymbolinOneKey(char ch) {
		return (ch == 'q' || ch == 'Q') || (ch == 'w' || ch == 'W')
				|| (ch == 'e' || ch == 'E') || (ch == 'r' || ch == 'R')
				|| (ch == 't' || ch == 'T')
				|| ((ch == 'o' || ch == 'O') || (ch == 'p' || ch == 'P'));
	}

	public int getSingleCharVal(String keySequence) {
		return DubeolSymbol.DubeolSingleLetter.valueOf(keySequence).value();
	}

	public String getSingleChar(int charVal) {
		char[] ch = new char[1];
		// single char value starts from 0x3130
		ch[0] = (char) (charVal + 0x3130);

		return new String(ch);
	}
}
