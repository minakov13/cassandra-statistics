package org.aminakov.cassandra.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User implements Serializable {
    /**
     * Class version.
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String phoneHash;
    private String nickname;
    private String email;
    private String postalCode;
    private String birthDate;
    private String bio;
    private String sex;
    private String avatar;
    private String wallpaper;
    private String pin;
    private boolean receiveAnyDM = false;
    private Long registrationDate = new Date().getTime();
    private String smsNickname;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isReceiveAnyDM() {
        return receiveAnyDM;
    }

    public void setReceiveAnyDM(boolean receiveAnyDM) {
        this.receiveAnyDM = receiveAnyDM;
    }

    public void setPhoneHash(String phoneHash) {
        this.phoneHash = phoneHash;
    }

    public String getPhoneHash() {
        return phoneHash;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    /**
     * Return string representation base on fields. Exclude sensitive information like pin.
     */
    public String toString() {
        return (new ReflectionToStringBuilder(this) {
            protected boolean accept(Field f) {
                return super.accept(f) && !f.getName().equals("pin");
            }
        }).toString();
    }

    public String getSmsNickname() {
        return smsNickname;
    }

    public void setSmsNickname(String smsNickname) {
        this.smsNickname = smsNickname;
    }

    /**
     * Updates fields that are contained in the user profile
     *
     * @param user user profile information
     */
    public void updateProfile(User user) {
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.bio = user.bio;
        this.birthDate = user.birthDate;
        this.postalCode = user.postalCode;
        this.receiveAnyDM = user.receiveAnyDM;
        this.email = user.email;
        this.sex = user.sex;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Returns user with fields that contained in the user profile
     *
     * @return user containing fields related to user profile
     */
    public User asProfile() {
        User user = new User();
        user.id = this.id;
        user.firstName = this.firstName;
        user.lastName = this.lastName;
        user.email = this.email;
        user.nickname = this.nickname;
        user.bio = this.bio;
        user.birthDate = this.birthDate;
        user.avatar = this.avatar;
        user.wallpaper = this.wallpaper;
        user.phone = this.phone;
        user.postalCode = this.postalCode;
        user.receiveAnyDM = this.receiveAnyDM;
        user.sex = this.sex;
        user.registrationDate = this.registrationDate;
        return user;
    }

}
