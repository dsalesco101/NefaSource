package ethos.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

public abstract class AttributesSerializable extends Attributes {

    public static <T extends AttributesSerializable> T getFromFile(String file, T attributesSerializable) throws IOException {
        if (new File(file).exists()) {
            T attributes = new Gson().fromJson(FileUtils.readFileToString(new File(file)), attributesSerializable.getType());
            if (attributes == null) {
                return attributesSerializable;
            }
            attributes.setFile(file);
            return attributes;
        } else {
            return attributesSerializable;
        }
    }

    public abstract Type getType();

    private transient String file;

    public AttributesSerializable(String file) {
        this.file = file;
    }

    void setFile(String file) {
        this.file = file;
    }

    public void write() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        try {
            throw new OperationNotSupportedException("Not supported by serializeable attributes.");
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void remove(Object key) {
        try {
            throw new OperationNotSupportedException("Not supported by serializeable attributes.");
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setInt(String key, int set) {
        super.setInt(key, set);
        write();
    }

    public void removeInt(String key) {
        super.removeInt(key);
        write();
    }

    public void removeDouble(String key) {
        super.removeDouble(key);
        write();
    }

    public void setBoolean(String key, boolean set) {
        super.setBoolean(key, set);
        write();
    }

    public void removeBoolean(String key) {
        super.removeBoolean(key);
        write();
    }

    public void setLong(String key, long set) {
        super.setLong(key, set);
        write();
    }

    public void removeLong(String key) {
        super.removeLong(key);
        write();
    }

    public void setString(String key, String set) {
        super.setString(key, set);
        write();
    }

    public void removeString(String key) {
        super.removeString(key);
        write();
    }

    public void setList(String key, List list) {
        super.setList(key, list);
        write();
    }

}
