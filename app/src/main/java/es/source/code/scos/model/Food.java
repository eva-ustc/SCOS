package es.source.code.scos.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.model
 * @date 2018/10/27 19:23
 * @description
 * God Bless,No Bug!
 */
public class Food implements Parcelable {
    private String foodName;
    private Integer foodId;
    private Float foodPrice;
    private String foodType;

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Float getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Float foodPrice) {
        this.foodPrice = foodPrice;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodName='" + foodName + '\'' +
                ", foodId=" + foodId +
                ", foodPrice=" + foodPrice +
                '}';
    }

    // Parcelable内容
    protected Food(Parcel in) {
        foodName = in.readString();
        foodId = in.readInt();
        foodPrice = in.readFloat();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 该方法负责序列化
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodName);
        dest.writeInt(foodId);
        dest.writeFloat(foodPrice);
    }
}
