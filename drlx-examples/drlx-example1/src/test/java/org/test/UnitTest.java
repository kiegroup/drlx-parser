package org.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.DataSource;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unit1.AdultUnit;
import org.unit1.AdultUnit.Person;

public class UnitTest {
    public static final Logger LOG = LoggerFactory.getLogger(UnitTest.class);
    
    @Test
    public void testUnit() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        LOG.info("Creating kieBase");
        KieBase kieBase = kContainer.getKieBase();

        LOG.info("There should be rules: ");
        for ( KiePackage kp : kieBase.getKiePackages() ) {
            for (Rule rule : kp.getRules()) {
                LOG.info("kp " + kp + " rule " + rule.getName());
            }
        }

        LOG.info("Creating kieSession");
        DataSource<Person> persons = DataSource.create( new Person( "Mario", 43 ),
                                                        new Person( "Marilena", 44 ),
                                                        new Person( "Sofia", 5 ) );

        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kieBase );
        
        RuleUnit ruleUnit = new AdultUnit( persons );

        assertEquals(2, executor.run( ruleUnit ) );
    }
}
