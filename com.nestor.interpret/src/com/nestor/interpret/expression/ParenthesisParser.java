/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-12
 ****************************************************************************/

package com.nestor.interpret.expression;

import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;

public final class ParenthesisParser<MATCH extends Value> implements Parser<MATCH>
{
    private final Parser<? extends MATCH> baseParser;

    public ParenthesisParser(Parser<? extends MATCH> baseParser)
    {
        this.baseParser = baseParser;
    }

    public String describe()
    {
        return StandardTokens.LP.describe() + this.baseParser.describe()
                + StandardTokens.RP.describe();
    }

    public MATCH parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        if (scanner.matchAdvance(StandardTokens.LP) != null)
        {
            MATCH match = this.baseParser.parse(scanner, env);
            scanner.required(StandardTokens.RP);
            return match;
        }
        return null;
    }
}
