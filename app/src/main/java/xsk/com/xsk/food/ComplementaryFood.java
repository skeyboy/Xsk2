package xsk.com.xsk.food;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @time: 2019-06-05$ 15:42$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class ComplementaryFood extends RealmObject {
    private Double price;
    @PrimaryKey
    private int id;
    private String name;
    private int total = 1;

    @Override
    public String toString() {
        return "ComplementaryFood{" +
                "price=" + price +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", total=" + total +
                '}';
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
