#Issues with logback logstash encoder
This repository is to demonstrate a problem I have with logback and the logstash encoder. Under certain circumstances
 logmessages are lost.
 
 To reproduce this behaviour I build this minimal example.
 
 ## Configuration
 ### logstash
 Please set the destination of the logstash appender in line 10 in [logback-spring.xml](/src/main/resources/logback-spring.xml) to your logstash server.
 
 Our input logstash server has a very simple input configuration:
 ```
input {
    tcp {
         port => 9000
         codec => json_lines
    }
}
```
 ### netcat
 Start a netcat listening on 8081 with
 ```bash
nc -Lp 8081
 ```

## Run
Just start the application main class with your IDE.
- In the output from the application I see the log messages 1-4.
- In the output from netcat I see the log messages 1-4, too.
- In kibana, that is connected to the elasticsearch cluster behind the logstash server, I see only log messages 1 and 4.

## Questions
- Why are log messages lost?
- Does it have something to do with the JSON string and not using `appendRaw`?
- Do I have to test the content of my message to decide, if I need `append` or `appendRaw`?
