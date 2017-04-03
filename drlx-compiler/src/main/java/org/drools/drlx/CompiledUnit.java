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

import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.RuleUnitExecutor;

public class CompiledUnit {

    private final String packageName;
    private final String unitName;
    private final KieContainer kieContainer;

    public CompiledUnit( String packageName, String unitName, KieContainer kieContainer ) {
        this.packageName = packageName;
        this.unitName = unitName;
        this.kieContainer = kieContainer;
    }

    public RuleUnitExecutor createExecutor() {
        KieBase kbase = kieContainer.getKieBase();
        return RuleUnitExecutor.create().bind( kbase );
    }

    public String getName() {
        return packageName + "." + unitName;
    }

    public ClassLoader getClassLoader() {
        return kieContainer.getClassLoader();
    }

    public Constructor<?> getConstructorFor(String className, Class<?>... parameterTypes) {
        try {
            Class<?> domainClass = Class.forName( getName() + "$" + className, true, getClassLoader() );
            return domainClass.getConstructor( parameterTypes );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }
}
