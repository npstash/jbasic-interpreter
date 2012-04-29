/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-13
 ****************************************************************************/

package com.nestor.interpret.expression.bool;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.RegexToken;
import com.nestor.interpret.type.BOOLEAN;

public final class BooleanValueParser implements Parser<Value<BOOLEAN>>
{
    public static final RegexToken BOOLEAN_TOKEN = new RegexToken("(true)|(false)", false);

    public static BooleanValueParser PARSER = new BooleanValueParser();

    private BooleanValueParser()
    {
        /* Empty */
    }

    public String describe()
    {
        return "bool value";
    }

    public BOOLEAN parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        String lexeme = scanner.matchAdvance(BOOLEAN_TOKEN);
        if (lexeme != null)
            return new BOOLEAN(Boolean.parseBoolean(lexeme));
        return null;
    }
}
