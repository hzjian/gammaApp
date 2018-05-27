package com.cellinfo.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity//注明这是一个实体类(必须的东西)
@Immutable//(网上的说法是注明这个类是不可变的.不过在使用中没有试验过删除了会不会报错,有闲心的可以试试)
@Synchronize({"my_order"})//网上的说法是用来注明查询的表,如果表里面的变动了,再次查询就会更新这个视图的数据
@Subselect(value=" SELECT COUNT(c.id) wait_pay_order,COUNT(d.id) all_order "
    + " FROM (SELECT id FROM my_order WHERE create_date >= DATE_FORMAT(NOW(),'%Y-%m-%d') "
    + " AND create_date <= DATE_FORMAT(DATE_SUB(NOW(),INTERVAL - 1 DAY),'%Y-%m-%d') "
    + " AND order_status = 1) c RIGHT JOIN (SELECT id FROM my_order "
    + " WHERE create_date >= DATE_FORMAT(NOW(),'%Y-%m-%d') AND "
    + " create_date <= DATE_FORMAT(DATE_SUB(NOW(),INTERVAL - 1 DAY),'%Y-%m-%d')) d ON c.id = d.id ")//这个就不用说了.sql语句
public class OrderViewEntity implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 4821069947331419615L;
  @Id//这个东西是必须的,不然启动会报错.至于网上的有些说法是要重新定义一个id,亲测不用,随便给查询出来的一个字段加上这个都行(ps:因为我查出来的数据就是一行.所以随便加在哪个字段都行,但其他的不知道,自己去实验吧)
  private String waitPayOrder;
  @Column(name="all_order")
  private String allOrder;
  //这里有一个坑,注意sql里面的查询结果的别名..一定要注意,经过多次实验报错得出的结论,字段名如果使用了驼峰命名
  //(allOrder),这里的sql查询结果别名就一定要用all_order或者字段名就直接写all_order;如果觉得别扭.可以使用
  //@Column(name="all_order")(其实使用驼峰命名后,不需要使用@Cloumn来指明,因为数据库默认就是按照all_order这
  //种格式来拆分驼峰命名的字段的)如:allOrder字段;当然,字段名和sql结果别名都是全小写(大写)就不会有这个问题了.
  public String getWaitPayOrder() {
    return waitPayOrder;
  }
  public void setWaitPayOrder(String waitPayOrder) {
    this.waitPayOrder = waitPayOrder;
  }
  public String getAllOrder() {
    return allOrder;
  }
  public void setAllOrder(String allOrder) {
    this.allOrder = allOrder;
  }

}