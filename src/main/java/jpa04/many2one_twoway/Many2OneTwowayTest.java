package jpa04.many2one_twoway;

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
public class Many2OneTwowayTest {
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
	 * 测试插入数据
	 * 
	 * 默认情况下：
	 *     1. 先保存customer，再保存order，三条插入sql，2条更新sql（两端都为维护关联关系）
	 *     2. 先保存order，再保存customer，三条插入sql，4条更新sql（两端都为维护关联关系）
	 * 
	 *【建议使用n端来维护关联关系，而1端不维护关联关系】
	 * 设置方法：设置1端 @OneToMany 的属性 mappedBy="customer"
	 * 			这个时候就1端就不能使用@JoinColumn注解了，否则jpa将抛出异常		
	 *
	 */
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
	
	
	/*
	 * 其他测试（略）
	 * 
	 */
}
