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

package com.nestor.interpret.jbasic.profile;

public abstract class RuntimeServiceProvider<T extends Throwable>
{
    public abstract <S> S getRuntimeService(Class<S> serviceClass) throws T;

    @SuppressWarnings("unchecked")
    public <S> S getRuntimeService(String serviceName) throws T
    {
        try
        {
            return (S) getRuntimeService(Class.forName(serviceName));
        } catch (ClassNotFoundException e)
        {
            return null;
        }
    }
}
