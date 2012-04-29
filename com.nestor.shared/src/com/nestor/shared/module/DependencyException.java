/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-09
 ****************************************************************************/

package com.nestor.shared.module;

public class DependencyException extends RuntimeException
{
    private static final long serialVersionUID = 2025888104519801751L;

    protected DependencyException(Module c1)
    {
        super("Components " + c1 + " causes dependency loop.");
    }

    public DependencyException(String message)
    {
        super(message);
    }
}