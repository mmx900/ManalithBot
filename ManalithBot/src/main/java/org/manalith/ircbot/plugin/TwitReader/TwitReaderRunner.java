package org.manalith.ircbot.plugin.TwitReader;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONTokener;
import org.json.JSONObject;

import org.manalith.ircbot.plugin.TwitReader.Exceptions.URLDoesntSpecifiedException;

public class TwitReaderRunner 
{
	private String url;
			
	public TwitReaderRunner ( )
	{
		this.setURL("");
	}
	public TwitReaderRunner ( String newURL )
	{
		this.setURL(newURL);
	}
	
	private void setURL ( String newURL )
	{
		this.url = newURL;
	}
	private String getURL ( )
	{
		return this.url;
	}
	
	public String[] run () 
	{

		String [] result = null;
		
		try
		{
			if ( !this.validateTwitterURL() )
			{
				result = new String[1];
				result[0] = "잘못된 URL입니다";
				return result;
			}
		}
		catch ( URLDoesntSpecifiedException e )
		{
			result = new String[1];
			result[0] = "URL이 비었습니다";
			return result;
		}
		
		// url has no problem
		
		try
		{
			JSONObject obj = new JSONObject ( new JSONTokener ( (new StreamDownloader(this.getJSONURL())).downloadDataStream() ) );
			
			String written_by = obj.getJSONObject("user").getString("name");
			String written_datetime = obj.getString("created_at");
			String Creating_Source = obj.getString("source");
			String body = obj.getString("text");
			
			result = new String[2];
			result[0] = "작성자 : " + written_by + ", 작성시각 : " + getDateTimeinKoreanFormat(written_datetime) + ", 작성 클라이언트 : " + Creating_Source.replaceAll( "\\<(\\/)?[a-zA-Z]+((\\s)[a-zA-Z]+\\=\\\"(\\s|\\S)+\\\")*\\>", "");
			result[1] = "본문 : " + body;
		}
		catch ( Exception e )
		{
			result = new String[1];
			result[0] = e.getMessage();
			return result;
		}
		
		return result;		
	}
	
	private boolean validateTwitterURL ( ) throws URLDoesntSpecifiedException
	{		
		if ( this.getURL().equals("") )
			throw new URLDoesntSpecifiedException();
		
		Pattern twitter_url_pattern = Pattern.compile("http(s)?\\:\\/\\/twitter\\.com\\/\\#\\!\\/(\\S)+\\/status\\/[0-9]+");
		Matcher twitter_url_pattern_matcher = twitter_url_pattern.matcher(this.getURL());
		
		return twitter_url_pattern_matcher.matches();
	}
	
	private String getJSONURL ( )
	{
		String [] split_url = this.getURL().split("\\/");
		String twit_id = split_url[split_url.length - 1];
		String json_requrl = "https://api.twitter.com/1/statuses/show.json?id=" + twit_id + "&include_entities=false";
		
		return json_requrl;
	}
	
	private String getDateTimeinKoreanFormat ( String datetime )
	{
		String result = "";
		String [] dayOfWeekKorean = { "일", "월", "화", "수", "목", "금", "토" };
		String [] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		
		String [] split_datetime_by_space = datetime.split("\\s");
		
		int yyyy = 0;
		int MM = 0;
		int dd = 0;
		int hh = 0;
		int mm = 0;
		int ss = 0;
		String [] hhmmss;
		
		GregorianCalendar gdt = null;
		
		try
		{
			yyyy = Integer.parseInt(split_datetime_by_space[5]);
			MM = this.indexOf(month, split_datetime_by_space[1] );
			dd = Integer.parseInt(split_datetime_by_space[2]);
		 
			hhmmss = split_datetime_by_space[3].split("\\:");
		
			hh = Integer.parseInt(hhmmss[0]);
			mm = Integer.parseInt(hhmmss[1]);
			ss = Integer.parseInt(hhmmss[2]);
		}
		catch ( Exception e )
		{
			;
		}
		
		if ( split_datetime_by_space[4].equals("+0000") )
		{
			try
			{
				
				gdt = new GregorianCalendar( yyyy, MM, dd );
				gdt.set(Calendar.ERA, GregorianCalendar.AD);
				gdt.setTimeZone(TimeZone.getTimeZone("UTC"));
				gdt.set(Calendar.HOUR, hh);
				gdt.set(Calendar.MINUTE, mm);
				gdt.set(Calendar.SECOND, ss);
	
				gdt.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
				gdt.add(Calendar.HOUR, 9);
					
			}
			catch ( Exception e )
			{
				;	
			}
		}
		else
		{
			try
			{
				gdt = new GregorianCalendar( yyyy, MM, dd );
				gdt.set(Calendar.ERA, GregorianCalendar.AD);
				gdt.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
				gdt.set(Calendar.HOUR, hh);
				gdt.set(Calendar.MINUTE, mm);
				gdt.set(Calendar.SECOND, ss);

				
				
				result += gdt.get(Calendar.YEAR) + "년 " + (gdt.get(Calendar.MONTH) + 1) + "월 " + gdt.get(Calendar.DAY_OF_MONTH) + "일 ";
				result += dayOfWeekKorean[gdt.get(Calendar.DAY_OF_WEEK) - 1] + "요일 ";
				result += String.format("%02d", gdt.get(Calendar.HOUR_OF_DAY)) + ":" 
				+ String.format("%02d", gdt.get(Calendar.MINUTE)) + ":" 
				+ String.format("%02d", gdt.get(Calendar.SECOND)); 
	
			}
			catch ( Exception e )
			{
				;	
			}
		}
		
		result += gdt.get(Calendar.YEAR) + "년 " + (gdt.get(Calendar.MONTH) + 1) + "월 " + gdt.get(Calendar.DAY_OF_MONTH) + "일 ";
		result += dayOfWeekKorean[gdt.get(Calendar.DAY_OF_WEEK) - 1] + "요일 ";
		result += String.format("%02d", gdt.get(Calendar.HOUR_OF_DAY)) + ":" 
		+ String.format("%02d", gdt.get(Calendar.MINUTE)) + ":" 
		+ String.format("%02d", gdt.get(Calendar.SECOND)); 


		return result;
	}
	
	private int indexOf ( String [] str, String value )
	{
		int result = -1;
		int length = str.length;
		
		for ( int i = 0 ; i < length ; i++ )
		{
			if ( str[i].equals(value) )
			{
				result  = i;
				break;
			}
		}
		
		return result;
	}
}
