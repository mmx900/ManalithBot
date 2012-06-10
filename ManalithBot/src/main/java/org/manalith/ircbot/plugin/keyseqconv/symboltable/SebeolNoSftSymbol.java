/*
 	org.manalith.ircbot.plugin.keyseqconv/SebeolSymbol.java
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
package org.manalith.ircbot.plugin.keyseqconv.symboltable;

public class SebeolNoSftSymbol {

	public static enum SebeolIConsonant {
		// ㅋ의 위치가 0 -> 9 로 바뀜.
		nul(99), k(0), kk(1), h(2), u(3), uu(4), y(5), i(6), _l(7), _l_l(8), n(
				9), nn(10), j(11), l(12), ll(13), o(14), _9(15), __l(16), p(17), m(
				18);

		private int value;

		SebeolIConsonant(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolISingleConsonant {
		// ㅋ의 위치가 0 -> 9로 바뀜
		nul(99), k(0), h(2), u(3), y(5), i(6), _l(7), n(9), j(11), l(12), o(14), _9(
				15), __l(16), p(17), m(18);

		private int value;

		SebeolISingleConsonant(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolIDoubleConsonant {
		// ㅋ를 제외한 자음은 세벌식 최종과 동일
		nul(99), kk(1), uu(4), _l_l(8), nn(10), ll(13);

		private int value;

		SebeolIDoubleConsonant(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolVowel {
		// G -> R -> 0 (ㅒ) : 최종->390->순아래. 이외에는 390과 동일.
		nul(99), f(0), r(1), _6(2), _0(3), t(4), c(5), e(6), _7(7), v(8), vf(9), vr(
				10), vd(11), _4(12), b(13), bt(14), bc(15), bd(16), _5(17), g(
				18), gd(19), d(20);

		private int value;

		SebeolVowel(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolFConsonant {
		// 순아래용 종성 재배치
		nul(0), x(1), xx(2), xq(3), s(4), s__0(5), s_11(6), ___m(7),
		w(8), wx(9), wz(10), w_3(11), wq(12), wW(13), wQ(14), w_1(15),
		z(16), _3(17), _3q(18), q(19), _2(20), a(21), __0(22), ___0(23),
		____0(24), _p(25), __p(26), _1(27);
		private int value;

		SebeolFConsonant(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolSingleLetter {
		// U+1100 ~ U+11FF HANGUL JAMO
		// Initial Consonant : 0 ~ 18 (세벌식 최종과 동일)
		// ㅋ의 위치가 0 -> 9 로 바뀜.
		k(0), kk(1), h(2), u(3), uu(4), y(5), i(6), _l(7), _l_l(8), 
		n(9), nn(10), j(11), l(12), ll(13), o(14), _9(15), __l(16), 
		p(17), m(18),
				
		// Vowel : 97 ~ 117
		f(97), r(98), _6(99), _0(100), t(101), c(102), e(103), _7(104),
		v(105), vf(106), vr(107), vd(108), _4(109), b(110), bt(111),
		bc(112), bd(113), _5(114), g(115), gd(116), d(117),
		
		// Final Consonant : 168 ~ 194
		x(168), xx(169), xq(170), s(171), s__0(172), s_11(173),
		___m(174), w(175), wx(176), wz(177), w_3(178), wq(179),
		wW(180), wQ(181), w_1(182), z(183), _3(184), _3q(185),
		q(186), _2(187), a(188), __0(189), ___0(190), ____0(191),
		_p(192), __p(193), _1(194);

		private int value;

		SebeolSingleLetter(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public static enum SebeolSpecialChar {
		// +32

		// excl, quotation, sharp, dollar, percent, ampersand, apostrophe, 
		// left paren, right paren, asterisk
		B(1), __L(2), _33(3), _44(4), _55(5), _77(6), H(7), 
		_99(8), _00(9), _88(10),

		// plus, comma, hyphen-minus, period, slash
		___00(11), _m(12), Z(13), __m(14), G(15),

		// number zero to nine. which means, these are real Arabic number.
		N(16), M(17), _M(18), __M(19), J(20), K(21), L(22), U(23), I(24), O(25),

		// colon, semicolon, gt, eq, lt, question, commercial at 
		_L(26), T(27), P(28), X(29), Y(30), ___M(31), _22(32),

		// open square bracket, back slash, close square bracket,
		// circumflex accent(hat), low line, grave accent
		S(59), C(60), D(61), _66(62), __00(63), _1_(64),
		
		// curly open, vertline, curly close, tilde
		_P(91), ____00(92), __P(93), _11_(94);
		


		private int value;

		SebeolSpecialChar(int initial) {
			setValue(initial);
		}

		public void setValue(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}
}
