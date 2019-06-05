package xsk.com.xsk.food;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @time: 2019-06-05$ 15:32$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class Food extends RealmObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public RealmList<Favor> getFavors() {
        return favors;
    }

    public void setFavors(RealmList<Favor> favors) {
        this.favors = favors;
    }

    public ComplementaryFood getComplementaryFood() {
        return complementaryFood;
    }

    public void setComplementaryFood(ComplementaryFood complementaryFood) {
        this.complementaryFood = complementaryFood;
    }

    private int id;
    private Double price;
    private RealmList<Favor> favors;
    private ComplementaryFood complementaryFood;

}
