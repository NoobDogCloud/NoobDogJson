package org.json.gsc;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class JSONObjectStreamTest extends TestCase {
    public void testPut() throws IOException {
        File file = new File("jsonTest.json");
        if (file.exists()) {
            file.delete();
        }
        // 创建
        JSONObjectStream stream = new JSONObjectStream(file);
        try {
            stream.put("foo2", "bar");
            stream.putJson("bar2", jsonStream -> jsonStream.put("foo", "bar22222"));
            stream.put("baz2", 312);
        } finally {
            stream.close();
        }
        try {
            String s = stream.toJSONString();
            System.out.println(s);
            assertEquals("{\"foo2\":\"bar\",\"bar2\":{\"foo\":\"bar22222\"},\"baz2\":312}", s);
        } finally {
            stream.close();
        }
        // 补充
        try {
            stream.put("_foo2", "bar");
            stream.put("_foo2", "bar");
            stream.<String>putJsonArray("_bar2", jsonStream -> jsonStream.add("_foo").add("bar22222"));
            stream.put("_baz2", 312);
        } finally {
            stream.close();
        }
        try {
            String s = JSONObject.toJSON(stream.toJSONString()).toString();
            System.out.println(s);
            assertEquals("{\"bar2\":{\"foo\":\"bar22222\"},\"_foo2\":\"bar\",\"_bar2\":[\"_foo\",\"bar22222\"],\"baz2\":312,\"foo2\":\"bar\",\"_baz2\":312}", s);
        } finally {
            stream.close();
        }
        // 查找
        try {
            String s = stream.getString("_foo2");
            System.out.println(s);
            assertEquals("bar", s);
        } finally {
            stream.close();
        }
        // 遍历
        try {
            stream.forEach((key, value) -> System.out.println(key + ":" + value));
        } finally {
            stream.close();
        }
        // 打印 stream
        try {
            String s = stream.getJsonStream("bar2").getString("foo");
            System.out.println("{dict}" + s);
            assertEquals("bar22222", s);
        } finally {
            stream.close();
        }
        try {
            String s = stream.getJsonArrayStream("_bar2").getString(1);
            System.out.println("{list}" + s);
            assertEquals("bar22222", s);
        } finally {
            stream.close();
        }
    }
}
