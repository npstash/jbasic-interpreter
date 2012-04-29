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

package com.nestor.interpret.parse;

public final class ParserDelegate<M extends Match> implements Parser<M>
{
    private Parser<? extends M> delegate;

    public ParserDelegate()
    {
        /* Empty */
    }

    public String describe()
    {
        return this.delegate.describe();
    }

    public Parser<? extends M> getDelegate()
    {
        return this.delegate;
    }

    public M parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.delegate.parse(scanner, env);
    }

    public void setDelegate(Parser<? extends M> delegate)
    {
        this.delegate = delegate;
    }

}
