package ethos;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import ethos.model.AttributesSerializable;

public class ServerAttributes extends AttributesSerializable {

    public ServerAttributes(String file) {
        super(file);
    }

    @Override
    public Type getType() {
        return new TypeToken<ServerAttributes>() {}.getType();
    }
}
