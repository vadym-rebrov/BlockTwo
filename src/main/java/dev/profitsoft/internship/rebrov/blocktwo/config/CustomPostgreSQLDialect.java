package dev.profitsoft.internship.rebrov.blocktwo.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.type.StandardBasicTypes;

// Add fulltext index search
public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        functionContributions
                .getFunctionRegistry()
                .registerPattern(
                        "fts",
                        "to_tsvector(?1, ?2) @@ to_tsquery(?1, ?3)",
                        functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)
                );
    }
}
