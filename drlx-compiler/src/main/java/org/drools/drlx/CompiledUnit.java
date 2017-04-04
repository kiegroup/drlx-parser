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
import java.util.List;

import org.drools.core.ruleunit.RuleUnitFactory;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;

import static java.util.Arrays.asList;

public class CompiledUnit {

    private final List<String> unitNames;
    private final KieContainer kieContainer;

    public CompiledUnit( KieContainer kieContainer, String unitName ) {
        this(kieContainer, asList(unitName));
    }

    public CompiledUnit( KieContainer kieContainer, List<String> unitNames ) {
        this.kieContainer = kieContainer;
        this.unitNames = unitNames;
    }

    public RuleUnitExecutor createExecutor() {
        KieBase kbase = kieContainer.getKieBase();
        return RuleUnitExecutor.create().bind( kbase );
    }

    public String getName() {
        return unitNames.get(0);
    }

    private ClassLoader getClassLoader() {
        return kieContainer.getClassLoader();
    }

    public RuleUnit getOrCreateRuleUnit() {
        return new RuleUnitFactory().getOrCreateRuleUnit( getName(), getClassLoader() );
    }

    public Constructor<?> getConstructorFor(String className, Class<?>... parameterTypes) {
        try {
            Class<?> domainClass = Class.forName( className, true, getClassLoader() );
            return domainClass.getConstructor( parameterTypes );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }
}
