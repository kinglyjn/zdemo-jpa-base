package jpa04.many2one_twoway;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity Customer
 * @author zhangqingli
 *
 */
@Entity(name="jpa04_customer")
@Table(name="jpa04_customer")
public class Customer {
	private Integer id;
	private String name;
	private Integer age;
	private Set<Order> orders = new HashSet<Order>(); //关系属性
	
	
	public Customer() {
		super();
	}
	public Customer(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	/*
	 * 映射1-n关联关系
	 * 
	 *【一般情况下映射双向n-1关联关系时两边的joinColumn_name必须一致】
	 * 
	 * 但是当@OneToMany设置了mappedBy="customer"（表示自己放弃关联关系的维护，交由对方(n端)的customer属性来映射和维护关联关系），
	 * 这个时候就不能使用@JoinColumn注解了，否则jpa将抛出异常
	 * 
	 */
	@OneToMany(mappedBy="customer", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	//@JoinColumn(name="custom_id")
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
