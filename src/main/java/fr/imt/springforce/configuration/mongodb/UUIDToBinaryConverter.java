package fr.imt.springforce.configuration.mongodb;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import java.nio.ByteBuffer;
import java.util.UUID;

@WritingConverter
public class UUIDToBinaryConverter implements Converter<UUID, Binary> {
    @Override
    public Binary convert(UUID source) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(source.getMostSignificantBits());
        bb.putLong(source.getLeastSignificantBits());
        return new Binary(BsonBinarySubType.UUID_STANDARD, bb.array());
    }
}