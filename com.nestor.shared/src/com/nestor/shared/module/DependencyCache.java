/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 27/07/2006
 ****************************************************************************/

package com.nestor.shared.module;

public interface DependencyCache
{
    public void addDependency(Module to) throws DependencyException;
}
