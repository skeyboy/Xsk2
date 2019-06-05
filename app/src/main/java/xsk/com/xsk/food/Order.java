package xsk.com.xsk.food;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @time: 2019-06-05$ 15:43$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class Order extends RealmObject {
    private int id;
    private Food food;
    private ComplementaryFood complementaryFood;
    private RealmList<Favor> favors;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", food=" + food +
                ", complementaryFood=" + complementaryFood +
                ", favors=" + favors +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public ComplementaryFood getComplementaryFood() {
        return complementaryFood;
    }

    public void setComplementaryFood(ComplementaryFood complementaryFood) {
        this.complementaryFood = complementaryFood;
    }

    public RealmList<Favor> getFavors() {
        return favors;
    }

    public void setFavors(RealmList<Favor> favors) {
        this.favors = favors;
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        return getId() == ((Order) obj).getId();
    }
}
