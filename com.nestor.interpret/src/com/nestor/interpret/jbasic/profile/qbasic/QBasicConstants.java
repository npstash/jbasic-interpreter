/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 6 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic;

import com.nestor.interpret.jbasic.profile.LangConstants;

public class QBasicConstants implements LangConstants
{
    private static QBasicConstants instance = null;

    public static synchronized QBasicConstants getInstance()
    {
        if (instance == null)
            instance = new QBasicConstants();
        return instance;
    }

    public boolean isParenthesisRequiredForCallableArgs()
    {
        return false;
    }
}
