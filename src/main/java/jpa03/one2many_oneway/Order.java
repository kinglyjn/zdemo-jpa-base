package jpa03.one2many_oneway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA entity Order
 * @author zhangqingli
 *
 */
//注意在一个持久化单元中entity_name必须唯一（默认值为非全限类名）
@Entity(name = "jpa03_order") 
@Table(name = "jpa03_order")
public class Order {
	private Integer id;
	private String name;

	public Order() {
		super();
	}
	public Order(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	
	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + "]";
	}
}
