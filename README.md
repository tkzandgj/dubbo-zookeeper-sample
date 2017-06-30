## ZooKeeper
分布式命名服务:
* 类似文件系统的树形结构
* 节点呈name:data结构 ,data可以存放1M的数据
* 可以由多个server形成服务集群 ,互相保持同步
* 每个server可以为多个client提供服务

可以实现分布式环境下的:
* 配置更新 - 订阅指定节点 ,当data变更时能及时收到通知 ,变更本地数据
* 分布式锁 - 通过对节点的订阅和管控 ,形成client之间的锁
* Leader选举/队列 ...

## Curator
ZooKeeper Client的封装 ,屏蔽了重复订阅watch的动作 ;自带了选举/锁的实现

## Dubbo
基于ZooKeeper的服务框架 ,发布/调用远端服务 ;使用ZooKeeper发布服务信息 ,远端订阅指定节点获取最新的可用服务 .


