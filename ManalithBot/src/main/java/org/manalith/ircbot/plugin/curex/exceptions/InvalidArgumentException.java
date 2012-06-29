//
// InvalidArgumentException.java extends Exception
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.curex.exceptions;

public class InvalidArgumentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidArgumentException ( )
	{
		super ( " 잘못된 인자 : 알 수 없음 ");
	}
	public InvalidArgumentException ( String newMessage )
	{
		super ( " 잘못된 인자 : " + newMessage );
	}
	
}
