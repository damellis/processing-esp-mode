/*
 * CCTokenMarker.java - C++ token marker
 * Copyright (C) 1999 Slava Pestov
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
 * C++ token marker.
 *
 * @author Slava Pestov
 * @version $Id: CCTokenMarker.java,v 1.6 1999/12/13 03:40:29 sp Exp $
 */
public class CCTokenMarker extends CTokenMarker
{
	public CCTokenMarker()
	{
		super(true,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(ccKeywords == null)
		{
			ccKeywords = new KeywordMap(false);

			ccKeywords.add("and", Token.KEYWORD3, false);
			ccKeywords.add("and_eq", Token.KEYWORD3, false);
			ccKeywords.add("asm", Token.KEYWORD2, false);         //
			ccKeywords.add("auto", Token.KEYWORD1, false);        //
			ccKeywords.add("bitand", Token.KEYWORD3, false);
			ccKeywords.add("bitor", Token.KEYWORD3, false);
			ccKeywords.add("bool",Token.KEYWORD3, false);
			ccKeywords.add("break", Token.KEYWORD1, false);	   //
			ccKeywords.add("case", Token.KEYWORD1, false);		   //
			ccKeywords.add("catch", Token.KEYWORD1, false);
			ccKeywords.add("char", Token.KEYWORD3, false);		   //
			ccKeywords.add("class", Token.KEYWORD3, false);
			ccKeywords.add("compl", Token.KEYWORD3, false);
			ccKeywords.add("const", Token.KEYWORD1, false);	   //
			ccKeywords.add("const_cast", Token.KEYWORD3, false);
			ccKeywords.add("continue", Token.KEYWORD1, false);	   //
			ccKeywords.add("default", Token.KEYWORD1, false);	   //
			ccKeywords.add("delete", Token.KEYWORD1, false);
			ccKeywords.add("do",Token.KEYWORD1, false);           //
			ccKeywords.add("double" ,Token.KEYWORD3, false);	   //
			ccKeywords.add("dynamic_cast", Token.KEYWORD3, false);
			ccKeywords.add("else", 	Token.KEYWORD1, false);	   //
			ccKeywords.add("enum",  Token.KEYWORD3, false);	   //
			ccKeywords.add("explicit", Token.KEYWORD1, false);			
			ccKeywords.add("export", Token.KEYWORD2, false);
			ccKeywords.add("extern", Token.KEYWORD2, false);	   //
			ccKeywords.add("false", Token.LITERAL2, false);
			ccKeywords.add("float", Token.KEYWORD3, false);	   //
			ccKeywords.add("for", Token.KEYWORD1, false);		   //
			ccKeywords.add("friend", Token.KEYWORD1, false);			
			ccKeywords.add("goto", Token.KEYWORD1, false);        //
			ccKeywords.add("if", Token.KEYWORD1, false);		   //
			ccKeywords.add("inline", Token.KEYWORD1, false);
			ccKeywords.add("int", Token.KEYWORD3, false);		   //
			ccKeywords.add("long", Token.KEYWORD3, false);		   //
			ccKeywords.add("mutable", Token.KEYWORD3, false);
			ccKeywords.add("namespace", Token.KEYWORD2, false);
			ccKeywords.add("new", Token.KEYWORD1, false);
			ccKeywords.add("not", Token.KEYWORD3, false);
			ccKeywords.add("not_eq", Token.KEYWORD3, false);
			ccKeywords.add("operator", Token.KEYWORD3, false);
			ccKeywords.add("or", Token.KEYWORD3, false);
			ccKeywords.add("or_eq", Token.KEYWORD3, false);
			ccKeywords.add("private", Token.KEYWORD1, false);
			ccKeywords.add("protected", Token.KEYWORD1, false);
			ccKeywords.add("public", Token.KEYWORD1, false);
			ccKeywords.add("register", Token.KEYWORD1, false);
			ccKeywords.add("reinterpret_cast", Token.KEYWORD3, false);
			ccKeywords.add("return", Token.KEYWORD1, false);      //
			ccKeywords.add("short", Token.KEYWORD3, false);	   //
			ccKeywords.add("signed", Token.KEYWORD3, false);	   //
			ccKeywords.add("sizeof", Token.KEYWORD1, false);	   //
			ccKeywords.add("static", Token.KEYWORD1, false);	   //
			ccKeywords.add("static_cast", Token.KEYWORD3, false);
			ccKeywords.add("struct", Token.KEYWORD3, false);	   //
			ccKeywords.add("switch", Token.KEYWORD1, false);	   //
			ccKeywords.add("template", Token.KEYWORD3, false);
			ccKeywords.add("this", Token.LITERAL2, false);
			ccKeywords.add("throw", Token.KEYWORD1, false);
			ccKeywords.add("true", Token.LITERAL2, false);
			ccKeywords.add("try", Token.KEYWORD1, false);
			ccKeywords.add("typedef", Token.KEYWORD3, false);	   //
			ccKeywords.add("typeid", Token.KEYWORD3, false);
			ccKeywords.add("typename", Token.KEYWORD3, false);
			ccKeywords.add("union", Token.KEYWORD3, false);	   //
			ccKeywords.add("unsigned", Token.KEYWORD3, false);	   //
			ccKeywords.add("using", Token.KEYWORD2, false);
			ccKeywords.add("virtual", Token.KEYWORD1, false);
			ccKeywords.add("void", Token.KEYWORD1, false);		   //
			ccKeywords.add("volatile", Token.KEYWORD1, false);	   //
			ccKeywords.add("wchar_t", Token.KEYWORD3, false);
			ccKeywords.add("while", Token.KEYWORD1, false);	   //
			ccKeywords.add("xor", Token.KEYWORD3, false);
			ccKeywords.add("xor_eq", Token.KEYWORD3, false);            

			// non ANSI keywords
			ccKeywords.add("NULL", Token.LITERAL2, false);
		}
		return ccKeywords;
	}

	// private members
	private static KeywordMap ccKeywords;
}
