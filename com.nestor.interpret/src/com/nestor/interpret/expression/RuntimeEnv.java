/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.expression;

/**
 * This interface is meant to be subclassed by other interfaces if their instances are to be passed
 * to Expression.evaluate()
 */
public interface RuntimeEnv
{
    public void log(String message);
}
