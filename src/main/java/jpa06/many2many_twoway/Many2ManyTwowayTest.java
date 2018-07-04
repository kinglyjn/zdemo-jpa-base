package jpa06.many2many_twoway;

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
public class Many2ManyTwowayTest {
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
	 * 测试保存
	 * 如果仅有一端维护关联关系的情况下，先插入那一端都不会多出额外的更新语句
	 * 
	 */
	@Test
	public void testPersist() {
		Item item1 = new Item("item-3");
		Item item2 = new Item("item-4");
		Category category1 = new Category("category-3");
		Category category2 = new Category("category-4");
		
		// 设置关联关系
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		entityManager.persist(item1);
		entityManager.persist(item2);
		entityManager.persist(category1);
		entityManager.persist(category2);
	}
	
	
	/**
	 * 测试find查询【两边对称】
	 * 无论对于哪一方，对于关联的集合对象，默认都使用懒加载策略
	 * 
	 */
	@Test
	public void testFind() {
		/*
		Item item = entityManager.find(Item.class, 1);
		System.out.println("------------------------");
		
		System.out.println(item.getClass()); //xx.Item
		System.out.println(item.getCategories().getClass()); //xx.PersistentSet
		System.out.println(item.getCategories()); //ok
		*/
		
		Category category = entityManager.find(Category.class, 1);
		System.out.println("------------------------");
		System.out.println(category.getClass()); //xx.Category
		System.out.println(category.getItems().getClass()); //xx.PersistentSet
		System.out.println(category.getItems()); //ok
	}
	
	
	/**
	 * 测试更新
	 * 
	 */
	@Test
	public void testUpdate() {
		Item item = entityManager.find(Item.class, 1);
		
		//更新关联对象的属性
		/*
		Category category = item.getCategories().iterator().next();
		category.setName("c-111");
		*/
		
		//清空集合 
		//默认将删除第三方表中对应的记录，而不删除对方数据表记录
		item.getCategories().clear();
	}
	
	
	/**
	 * 测试删除
	 * 1. 默认只删除自己对应的表和第三方表所对应的记录
	 * 2. 可以
	 * 
	 */
	@Test
	public void testRemove() {
		Item item = entityManager.find(Item.class, 3);
		entityManager.remove(item);
	}
}

