package dev.thebjoredcraft.railway.redis.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import java.io.OutputStreamWriter

class RedisRepositoryProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver.getSymbolsWithAnnotation("dev.thebjoredcraft.railway.redis.annotations.RedisRepository")
        val ret = symbols.filter { !it.validate() }.toList()

        for (symbol in symbols.filterIsInstance<KSClassDeclaration>()) {
            generateRepositoryImpl(symbol)
        }
        return ret
    }

    private fun generateRepositoryImpl(classDecl: KSClassDeclaration) {
        val packageName = classDecl.packageName.asString()
        val interfaceName = classDecl.simpleName.asString()
        val implName = "${interfaceName}Impl"

        val repoAnnotation = classDecl.annotations.first {
            it.shortName.asString() == "RedisRepository"
        }
        val repoPrefix = repoAnnotation.arguments.first().value.toString()

        val file = codeGenerator.createNewFile(
            Dependencies(true, classDecl.containingFile!!),
            packageName,
            implName
        )

        OutputStreamWriter(file, Charsets.UTF_8).use { writer ->
            writer.write("package $packageName\n\n")
            writer.write("import dev.thebjoredcraft.railway.redis.runtime.*\n")
            writer.write("import dev.thebjoredcraft.railway.redis.api.*\n")
            writer.write("import org.redisson.api.RedissonReactiveClient\n")
            writer.write("import java.util.UUID\n\n")

            writer.write("class $implName(\n")
            writer.write("    private val client: RedissonReactiveClient\n")
            writer.write(") : $interfaceName {\n\n")

            for (func in classDecl.declarations.filterIsInstance<KSFunctionDeclaration>()) {
                val funcName = func.simpleName.asString()
                val parameters = func.parameters

                val annotation = func.annotations.firstOrNull {
                    it.shortName.asString().startsWith("Redis")
                } ?: continue

                val annotationName = annotation.shortName.asString()
                val keyTemplateArg = annotation.arguments.first().value.toString()

                var resolvedKeyExpr: String
                var template = keyTemplateArg
                for (param in parameters) {
                    val pName = param.name?.asString() ?: continue
                    template = template.replace("{$pName}", "\$$pName")
                }
                resolvedKeyExpr = if (repoPrefix.isNotEmpty()) {
                    "\"$repoPrefix:$template\""
                } else {
                    "\"$template\""
                }

                val paramListStr = parameters.joinToString(", ") { p ->
                    val pName = p.name?.asString()
                    val pType = p.type.resolve().toTypeName()
                    "$pName: $pType"
                }

                writer.write("    override fun $funcName($paramListStr) {\n")

                when (annotationName) {
                    "RedisBucket" -> {
                        writer.write("        val key = $resolvedKeyExpr\n")
                        writer.write("        val bucket = client.getBucket<Any>(key)\n")
                        writer.write("        return InternalRedisValue(bucket as org.redisson.api.RBucketReactive<Any>)\n")
                    }

                    "RedisMap" -> {
                        writer.write("        val key = $resolvedKeyExpr\n")
                        writer.write("        val map = client.getMap<Any, Any>(key)\n")
                        writer.write("        return InternalRedisMap(map as org.redisson.api.RMapReactive<Any, Any>)\n")
                    }

                    "RedisSet" -> {
                        writer.write("        val key = $resolvedKeyExpr\n")
                        writer.write("        val set = client.getSet<Any>(key)\n")
                        writer.write("        return InternalRedisSet(set as org.redisson.api.RSetReactive<Any>)\n")
                    }

                    "RedisQueue" -> {
                        writer.write("        val key = $resolvedKeyExpr\n")
                        writer.write("        val queue = client.getQueue<Any>(key)\n")
                        writer.write("        return InternalRedisQueue(queue as org.redisson.api.RQueueReactive<Any>)\n")
                    }

                    "RedisStream" -> {
                        writer.write("        val key = $resolvedKeyExpr\n")
                        writer.write("        val stream = client.getStream<String, Any>(key)\n")
                        writer.write("        return InternalRedisStream(stream as org.redisson.api.RStreamReactive<String, Any>)\n")
                    }

                    else -> {
                        logger.error("Unsupported annotation $annotationName", func)
                    }
                }
                writer.write("    }\n\n")
            }

            writer.write("}\n")
        }
    }

    private fun KSType.toTypeName(): String {
        return this.declaration.qualifiedName?.asString() ?: "Any"
    }
}

class RedisRepositoryProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return RedisRepositoryProcessor(environment.codeGenerator, environment.logger)
    }
}