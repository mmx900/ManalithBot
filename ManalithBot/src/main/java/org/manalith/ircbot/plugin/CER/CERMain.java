//
// CERMain.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
//
// This class is just a test.
//

package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.plugin.CER.Exceptions.ArgumentNotFoundException;
import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;
import java.util.GregorianCalendar;

public class CERMain {
	public static void main (String [] args) throws ArgumentNotFoundException
	{
		
		GregorianCalendar now = new GregorianCalendar();
		int thishour = now.get(GregorianCalendar.HOUR_OF_DAY);
		int thisday = now.get(GregorianCalendar.DAY_OF_WEEK);
		
		// if now is in trading time.
		if ( ( thisday != GregorianCalendar.SUNDAY && thisday != GregorianCalendar.SATURDAY ) && ( thishour > 9 && thishour < 18 ) )
		{
			try
			{
				/// Update database for listing money exchange information
				/// if time is expired.
	
				String propFilename = "plugin/CER/data/LatestUpdatedDatetime.prop";
				RemoteLocalDatetimeChecker check = new RemoteLocalDatetimeChecker(
						"http://info.finance.naver.com/marketindex/exchangeMain.nhn",
						propFilename );
				
				DateTimeRound local = check.checkLatestUpdatedLocalDateandTime();
				DateTimeRound remote = check.checkLatestNoticeDateandTime();
				
				if ( remote.compareTo(local) > 0 )
				{
					// set properties using the value.
					PropertyManager pm = new PropertyManager ( propFilename );
					pm.loadProperties();
					
					String date = Integer.toString(remote.getCalendar().get(GregorianCalendar.YEAR));
					date += ".";
					date += Integer.toString(remote.getCalendar().get(GregorianCalendar.MONTH));
					date += ".";
					date += Integer.toString(remote.getCalendar().get(GregorianCalendar.DAY_OF_MONTH));
					date += " ";			
					date += Integer.toString(remote.getCalendar().get(GregorianCalendar.HOUR_OF_DAY));
					date += ":";
					date += Integer.toString(remote.getCalendar().get(GregorianCalendar.MINUTE));
					pm.setValue("date", date);
					
					String round = Integer.toString(remote.getRoundVal());
					while ( round.length() < 3 )
						round = "0" + round;
					pm.setValue("round", round);
					
					// commit into prop file.
					pm.storeProperties();
					
					// update Current Exchange Rate Table.
					CERTableUpdater updater = new CERTableUpdater ( "http://info.finance.naver.com/marketindex/exchangeList.nhn" );
					updater.update();
				}
			}
			catch (Exception e)
			{
				System.out.println( e.getMessage() );
			}
		}
		
		/// compile command to print money exchange info.
		
		//String command = "--sendremit USD 1500.23";
		String command = "--sendremit USD 1500";
		/*
		int len = args.length;
		for ( int i = 0 ; i < len ; i++ )
		{
			command += args[i];
			if ( i != len - 1 ) command += " ";
		}
		//*/
		
		CERInfoProvider cip = new CERInfoProvider ( command );
		String print = "";
		try
		{
			print = cip.commandInterpreter();
		}
		catch ( EmptyTokenStreamException e )
		{
			System.out.println ("No argument specified.");
			cip = new CERInfoProvider ( "--help" );
			try
			{
				print = cip.commandInterpreter();
			}
			catch ( Exception ex )
			{
				;
			}
		}
		catch ( Exception e )
		{
			System.out.println ( e.getMessage() );
		}
		
		if ( !print.equals("Help!") )
		{
			System.out.println (print);
		}
	}
}
