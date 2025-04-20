package com.example.Job.functions;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;


import org.hibernate.query.sqm.function.SqmFunctionRegistry;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

// Register the Postgres FTS functionality
public class PostgreSQLFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {

        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        TypeConfiguration typeConfiguration = functionContributions.getTypeConfiguration();


        functionRegistry.registerPattern(
                "fts_match",
                "(?1 @@ plainto_tsquery('pg_catalog.simple', unaccent(?2)))",
                typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)

        );

        functionRegistry.registerPattern(
                "fts_rank",
                "(ts_rank_cd(?1, plainto_tsquery('pg_catalog.simple', unaccent(?2))))",
                typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.FLOAT)
        );

        functionRegistry.registerPattern(
                "fts_or_query",
                "?1 @@ replace(plainto_tsquery('pg_catalog.simple', unaccent(?2))::text, '&', '|')::tsquery",
                typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.OBJECT_TYPE) // or TSQUERY if you have a mapped type
        );

    }
}

