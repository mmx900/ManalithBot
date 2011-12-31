//
// NotImplementedException.java extends Exception
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.calc.exceptions;

public class NotImplementedException extends Exception{

	private static final long serialVersionUID = 1L;

	public NotImplementedException()
	{
		super("This feature is not implemented.");
	}
}
