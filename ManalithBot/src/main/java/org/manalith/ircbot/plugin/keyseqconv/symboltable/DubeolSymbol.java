/*
 	org.manalith.ircbot.plugin.keyseqconv/DubeolSymbol.java
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

public class DubeolSymbol {

	public static enum DubeolIConsonant {
		nul(99), r(0), R(1), s(2), e(3), E(4), f(5), a(6), q(7), Q(8), t(9), T(
				10), d(11), w(12), W(13), c(14), z(15), x(16), v(17), g(18);

		private final int value;

		DubeolIConsonant(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}

	public static enum DubeolISingleConsonant {
		nul(99), r(0), R(1), s(2), e(3), E(4), f(5), a(6), q(7), Q(8), t(9), T(
				10), d(11), w(12), W(13), c(14), z(15), x(16), v(17), g(18);

		private final int value;

		DubeolISingleConsonant(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}

	public static enum DubeolIDoubleConsonant {
		nul(99), R(1), E(4), Q(8), T(10), W(13);

		private final int value;

		DubeolIDoubleConsonant(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}

	public static enum DubeolVowel {
		nul(99), k(0), o(1), i(2), O(3), j(4), p(5), u(6), P(7), h(8), hk(9), ho(
				10), hl(11), y(12), n(13), nj(14), np(15), nl(16), b(17), m(18), ml(
				19), l(20);

		private final int value;

		DubeolVowel(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}

	public static enum DubeolFConsonant {
		nul(0), r(1), R(2), rt(3), s(4), sw(5), sg(6), e(7), f(8), fr(9), fa(10), fq(
				11), ft(12), fx(13), fv(14), fg(15), a(16), q(17), qt(18), t(19), T(
				20), d(21), w(22), c(23), z(24), x(25), v(26), g(27);

		private final int value;

		DubeolFConsonant() {
			value = 0; // no final consonant
		}

		DubeolFConsonant(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}

	public static enum DubeolSingleLetter {
		// 0x3130 ~ 0x318F HANGUL COMPATIBILITY JAMO
		// Single Consonant : 30
		r(1), R(2), rt(3), s(4), sw(5), sg(6), e(7), E(8), f(9), fr(10), fa(11), fq(
				12), ft(13), fx(14), fv(15), fg(16), a(17), q(18), Q(19), qt(20), t(
				21), T(22), d(23), w(24), W(25), c(26), z(27), x(28), v(29), g(
				30),

		// Single Vowel : 21
		k(31), o(32), i(33), O(34), j(35), p(36), u(37), P(38), h(39), hk(40), ho(
				41), hl(42), y(43), n(44), nj(45), np(46), nl(47), b(48), m(49), ml(
				50), l(51);

		private final int value;

		DubeolSingleLetter(int initial) {
			value = initial;
		}

		public int value() {
			return value;
		}
	}
}
