
一、hello-jpa项目的构建：
	1) 导入jar包（选择hibernate作为jpa的实现产品，数据库选用mysql）
		hibernate-entitymanager  
		hibernate-core
		hibernate-c3p0
		hibernate-ehcache
		mysql-connector-java
		
		
	2) 配置JPA基本配置文件（默认为类路径下 META-INF/persistence.xml，当jpa与spring整合的时候，可以移除该配置文件到spring）
		
		<?xml version="1.0" encoding="UTF-8"?>
		<persistence version="2.1"
			xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
			
			<!--
				持久化单元
				name: 持久化单元名称
				transaction-type: 持久化单元事务类型（JTA 和 RESOURCE_LOCAL）
			-->
			<persistence-unit name="zdemo-jpa-base" transaction-type="RESOURCE_LOCAL">
				<!-- 
					配置jpa实现产品 
					1. 实际上配置的是javax.persistence.spi.PersistenceProvider接口的实现类 
					2. 若jpa项目中只有一个jpa的实现产品，也可以不配置该节点 
				-->
				<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
		
		
				<!-- 添加持久化类 -->
				<class>jpa01.hello.Customer</class>
		
		
				<!-- 配置jpa实现产品的具体属性（hibernate） -->
				<properties>
					<property name="javax.persistence.jdbc.url" value="jdbc:mysql://dbserver:3306/test" />
					<property name="javax.persistence.jdbc.user" value="root" />
					<property name="javax.persistence.jdbc.password" value="23wesdxc" />
					<property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
		
					<property name="hibernate.format_sql" value="true" />
					<property name="hibernate.show_sql" value="true" />
					<property name="hibernate.hbm2ddl.auto" value="update" />
				</properties>
			</persistence-unit>
		</persistence>
		
	
	3) 编写持久化类并使用注解的方式配置orm映射关系，并编写测试类进行测试
		
	
	
	
二、entityManager api：
	在jpa规范中，entityManage是完成持久化类操作的核心对象。实体作为普通java对象，只有在调用entityManager将其持久化
	之后才会变成持久化对象。entityManager对象在一组实体类与底层数据源之间进行orm映射的管理。他可以用来管理和更新
	entity bean，根据主键查找entity bean，还可以通过JPQL来查询实体。
	
	
	实体的状态：
		新建状态（没入职）：新创建的对象，尚未拥有持久性主键，数据库中没有对应的记录
		持久化状态（入职了）：已经拥有持久性主键，并和持久化建立了上下文环境，数据库中有对应的记录
		游离状态（请假了）：拥有持久化主键，没有与持久化建立上下文环境，数据库中有对应的记录
		删除状态（离职了）：拥有持久化主键，没有与持久化建立上下文环境，数据库中记录被移除
	
		
	核心方法：
	------------
		persist：
			若对象有id则不能执行insert操作，而会抛出异常，而hibernate#save不会抛异常
		
		remove：
			该方法只能移除持久化对象，而hibernate#delete方法还可以移除游离对象
		
		find：
			相当于hibernate的get方法，立即向数据库发送sql去查询结果
		
		getReference：
			相当于hibernate的load方法，注意懒加载异常
		
		merge：(类比hibernate的session#saveOrUpdate操作，稍有不同，主要是因为session不能同时跟两个具有相同id的对象进行管关联)
			测试entityManager#merge(临时对象)
			 1. 首先会根据customer对象clone一个新的customer
			 2. 然后持久化并返回这个新的customer对象
			
			测试entityManager#merge(游离对象) [一级缓存中不存在对应持久化对象的情况]
			 1. 首先JPA会查询该游离对象在数据库中的记录，返回该记录的一个持久化对象
			 2. 然后会把游离对象的属性clone到新查询到的持久化对象中，最后对查询到的持久化对象进行更新操作	
					
			测试entityManager#merge(游离对象) [一级缓存中存在对应持久化对象的情况]
			 1. 首先JPA将游离的对象属性clone到一级缓存中对应的持久化对象中
			 2. 然后对一级缓存中的对象进行更新操作 【注意更新操作发生在事务提交之后】
	
	
	非核心方法：
	------------		
		flush：
			将持久上下文环境的所有未保存实体的状态信息保存到数据库中
			setFlushMode(FlushModeType)：
				设置持久上下文环境的flush模式，参数可以取两个枚举值
				FlushModeType.AUTO: 自动更新数据库实体
				FlushModeType.COMMIT: 直到提交事务时才更新数据库记录
				
			getFlushMode：
				获取上持久下文环境的flush模式，返回FlushModeType枚举类型
				
			测试flush方法
			  JPA和hibernate一样，在提交事务的时候会强制进行flush操作（隐式地进行flush）
			  也可以显式地调用entityManager#flush方法进行显式地刷新
			 【注】真正向数据库发送更新和删除等sql是在 显式或隐式flush操作之后！

		refresh：
			将数据库中最新数据同步到持久上下文中
			
		
		clear：
			清除持久上下文环境，断开所有关联实体，如果这时候还有未提交的更新则会被撤销
		contains：
			判断一个实例是否属于当前持久上下文环境管理的实体
		isOpen：
			判断当前的实体管理器是否处于打开状态
		close：
			关闭实体管理器。之后若调用实体管理器的方法或其派生的查询对向的方法将抛出IlligalstateException
			(ipOpen和getTransation方法除外)。不过当与实体管理器关联的事务处于活动状态时，调用close方法后
			持久上下文仍将处于被管理的状态，直到事务完成。
				
			
		
	














	
	
	