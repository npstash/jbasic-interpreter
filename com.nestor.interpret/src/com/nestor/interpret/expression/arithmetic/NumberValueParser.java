/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-31
 ****************************************************************************/

package com.nestor.interpret.expression.arithmetic;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.type.DOUBLE;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.NUMBER;

public final class NumberValueParser implements Parser<Value<? extends NUMBER>>
{
    public static final NumberValueParser PARSER = new NumberValueParser();

    public static final RegexToken DOUBLE_TOKEN = new RegexToken("\\d+\\.\\d+");

    public static final RegexToken INTEGER_TOKEN = new RegexToken("\\d+");

    private NumberValueParser()
    {
        /* Empty */
    }

    public NUMBER<? extends NUMBER> parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        String lex = scanner.matchAdvance(DOUBLE_TOKEN);
        if (lex == null)
        {
            lex = scanner.matchAdvance(INTEGER_TOKEN);
            if (lex == null)
                return null;
            return new INTEGER(Integer.parseInt(lex));
        }
        return new DOUBLE(Double.parseDouble(lex));
    }

    public String describe()
    {
        return "number";
    }
}
