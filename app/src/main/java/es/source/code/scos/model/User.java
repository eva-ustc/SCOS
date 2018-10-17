package es.source.code.scos.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LRK
 * @project_name SCOS
 * @package_name es.source.code.scos.model
 * @date 2018/10/5 19:06
 * @description
 */

public class User implements Parcelable {
    private String userName;
    private String password;
    private Boolean oldUser;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOldUser() {
        return oldUser;
    }

    public void setOldUser(Boolean oldUser) {
        this.oldUser = oldUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", oldUser=" + oldUser +
                '}';
    }

    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        oldUser = in.readByte() != 0;
    }

    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeByte((byte) (oldUser ? 1 : 0));
    }
}
