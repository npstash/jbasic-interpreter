/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 8 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.arithmetic.NumberValueParser;
import com.nestor.interpret.expression.bool.BooleanValueParser;
import com.nestor.interpret.expression.string.StringValueParser;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;

public final class JBasicValueParser implements Parser<Value<? extends DATA>>
{
    private final ChoiceParser<Value<? extends DATA>> parser;

    public JBasicValueParser(Configurator configurator)
    {
        this.parser = new ChoiceParser<Value<? extends DATA>>();
        this.parser.addParser(NumberValueParser.PARSER);
        this.parser.addParser(StringValueParser.PARSER);
        this.parser.addParser(BooleanValueParser.PARSER);
        configurator.addTypeValueParsers(this.parser);
    }

    public String describe()
    {
        return this.parser.describe();
    }

    public Value<? extends DATA> parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.parser.parse(scanner, env);
    }
}
