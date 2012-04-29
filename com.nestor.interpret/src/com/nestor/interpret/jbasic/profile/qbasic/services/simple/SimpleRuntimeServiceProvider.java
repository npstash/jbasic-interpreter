/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 4 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.services.simple;

import com.nestor.interpret.jbasic.profile.RuntimeServiceProvider;
import com.nestor.interpret.jbasic.profile.qbasic.parse.GraphicServices;
import com.nestor.interpret.jbasic.profile.qbasic.parse.TextServices;

public class SimpleRuntimeServiceProvider extends RuntimeServiceProvider<RuntimeException>
{
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRuntimeService(Class<T> serviceClass)
    {
        if (serviceClass.equals(TextServices.class))
            return (T) new SimpleTextServices();
        else if (serviceClass.equals(GraphicServices.class))
            return (T) new AWTGraphicServices();
        return null;
    }
}
