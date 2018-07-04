package jpa05.one2one_foreign_twoway;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * jpa entity Mgr
 * 
 * @author zhangqingli
 *
 */
@Entity(name="jpa05_mgr")
@Table(name="jpa05_mgr")
public class Mgr {
	private Integer id;
	private String name;
	private Dept dept;

	
	public Mgr() {
		super();
	}
	public Mgr(String name) {
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
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dept_id", unique=true) //外键我们选择放在mgr表中
	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	@Override
	public String toString() {
		return "Mgr [id=" + id + ", name=" + name + "]";
	}

}
