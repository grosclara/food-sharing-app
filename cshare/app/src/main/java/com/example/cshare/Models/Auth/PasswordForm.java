package com.example.cshare.Models.Auth;

public class PasswordForm {

    private String old_password;
    private String new_password1;
    private String new_password2;

    public PasswordForm(String oldPassword, String newPassword, String confirmNewPassword){
        this.old_password = oldPassword;
        this.new_password1 = newPassword;
        this.new_password2 = confirmNewPassword;
    }

    public Boolean isValid(){
        return Boolean.TRUE;
    }
}
