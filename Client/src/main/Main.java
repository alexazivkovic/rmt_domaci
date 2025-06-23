package main;

import forms.LoginForm;

public class Main{
	public static void main(String[] args) {
		try{
			new LoginForm().setVisible(true);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
