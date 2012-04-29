/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 7 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;

public final class QNumberValueParser implements Parser<Value<? extends NUMBER>>
{
    public static final QNumberValueParser PARSER = new QNumberValueParser();

    public static final RegexToken QDOUBLE_TOKEN = new RegexToken("\\d*\\.\\d+#?");

    public static final RegexToken QINTEGER_TOKEN = new RegexToken("&?H?\\d+[#&]?");

    private QNumberValueParser()
    {
        /* Empty */
    }

    public String describe()
    {
        return "number";
    }

    public NUMBER<? extends NUMBER> parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        String lex;
        if ((lex = scanner.matchAdvance(QINTEGER_TOKEN)) != null)
        {
            if (lex.startsWith("&"))
                lex = lex.substring(1);
            if (lex.endsWith("&") || lex.endsWith("#"))
                lex = lex.substring(0, lex.length() - 1);
            if (lex.startsWith("H"))
                return new INTEGER(Integer.parseInt(lex.substring(1), 16));
            return new INTEGER(Integer.parseInt(lex));
        } else if ((lex = scanner.matchAdvance(QDOUBLE_TOKEN)) != null)
        {
            if (lex.startsWith("."))
                lex = "0" + lex;
            if (lex.endsWith("#"))
                lex = lex.substring(0, lex.length() - 1);
            return new DOUBLE(Double.parseDouble(lex));
        }
        return null;
    }
}
