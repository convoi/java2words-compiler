package ce.payback.rainbow.writer;

import java.io.Writer;

public class JsonWriter {

  public static JsonWriter of(Writer writer) {
    return new JsonWriter(writer);
  }

  private final com.google.gson.stream.JsonWriter stream;

  private JsonWriter(Writer writer) {
    this.stream = new com.google.gson.stream.JsonWriter(writer);
    this.stream.setSerializeNulls(false);
    this.stream.setLenient(false);
  }

  // for unit testing
  JsonWriter(com.google.gson.stream.JsonWriter stream) {
    this.stream = stream;
  }

  /**
   * Begins encoding a new array. Each call to this method must be paired with
   * a call to {@link #endArray}. Output is <code>[</code>.
   *
   */
  public JsonWriter beginArray() throws WriterException {
    try {
      stream.beginArray();
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Ends encoding the current array. Output is <code>]</code>.
   */
  public JsonWriter endArray() throws WriterException {
    try {
      stream.endArray();
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Begins encoding a new object. Each call to this method must be paired
   * with a call to {@link #endObject}. Output is <code>{</code>.
   */
  public JsonWriter beginObject() throws WriterException {
    try {
      stream.beginObject();
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Ends encoding the current object. Output is <code>}</code>.
   */
  public JsonWriter endObject() throws WriterException {
    try {
      stream.endObject();
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Encodes the property name. Output is <code>"theName":</code>.
   */
  public JsonWriter name(String name) throws WriterException {
    try {
      stream.name(name);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Encodes {@code value}. Output is <code>true</code> or <code>false</code>.
   */
  public JsonWriter value(boolean value) throws WriterException {
    try {
      stream.value(value);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   */
  public JsonWriter value(double value) throws WriterException {
    try {
      stream.value(value);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   */
  public JsonWriter value(String value) throws WriterException {
    try {
      stream.value(value);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   */
  public JsonWriter value(long value) throws WriterException {
    try {
      stream.value(value);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   */
  public JsonWriter value(Number value) throws WriterException {
    try {
      stream.value(value);
      return this;
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  /**
   * Encodes the property name and value. Output is for example <code>"theName":123</code>.
   */
  public JsonWriter prop(String name, Number value) throws WriterException {
    return name(name).value(value);
  }

  /**
   */
  public JsonWriter prop(String name, String value) throws WriterException {
    return name(name).value(value);
  }

  /**
   */
  public JsonWriter prop(String name, boolean value) throws WriterException {
    return name(name).value(value);
  }

  /**
   */
  public JsonWriter prop(String name, long value) throws WriterException {
    return name(name).value(value);
  }

  /**
   */
  public JsonWriter prop(String name, double value) throws WriterException {
    return name(name).value(value);
  }

  /**
   */
  public void close() throws WriterException {
    try {
      stream.close();
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  private IllegalStateException rethrow(Exception e) throws WriterException {
    // stacktrace is not helpful
    throw new WriterException("Fail to write JSON: " + e.getMessage());
  }
}
