package fr.imt.springforce.configuration.mongodb;

import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

@ReadingConverter
public class BinaryToUUIDConverter implements Converter<Binary, UUID> {
    @Override
    public UUID convert(Binary source) {
        ByteBuffer bb = ByteBuffer.wrap(source.getData());
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}