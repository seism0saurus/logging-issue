#Issues with logback logstash encoder
This repository is to demonstrate a problem I have with logback and the logstash encoder. Under certain circumstances
 logmessages are lost.
 
 To reproduce this behaviour I build this minimal example.
 
 ## Configuration
 ### logstash
 Please set the destination of the logstash appender in line 10 in [logback-spring.xml](/src/main/resources/logback-spring.xml) to your logstash server.
 
 Our input rule is very simple:
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
- In the output from the application I see the log messages 1-5.
- In the output from netcat I see the log messages 1-5, too.
- In kibana, that is connected to the elasticsearch cluster behind the logstash server, I see only log messages 1 and 5.

If I comment out the line 31 in the [LoggingIssueApplication](/src/main/java/de/seism0saurus/loggingissue/LoggingIssueApplication.java) 
(line after the comment) the behaviour changes in an unexpected way.
- In the output from the application I see the log messages 1-5.
- In the output from netcat I see only log message 1.
- In kibana, that is connected to the elasticsearch cluster behind the logstash server, I see only log messages 1.

## Questions
- Why are log messages lost in the first case?
- What happens in the second case, if I remove log message 3. Why does it affect even message 2, that should be
 logged before message 3?
