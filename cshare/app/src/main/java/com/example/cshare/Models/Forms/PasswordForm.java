package com.example.cshare.Models.Forms;

public class PasswordForm {

    private String old_password;
    private String new_password1;
    private String new_password2;

    public PasswordForm(String oldPassword, String newPassword, String confirmNewPassword){
        this.old_password = oldPassword;
        this.new_password1 = newPassword;
        this.new_password2 = confirmNewPassword;
    }

    public String getOld_password() {
        return old_password;
    }

    public String getNew_password1() {
        return new_password1;
    }

    public String getNew_password2() {
        return new_password2;
    }
}
