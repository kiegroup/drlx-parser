package org.mypackage;

import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.DataSource;

public class AdultUnit implements RuleUnit {
    private int adultAge = 0;
    private DataSource<Person> persons;

    public AdultUnit( ) { }

    public AdultUnit( DataSource<Person> persons ) {
        this.persons = persons;
    }

    public DataSource<Person> getPersons() {
        return persons;
    }

    public int getAdultAge() {
        return adultAge;
    }

    rule Adult {
        Person p = /persons[age > 18];
        do {
            System.out.println(p.getName());
        }
    }
}