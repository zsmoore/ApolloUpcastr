package com.zachary_moore.apolloupcastr.plugin

import com.apollographql.apollo3.ast.GQLFieldDefinition
import com.apollographql.apollo3.ast.GQLObjectTypeDefinition
import com.apollographql.apollo3.ast.GQLTypeDefinition
import com.apollographql.apollo3.compiler.CodegenSchema
import com.apollographql.apollo3.compiler.Plugin
import com.apollographql.apollo3.compiler.Transform
import com.apollographql.apollo3.compiler.codegen.SchemaAndOperationsLayout
import com.apollographql.apollo3.compiler.codegen.kotlin.KotlinOutput
import com.squareup.javapoet.TypeName
import com.squareup.kotlinpoet.*

const val PACKAGE = "com.zachary_moore.apolloupcastr.hook.interface"

class ApolloUpcastrPlugin : Plugin {

    private lateinit var allTypes: FileSpec

    override fun layout(codegenSchema: CodegenSchema): SchemaAndOperationsLayout {
        allTypes = generateInterfaces(codegenSchema.schema.typeDefinitions)
        return SchemaAndOperationsLayout(
            codegenSchema = codegenSchema,
            packageName = PACKAGE,
            rootPackageName = null,
            useSemanticNaming = null,
            decapitalizeFields = null,
            generatedSchemaName = null
        )
    }

    override fun kotlinOutputTransform(): Transform<KotlinOutput>? {
        return object : Transform<KotlinOutput> {
            override fun transform(input: KotlinOutput): KotlinOutput {
                return KotlinOutput(
                    fileSpecs = enhanceSpecs(input.fileSpecs) + allTypes,
                    codegenMetadata = input.codegenMetadata
                )
            }
        }
    }

    private fun generateInterfaces(allTypes: Map<String, GQLTypeDefinition>): FileSpec {
        val name = ClassName.bestGuess("$PACKAGE.Types")
        val parentType = TypeSpec.classBuilder(name)
        val spec = FileSpec.builder(name)
        allTypes.filter { it.value is GQLObjectTypeDefinition && !it.key.contains("_")}.forEach { entry ->
            val typeSpec = TypeSpec.interfaceBuilder(ClassName.bestGuess(entry.key))
            entry.value.children.forEach { child ->
                if (child is GQLFieldDefinition) {
                    typeSpec.addFunction(
                        FunSpec.builder(child.name)
                            .returns(name.nestedClass(child.name).copy(true))
                            .addCode("return null")
                            .build()
                    )
                }
            }
            parentType.addType(typeSpec.build())
        }
        spec.addType(parentType.build())
        return spec.build()
    }

    private fun enhanceSpecs(fileSpecs: List<FileSpec>): List<FileSpec> {
        return fileSpecs.map { spec ->
            spec
        }
    }
}