/*
 * CTokenMarker.java - C token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package processing.mode.esp;

import javax.swing.text.Segment;

import processing.app.syntax.KeywordMap;
import processing.app.syntax.Token;
import processing.app.syntax.TokenMarker;

/**
 * C token marker.
 *
 * @author Slava Pestov
 * @version $Id: CTokenMarker.java,v 1.34 1999/12/13 03:40:29 sp Exp $
 */
public class CTokenMarker extends TokenMarker
{
	public CTokenMarker()
	{
		this(true,getKeywords());
	}

	public CTokenMarker(boolean cpp, KeywordMap keywords)
	{
		this.cpp = cpp;
		this.keywords = keywords;
	}

	public byte markTokensImpl(byte token, Segment line, int lineIndex)
	{
		char[] array = line.array;
		int offset = line.offset;
		lastOffset = offset;
		lastKeyword = offset;
		int length = line.count + offset;
		boolean backslash = false;

loop:		for(int i = offset; i < length; i++)
		{
			int i1 = (i+1);

			char c = array[i];
			if(c == '\\')
			{
				backslash = !backslash;
				continue;
			}

			switch(token)
			{
			case Token.NULL:
				switch(c)
				{
				case '#':
					if(backslash)
						backslash = false;
					else if(cpp)
					{
						if(doKeyword(line,i,c))
							break;
						addToken(i - lastOffset,token);
						addToken(length - i,Token.KEYWORD2);
						lastOffset = lastKeyword = length;
						break loop;
					}
					break;
				case '"':
					doKeyword(line,i,c);
					if(backslash)
						backslash = false;
					else
					{
						addToken(i - lastOffset,token);
						token = Token.LITERAL1;
						lastOffset = lastKeyword = i;
					}
					break;
				case '\'':
					doKeyword(line,i,c);
					if(backslash)
						backslash = false;
					else
					{
						addToken(i - lastOffset,token);
						token = Token.LITERAL2;
						lastOffset = lastKeyword = i;
					}
					break;
				case ':':
					if(lastKeyword == offset)
					{
						if(doKeyword(line,i,c))
							break;
						backslash = false;
						addToken(i1 - lastOffset,Token.LABEL);
						lastOffset = lastKeyword = i1;
					}
					else if(doKeyword(line,i,c))
						break;
					break;
				case '/':
					backslash = false;
					doKeyword(line,i,c);
					if(length - i > 1)
					{
						switch(array[i1])
						{
						case '*':
							addToken(i - lastOffset,token);
							lastOffset = lastKeyword = i;
							if(length - i > 2 && array[i+2] == '*')
								token = Token.COMMENT2;
							else
								token = Token.COMMENT1;
							break;
						case '/':
							addToken(i - lastOffset,token);
							addToken(length - i,Token.COMMENT1);
							lastOffset = lastKeyword = length;
							break loop;
						}
					}
					break;
				default:
					backslash = false;
					if(!Character.isLetterOrDigit(c)
						&& c != '_')
						doKeyword(line,i,c);
					break;
				}
				break;
			case Token.COMMENT1:
			case Token.COMMENT2:
				backslash = false;
				if(c == '*' && length - i > 1)
				{
					if(array[i1] == '/')
					{
						i++;
						addToken((i+1) - lastOffset,token);
						token = Token.NULL;
						lastOffset = lastKeyword = i+1;
					}
				}
				break;
			case Token.LITERAL1:
				if(backslash)
					backslash = false;
				else if(c == '"')
				{
					addToken(i1 - lastOffset,token);
					token = Token.NULL;
					lastOffset = lastKeyword = i1;
				}
				break;
			case Token.LITERAL2:
				if(backslash)
					backslash = false;
				else if(c == '\'')
				{
					addToken(i1 - lastOffset,Token.LITERAL1);
					token = Token.NULL;
					lastOffset = lastKeyword = i1;
				}
				break;
			default:
				throw new InternalError("Invalid state: "
					+ token);
			}
		}

		if(token == Token.NULL)
			doKeyword(line,length,'\0');

		switch(token)
		{
		case Token.LITERAL1:
		case Token.LITERAL2:
			addToken(length - lastOffset,Token.INVALID);
			token = Token.NULL;
			break;
		case Token.KEYWORD2:
			addToken(length - lastOffset,token);
			if(!backslash)
				token = Token.NULL;
		default:
			addToken(length - lastOffset,token);
			break;
		}

		return token;
	}

