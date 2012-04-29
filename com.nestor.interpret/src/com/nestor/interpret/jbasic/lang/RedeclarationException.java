/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-01
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.shared.Description;

public class RedeclarationException extends DeclarationException
{
    private static final long serialVersionUID = 4174739255130017934L;

    public RedeclarationException(String name, Description redefined)
    {
        super("Identifier " + name + " causes redeclaration of " + redefined.describe());
    }
}
