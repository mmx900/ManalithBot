//
// HSQLDBTableManager.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.CER;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.sql.SQLException;
// import java.util.logging.Logger;
import org.apache.log4j.Logger;


public class HSQLDBTableManager {
	
	private Connection conn;
	private Statement stat;
	
	private String path;
	private String filename;
	
	public HSQLDBTableManager ( String newFilename ) throws SQLException,ClassNotFoundException
	{
		this.setPath ( "" );
		this.setFilename ( newFilename );
		this.initJDBCDriverHSQLDB();
	}
	public HSQLDBTableManager ( String newPath, String newFilename ) throws SQLException, ClassNotFoundException
	{
		this.setPath ( newPath );
		this.setFilename ( newFilename );
		this.initJDBCDriverHSQLDB();
	}
	
	private void setPath ( String newPath )
	{
		this.path = newPath;
	}
	private String getPath ( )
	{
		return this.path;
	}
	private void setFilename ( String newFilename )
	{
		this.filename = newFilename;
	}
	private String getFilename ( )
	{
		return this.filename;
	}
	
	private void initJDBCDriverHSQLDB ( ) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
	
		conn = DriverManager.getConnection("jdbc:hsqldb:" + this.getPath() + this.getFilename());
		stat = conn.createStatement();
	}
	
	private void initCurrencyRateTable () throws SQLException
	{
		stat.executeUpdate("drop table if exists CurrencyRate");
		//stat.executeUpdate("vacuum");
		
		String fields = "";
		fields += "country_name VARCHAR(15) NOT NULL ,";
		fields += "currency_name VARCHAR(3) NOT NULL ,";
		fields += "currency_unit INT NOT NULL ,";
		fields += "central_rate REAL NOT NULL ,";
		fields += "cash_buy REAL NOT NULL ,";
		fields += "cash_cell REAL NOT NULL ,";
		fields += "remittance_send REAL NOT NULL ,";
		fields += "remittance_recv REAL NOT NULL ,";
		fields += "exchan_comm_rate REAL NOT NULL ,";
		fields += "dollar_exc_rate REAL NOT NULL";
		
		stat.executeUpdate("create table CurrencyRate(" + fields + ");");
	}
	
	public void insertDataToTable ( TokenArray tArray ) throws SQLException
	{
		this.initCurrencyRateTable();
		
		PreparedStatement stmt = conn.prepareStatement("insert into CurrencyRate values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		int length = tArray.getSize();
		
		// remove </?TBODY>
		tArray.removeElement( length - 1 );
		tArray.removeElement( 0 );
		
		length -= 2;
		
		int i = 0;
		int field_count;
		TokenUnit tu = null;
		while ( i < length )
		{
			tu = tArray.getElement(i++);
			if ( tu.getTokenSubtype() == TokenSubtype.TROpen )
			{
				field_count = 1;
				while ( tu.getTokenSubtype() != TokenSubtype.TRClose )
				{
					tu = tArray.getElement(i++);
					if ( tu.getTokenSubtype() == TokenSubtype.TDOpen )
					{
						while ( tu.getTokenSubtype() != TokenSubtype.TDClose )
						{
							tu = tArray.getElement(i++);
							if ( tu.getTokenSubtype() == TokenSubtype.TextString )
							{
								if ( field_count == 1 )
								{
									String ts = tu.getTokenString();
									int strlen = ts.length();
									String su = "";
									int j = 0;
									while ( true )
									{
										
										if ( ts.charAt(j) >= 'A' && ts.charAt(j) <= 'Z' )
										{
											su = su.substring(0,su.length() - 1);
											stmt.setString(field_count++, su);
											su = "";
											break;
										}
										su += Character.toString(ts.charAt(j++));
									}
									
									while ( true )
									{
										if ( j == strlen )
										{
											stmt.setString(field_count++, su);
											break;
										}
										
										if ( ts.charAt(j) == ' '  )
										{
											stmt.setString(field_count++, su);
											su = "";
											break;
										}
										su += Character.toString(ts.charAt(j++));
									}
									
									if ( j == strlen )
									{
										stmt.setInt(field_count++, 1);
										continue;
									}
									
									while ( true )
									{
										if ( j == strlen )
										{
											stmt.setInt(field_count++, Integer.parseInt(su));
											su = "";
											break;
										}
										if ( ts.charAt(j) >= '0' && ts.charAt(j) <= '9' )
											su += Character.toString(ts.charAt(j));
										
										j++;
									}
								}
								else
								{
									String ts = tu.getTokenString();
									int strlen = ts.length();
									String su = "";
									int j = 0;
									
									while ( j < strlen )
									{
										if ( ( ts.charAt(j) >= '0' && ts.charAt(j) <= '9' ) || ts.charAt(j) == '.' )
											su += Character.toString(ts.charAt(j));
										j++;
									}
									
									if ( su.length() == 0 )
										stmt.setDouble( field_count++, 0.0 );
									else
										stmt.setDouble( field_count++, Double.parseDouble(su) );

								}
							}
						}
					}
				}
			}
			stmt.addBatch();
			conn.setAutoCommit(false);
			stmt.executeBatch();
			conn.setAutoCommit(true);
			stmt.clearParameters();
		}
	}

	public String [] selectDataFromTable ( String field, String CurrencyName ) throws SQLException
	{		
		String [] result = null;
		
		String sqlcmd = "select " + field + " from CurrencyRate where currency_name=\'" + CurrencyName + "\';";
		
		ResultSet rs = stat.executeQuery(sqlcmd);
		
		while ( rs.next() )
		{
			if ( field.equals("*") )
			{
				int j = 0;
				result = new String[9];
				result[j++] = rs.getString(1);
				result[j++] = Integer.toString(rs.getInt(3));
				for ( int i = 4 ; i <= 10 ; i++ )
					result[j++] = Double.toString(rs.getDouble(i));
				
			}
			else
			{
				String [] separatedfield = field.split("\\,");
				
				result = new String[separatedfield.length];
				for ( int i = 0 ; i < separatedfield.length ; i++ )
				{
					if ( separatedfield[i].contains("name") )
					{
						result[i] = rs.getString(i+1);
					}
					else if ( separatedfield[i].equals("currency_unit") )
					{
						result[i] = Integer.toString(rs.getInt(i+1));
					}
					else
					{
						result[i] = Double.toString(rs.getDouble(i+1));
					}
				}
			}
		}
		
		return result;
	}
	
	public void close () throws SQLException
	{
		conn.close();
	}
}
