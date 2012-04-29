/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-27
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.jbasic.libs.Maths;
import com.nestor.interpret.jbasic.libs.Numbers;
import com.nestor.interpret.jbasic.libs.Std;
import com.nestor.interpret.jbasic.libs.Strings;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.type.TYPES;

/**
 * {@link ModuleFactory} is responsible for instantiating new module builders ({@link ModuleBuilder}),
 * with all shared basic functionality and data.
 */
public class ModuleFactory
{
    private static void initalizeModule(ModuleBuilder moduleBuilder) throws DeclarationException
    {
        moduleBuilder.addType(TYPES.INTEGER_TYPE);
        moduleBuilder.addType(TYPES.DOUBLE_TYPE);
        moduleBuilder.addType(TYPES.BOOLEAN_TYPE);
        moduleBuilder.addType(TYPES.STRING_TYPE);

        new Maths().load(moduleBuilder);
        new Strings().load(moduleBuilder);
        new Numbers().load(moduleBuilder);
        new Std().load(moduleBuilder);
    }

    public static ModuleBuilder prepareModuleBuilder(String name, Configurator config)
            throws DeclarationException
    {
        ModuleBuilder moduleBuilder = new ModuleBuilder(name, config);
        initalizeModule(moduleBuilder);
        config.prepareModule(moduleBuilder);
        return moduleBuilder;
    }
}
