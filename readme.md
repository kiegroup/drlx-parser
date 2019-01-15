## Java Parser extension to support Drools drlx syntax

This project is a fork of [JavaParser](https://github.com/javaparser/javaparser)
enriching plain Java parsing with Drools drlx syntax.


Instruction to Rebase

- Rebase against tags, not snapshot
- Accept theirs on every pom.xml
- Replace every parent pom from `<groupId>com.github.javaparser</groupId>` to `<groupId>org.drools.javaparser</groupId>`
- MethodCallExpr FieldAccessExpr not final
- fix backReferencesCount in NameExpr
- add back_references_count in ObservableProperty
- fix tokens (change token size, kind etc)