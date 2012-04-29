/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-09
 ****************************************************************************/

package com.nestor.interpret.parse;

/**
 * @param <M>
 *            describes what is the most general class that will be checked and parsed by this
 *            wrapper.
 */
public abstract class RefinedParser<M extends Match> implements Parser<M>
{
    private final Class<M> matchClass;

    private Parser subparser;

    public RefinedParser(Class<M> matchClass, Parser subparser)
    {
        this.matchClass = matchClass;
        this.subparser = subparser;
    }

    @SuppressWarnings("unchecked")
    protected final M castIfMatchCompatible(Match match)
    {
        if (match == null)
            return null;
        if (this.matchClass.isAssignableFrom(match.getClass()))
            return (M) match;
        return null;
    }

    protected final void changeSubparser(Parser subparser1)
    {
        this.subparser = subparser1;
    }

    public final Class<M> getMatchClass()
    {
        return this.matchClass;
    }

    protected final Match matchAny(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.subparser.parse(scanner, env);
    }

    protected final M seekCompatibleMatch(Scanner scanner, ParseEnv env) throws ParseException
    {
        Match match = matchAny(scanner, env);
        M mMatch = castIfMatchCompatible(match);
        if (mMatch == null)
            return null;
        return mMatch;
    }

    @Deprecated
    protected final M requireCompatibleMatch(Scanner scanner, ParseEnv env) throws ParseException
    {
        ParseException failure = scanner.expected(this.matchClass.getSimpleName());
        Match match = matchAny(scanner, env);
        M mMatch = castIfMatchCompatible(match);
        if (mMatch == null)
            throw failure;
        return mMatch;
    }
}
