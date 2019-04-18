package com.example.task;

public class UserProfile {
    String name,country,emailAddress,phone;
   boolean email,sms,push;
int creditCount=0,promoCodeCount=0;

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public UserProfile(String name, String country, String emailAddress, String phone, boolean email, boolean sms, boolean push) {
        this.name = name;
        this.country = country;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.email = email;
        this.sms = sms;
        this.push = push;
    }
public UserProfile()
{

}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public int getPromoCodeCount() {
        return promoCodeCount;
    }

    public void setPromoCodeCount(int promoCodeCount) {
        this.promoCodeCount = promoCodeCount;
    }
}
