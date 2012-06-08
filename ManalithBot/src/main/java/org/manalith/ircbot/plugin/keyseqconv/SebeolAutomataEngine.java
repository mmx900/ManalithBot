/*
 	org.manalith.ircbot.plugin.keyseqconv/SebeolAutomataEngine.java
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
package org.manalith.ircbot.plugin.keyseqconv;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

import org.manalith.ircbot.plugin.keyseqconv.exceptions.LayoutNotSpecifiedException;
import org.manalith.ircbot.plugin.keyseqconv.exceptions.BackSlashesDoNotMatchException;

public class SebeolAutomataEngine extends InputSequenceAutomataEngine {

	private boolean enableParseExceptionSyntax;
	LetterObject syl;
	
	public SebeolAutomataEngine () 
	{
		try {
			syl = new LetterObject ( LetterObject.LayoutFlag.Sebeol );
		} catch (LayoutNotSpecifiedException e) {
			// Do Nothing.
			// e.printStackTrace();
		}
	}
	
	private String changeKeyValToInternalSymbol ( final String str )
	{
		String result = "";
		String strbuf = "";
		
		// do not change an order both 1st and 2nd
		
		
		// 1st phrase : change number 0 and 1 to _0 and _1 respectivelly
		strbuf = str.replaceAll("\\_","_0¯").replaceAll("0", "_0")
				.replaceAll("1", "_1").replaceAll("¯","0");
		
		// 2nd phase : change special char with another special char
		//             to low lines + alphanumeric characters.
		strbuf = strbuf.replaceAll("2", "_2").replaceAll("3", "_3")
				.replaceAll("4", "_4").replaceAll("5", "_5")
				.replaceAll("6", "_6").replaceAll("7", "_7")
				.replaceAll("8", "gd").replaceAll("9", "b");
		
		// 3rd phase : change special char printed above number to low line + double number.
		strbuf = strbuf.replaceAll("\\!", "_11").replaceAll("\\@", "_22")
				.replaceAll("\\#", "_33").replaceAll("\\$", "_44")
				.replaceAll("\\%", "_55").replaceAll("\\^", "_66")
				.replaceAll("\\&", "_77").replaceAll("\\*", "_88")
				.replaceAll("\\(", "_99").replaceAll("\\)", "_00");
		
		// ` ~ - = + [ ] { }  ; ' : " , . / < > ?  
		result = strbuf.replaceAll("\\`","_1_").replaceAll("\\~", "_11_")
				.replaceAll("\\-","__0").replaceAll("\\=","___0")
				.replaceAll("\\+", "___00").replaceAll("\\\\","____0")
				.replaceAll("\\|", "____00").replaceAll("\\[","_p")
				.replaceAll("\\]","__p").replaceAll("\\{","_P")
				.replaceAll("\\}","__P").replaceAll("\\;", "_l")
				.replaceAll("\\'", "__l").replaceAll("\\:", "_L")
				.replaceAll("\\\"", "__L").replaceAll("[\\,\\<]", "_m")
				.replaceAll("[\\.\\>]", "__m").replaceAll("\\/", "v")
				.replaceAll("\\?", "___M");
		
		
		return result;
	}

	public void setEnableParsingExceptionSyntax(boolean enable) {
		this.enableParseExceptionSyntax = enable;
	}
	public boolean isEnableParsingExceptionSyntax() {
		return this.enableParseExceptionSyntax;
	}

	protected boolean isISingleConsonant(String tICon) {
		try
		{
			@SuppressWarnings("unused")
			SebeolSymbol.SebeolISingleConsonant check1 = 
				SebeolSymbol.SebeolISingleConsonant.valueOf(tICon);
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}	
	protected boolean isIDoubleConsonant(String tICon) {
		try
		{
			@SuppressWarnings("unused")
			SebeolSymbol.SebeolIDoubleConsonant check1 =
				SebeolSymbol.SebeolIDoubleConsonant.valueOf(tICon);
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}
	protected boolean isVowel(String tVow) {
		try
		{
			@SuppressWarnings("unused")
			SebeolSymbol.SebeolVowel check1 = 
				SebeolSymbol.SebeolVowel.valueOf(tVow);
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}	
	protected boolean isFConsonant(String tFCon) {
		try
		{
			@SuppressWarnings("unused")
			SebeolSymbol.SebeolFConsonant check1 = 
				SebeolSymbol.SebeolFConsonant.valueOf(tFCon);
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}
	private boolean isSpecialChar(String tChar) {
		try
		{
			@SuppressWarnings("unused")
			SebeolSymbol.SebeolSpecialChar check1 = 
				SebeolSymbol.SebeolSpecialChar.valueOf(tChar);
			return true;
		}
		catch ( Exception e )
		{
			return false;
		}
	}
	
	public String parseKoreanStringToEngSpell(String korean)
			throws BackSlashesDoNotMatchException {
		
		String result = "";
		
		return result;
	}
	
	public String parseKeySequenceToKorean(String keySequence)
			throws BackSlashesDoNotMatchException, LayoutNotSpecifiedException
	{
		String result = "";
				
		String tToken = "";
		String tTokenLookahead = "";
		String tTokenLookaheadCombination = "";
		
		// 기본값은 마지막 위치가 아닌걸로 태깅
		boolean isLastPosition = false; 
		
		if (this.isEnableParsingExceptionSyntax()
				&& StringUtils.countMatches(keySequence, "\\") % 2 == 1)
			throw new BackSlashesDoNotMatchException();

		for ( int i = 0 ; i < keySequence.length() ; i++ )
		{
			if (!CharUtils.isAsciiAlpha(keySequence.charAt(i))) {
				if (keySequence.charAt(i) == '\\'
						&& this.isEnableParsingExceptionSyntax()) {
					i++;
					while (true) {
						if (i + 1 <= keySequence.length() - 1) {
							if (keySequence.charAt(i) == '\\') {
								if (keySequence.charAt(i + 1) == '\\') {
									i++;
									result += '\\';
								} else
									break;
							} else {
								result += keySequence.charAt(i);
							}
						} else {
							if (keySequence.charAt(i) == '\\') {
								break;
							} else {
								result += keySequence.charAt(i);
							}
						}
						i++;
					}
					continue;
				}
				else if (keySequence.charAt(i) == ' ' )
				{
					if ( syl.isCompleteSyllable() )
					{
						result += syl.getLetter();
					}
					else
					{
						SebeolSymbol.SebeolIConsonant init = 
								SebeolSymbol.SebeolIConsonant.valueOf(syl.getIConsonantKeySymbol());
						SebeolSymbol.SebeolVowel vow = 
								SebeolSymbol.SebeolVowel.valueOf(syl.getVowelKeySymbol());
						SebeolSymbol.SebeolFConsonant fin = 
								SebeolSymbol.SebeolFConsonant.valueOf(syl.getFConsonantKeySymbol());
						
						if ( syl.isAssignedFConstantFirst() )
						{
							result += this.getSingleChar(this.getSingleCharVal(fin.toString()));
							if ( init != SebeolSymbol.SebeolIConsonant.nul )
								result += this.getSingleChar(this.getSingleCharVal(init.toString()));
							if ( vow != SebeolSymbol.SebeolVowel.nul )
								result += this.getSingleChar(this.getSingleCharVal(vow.toString()));
						}
						else
						{
							if ( init != SebeolSymbol.SebeolIConsonant.nul )
								result += this.getSingleChar(this.getSingleCharVal(init.toString()));
							if ( vow != SebeolSymbol.SebeolVowel.nul )
								result += this.getSingleChar(this.getSingleCharVal(vow.toString()));
						}
					}
					
					syl.initLetter();
					result += keySequence.charAt(i);
					continue;
				}
			}
			
			tTokenLookahead = tTokenLookaheadCombination = "";
			tToken = Character.toString(keySequence.charAt(i));
			
			if ( i < keySequence.length() - 1 )
			{
				tTokenLookahead = Character.toString(keySequence.charAt(i+1));
				tTokenLookaheadCombination = tToken + tTokenLookahead;
				
				// 마지막 위치 여부 저장
				isLastPosition = (i + 1 == keySequence.length() - 1);
				
				// 쌍자음
				if ( this.isIDoubleConsonant(
					this.changeKeyValToInternalSymbol(tTokenLookaheadCombination)) ) {
					result += this.inputIConsonant(isLastPosition, tTokenLookaheadCombination);
					if ( isLastPosition ) {
						syl.initLetter(); break;
					} 
					else {
						i++; continue; 
					}
				}
				// 겹모음!
				else if ( this.isVowel(
					this.changeKeyValToInternalSymbol(tTokenLookaheadCombination)) ) {
					result += this.inputVowel(isLastPosition, tTokenLookaheadCombination);
					if ( isLastPosition ) {
						syl.initLetter(); break;
					}
					else { 
						i++; continue; 
					}
				}
			}

			// 쌍자음,겹모음 검사에서 통과하지 않았으므로 단자음 단모음 받침 검사
			// 마지막 위치 여부 입력 
			isLastPosition = (i == keySequence.length() - 1);

			// 단자음
			if ( this.isISingleConsonant(this.changeKeyValToInternalSymbol(tToken)) )
				result += this.inputIConsonant( isLastPosition, tToken );
				
			// 단모음
			else if ( this.isVowel(this.changeKeyValToInternalSymbol(tToken)) )
				result += this.inputVowel( isLastPosition, tToken );

			// 받침은 키 하나로만 입력
			else if ( this.isFConsonant(this.changeKeyValToInternalSymbol(tToken)) )
				result += this.inputFConsonant( isLastPosition, tToken );
			
			else if ( this.isSpecialChar(this.changeKeyValToInternalSymbol(tToken)) )
				result += this.inputSpecialChar( tToken );

			// 마지막이면 루프를 깨고 아니면 계속
			if (isLastPosition) { syl.initLetter(); break; } else continue;
			
			// 끗.
		}
		
		return result;
	}
	
	private String inputIConsonant ( boolean isLastPosition, String token ) 
			throws LayoutNotSpecifiedException
	{
		String result = "";

		// 초성 유무 상태 저장
		boolean beingIConsonant = 
			( syl.getIConsonantValue() != SebeolSymbol.SebeolIConsonant.nul.value() );
			
		// 초성이 없으면 초성을 넣는다
		if ( !beingIConsonant )		
			syl.setIConsonant(this.changeKeyValToInternalSymbol(token));
		
		// 마지막 입력도 아니고 초성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if ( !( !isLastPosition && !beingIConsonant ) )
		{
			// 중성 있는 완성 글자 (최소한 초성+중성)
			if ( syl.isCompleteSyllable() )
				result += syl.getLetter();

			// 초성은 있고 중성이 없다?!
			else
			{
				SebeolSymbol.SebeolIConsonant init = 
					SebeolSymbol.SebeolIConsonant.valueOf(syl.getIConsonantKeySymbol());
				SebeolSymbol.SebeolFConsonant fin = 
					SebeolSymbol.SebeolFConsonant.valueOf(syl.getFConsonantKeySymbol());
				
				// 받침이 있으면
				if ( syl.isAssignedFConstantFirst() )
					result += this.getSingleChar(this.getSingleCharVal(fin.toString()))
						+ this.getSingleChar(this.getSingleCharVal(init.toString()));
				// 초성만 있다.
				else
					result += this.getSingleChar(this.getSingleCharVal(init.toString()));
			}
		}
		
		// 초성이 있으면
		if ( beingIConsonant )
		{
			// 마지막 입력이면
			if ( isLastPosition )
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(token));
			
			// 아니면
			else
			{
				// 초기화 하고 슬롯에 넣는다.
				syl.initLetter();
				syl.setIConsonant(this.changeKeyValToInternalSymbol(token));
			}
		}
		
		return result;
	}
	private String inputVowel ( boolean isLastPosition, String token ) 
			throws LayoutNotSpecifiedException
	{
		String result = "";
		
		// 중성 유무 상태 저장
		boolean beingVowel = 
			( syl.getVowelValue() != SebeolSymbol.SebeolVowel.nul.value() );
		
		// 중성이 없으면 중성을 넣는다
		if ( !beingVowel )
			syl.setVowel(this.changeKeyValToInternalSymbol(token));
		
		// 마지막 입력도 아니고 중성이 있는 상태도 아닌 상태의 나머지 경우에 실행
		if ( !( !isLastPosition && !beingVowel ) )
		{
			// 모음이 이미 있으면서 완성체인가?
			if ( syl.isCompleteSyllable() )
				result += syl.getLetter();
			// 자음이 없어서 완성체가 아닌 경우
			else
			{
				SebeolSymbol.SebeolVowel vow = 
					SebeolSymbol.SebeolVowel.valueOf(syl.getVowelKeySymbol());
				SebeolSymbol.SebeolFConsonant fin =
					SebeolSymbol.SebeolFConsonant.valueOf(syl.getFConsonantKeySymbol());
				
				// 받침이 있으면
				if ( syl.isAssignedFConstantFirst() )
					result += this.getSingleChar(this.getSingleCharVal(fin.toString()))
						+ this.getSingleChar(this.getSingleCharVal(vow.toString()));
				
				// 받침이 없으면
				else
					result += this.getSingleChar(this.getSingleCharVal(vow.toString()));
				
			}
		}
		
		// 모음 존재
		if ( beingVowel )
		{
			// 마지막 입력이면
			if ( isLastPosition )
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(token));
			
			// 아니면
			else
			{
				// 초기화 하고 슬롯에 넣는다
				syl.initLetter();
				syl.setVowel(this.changeKeyValToInternalSymbol(token));
			}
		}
		
		return result;
	}
	private String inputFConsonant ( boolean isLastPosition, String token ) 
			throws LayoutNotSpecifiedException
	{
		String result = "";
		
		boolean beingFConsonant = 
			( syl.getFConsonantValue() != SebeolSymbol.SebeolFConsonant.nul.value() ); 
		
		// 종성이 슬롯에 없으면 종성을 일단 넣는다.
		if ( !beingFConsonant )
			syl.setFConsonant(this.changeKeyValToInternalSymbol(token));
		
		if ( !( !isLastPosition && !beingFConsonant ) )
		{
			// 완성 글자 초+중+종
			if ( syl.isCompleteSyllable() )
				result += syl.getLetter(); 

			// 초성 빠져 있고 종성 있으면 (초성이 있고 없고.)
			else
			{
				SebeolSymbol.SebeolIConsonant init =
					SebeolSymbol.SebeolIConsonant.valueOf(syl.getIConsonantKeySymbol());
				SebeolSymbol.SebeolVowel vow =
					SebeolSymbol.SebeolVowel.valueOf(syl.getVowelKeySymbol());
				SebeolSymbol.SebeolFConsonant fin =
					SebeolSymbol.SebeolFConsonant.valueOf(syl.getFConsonantKeySymbol());

				// 초성이 있으면 (초성 -> 종성)
				if ( init != SebeolSymbol.SebeolIConsonant.nul )
					result += this.getSingleChar(this.getSingleCharVal(init.toString()));
				// 중성이 있으면 (중성 -> 종성)
				if ( vow != SebeolSymbol.SebeolVowel.nul)
					result += this.getSingleChar(this.getSingleCharVal(vow.toString()));
				
				result += this.getSingleChar(this.getSingleCharVal(fin.toString()));
			}
		}
		
		// 종성이 슬롯에 있을때
		if ( beingFConsonant )
		{
			// 마지막 잉여 종성 입력이면
			if ( isLastPosition )
				// 출력
				result += this.getSingleChar(this.getSingleCharVal(token));
			// 아니면
			else
			{
				// 종성이 이미 있으므로 슬롯을 초기화 하고 넣는다.
				syl.initLetter();
				syl.setFConsonant(this.changeKeyValToInternalSymbol(token));
			}
		}
		
		return result;
	}
	private String inputSpecialChar ( String token )
	{
		String result = "";
		String internalSymbol = this.changeKeyValToInternalSymbol(token);
		SebeolSymbol.SebeolSpecialChar spec =
			SebeolSymbol.SebeolSpecialChar.valueOf(internalSymbol);
		
		
		if ( syl.isCompleteSyllable() )
		{
			result += syl.getLetter();
			result += this.getSpecialChar(spec.value());
		}
		// 모음이 없다.
		else
		{
			SebeolSymbol.SebeolIConsonant init = 
				SebeolSymbol.SebeolIConsonant.valueOf(syl.getIConsonantKeySymbol());
			SebeolSymbol.SebeolFConsonant fin = 
				SebeolSymbol.SebeolFConsonant.valueOf(syl.getFConsonantKeySymbol());
			
			// 받침이 있으면
			if ( syl.isAssignedFConstantFirst() )
			{
				result += this.getSingleChar(this.getSingleCharVal(fin.toString()));
				// 초성이 있으면
				if ( init != SebeolSymbol.SebeolIConsonant.nul )
					result += this.getSingleChar(this.getSingleCharVal(init.toString()));
			}
			// 초성만 있거나 아무것도 없다?
			else
			{
				if ( init != SebeolSymbol.SebeolIConsonant.nul )
					result += this.getSingleChar(this.getSingleCharVal(init.toString()));
			}
			
			result += this.getSpecialChar(spec.value());
		}
		
		
		return result;
	}
	
	@Override
	protected int getSingleCharVal(String keySymbol) {
		return SebeolSymbol.SebeolSingleLetter.valueOf(keySymbol).value();
	}
	
	private String getSpecialChar(int charVal)
	{
		char [] ch = new char[1];
		ch[0] = (char)(charVal + 32);
		
		return new String(ch);
	}

	@Override
	protected String getSingleChar(int charVal) {
		char[] ch = new char[1];
		// single char value starts from 0x1100 
		ch[0] = (char) (charVal + 0x1100);

		return new String(ch);
	}
}
