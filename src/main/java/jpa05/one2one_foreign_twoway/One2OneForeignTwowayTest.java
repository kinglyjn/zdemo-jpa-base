package jpa05.one2one_foreign_twoway;

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
public class One2OneForeignTwowayTest {
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
	 */
	@Test
	public void testPersist() {
		Dept dept = new Dept("dept-1");
		Mgr mgr = new Mgr("mgr-1");
		
		//设置关联关系
		dept.setMgr(mgr);
		mgr.setDept(dept);
		
		entityManager.persist(dept);
		entityManager.persist(mgr);
	}
	
	
	/**
	 * 测试find查询（查询维护关联关系的一方）
	 * 
	 * 默认情况下，若获取维护关联关系的一方，则会通过左外连接获取其关联的对象。
	 * 可以通过 @OneToOne 的 fetch 属性来修改加载策略
	 * 
	 */
	@Test
	public void testFind1() {
		Mgr mgr = entityManager.find(Mgr.class, 1);
		
		//使用懒加载策略获取的结果
		System.out.println(mgr.getClass()); //xx.Mgr
		System.out.println(mgr.getDept().getClass()); //xx.Dept_$$_jvst76a_1（返回未初始化的代理对对象，注意延迟加载异常）
		System.out.println(mgr.getDept()); //ok
	}
	
	
	/**
	 * 测试find查询（查询不维护关联关系的一方）
	 * 
	 * 1. 默认情况下，若获取不维护关联关系的一方，则也会通过左外连接获取其关联的对象。
	 * 2. 也可以通过 @OneToOne 的 fetch 属性来修改加载策略，但依然会再发送sql语句来初始化器关联的对象！
	 * 【建议 不要修改不维护关联关系系的一方的 fetch属性】
	 * 
	 * 为什么jpa一定要发送sql语句非要查出不维护关系一方的数据呢？
	 * 因为不维护关系一方数据表中没有外键字段，除非再去查询关联表，
	 * 否则无法判断是否需要代理类，因而一次性查出所有数据而不设置代理。
	 * 【注意和双向n-1区分，因为双向n-1的1方总是使用PersistSet来存放n方数据，是有明确的保存n方数据策略的，因而可以进行懒加载】
	 * 
	 */
	@Test
	public void testFind2() {
		Dept dept = entityManager.find(Dept.class, 1);
		
		//使用懒加载策略获取的结果依然不是代理！
		System.out.println(dept.getClass()); //xx.Dept
		System.out.println(dept.getMgr().getClass()); //xx.Mgr
		System.out.println(dept.getMgr()); //ok
	}
}

