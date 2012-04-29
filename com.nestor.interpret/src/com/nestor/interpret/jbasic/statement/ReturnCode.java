/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-29
 ****************************************************************************/

package com.nestor.interpret.jbasic.statement;

import com.nestor.shared.Description;

public class ReturnCode implements Description
{
    public static class Error extends ReturnCode
    {
        private String msg;

        Error(String msg)
        {
            super("ERROR");
            this.msg = msg;
        }

        public String message()
        {
            return this.msg;
        }
    }

    public static final ReturnCode EXIT_DO = new ReturnCode("EXIT DO");

    public static final ReturnCode LOOP_DOWHILE = new ReturnCode("LOOP");

    public static final ReturnCode EXIT_FOR = new ReturnCode("EXIT FOR");

    public static final ReturnCode NEXT_FOR = new ReturnCode("NEXT FOR");

    public static final ReturnCode END = new ReturnCode("END");

    public static final ReturnCode SUCCESS = new ReturnCode("OK");

    private final String desc;

    public static final ReturnCode ERROR(String message)
    {
        return new Error(message);
    }

    protected ReturnCode(String desc)
    {
        this.desc = desc;
    }

    public String describe()
    {
        return this.desc;
    }
}