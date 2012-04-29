/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-19
 ****************************************************************************/

package com.nestor.interpret.type;

public class PromotionNotSupportedException extends Exception
{
    private static final long serialVersionUID = 694858164853982127L;

    public PromotionNotSupportedException(TYPE from, TYPE to)
    {
        super("Value of type " + from + " cannot be promoted to " + to);
    }
}
