package jpa07.second_ehcache;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试类
 * @author zhangqingli
 *
 */
public class SecondLevelCacheTest {
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
		Customer customer = new Customer("张三", 23);
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
	 * 测试一级缓存
	 * 测试结果：只向数据库发送一条sql查询语句
	 * 
	 */
	@Test
	public void testFirstLevelCache() {
		Customer customer = entityManager.find(Customer.class, 1);
		customer = entityManager.find(Customer.class, 1);
		System.out.println(customer);
	}
	
	/**
	 * 测试二级缓存
	 * 测试结果：只向数据库发送一条sql查询语句
	 * 
	 */
	@Test
	public void testSecondLevelCache() {
		Customer customer = entityManager.find(Customer.class, 1);
		//Order order = entityManager.find(Order.class, 1);
		
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();
		tx = entityManager.getTransaction();
		tx.begin();
		
		customer = entityManager.find(Customer.class, 1);
		System.out.println(customer);
		//order = entityManager.find(Order.class, 1);
		//System.out.println(order);
	}
	
}
