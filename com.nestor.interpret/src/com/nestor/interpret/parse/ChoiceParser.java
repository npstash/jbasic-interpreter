/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-01
 ****************************************************************************/

package com.nestor.interpret.parse;

import java.util.ArrayList;

public final class ChoiceParser<M extends Match> implements Parser<M>
{
    private final String customDescription;

    private final ArrayList<Parser<? extends M>> options = new ArrayList<Parser<? extends M>>();

    public ChoiceParser()
    {
        this.customDescription = null;
    }

    public ChoiceParser(String customDescription)
    {
        this.customDescription = customDescription;
    }

    public void addParser(Parser<? extends M> parser)
    {
        this.options.add(parser);
    }

    public final String describe()
    {
        if (this.customDescription != null)
            return this.customDescription;
        String res = "";
        boolean first = true;
        for (Parser<? extends M> option : this.options)
        {
            if (!first)
                res += " or ";
            else
                first = false;
            res += option.toString();
        }
        return res;
    }

    public final M parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        for (Parser<? extends M> parser : this.options)
        {
            M match = parser.parse(scanner, env);
            if (match != null)
                return match;
        }
        return null;
    }
}
