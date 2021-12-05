package thewall.engine.twilight.scripting;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ScriptAPI {
    static String content;
    static String pluginName;
    static PrintStream systemWriter;

    static {
        try {
            content = Files.readString(Path.of("test.js"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Printer {
        public void print(String string){
            System.out.printf("[%s]: %s%n", pluginName, string);
        }
    }

    public static class Stream extends PrintStream {

        static class Printer extends OutputStream {

            static ArrayList<Character> chars = new ArrayList<>();
            @Override
            public void write(int b) throws IOException {
                /*
                systemWriter.println(b);
                if(b == 0x0A){
                    char[] japierdole = new char[chars.size() + 1];
                    int index = 0;
                    for(Character character : chars){
                        if(character == 0){
                            continue;
                        }
                        japierdole[++index] = character;
                    }

                    int[] japierdoleraw = new int[japierdole.length + 1];
                    index = 0;
                    for(char jprdl : japierdole){
                        japierdoleraw[++index] = jprdl;
                    }

                    systemWriter.println(Arrays.toString(japierdoleraw));

                    systemWriter.println(String.valueOf(japierdole));
                    return;
                }
                chars.add((char) b);

                 */
                systemWriter.print((char) b);
                if(b == 0x0A){

                }
            }
        }

        public Stream() {
            super(new Printer());
        }
    }

    public static void main(String[] args) throws IOException {
        systemWriter = System.out;
        System.setOut(new Stream());
        V8 v8 = V8.createV8Runtime();
        V8Object v8Console = new V8Object(v8);
        v8.add("console", v8Console);
        v8Console.registerJavaMethod(new Printer(), "print", "log", new Class<?>[]{String.class});
        v8.executeScript(content);
        pluginName = ((V8Object) v8.get("name")).getString("pluginName");
        System.out.println("1234");
        /*
        try {
            v8.executeJSFunction("init");
            v8.executeJSFunction("onEnable");
            v8.executeJSFunction("onDisable");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

         */
    }

}
