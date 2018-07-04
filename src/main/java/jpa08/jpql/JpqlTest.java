package jpa08.jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.jpa.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试类
 * @author zhangqingli
 *
 */
public class JpqlTest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction tx;
	
	
	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory("zdemo-jpa-base");
		entityManager = entityManagerFactory.createEntityManager();
		tx = entityManager.getTransaction();
		tx.begin();
	}
	
	@After
	public void destroy() {
		tx.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	
	@Test
	public void testPersist() {
		Customer customer = new Customer("小娟", 28);
		Order order1 = new Order("order-1");
		Order order2 = new Order("order-2");
		
		//设置关联关系
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}
	
	
	/**
	 * 查询对象列表（参数位置绑定）
	 */
	@Test
	public void testGetResultList() {
		String jpql = "from jpa08.jpql.Customer c where c.age > ?"; //可以使用全限类名或非全限类名
		Query query = entityManager.createQuery(jpql).setParameter(1, 22);
		
		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 查询部分属性列表（默认返回List<Object[]> 类型数据）
	 */
	@Test
	public void testGetPartlyField1() {
		String jpql = "select c.name, c.age from jpa08.jpql.Customer c where c.age > ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, 22);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (Object[] result : results) {
			System.out.println(result[0] + " " + result[1]);
		}
	}
	
	
	/**
	 * 查询部分属性（返回List<Customer> 类型数据，Customer需要有相应的构造器）
	 */
	@Test
	public void testGetPartlyField2() {
		String jpql = "select new jpa08.jpql.Customer(c.name, c.age) from jpa08.jpql.Customer c where c.age > ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, 22);
		
		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 测试参数名称绑定
	 */
	@Test
	public void testNamedParam() {
		String jpql = "select new jpa08.jpql.Customer(c.name, c.age) from jpa08.jpql.Customer c where c.age > :age";
		Query query = entityManager.createQuery(jpql).setParameter("age", 22);
		
		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 测试命名查询（命名查询语句可以在相应实体类上使用）
	 */
	@Test
	public void testNamedQuery() {
		Query query = entityManager.createNamedQuery("myQueryByCustomerId").setParameter("id", 1);
		
		Customer customer = (Customer) query.getSingleResult(); //注意如果数据库中没有相应的记录则会抛异常
		System.out.println(customer);
	}
	
	
	/**
	 * 测试本地sql查询
	 */
	@Test
	public void testNativeQuery() {
		String sql = "select age from jpa08_customer where id=:id";
		Query query = entityManager.createNativeQuery(sql).setParameter("id", 1);
		
		Integer age = (Integer) query.getSingleResult();
		System.out.println(age);
	}
	
	
	/**
	 * 测试查询缓存
	 * query#setHint，并且需要设置
	 * <property name="hibernate.cache.use_query_cache" value="true" />
	 * 
	 * 测试结果：只向数据库发送一条查询语句
	 * 
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testQueryCache() {
		String jpql = "from jpa08.jpql.Customer c where c.age > ?";
		Query query = entityManager.createQuery(jpql).setParameter(1, 22).setHint(QueryHints.HINT_CACHEABLE, true);
		List<Customer> customers = query.getResultList();
		customers = query.getResultList();
		System.out.println(customers);
		
		jpql = "from jpa08.jpql.Customer c where c.age > ?";
		query = entityManager.createQuery(jpql).setParameter(1, 22).setHint(QueryHints.HINT_CACHEABLE, true);
		customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 测试分组和排序
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testOrderByAndGroupByAndHaving() {
		String jpql = "select o.customer, count(o.id) from jpa08.jpql.Order o group by o.customer having count(o.id) >= 2 order by o.customer.age desc";
		Query query = entityManager.createQuery(jpql);
		
		List<Object[]> results = query.getResultList();
		for (Object[] result : results) {
			System.out.println(result[0] + "-->" + result[1]);
		}
		/*
		测试结果：
		Hibernate: 
	    select
	        order0_.customer_id as col_0_0_,
	        count(order0_.id) as col_1_0_,
	        customer1_.id as id1_14_,
	        customer1_.age as age2_14_,
	        customer1_.name as name3_14_ 
	    from
	        jpa08_order order0_ 
	    inner join
	        jpa08_customer customer1_ 
	            on order0_.customer_id=customer1_.id 
	    group by
	        order0_.customer_id 
	    having
	        count(order0_.id)>=2 
	    order by
	        customer1_.age desc
	        
		Customer [id=3, name=王五, age=28]-->2
		Customer [id=2, name=李四, age=25]-->2
		Customer [id=1, name=张三, age=23]-->2
		Customer [id=4, name=小娟, age=23]-->3
		*/
	}
	
	
	/**
	 * 测试关联查询（不好的方案）
	 * 
	 */
	@Test
	public void testJoin1() {
		String jpql = "select o.customer from jpa08.jpql.Order o where o.id = :id";
		Query query = entityManager.createQuery(jpql).setParameter("id", 1);
		
		Customer customer = (Customer) query.getSingleResult();
		System.out.println(customer);
		System.out.println(customer.getOrders());
		
		/*
		 测试结果：要发送两条sql查询语句
		 	Hibernate: 
		    select
		        customer1_.id as id1_14_,
		        customer1_.age as age2_14_,
		        customer1_.name as name3_14_ 
		    from
		        jpa08_order order0_ 
		    inner join
		        jpa08_customer customer1_ 
		            on order0_.customer_id=customer1_.id 
		    where
		        order0_.id=?
			Customer [id=1, name=张三, age=23]
			
			Hibernate: 
			    select
			        orders0_.customer_id as customer3_14_0_,
			        orders0_.id as id1_15_0_,
			        orders0_.id as id1_15_1_,
			        orders0_.customer_id as customer3_15_1_,
			        orders0_.name as name2_15_1_ 
			    from
			        jpa08_order orders0_ 
			    where
			        orders0_.customer_id=?
			[Order [id=1, name=order-1], Order [id=2, name=order-2]]
		 */
	}
	
	
	/**
	 * 测试关联查询（迫切左外连接，较好的方案）
	 * distinct关键字: 过滤重复数据
	 * fetch关键字: 在加载1的一端的对象时，使用迫切左外连接（使用左外连接查询，且把集合属性进行初始化）
	 *		   	   的方式检索n的一端的集合属性，lazy属性会被忽略，这样可以避免发送n条查询语句来初始化n方集合
	 *  
	 */
	@Test
	public void testJoin2() {
		String jpql = "select distinct c from jpa08.jpql.Customer c left join fetch c.orders where c.id=:id";
		Query query = entityManager.createQuery(jpql).setParameter("id", 1);
		
		Customer customer = (Customer) query.getSingleResult();
		System.out.println(customer); 
		System.out.println(customer.getOrders()); 
		
		/*
		Hibernate: 
		    select
		        distinct customer0_.id as id1_14_0_,
		        orders1_.id as id1_15_1_,
		        customer0_.age as age2_14_0_,
		        customer0_.name as name3_14_0_,
		        orders1_.customer_id as customer3_15_1_,
		        orders1_.name as name2_15_1_,
		        orders1_.customer_id as customer3_14_0__,
		        orders1_.id as id1_15_0__ 
		    from
		        jpa08_customer customer0_ 
		    left outer join
		        jpa08_order orders1_ 
		            on customer0_.id=orders1_.customer_id 
		    where
		        customer0_.id=?
		     
		   【使用distinct关键字查询结果】
		    Customer [id=1, name=张三, age=23]
		 	[Order [id=1, name=order-1], Order [id=2, name=order-2]]
		 	
		   【而没有加distinct关键字之前的结果如下】
		 	Customer [id=1, name=张三, age=23] Order [id=1, name=order-1] 
		 	Customer [id=1, name=张三, age=23] Order [id=2, name=order-2] 
		 */
	}
	
	
	/**
	 * 测试子查询
	 * 
	 */
	@Test
	public void testSubQuery() {
		String jpql = "select o from jpa08.jpql.Order o where o.customer=(select c from jpa08.jpql.Customer c where c.id=?)";
		Query query = entityManager.createQuery(jpql).setParameter(1, 1);
		
		@SuppressWarnings("unchecked")
		List<Order> orders = query.getResultList();
		System.out.println(orders);
	}
	
	
	/**
	 * 测试JPQL的内建函数
	 * 
	 */
	@Test
	public void testJpqlFunction() {
		String jpql = "select upper(c.name) from jpa08.jpql.Customer c";
		Query query = entityManager.createQuery(jpql);
		
		@SuppressWarnings("unchecked")
		List<String> results = query.getResultList();
		System.out.println(results); //[张三, 李四, 王五, 小娟, KINGLYJN, XIAOJUAN]
	}
	
	
	/**
	 * 测试分页查询
	 * 
	 */
	@Test
	public void testPageQuery() {
		final int pageSize = 3;
		final int pageNo = 2;
		String jpql = "from jpa08.jpql.Customer c order by c.id desc";
		Query query = entityManager.createQuery(jpql).setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
		
		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 测试like查询
	 */
	@Test
	public void testLikeQuery1() {
		String jpql = "from jpa08.jpql.Customer c where c.name like ?1";
		Query query = entityManager.createQuery(jpql).setParameter(1, "%李%");
		
		@SuppressWarnings("unchecked")
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	
	/**
	 * 测试更新和删除
	 * 
	 */
	@Test
	public void testUpdateAndDelete() {
		String jpql = "update jpa08.jpql.Customer c set c.name='aaaaa' where c.id=:id";
		//String jpql = "delete from jpa08.jpql.Customer c where c.id=:id";
		Query query = entityManager.createQuery(jpql).setParameter("id", 1);
		
		int result = query.executeUpdate();
		System.out.println(result);
	}
}


