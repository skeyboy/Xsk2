package xsk.com.xsk.food;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @time: 2019-06-05$ 15:46$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class OrderEnty extends RealmObject {
    private RealmList<Order> orders;
    @PrimaryKey
    private int id;

    public boolean add(Order order) {
        boolean hasOrder = orders.contains(order);
        if (hasOrder == false) {
            return orders.add(order);
        }
        return false;
    }

    public boolean add(Order order, boolean force) {
        orders.remove(order);
        return orders.add(order);
    }
}
