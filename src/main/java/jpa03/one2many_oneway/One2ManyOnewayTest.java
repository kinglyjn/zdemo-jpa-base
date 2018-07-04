package jpa03.one2many_oneway;

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
public class One2ManyOnewayTest {
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
	 * 测试插入数据（对比hibernate的单向一对多保存操作）
	 * 1. 先保存customer，再保存order，三条插入sql，两条更新sql
	 * 2. 先保存order，再保存customer，三条插入sql，两条更新sql
	 *【由于始终是1端在维护关联关系，因此无论先保存哪一端，都会多出2条更新sql】
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
		
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}
	
	
	/**
	 * 测试find查询
	 * [注意] @OneToMany 默认采用懒加载策略，而 @ManyToOne 默认采用饥饿加载策略！
	 * 
	 */
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1); 
		
		System.out.println(customer.getClass()); //xx.Customer
		System.out.println(customer.getOrders().getClass()); //xx.PersistentSet
		System.out.println(customer.getOrders()); //ok
	}
	
	
	/**
	 * 测试删除
	 * 1. 默认情况下若删除1端，先将n端对应记录的外键置空，再删除1端记录
	 * 2. 可以使用 @OneToMany 的 cascade 属性进行更改，cascade的取值为CascadeType枚举类型的数组
	 * 
	 */
	@Test
	public void testRemove() {
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	
	
	/**
	 * 测试更新
	 * 
	 */
	@Test
	public void testUpdate() {
		Customer customer = entityManager.find(Customer.class, 2);
		
		//更新n端记录的属性
		/*
		Order order = customer.getOrders().iterator().next();
		order.setName("order-11");
		*/
		
		//更新1端的set集合（这时候会将1端set集合对应的n端记录的外键置为空）
		customer.getOrders().clear();
		System.out.println(customer.getOrders());
	}
}
