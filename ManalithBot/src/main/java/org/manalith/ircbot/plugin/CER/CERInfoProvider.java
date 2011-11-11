//
// CERInfoProvider.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.CER.Exceptions.InvalidArgumentException;
import org.manalith.ircbot.plugin.CER.Exceptions.FileNotSpecifiedException;
import org.manalith.ircbot.plugin.CER.Exceptions.URLNotSpecifiedException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Properties;

public class CERInfoProvider {
	
	private String command;
	private String path;
	
	public CERInfoProvider ( )
	{
		this.setCommand ( "" );
	}
	public CERInfoProvider ( String newCommand )
	{
		this.setCommand ( newCommand );
		this.setPath ( "" );
	}
	public CERInfoProvider ( String newPath, String newCommand )
	{
		this.setCommand( newCommand );
		this.setPath ( newPath );
	}
	
	public void setCommand ( String newCommand )
	{
		this.command = newCommand;
	}
	public String getCommand ( )
	{
		return this.command;
	}
	public void setPath ( String newPath )
	{
		this.path = newPath;
	}
	public String getPath ( )
	{
		return this.path;
	}
	
	public String commandInterpreter ( ) throws EmptyTokenStreamException, InvalidArgumentException, IOException,
											SQLException, ClassNotFoundException, FileNotSpecifiedException, URLNotSpecifiedException
	{
		String result = "";
		
		CommandTokenAnalyzer cta = new CommandTokenAnalyzer ( this.getCommand() );
		TokenArray ta = cta.analysisTokenStream();
		TokenUnit tu = null;
		TokenType [] TokenTypeSequence = new TokenType[3];
		TokenSubtype [] TokenSubTSequence = new TokenSubtype[3];
		
		for ( int i = 0 ; i < 3 ; i++ )
		{
			TokenTypeSequence[i] = TokenType.Unknown;
			TokenSubTSequence[i] = TokenSubtype.Unknown;
		}
		
		int len = ta.getSize();
		if ( len > 3 )
			throw new InvalidArgumentException ("Too many argument specified.");

		int i = 0;
		
		// check integrity sequence of arguments.
		while ( i < len )
		{
			tu = ta.getElement(i);
			if ( i == 0 )
			{
				if( tu.getTokenType() != TokenType.Command ) 
					throw new InvalidArgumentException("Invalid command.");
				else if ( ( tu.getTokenSubtype() == TokenSubtype.CommandHelp || tu.getTokenSubtype() == TokenSubtype.CommandLatestRound )
						|| tu.getTokenSubtype() == TokenSubtype.CommandForceUpdate )
				{
					if ( len > 1 ) throw new InvalidArgumentException("Unnecessary argument specified.");
				}
				else if ( tu.getTokenSubtype() == TokenSubtype.CommandShow )
				{
					if ( len < 2 ) throw new InvalidArgumentException("Missing necessary argument.");
					else if ( len > 3 ) throw new InvalidArgumentException("Unnecessary argument specified.");
				}
				else if ( 
						( tu.getTokenSubtype() == TokenSubtype.CommandConvertFrom || tu.getTokenSubtype() == TokenSubtype.CommandConvertTo ) ||
						( ( tu.getTokenSubtype() == TokenSubtype.CommandBuyCash || tu.getTokenSubtype()== TokenSubtype.CommandCellCash ) ||
						( tu.getTokenSubtype() == TokenSubtype.CommandRecvRemittance || tu.getTokenSubtype() == TokenSubtype.CommandSendRemittance ) )
						)
				{
					if ( len < 3 ) throw new InvalidArgumentException("Missing necessary argument.");
				}
				else
				{
					throw new InvalidArgumentException("Unknown command [ " + tu.getTokenString() + " ]");
				}
			}
			else if ( i == 1 )
			{
				if ( TokenSubTSequence[0] == TokenSubtype.CommandShow )
				{
					if ( tu.getTokenType() != TokenType.CurrencyName )
						throw new InvalidArgumentException("Invalid option. [ " + tu.getTokenString() + " ]");
					else if ( tu.getTokenSubtype() == TokenSubtype.Unknown )
						throw new InvalidArgumentException("Unknown option. [ " + tu.getTokenString() + " ]");
				}
				else if ( 
					( TokenSubTSequence[0] == TokenSubtype.CommandConvertFrom || TokenSubTSequence[0] == TokenSubtype.CommandConvertTo ) ||
					( ( TokenSubTSequence[0] == TokenSubtype.CommandBuyCash || TokenSubTSequence[0] == TokenSubtype.CommandCellCash ) ||
					( TokenSubTSequence[0] == TokenSubtype.CommandRecvRemittance || TokenSubTSequence[0] == TokenSubtype.CommandSendRemittance ) )
					)
				{
					if ( tu.getTokenType() != TokenType.CurrencyName )
						throw new InvalidArgumentException("Invalid option. [ " + tu.getTokenString() + " ]");
					else if ( tu.getTokenSubtype() == TokenSubtype.Unknown )
						throw new InvalidArgumentException("Unknown option. [ " + tu.getTokenString() + " ]");
				}
					
			}
			else if ( i == 2 )
			{
				if ( TokenSubTSequence[0] == TokenSubtype.CommandShow && tu.getTokenType() != TokenType.FieldAbbr )
					throw new InvalidArgumentException("Invalid option. [ " + tu.getTokenString() + " ]");
				else if ( 
						( TokenSubTSequence[0] == TokenSubtype.CommandConvertFrom || TokenSubTSequence[0] == TokenSubtype.CommandConvertTo ) ||
						( ( TokenSubTSequence[0] == TokenSubtype.CommandBuyCash || TokenSubTSequence[0] == TokenSubtype.CommandCellCash ) ||
						( TokenSubTSequence[0] == TokenSubtype.CommandRecvRemittance || TokenSubTSequence[0] == TokenSubtype.CommandSendRemittance ) )
						)
						if ( tu.getTokenType() != TokenType.Amount )
							throw new InvalidArgumentException("Invalid option. [ " + tu.getTokenString() + " ]");
			}
			
			// continue;
			TokenTypeSequence[i] = tu.getTokenType();
			TokenSubTSequence[i] = tu.getTokenSubtype();
			i++;
		}
		
		// interpret command.
		i = 0;

		if ( TokenSubTSequence[i] == TokenSubtype.CommandHelp )
		{
			return "Help!";
		}
		else if ( TokenSubTSequence[i] == TokenSubtype.CommandLatestRound )
		{
			result = this.showLatestRound();
		}
		else if ( TokenSubTSequence[i] == TokenSubtype.CommandForceUpdate )
		{
			this.forceupdate();
			result = "Information table is updated, now! ";
			result += this.showLatestRound();
		}
		else if ( TokenSubTSequence[i] == TokenSubtype.CommandShow )
		{
			i++;
			String currency_name = TokenSubTSequence[i++].toString().substring(8);
			String field_name = "country_name,";
			
			if ( TokenSubTSequence[i] == TokenSubtype.FAAll )
				field_name = "*";
			else if ( TokenSubTSequence[i] == TokenSubtype.Unknown || TokenSubTSequence[i] == TokenSubtype.FACentralRate )
				field_name += "currency_unit,central_rate";
			else if ( TokenSubTSequence[i] == TokenSubtype.FABuyCash )
				field_name += "currency_unit,cash_buy";
			else if ( TokenSubTSequence[i] == TokenSubtype.FACellCash )
				field_name += "currency_unit,cash_cell";
			else if ( TokenSubTSequence[i] == TokenSubtype.FARecvRemit )
				field_name += "currency_unit,remittance_recv";
			else if ( TokenSubTSequence[i] == TokenSubtype.FASendRemit )
				field_name += "currency_unit,remittance_send";
			else if ( TokenSubTSequence[i] == TokenSubtype.FAECRate )
				field_name += "currency_unit,exchan_comm_rate";
			else if ( TokenSubTSequence[i] == TokenSubtype.FADollarExcRate )
				field_name += "currency_unit,dollar_exc_rate";
			else 
				throw new InvalidArgumentException("Unknown field abbreviation [ " + ta.getElement(i).getTokenString() + " ] " );
			
			result = this.showCurrencyRate(currency_name, field_name);
		}
		else
		{
			i++;
			String currency_name = TokenSubTSequence[i++].toString().substring(8);
			String value = "";
			
			if ( TokenSubTSequence[i] == TokenSubtype.AmountNatural )
				value = ta.getElement(i).getTokenString() + ".0";
			else 
				value = ta.getElement(i).getTokenString();

			if ( TokenSubTSequence[0] == TokenSubtype.CommandConvertFrom )
				result = this.convertFrom( currency_name, value );
			else if ( TokenSubTSequence[0] == TokenSubtype.CommandConvertTo )
				result = this.convertTo( currency_name, value );
			else if ( TokenSubTSequence[0] == TokenSubtype.CommandBuyCash )
				result = this.buyCash( currency_name, value );
			else if ( TokenSubTSequence[0] == TokenSubtype.CommandCellCash )
				result = this.cellCash( currency_name, value );
			else if ( TokenSubTSequence[0] == TokenSubtype.CommandSendRemittance )
				result = this.sendRemittance( currency_name, value );
			else if ( TokenSubTSequence[0] == TokenSubtype.CommandRecvRemittance )
				result = this.recvRemittance( currency_name, value );	
		}
		
		return result;
	}
	
