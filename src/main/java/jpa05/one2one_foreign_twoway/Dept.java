package jpa05.one2one_foreign_twoway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * jpa entity Dept
 * @author zhangqingli
 *
 */
@Entity(name="jpa05_dept")
@Table(name="jpa05_dept")
public class Dept {
	private Integer id;
	private String name;
	private Mgr mgr;

	
	public Dept() {
		super();
	}

	public Dept(String name) {
		this.name = name;
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

	/*
	 * 唯一外键方式映射1-1关联关系
	 */
	@OneToOne(mappedBy="dept") //放弃关联关系的维护，交由对方dept属性来映射和维护关联关系
	public Mgr getMgr() {
		return mgr;
	}

	public void setMgr(Mgr mgr) {
		this.mgr = mgr;
	}

	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name + "]";
	}
}
