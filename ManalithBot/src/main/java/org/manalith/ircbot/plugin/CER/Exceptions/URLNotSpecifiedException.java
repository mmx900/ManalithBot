//
// URLNotSpecifieddException.java extends Exception
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.CER.Exceptions;

public class URLNotSpecifiedException extends Exception{

	private static final long serialVersionUID = 1L;

	public URLNotSpecifiedException () 
	{
		super ("URL is not specified.");
	}
}
