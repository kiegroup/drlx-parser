/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.drlx;

import java.lang.reflect.Constructor;

import org.drools.core.ruleunit.RuleUnitFactory;
import org.junit.Test;
import org.kie.api.runtime.rule.DataSource;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;

import static org.drools.drlx.DrlxCompiler.compile;
import static org.junit.Assert.assertEquals;

public class UnitCompilationTest {

    @Test
    public void test() throws Exception {
        CompiledUnit unit = compile(getClass().getClassLoader().getResourceAsStream( "AdultUnit.java" ));

        RuleUnitExecutor executor = unit.createExecutor();

        Constructor<?> constructor = unit.getConstructorFor( "Person", String.class, int.class );

        DataSource<?> persons = executor.newDataSource( "persons",
                                                         constructor.newInstance( "Mario", 43 ),
                                                         constructor.newInstance( "Marilena", 44 ),
                                                         constructor.newInstance( "Sofia", 5 ) );

        // explicitly create unit
        RuleUnit ruleUnit = new RuleUnitFactory().getOrCreateRuleUnit( unit.getName(), unit.getClassLoader() );

        assertEquals(2, executor.run( ruleUnit ) );
    }
}
