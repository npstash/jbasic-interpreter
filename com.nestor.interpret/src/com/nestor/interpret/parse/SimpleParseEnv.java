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

package com.nestor.interpret.parse;

import java.io.PrintStream;

public final class SimpleParseEnv implements ParseEnv
{
    private final PrintStream err;

    private final PrintStream log;

    public SimpleParseEnv()
    {
        this(System.out, System.err);
    }

    public SimpleParseEnv(PrintStream log, PrintStream err)
    {
        this.log = log;
        this.err = err;
    }

    public void error(String msg)
    {
        this.err.println("ERROR: " + msg);
    }

    public void log(String msg)
    {
        this.log.println(msg);
    }

    public void warning(String msg)
    {
        this.log.println("WARN: " + msg);
    }
}