	public static String getIRCHelpMessagePart1 ( )
	{
		String result = "!cer ( [Option] [Currency_Unit] [FieldAbbr/Amount] ) [Option] : help, show, lastround, convfrom, convto, buycash, cellcash, sendremit, recvremit ";
		result += "[Currency_Unit] : USD:U.S, EUR:Europe, JPY:Japan, CNY:China, HKD:HongKong, TWD:Taiwan, GBP:Great Britain, CAD:Canada, CHF:Switzerland, SEK:Sweden, ";
		result += "AUD:Australia, NZD:NewZealand, ISL:Israel, DKK:Denmark, NOK:Norway, SAR:Saudi Arabia, KWD:Kuweit, BHD:Bahrain, AED:United of Arab Emirates, ";
		result += "JOD:Jordan, ";
		
		return result;
	}
	public static String getIRCHelpMessagePart2 ( )
	{
		String result = "EGP:Egypt, THB:Thailand, SGD:Singapore, MYR:Malaysia, IDR:Indonesia, BND:Brunei, INR:India, PKR:Pakistan, BDT:Bangladesh, PHP:Philippine, ";
		result += "MXN:Mexico, BRL:Brazil, VND:Vietnam, ZAR:Republic of South Africa, RUB:Russia, HUF:Hungary, PLN:Poland ";
		result += "[FieldAbbr] (show 명령에만 해당) 모두보기, 매매기준, 현찰매수, 현찰매도, 송금보냄, 송금받음, 환수수료, 대미환산 ";
		result += "[Amount] 금액";
		
		return result;
	}
	/* This method is for executing on the CLI environment.
	private void showHelpMessage ( )
	{
		// this method must be deprecated for executing from ircbot.
		System.out.println ( "사용법 : ./run.sh --command [옵션]\n");
		System.out.println ( "\t--help : 이 메시지를 보여줍니다.");
		System.out.println ( "\t--latestround : 최종 갱신 일자, 시간, 회차를 보여줍니다.");
		System.out.println ( "\t--forceupdate : 강제로 정보 테이블을 갱신합니다.");
		System.out.println ( "\t--show [Currency_name] [Field] : 지정한 화폐단위에 대한 환율 항목을 보여줍니다.");
		System.out.println ( "\t--convertfrom [Currency_name] [Amount] : [Currency_name]단위의 [Amount] 금액으로부터 매매기준율을 이용하여 변환합니다.");
		System.out.println ( "\t--convertto [Currency_name] [Amount] : [Amount] 원화단위 금액을 [Current_name]단위로 매매기준율을 이용하여 변환합니다");
		System.out.println ( "\t--buycash [Currency_name] [Amount] : 환전 거래시 [Currency_name]단위의 [Amount] 금액으로 바꾸기 위해 필요한 원화금액을 보여줍니다.");
		System.out.println ( "\t--cellcash [Currency_name] [Amount] : 환전 거래시 [Currency_name]단위의 [Amount] 금액을 받을 수 있는 원화금액을 보여줍니다." );
		System.out.println ( "\t--sendremit [Currency_name] [Amount] : 송금 보낼 때 [Currency_name]단위의 [Amount] 금액을 보내기 위해 필요한 원화금액을 보여줍니다." );
		System.out.println ( "\t--recvremit [Currency_name] [Amount] : 송금 받을 때 [Currency_name]단위의 [Amount] 금액에 대해 받을 수 있는 원화금액을 보여줍니다.\n" );
		
		System.out.println ( "\t [Currency_name]");
		System.out.println ( "\t USD : U.S Dollar, EUR : Europe United EURO, JPY : Japan Yen, CNY : China Yuan");
		System.out.println ( "\t HKD : HongKong HongKongDollar, TWD : Taiwan NewTaiwanDollar, GBP : Great Britain Pound");
		System.out.println ( "\t CAD : Canada CanadianDollar, CHF : Switzerland SwissFranc, SEK : Sweden SwedishKrona");
		System.out.println ( "\t AUD : Australia AustralianDollar, NZD : NewZealand NewZealandDollar, ISL : Israel NewShekel");
		System.out.println ( "\t DKK : Denmark DanishKrone, NOK : Norway NorwegianKrone, SAR : Saudi Arabia SaudiRial");
		System.out.println ( "\t KWD : Kuweit Dinar, BHD : Bahrain Dinar, AED : United of Arab Emirates EmiratiDirham");
		System.out.println ( "\t JOD : Jordan Dinar, EGP : Egypt EgyptianPound, THB : Thailand Baht, SGD : Singapore SingaporeDollar");
		System.out.println ( "\t MYR : Malaysia Ringgit, IDR : Indonesia Rupiah, BND : Brunei BruneiDollar, INR : India Rupee");
		System.out.println ( "\t PKR : Pakistan PakistaniRupee, BDT : Bangladesh Taka, PHP : Philippine Peso, MXN : Mexico MexicanPero");
		System.out.println ( "\t BRL : Brazil Real, VND : Vietnam VietnameseDong, ZAR : Republic of South Africa Rand");
		System.out.println ( "\t RUB : Russia Ruble, HUF : Hungary Forint, PLN : Poland Zloty\n");
		
		System.out.println ( "\t [Field]");
		System.out.println ( "\t all : 모두 보여주기");
		System.out.println ( "\t cr : 매매 기준율 (기본값), cb : 현찰 매수 지불, cc : 현찰 매도 수익");
		System.out.println ( "\t rs : 송금 보낼 때, rr : 송금 받을 때, ec : 환전 수수료율");
		System.out.println ( "\t de : 달러 환산율");
	}
	*/
	private String showLatestRound ( ) throws IOException
	{
		String result = "";
		String [] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		
		PropertyManager pm = new PropertyManager ( this.getPath(), "LatestUpdatedDatetime.prop" );
		pm.loadProperties();
		
		String dt = pm.getValue("date");
		String roundstr = pm.getValue("round");
		
		String [] datetime = dt.split("\\s");
		String [] date = datetime[0].split("\\.");
		
		result = "Last Updated : " + month[Integer.parseInt(date[1]) - 1] + " " + date[2] + ", " + date[0] + "  "+ datetime[1] + ", " + Integer.parseInt(roundstr);
		int len = roundstr.length();
		char ch = roundstr.charAt(len-1);
		switch ( ch )
		{
			case '1':
				result += "st round.";
				break;
			case '2':
				result += "nd round.";
				break;
			case '3':
				result += "rd round.";
				break;
			default:
				result += "th round.";
				
		}
		return result;
	}
	private void forceupdate ( ) throws EmptyTokenStreamException, IOException, ClassNotFoundException, SQLException,
										FileNotSpecifiedException, URLNotSpecifiedException
	{
		
		String propFilename = this.getPath() + "LatestUpdatedDatetime.prop";	
		RemoteLocalDatetimeChecker check = new RemoteLocalDatetimeChecker(
				"http://info.finance.naver.com/marketindex/exchangeMain.nhn",
				propFilename );
		DateTimeRound remote = check.checkLatestNoticeDateandTime();
		
		// set properties using the value.
		PropertyManager pm = new PropertyManager ( propFilename );
		try
		{
			pm.loadProperties();
		}
		catch ( IOException e )
		{
			pm.setProp(new Properties());
			pm.setValue("date", "");
			pm.setValue("round", "0" );
			
			pm.storeProperties();
		}
		
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
		CERTableUpdater updater = new CERTableUpdater ( this.getPath(), "http://info.finance.naver.com/marketindex/exchangeList.nhn" );
		updater.update();
	}
	private String showCurrencyRate ( String CurrencyUnit, String FieldId ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		
		HSQLDBTableManager sqlManager = new HSQLDBTableManager( this.getPath(), "currency.db" );
		String [] data = sqlManager.selectDataFromTable( FieldId.toString() , CurrencyUnit.toString() );
		if ( FieldId.equals("*") )
			FieldId = "country_name,currency_unit,central_rate,cash_buy,cash_cell,remittance_send,remittance_recv,exchan_comm_rate,dollar_exc_rate";
		String [] separatedfield = FieldId.split("\\,");
		
		for( int j = 2 ; j < separatedfield.length ; j++ )
		{
			if ( j == 2 )
				result = data[0] + "(";
			else 
				result += ", " + data[0] + "(";
			
			if ( Integer.parseInt(data[1]) == 100 )
				result += data[1] + " ";
			
			result += ( CurrencyUnit + ")에 대한 ");
			
			if ( separatedfield[j].equals("exchan_comm_rate") ) 
			{
				if ( data[j].equals("0.0") )
					result += "환전수수료율을 가져올 수 없습니다. (존재하지 않음)";
				else
					result += "환전수수료율 : " + data[j] + "%";
			}
			else if ( separatedfield[j].equals("dollar_exc_rate") )
			{
				result += "대미환산율 : " + String.format("%.3f", (Double.parseDouble(data[j]) * 100.0)) + "%";
			}
			else 
			{
				if ( separatedfield[j].equals("central_rate") )
					result += "거래기준액";
				else if ( separatedfield[j].equals("cash_buy") )
					result += "현금매수액";
				else if ( separatedfield[j].equals("cash_cell") )
					result += "현금매도액";
				else if ( separatedfield[j].equals("remittance_send") )
					result += "송금수신액";
				else if ( separatedfield[j].equals("remittance_recv") )
					result += "송금전송액";
					
				if ( data[j].equals("0.0") )
					result += " : (거래불가) ";
				else
					result += " : ￦" + data[j];
			}
			
		}
		return result ;
	}
	private String convertFrom ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,central_rate";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);
		
