package jpa02.many2one_oneway;

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
public class Many2OneOnewayTest {
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
	public void testInit() {
		
	}
	
	
	/**
	 * 测试插入数据（对比hibernate的单向多对一保存操作）
	 * 1. 先保存customer，再保存order，三条插入sql
	 * 2. 先保存order，再保存customer，三条插入sql，两条更新sql
	 *【建议先保存1端，后保存n端，这样不会多出额外的sql语句】
	 *
	 */
	@Test
	public void testPersist() {
		Customer customer = new Customer("张三", 23);
		Order order1 = new Order("order-1");
		Order order2 = new Order("order-2");
		
		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}
	
	
	/**
	 * 测试find查询
	 * 默认情况 @ManyToOne(fetch=FetchType.EAGER) 使用左外连接方式来获取n端对象和器关联的1端对象
	 * 当配置为 @ManyToOne(fetch=FetchType.LAZY) 则使用懒加载的方式获取和n端关联的1端对象
	 * 
	 */
	@Test
	public void testFind() {
		Order order1 = entityManager.find(Order.class, 1);
		
		/*
		//默认情况下使用 @ManyToOne(fetch=FetchType.EAGER)方式
		System.out.println(order1.getClass()); //xx.Order
		System.out.println(order1.getCustomer().getClass()); //xx.Customer
		System.out.println(order1.getCustomer()); //ok
		*/
		
		System.out.println(order1.getClass()); //xx.Order
		System.out.println(order1.getCustomer().getClass()); //Customer_$$_jvst9da_1（返回的是未初始化的代理对象，注意懒加载异常）
		System.out.println(order1.getCustomer()); //ok
	}
	
	
	/**
	 * 测试删除n端
	 * 查询出来后直接删除
	 * 
	 */
	@Test
	public void testRemove() {
		Order order1 = entityManager.find(Order.class, 1);
		entityManager.remove(order1);
	}
	
	
	/**
	 * 测试更新
	 * 
	 */
	@Test
	public void test() {
		Order order1 = entityManager.find(Order.class, 2);
		
		//更新1端属性
		//order1.getCustomer().setName("李四");
		
		//更新n端外键
		Customer customer = entityManager.find(Customer.class, 2);
		order1.setCustomer(customer);
	}
}
