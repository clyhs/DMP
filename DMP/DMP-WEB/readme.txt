1、mysql 写入日志时 在数据库后台多加mcp_stat?relaxAutoCommit=true
不然会把Can't call commit when autocommit=true
oracle没有这情况

2、项目第一次执行时：先执行 DMP-CLASS下的SQL目录的文件，包含：quartz和kettle

3、hive 用的服务1，命令为hive --service hiveserver
  如果用hive --service hiveserver2则无法启动 可以试试2的驱动：org.apache.hive.jdbc.HiveDriver
