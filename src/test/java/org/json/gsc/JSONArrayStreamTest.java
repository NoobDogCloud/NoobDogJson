package org.json.gsc;

import junit.framework.TestCase;

import java.io.File;

public class JSONArrayStreamTest extends TestCase {
    public void testAdd() {
        File file = new File("jsonArrayTest.json");
        if (file.exists()) {
            file.delete();
        }
        // 创建
        JSONArrayStream<Object> stream = new JSONArrayStream<>(file);
        try {
            stream.addJson(jsonStream -> {
                jsonStream.put("name", "John");
                jsonStream.put("age", 30);
            });
            stream.addJson(jsonStream -> {
                jsonStream.put("name", "Mary");
                jsonStream.put("age", 25);
            });
            stream.addJson(jsonStream -> {
                jsonStream.put("name", "Peter");
                jsonStream.put("age", 35);
            });
        } finally {
            stream.close();
        }
        try {
            String s = stream.toJSONString();
            System.out.println(s);
            assertEquals("[{\"name\":\"John\",\"age\":30},{\"name\":\"Mary\",\"age\":25},{\"name\":\"Peter\",\"age\":35}]", s);
        } finally {
            stream.close();
        }
        // 补充
        try {
            stream.add("_foo2");
            stream.add("_foo2");
            stream.<String>addJsonArray(jsonStream -> jsonStream.add("_foo").add("bar33333"));
            stream.add(312);
        } finally {
            stream.close();
        }
        try {
            String s = JSONArray.toJSONArray(stream.toJSONString()).toString();
            System.out.println(s);
            assertEquals("[{\"name\":\"John\",\"age\":30},{\"name\":\"Mary\",\"age\":25},{\"name\":\"Peter\",\"age\":35},\"_foo2\",\"_foo2\",[\"_foo\",\"bar33333\"],312]", s);
        } finally {
            stream.close();
        }
        // 查找
        try {
            String s = stream.getString(4);
            System.out.println(s);
            assertEquals("_foo2", s);
        } finally {
            stream.close();
        }
        // 遍历
        try {
            stream.forEach(System.out::println);
        } finally {
            stream.close();
        }
        // 打印 stream
        try {
            String s = stream.getJsonStream(0).getString("name");
            System.out.println("{dict}" + s);
            assertEquals("John", s);
        } finally {
            stream.close();
        }
        try {
            String s = stream.getJsonArrayStream(5).getString(1);
            System.out.println("{list}" + s);
            assertEquals("bar33333", s);
        } finally {
            stream.close();
        }
    }
}
