测试：

测试一：

A、 先运行Sender类，待运行完毕后，运行Receiver类

B、 在此过程中activemq数据库的activemq_msgs表中没有数据

C、 再次运行Receiver，消费不到任何信息

测试二：

A、  先运行Sender类

B、 重启电脑

C、 运行Receiver类，无任何信息被消费

测试三：

A、   把Sender类中的producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);改为producer.setDeliveryMode(DeliveryMode.PERSISTENT);

B、   先运行Sender类，待运行完毕后，运行Receiver类

C、   在此过程中activemq数据库的activemq_msgs表中有数据生成，运行完Receiver类后，数据清除

测试四：

A、    把Sender类中的producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);改为producer.setDeliveryMode(DeliveryMode.PERSISTENT);

B、    运行Sender类

C、    重启电脑

D、    运行Receiver类，有消息被消费

结论：   

通过以上测试，可以发现，在P2P类型中当DeliveryMode设置为NON_PERSISTENCE时，消息被保存在内存中，而当DeliveryMode设置为PERSISTENCE时，消息保存在broker的相应的文件或者数据库中。而且P2P中消息一旦被Consumer消费就从broker中删除。


















测试一：

A、先启动Sender类

B、再启动Receiver类

C、结果无任何记录被订阅

测试二：

A、先启动Receiver类，让Receiver在相关主题上进行订阅

B、停止Receiver类，再启动Sender类

C、待Sender类运行完成后，再启动Receiver类

D、结果发现相应主题的信息被订阅