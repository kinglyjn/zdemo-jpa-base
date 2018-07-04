package jpa07.second_ehcache;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity Customer
 * @author zhangqingli
 *
 */
@Cacheable(true)
@Entity(name="jpa07_customer")
@Table(name="jpa07_customer")
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
	@GeneratedValue
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
	
	@OneToMany(mappedBy="customer", fetch=FetchType.LAZY)
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
