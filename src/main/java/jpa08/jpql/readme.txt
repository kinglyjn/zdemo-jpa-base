
JPQL语言：
	JPQL语言，即java persistence query language的简称，jpql是一种非常类似与sql的
	中间性和对象化的查询语言，它最终会被编译成针对不同底层数据库的sql查询，从而屏蔽不
	同数据库之间的差异。
	
	JPQL语言的语句可以是select、update或delete语句，它们都是通过Query接口封装执行的。
	Query接口封装了执行数据库查询的相关方法。调用EntityManager 的 createQuery、
	createNamedQuery 及 createNativeQuery 方法可以获取到相关的 Query 对象。
	
	Query接口的主要方法：
		init executeUpdate：
			用于执行update或者delete语句
		
		List getResultList：
			用于执行select语句并返回结果实体列表
		
		Object getSingleResult：
			用于执行返回单个结果实体的select语句
		
		Query setFirstResult(int startposition)： 
			用于设置从哪个实体记录开始返回查询结果
			
		Query setMaxResults(int maxResult)：
			用于设置返回结果实体的最大数量（与setFirstResult配合使用以完成分页功能）
			
		Query setFlushMode(FlushModeType)：
			设置查询对象的flush模式，参数值可以取2个枚举值
			FlushModeType.AUTO：自动更新数据库记录
			FlushModeType.COMMIT：直到提交事务才更新数据库记录
			
		setHint(String hintName, Object value)：
			设置与查询对象相关的特定供应商参数或提示信息。参数名及其取值需要参考特定JPA实现库供应商的文档。
			如果第二个参数无效将会抛出IllegalArgumentException异常。
			
		setParameter(int position, Object value)：
			为查询语句的特定位置参数赋值。position指定参数序号，value为赋给参数的值
			jpql占位符的标记是从1开始的（而hql占位符的标记从0开始）
			
		setParameter(int position, Date d, TemporalType type)：
			为查询语句的指定位置赋date类型的值，TemporalType取TemporalType的枚举常量，
			包括DATE、TIME及TIMESTAMP三个
			
		
	JPQL提供了以下一些内建函数，包括字符串处理函数、算数函数和日期函数。
		字符串处理函数主要有：
			concat(str1, str2)
			substring(str, start, length)
			trim([leading|trailing|both], [char c], str)
			lower(str)
			upper(str)
			length(str)
			locate(str1, str2, [start]) 从str1中查找str2（字串）出现的位置，若未找到则返回0
			
		算数函数主要有：
			abs
			mod
			sqrt
			size（用于求集合中元素的个数）
			
		日期函数主要有：
			current_date
			current_time
			current_timestamp
			
		
				
			
			
	
	