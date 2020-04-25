package com.example.cshare.Models.Forms;

public class ResetPasswordForm {

    private String email;

    public ResetPasswordForm(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValid() {
        return true;
    }
}
