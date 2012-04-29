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

package com.nestor.interpret.expression;

import com.nestor.interpret.parse.Match;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.RefinedParser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public abstract class RefinedValueParser<VAL extends DATA> extends RefinedParser<Value>
{
    private final TYPE<? extends VAL> valueClass;

    public RefinedValueParser(TYPE<? extends VAL> valueClass, Parser<? extends Value> subparser)
    {
        super(Value.class, subparser);
        this.valueClass = valueClass;
    }

    protected final Value<VAL> castIfValueCompatible(Match match)
    {
        Value valueMatch = super.castIfMatchCompatible(match);
        if (valueMatch == null)
            return null;
        return castIfValueCompatible(valueMatch);
    }

    @SuppressWarnings("unchecked")
    protected final Value<VAL> castIfValueCompatible(Value valueMatch)
    {
        if (valueMatch == null)
            return null;
        if (this.valueClass.isAssignableFrom(valueMatch.getValueType()))
            return valueMatch;
        return null;
    }

    public final TYPE<? extends VAL> getValueClass()
    {
        return this.valueClass;
    }

    protected final Value<VAL> requireCompatibleValue(Scanner scanner, ParseEnv env)
            throws ParseException
    {
        ParseException failure = scanner.expected(this.valueClass.describe());
        Value valueMatch = super.seekCompatibleMatch(scanner, env);
        Value<VAL> vValueMatch = castIfValueCompatible(valueMatch);
        if (vValueMatch == null)
            throw failure;
        return vValueMatch;
    }
}
