# Issues with logback logstash encoder
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


## Answer
We found the problem. To help others, who come across this question, I will explain the problem and the solution here.

The log messages were successfully transmitted to logstash and elasticsearch. So there were no errors in those logs. The reason, that we could not find the log entries was the field for the "payload". We have multiple services and one of them was sending an object instead of a string. That lead to a mapping conflict and the log messages could not be accessed. 

The solution for us was to define an explicit mapping and to introduce a naming convention. Now we have a text field payload and an object field payloadAsObject. This convention is used in all of our services.

The problematic log entries were far enough in the past, so we could delete the old index and solve the conflict. But there are possibilities to reindex the data without downtime. For example this one: https://medium.com/craftsmenltd/rebuild-elasticsearch-index-without-downtime-168363829ea4

