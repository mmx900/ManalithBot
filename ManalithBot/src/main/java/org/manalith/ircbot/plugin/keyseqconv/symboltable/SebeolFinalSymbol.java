package org.manalith.ircbot.plugin.keyseqconv.symboltable;

public class SebeolFinalSymbol {

	public static enum SebeolIConsonant {
		nul(99), k(0), kk(1), h(2), u(3), uu(4), y(5), i(6), _l(7), _l_l(8),
		n(9), nn(10), j(11), l(12), ll(13), o(14), _0(15), __l(16), p(17),
		m(18);

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
		nul(99), k(0), h(2), u(3), y(5), i(6), _l(7), n(9), j(11), l(12), o(14),
		_0(15), __l(16), p(17), m(18);

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
		nul(99), f(0), r(1), _6(2), G(3), t(4), c(5), e(6), _7(7), v(8), vf(9),
		vr(10), vd(11), _4(12), b(13), bt(14), bc(15), bd(16), _5(17),
		g(18), gd(19), d(20);

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
		nul(0), x(1), _11(2), V(3), s(4), E(5), S(6), A(7), w(8), _22(9), F(10),
		D(11), T(12), _55(13), _44(14), R(15), z(16), _3(17), X(18), q(19),
		_2(20), a(21), _33(22), Z(23), C(24), W(25), Q(26), _1(27);

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
		k(0), kk(1), h(2), u(3), uu(4), y(5), i(6), _l(7), _l_l(8), n(9), nn(10),
		j(11), l(12), ll(13), o(14), _0(15), __l(16), p(17), m(18), f(97),
		r(98), _6(99), G(100), t(101), c(102), e(103), _7(104), v(105), 
		vf(106), vr(107), vd(108), _4(109), b(110), bt(111), bc(112), 
		bd(113), _5(114), g(115), gd(116), d(117), x(168), _11(169),
		V(170), s(171), E(172), S(173), A(174), w(175), _22(176), F(177), 
		D(178), T(179), _55(180), _44(181), R(182), z(183), _3(184), X(185),
		q(186), _2(187), a(188), _33(189), Z(190), C(191), W(192), Q(193), _1(194);

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

		// excl, quotation, percent, apostrophe, left paren, right paren,
		// asterisk
		___M(1), M(2), _P(5), _99(7), _p(8), __0(9), _1_(10),

		// plus, comma, hyphen-minus, period, slash
		___00(11), _m(12), N(13), __m(14), __P(15),

		// number zero to nine. which means, these are real Arabic number.
		H(16), J(17), K(18), L(19), _L(20), Y(21), U(22), I(23), O(24), P(25),

		// colon, semicolon,gt, eq, lt, question (commercial at mark is not
		// being in sebeol)
		____0(26), __00(27), __p(28), _66(29), ___0(30), B(31),

		____00(60), _00(94), // back slash, tilde

		__L(151), // middle dot

		_77(8188), _88(8189), // left, right double quotation mark
		_11_(8219); // refer

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
