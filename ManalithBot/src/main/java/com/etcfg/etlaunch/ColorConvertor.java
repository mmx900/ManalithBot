package com.etcfg.etlaunch;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class ColorConvertor {
	private static final int[] COLORS = { 0x000000, 0xff0000, 0x00ff00,
			0xffff00, 0x0000ff, 0x00ffff, 0xff00ff, 0xffffff, 0xff7f00,
			0x7f7f7f, 0xbfbfbf, 0xbfbfbf, 0x007f00, 0x7f7f00, 0x00007f,
			0x7f0000, 0x7f3f00, 0xff9919, 0x007f7f, 0x7f007f, 0x007fff,
			0x7f00ff, 0x3399cc, 0xccffcc, 0x006633, 0xff0033, 0xb21919,
			0x993300, 0xcc9933, 0x999933, 0xffffbf, 0xffff7f };

	public static String getColor(char character) {

		int i = ((character + 16) & 31);
		System.out.println(character + "=" + COLORS[i]);
		return Integer.toHexString(COLORS[i]);
	}

	public static int getColorInt(char character) {
		int i = ((character + 16) & 31);
		return COLORS[i];
	}

	public static List<JLabel> quakeColorNotationToJLabels(String coloredName) {
		boolean colorChar = false;
		List<JLabel> returnValue = new ArrayList<JLabel>();
		JLabel currentLabel = new JLabel();
		returnValue.add(currentLabel);
		for (int i = 0; i < coloredName.length(); i++) {
			char charAt = coloredName.charAt(i);
			if (colorChar) {
				colorChar = false;
				int color = getColorInt(charAt);

				currentLabel = new JLabel();
				currentLabel.setForeground(new Color(color));
				returnValue.add(currentLabel);
				continue;
			}
			if (charAt == '^') {
				colorChar = true;
				continue;
			}
			currentLabel.setText(currentLabel.getText() + charAt);

		}

		return returnValue;
	}

	public static void main(String[] args) {

		System.out.println(getColor('_'));
	}

	public static String convertToPlainString(String input) {
		StringBuilder returnValue = new StringBuilder();
		boolean skipNext = false;
		for (int i = 0; i < input.length(); i++) {
			char charAt = input.charAt(i);
			if (skipNext) {
				skipNext = false;
				continue;
			}
			if (charAt == '^' && skipNext == false) {
				skipNext = true;
				continue;
			}
			returnValue.append(charAt);
		}
		return returnValue.toString();
	}

	public static void drawQuakeString(String inputString, Graphics2D g2d,
			Point location) {
		int i = 0;
		for (i = 0; i < inputString.length(); i++) {
			char curChar = inputString.charAt(i);

		}

	}
}
