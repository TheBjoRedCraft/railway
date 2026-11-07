package dev.thebjoredcraft.railway.config.serializer

import io.leangen.geantyref.GenericTypeReflector
import io.leangen.geantyref.TypeToken
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.util.EnumLookup
import java.lang.reflect.AnnotatedType
import java.util.function.Predicate

internal object EnumValueSerializer :
    ScalarSerializer.Annotated<Enum<*>>(object : TypeToken<Enum<*>>() {}) {
    override fun deserialize(type: AnnotatedType, obj: Any): Enum<*> {
        val constant = obj.toString()
        val typeClass = GenericTypeReflector.erase(type.type).asSubclass(Enum::class.java)

        var foundEnum = EnumLookup.lookupEnum(typeClass, constant)
        if (foundEnum == null) {
            foundEnum = EnumLookup.lookupEnum(typeClass, constant.replace("_", "-"))
        }
        if (foundEnum == null) {
            val joinedEnumOptions = typeClass.enumConstants.joinToString(limit = 10)
            throw SerializationException(
                type.type,
                "Invalid enum constant '$constant' for ${typeClass.simpleName}, expected one of: [$joinedEnumOptions]"
            )
        }

        return foundEnum
    }

    override fun serialize(
        type: AnnotatedType,
        item: Enum<*>,
        typeSupported: Predicate<Class<*>>
    ): Any {
        return item.name
    }
}