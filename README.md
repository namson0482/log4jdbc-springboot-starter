Log4jdbc Spring Boot Starter
============================

[![DevOps By Rultor.com](http://www.rultor.com/b/candrews/log4jdbc-spring-boot-starter)](http://www.rultor.com/p/candrews/log4jdbc-spring-boot-starter)
[![Build Status](https://travis-ci.org/candrews/log4jdbc-spring-boot-starter.svg?branch=master)](https://travis-ci.org/candrews/log4jdbc-spring-boot-starter)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.integralblue/log4jdbc-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.integralblue/log4jdbc-spring-boot-starter)
[![Reference Status](https://www.versioneye.com/java/com.integralblue:log4jdbc-spring-boot-starter/reference_badge.svg?style=flat-square)](https://www.versioneye.com/java/com.integralblue:log4jdbc-spring-boot-starter/references)
[![Dependency Status](https://www.versioneye.com/java/com.integralblue:log4jdbc-spring-boot-starter/badge?style=flat-square)](https://www.versioneye.com/java/com.integralblue:log4jdbc-spring-boot-starter)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.integralblue/log4jdbc-spring-boot-starter/badge.svg)](http://www.javadoc.io/doc/com.integralblue/log4jdbc-spring-boot-starter)

The Log4jdbc Spring Boot Starter facilitates the quick and easy use of [log4jdbc](http://log4jdbc.brunorozendo.com/) in Spring Boot projects.

Log4jdbc is particularly handy as it can log SQL that is ready to run. Instead of logging SQL with '?' where parameter values need to be inserted (like, for example, what `spring.jpa.properties.hibernate.show_sql=true` does), log4jdbc can log SQL with those place holders substituted with their actual values. So instead of `select name from User where id = ?` the log will say `select name from User where id = 5`.

Quick Start
===========
* **Minimum requirements** — You'll need Java 1.8+ and Spring Boot 2.1+.
* **Download** — Depend on this library using, for example, Maven:
```xml
  <dependency>
    <groupId>com.integralblue</groupId>
    <artifactId>log4jdbc-spring-boot-starter</artifactId>
    <version>[INSERT VERSION HERE]</version>
  </dependency>
```
* **Configure** — In application.properties, enable a logger (for example, `logging.level.jdbc.sqlonly=ERROR`). See [Loggers](#loggers) for details.

Configuration
=============
Use application.properties to configure log4jdbc. There is [Spring configuration meta-data](http://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html) for IDE autocompletion; see [spring-configuration-metadata.json](src/main/resources/META-INF/spring-configuration-metadata.json). Supported settings are:

|property                                     |default                                           | description 
|---------------------------------------------|--------------------------------------------------|-------------
|log4jdbc.drivers                             |                                                  |One or more fully qualified class names for JDBC drivers that log4jdbc should load and wrap. If more than one driver needs to be specified here, they should be comma separated with no spaces. This option is not normally needed because most popular JDBC drivers are already loaded by default. This should be used if one or more additional JDBC drivers that (log4jdbc doesn't already wrap) needs to be included.
|log4jdbc.auto.load.popular.drivers           |true                                              |Set this to false to disable the feature where popular drivers are automatically loaded. If this is false, you must set the log4jdbc.drivers property in order to load the driver(s) you want.
|log4jdbc.debug.stack.prefix                  |                                                  |A REGEX matching the package name of your application. The call stack will be searched down to the first occurrence of a class that has the matching REGEX. If this is not set, the actual class that called into log4jdbc is used in the debug output (in many cases this will be a connection pool class). For example, setting a system property such as this: `-Dlog4jdbc.debug.stack.prefix=^com\.mycompany\.myapp.*` would cause the call stack to be searched for the first call that came from code in the com.mycompany.myapp package or below, thus if all of your sql generating code was in code located in the com.mycompany.myapp package or any subpackages, this would be printed in the debug information, rather than the package name for a connection pool, object relational system, etc. Please note that the behavior of this property has changed as compared to the standard log4jdbc implementation. This property is now a REGEX, instead of being just the package prefix of the stack trace. So, for instance, if you want to target the prefix org.mypackage, the value of this property should be: `^org\.mypackage.*.`
|log4jdbc.sqltiming.warn.threshold            |                                                  |Millisecond time value. Causes SQL that takes the number of milliseconds specified or more time to execute to be logged at the warning level in the sqltiming log. Note that the sqltiming log must be enabled at the warn log level for this feature to work. Also the logged output for this setting will log with debug information that is normally only shown when the sqltiming log is enabled at the debug level. This can help you to more quickly find slower running SQL without adding overhead or logging for normal running SQL that executes below the threshold level (if the logging level is set appropriately).
|log4jdbc.sqltiming.error.threshold           |                                                  |Millisecond time value. Causes SQL that takes the number of milliseconds specified or more time to execute to be logged at the error level in the sqltiming log. Note that the sqltiming log must be enabled at the error log level for this feature to work. Also the logged output for this setting will log with debug information that is normally only shown when the sqltiming log is enabled at the debug level. This can help you to more quickly find slower running SQL without adding overhead or logging for normal running SQL that executes below the threshold level (if the logging level is set appropriately).
|log4jdbc.dump.booleanastruefalse             |false                                             |When dumping boolean values in SQL, dump them as 'true' or 'false'. If this option is not set, they will be dumped as 1 or 0 as many databases do not have a boolean type, and this allows for more portable sql dumping.
|log4jdbc.dump.sql.maxlinelength              |90                                                |When dumping SQL, if this is greater than 0, than the dumped SQL will be broken up into lines that are no longer than this value. Set this value to 0 if you don't want log4jdbc to try and break the SQL into lines this way. In future versions of log4jdbc, this will probably default to 0.|
|log4jdbc.dump.fulldebugstacktrace            |false                                             |If dumping in debug mode, dump the full stack trace. This will result in EXTREMELY voluminous output, but can be very useful under some circumstances when trying to track down the call chain for generated SQL.
|log4jdbc.dump.sql.select                     |true                                              |Set this to false to suppress SQL select statements in the output. Note that if you use the Log4j 2 logger, it is also possible to control select statements output via the marker LOG4JDBC_SELECT (see [section "Disabling some SQL operations, or dispatching them in different files"](http://log4jdbc.brunorozendo.com/)). The use of this property prepend the use of the marker.
|log4jdbc.dump.sql.insert                     |true                                              |Set this to false to suppress SQL insert statements in the output. Note that if you use the Log4j 2 logger, it is also possible to control insert statements output via the marker LOG4JDBC_INSERT (see [section "Disabling some SQL operations, or dispatching them in different files"](http://log4jdbc.brunorozendo.com/)). The use of this property prepend the use of the marker.
|log4jdbc.dump.sql.update                     |true                                              |Set this to false to suppress SQL update statements in the output. Note that if you use the Log4j 2 logger, it is also possible to control update statements output via the marker LOG4JDBC_UPDATE (see [section "Disabling some SQL operations, or dispatching them in different files"](http://log4jdbc.brunorozendo.com/)). The use of this property prepend the use of the marker.
|log4jdbc.dump.sql.delete                     |true                                              |Set this to false to suppress SQL delete statements in the output. Note that if you use the Log4j 2 logger, it is also possible to control delete statements output via the marker LOG4JDBC_DELETE (see [section "Disabling some SQL operations, or dispatching them in different files"](http://log4jdbc.brunorozendo.com/)). The use of this property prepend the use of the marker.
|log4jdbc.dump.sql.create                     |true                                              |Set this to false to suppress SQL create statements in the output. Note that if you use the Log4j 2 logger, it is also possible to control create statements output via the marker LOG4JDBC_CREATE (see [section "Disabling some SQL operations, or dispatching them in different files"](http://log4jdbc.brunorozendo.com/)). The use of this property prepend the use of the marker.
|log4jdbc.dump.sql.addsemicolon               |false                                             |Set this to true to add an extra semicolon to the end of SQL in the output. This can be useful when you want to generate SQL from a program with log4jdbc in order to create a script to feed back into a database to run at a later time.
|log4jdbc.spylogdelegator.name                |net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator    |The qualified class name of the SpyLogDelegator to use. Note that if you want to use log4jdbc-log4j2 with  Log4j 2 rather than SLF4J, you must set this property to: `net.sf.log4jdbc.log.log4j2.Log4j2SpyLogDelegator`. Note that the default in this Starter is to use SLF4J which is different from the log4jdbc library's default.
|log4jdbc.statement.warn                      |false                                             |Set this to true to display warnings (Why would you care?) in the log when Statements are used in the log.
|log4jdbc.trim.sql                            |true                                              |Set this to false to not trim the logged SQL.
|log4jdbc.trim.sql.extrablanklines            |true                                              |Set this to false to not trim extra blank lines in the logged SQL (by default, when more than one blank line in a row occurs, the contiguous lines are collapsed to just one blank line).
|log4jdbc.suppress.generated.keys.exception   |false                                             |Set to true to ignore any exception produced by the method `Statement.getGeneratedKeys()`
|log4jdbc.log4j2.properties.file              |log4jdbc.log4j2.properties                        |Set the name of the property file to use 


Loggers
=======
Note that, by default, nothing will be logged. In fact, if all the loggers are disabled (which is the default), then log4jdbc doesn't even wrap the `java.sql.Connection` returned by `javax.sql.DataSource.getConnection()` (which is useful for production configurations).

|logger              |description
|--------------------|------------
|jdbc.sqlonly        |Logs only SQL. SQL executed within a prepared statement is automatically shown with it's bind arguments replaced with the data bound at that position, for greatly increased readability.
|jdbc.sqltiming      |Logs the SQL, post-execution, including timing statistics on how long the SQL took to execute.
|jdbc.audit          |Logs ALL JDBC calls except for ResultSets. This is a very voluminous output, and is not normally needed unless tracking down a specific JDBC problem.
|jdbc.resultset      |Even more voluminous, because all calls to ResultSet objects are logged.
|jdbc.resultsettable |Log the jdbc results as a table. Level debug will fill in unread values in the result set.
|jdbc.connection     |Logs connection open and close events as well as dumping all open connection numbers. This is very useful for hunting down connection leak problems.

To set a logger's level, use application.properties in the same way any other log level is configured (see the [Spring Boot Logging Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html) for reference). For example, `logging.level.jdbc.sqlonly=DEBUG`
