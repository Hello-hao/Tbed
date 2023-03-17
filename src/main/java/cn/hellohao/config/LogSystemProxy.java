package cn.hellohao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.PrintStream;
import java.util.function.Consumer;

@Configuration
public class LogSystemProxy {

    private final static Logger log = LoggerFactory.getLogger("proxy.system.log");

    @PostConstruct
    public void initProxy(){
        log.debug("LogSystemProxy init .....");
        System.setOut(getLoggerProxy(StdType.OUT));
        System.setErr(getLoggerProxy(StdType.ERR));
    }


    private enum StdType{
        OUT(System.out, log::info),
        ERR(System.err, log::error),
        ;
        PrintStream stream;
        Consumer<String> consumer;
        StdType(PrintStream stream,Consumer<String> consumer){
            this.stream = stream;
            this.consumer = consumer;
        }
    }

    private PrintStream getLoggerProxy(StdType stdType){
        return new PrintStream(stdType.stream){
            @Override
            public void print(String s) {
                stdType.stream.print(s);
                stdType.consumer.accept(s);
            }
        };
    }
}