	public static KeywordMap getKeywords()
	{
		if(cKeywords == null)
		{
			cKeywords = new KeywordMap(false);
			cKeywords.add("char",Token.KEYWORD3,false);
			cKeywords.add("double",Token.KEYWORD3,false);
			cKeywords.add("enum",Token.KEYWORD3,false);
			cKeywords.add("float",Token.KEYWORD3,false);
			cKeywords.add("int",Token.KEYWORD3,false);
			cKeywords.add("long",Token.KEYWORD3,false);
			cKeywords.add("short",Token.KEYWORD3,false);
			cKeywords.add("signed",Token.KEYWORD3,false);
			cKeywords.add("struct",Token.KEYWORD3,false);
			cKeywords.add("typedef",Token.KEYWORD3,false);
			cKeywords.add("union",Token.KEYWORD3,false);
			cKeywords.add("unsigned",Token.KEYWORD3,false);
			cKeywords.add("void",Token.KEYWORD3,false);
			cKeywords.add("auto",Token.KEYWORD1,false);
			cKeywords.add("const",Token.KEYWORD1,false);
			cKeywords.add("extern",Token.KEYWORD1,false);
			cKeywords.add("register",Token.KEYWORD1,false);
			cKeywords.add("static",Token.KEYWORD1,false);
			cKeywords.add("volatile",Token.KEYWORD1,false);
			cKeywords.add("break",Token.KEYWORD1,false);
			cKeywords.add("case",Token.KEYWORD1,false);
			cKeywords.add("continue",Token.KEYWORD1,false);
			cKeywords.add("default",Token.KEYWORD1,false);
			cKeywords.add("do",Token.KEYWORD1,false);
			cKeywords.add("else",Token.KEYWORD1,false);
			cKeywords.add("for",Token.KEYWORD1,false);
			cKeywords.add("goto",Token.KEYWORD1,false);
			cKeywords.add("if",Token.KEYWORD1,false);
			cKeywords.add("return",Token.KEYWORD1,false);
			cKeywords.add("sizeof",Token.KEYWORD1,false);
			cKeywords.add("switch",Token.KEYWORD1,false);
			cKeywords.add("while",Token.KEYWORD1,false);
			cKeywords.add("asm",Token.KEYWORD2,false);
			cKeywords.add("asmlinkage",Token.KEYWORD2,false);
			cKeywords.add("far",Token.KEYWORD2,false);
			cKeywords.add("huge",Token.KEYWORD2,false);
			cKeywords.add("inline",Token.KEYWORD2,false);
			cKeywords.add("near",Token.KEYWORD2,false);
			cKeywords.add("pascal",Token.KEYWORD2,false);
			cKeywords.add("true",Token.LITERAL2,false);
			cKeywords.add("false",Token.LITERAL2,false);
			cKeywords.add("NULL",Token.LITERAL2,false);
		}
		return cKeywords;
	}

	// private members
	private static KeywordMap cKeywords;

	private boolean cpp;
	private KeywordMap keywords;
	private int lastOffset;
	private int lastKeyword;

	private boolean doKeyword(Segment line, int i, char c)
	{
		int i1 = i+1;

		int len = i - lastKeyword;
		byte id = keywords.lookup(line,lastKeyword,len,false);
		if(id != Token.NULL)
		{
			if(lastKeyword != lastOffset)
				addToken(lastKeyword - lastOffset,Token.NULL);
			addToken(len,id);
			lastOffset = i;
		}
		lastKeyword = i1;
		return false;
	}

	@Override
	public void addColoring(String keyword, String coloring) {
		System.out.format("addColoring(%s, %s) called in HtmlTokenMarker%n");
	}
}
