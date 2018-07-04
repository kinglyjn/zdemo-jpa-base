package jpa01.hello;

import java.util.Date;

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
public class HelloJpaTest {
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
	
	
	/**
	 * 测试插入数据
	 * #
	 * 和hibernate#save的不同之处：
	 * 若对象有id则不能执行insert操作，而会抛出异常
	 * 
	 */
	@Test
	public void testPersist() {
		Customer customer = new Customer("李四", "lisi@keyllo.com", 24, new Date(), new Date());
		//customer.setId(12);
		entityManager.persist(customer);
	}
	
	
	/**
	 * 测试删除数据
	 * 
	 * 和hibernate#delete方法的不同之处：
	 * 该方法只能移除持久化对象，而hibernate#delete方法还可以移除游离对象
	 * 
	 */
	@Test
	public void testRemove() {
		Customer customer = entityManager.find(Customer.class, 2);
		entityManager.remove(customer);
	}
	
	
	/**
	 * 测试find查询（相当于hibernate的get方法，立即向数据库发送sql去查询结果）
	 */
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println(customer.getClass()); //xx.Customer
		System.out.println(customer);
	}
	
	/**
	 * 测试getReference查询（相当于hibernate的load方法，注意懒加载异常）
	 */
	@Test
	public void testGet() {
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass()); //xx.Customer_$$_jvstf04_0 //这是一个代理类（注意懒加载异常）
		System.out.println(customer); 
	}
	
	
	/**
	 * 测试entityManager#merge(临时对象)
	 * 1. 首先会根据customer对象clone一个新的customer
	 * 2. 然后持久化并返回这个新的customer对象
	 * 
	 */
	@Test
	public void testMerge1() {
		Customer customer01 = new Customer("小娟", "xiaojuan@keyllo.com", 23, new Date(), new Date());
		customer01.setId(100); //数据库中没有对应的记录（customer01是一个临时对象，也可以不设置id的值）
		
		Customer customer02 = entityManager.merge(customer01);
		System.out.println(customer02);
		System.out.println(customer02==customer01);
	}
	
	/**
	 * 测试entityManager#merge(游离对象) [一级缓存中不存在对应持久化对象的情况]
	 * 1. 首先JPA会查询该游离对象在数据库中的记录，返回该记录的一个持久化对象
	 * 2. 然后会把游离对象的属性clone到新查询到的持久化对象中，最后对查询到的持久化对象进行更新操作
	 * 
	 */
	@Test
	public void testMerge2() {
		Customer customer01 = new Customer("小娟", "xiaojuan@keyllo.com", 23, new Date(), new Date());
		customer01.setId(1); //数据库中有对应的记录（customer01是一个游离对象）
		
		Customer customer02 = entityManager.merge(customer01);
		System.out.println(customer02);
		System.out.println(customer02==customer01);
	}
	
	/**
	 * 测试entityManager#merge(游离对象) [一级缓存中存在对应持久化对象的情况]
	 * 1. 首先JPA将游离的对象属性clone到一级缓存中对应的持久化对象中
	 * 2. 然后对一级缓存中的对象进行更新操作 【注意更新操作发生在事务提交之后】
	 * 
	 */
	@Test
	public void testMerge3() {
		Customer customer01 = new Customer("小娟", "xiaojuan@keyllo.com", 23, new Date(), new Date());
		customer01.setId(1); //数据库中有对应的记录（customer01是一个游离对象）
		System.out.println("customer01: " + customer01);
		
		Customer customer02 = entityManager.find(Customer.class, 1);
		Customer customer03 = entityManager.merge(customer01);
		
		System.out.println("customer02: " + customer02);
		System.out.println("customer03: " + customer03);
		System.out.println("customer01==customer02? " + (customer01==customer02)); //false
		System.out.println("customer02==customer03? " + (customer02==customer03)); //true
	}

	
	/**
	 * 测试flush方法
	 * JPA和hibernate一样，在提交事务的时候会强制进行flush操作（隐式地进行flush）
	 * 也可以显式地调用entityManager#flush方法进行显式地刷新
	 *
	 *【注】真正向数据库发送更新和删除等sql是在 显式或隐式flush操作之后！
	 * 
	 */
	@Test
	public void testFlush() {
		Customer customer = entityManager.find(Customer.class, 1);
		customer.setName("张三");
		
		entityManager.flush(); //在此处打断点
	}
	
	
	/**
	 * 测试refresh方法
	 * 将数据库中最新数据同步到持久上下文中
	 * 
	 */
	@Test
	public void testRefresh() {
		Customer customer1 = entityManager.find(Customer.class, 1);
		System.out.println(customer1);
		
		//entityManager.refresh(customer1); //如果没有refresh操作，将只会发送一条sql语句
		
		Customer customer2 = entityManager.find(Customer.class, 1);
		System.out.println(customer2);
		System.out.println(customer1==customer2); //true
	}
}
