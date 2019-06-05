package xsk.com.xsk.food;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @time: 2019-06-05$ 15:42$
 * @Author: 如梦一般
 * @email: 994158307@qq.com
 */
public class Favor extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "Favor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
