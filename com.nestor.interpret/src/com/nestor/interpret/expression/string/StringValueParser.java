/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-03
 ****************************************************************************/

package com.nestor.interpret.expression.string;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.STRING;

public final class StringValueParser implements Parser<Value<? extends STRING>>
{
    public static final StringValueParser PARSER = new StringValueParser();

    public static final Token STRING_TOKEN = new RegexToken("string", "\"[^\"\\n]*\"", false);

    private StringValueParser()
    {
        /* Empty */
    }

    public STRING parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        String lex = scanner.matchAdvance(STRING_TOKEN);
        if (lex == null)
            return null;
        return new STRING(lex.substring(1, lex.length() - 1));
    }

    public String describe()
    {
        return "string";
    }
}