		String resval = String.format("%.2f", ( s_value / currency_unit ) * central_rate );
		result = value + " " + CurrencyUnit + " => ￦" + resval;
		
		return result;
	}
	private String convertTo ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,central_rate";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);

		String resval = String.format("%.2f", ( s_value / central_rate ) * currency_unit );
		result  = "￦" + value + " => " + resval + " " + CurrencyUnit;
		
		return result;
	}
	private String buyCash ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,cash_buy";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);
		
		String resval = String.format("%.2f", ( s_value / currency_unit ) * central_rate );
		result = value + " " + CurrencyUnit + " 을(를) 받기위한 필요 원화금액 : ￦" + resval;
		
		return result;
	}
	private String cellCash ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,cash_cell";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);
		
		String resval = String.format("%.2f", ( s_value / currency_unit ) * central_rate );
		result =  value + " " + CurrencyUnit + " (으)로 받을 수 있는 원화금액 : ￦" + resval;
		
		return result;
	}
	private String recvRemittance ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,remittance_recv";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);
		
		String resval = String.format("%.2f", ( s_value / currency_unit ) * central_rate );
		result = value + " " + CurrencyUnit + " (으)로 송금받을 수 있는 원화금액 : ￦" + resval;
		
		return result;
	}
	private String sendRemittance ( String CurrencyUnit, String value ) throws SQLException, ClassNotFoundException
	{
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(), "currency.db");
		String field = "country_name,currency_unit,remittance_send";
		String [] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();
		
		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);
		
		String resval = String.format("%.2f", ( s_value / currency_unit ) * central_rate );
		result = value + " " + CurrencyUnit + " 을(를) 송금하기위한 필요 원화금액 : ￦" + resval;
		
		return result;
	}

}
