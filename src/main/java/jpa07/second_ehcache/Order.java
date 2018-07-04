package jpa07.second_ehcache;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPA entity Order
 * @author zhangqingli
 *
 */
@Cacheable(true)
@Entity(name = "jpa07_order") 
@Table(name = "jpa07_order")
public class Order {
	private Integer id;
	private String name;
	private Customer customer; //关系属性

	
	public Order() {
		super();
	}
	public Order(String name) {
		this.name = name;
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

	@ManyToOne
	@JoinColumn(name="customer_id")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + "]";
	}
}
