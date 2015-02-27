package com.bd.serwis.controllers;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.bd.serwis.model.User;
import com.bd.serwis.services.UserService;

@ManagedBean(name = "userController")
@SessionScoped
public class UserController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();
	private User loggedUser;
	private User user = new User();

	public UserController() {

	}

	public String createUser() {
		user = new User();
		return "redirect_user_registration";
	}

	public String saveUser() {
		String r = "success";
		try {
			userService.createUser(user);
		} catch (NoSuchAlgorithmException ee) {
			r = "failed";
		}
		return r;
	}

	public String updateUser() {
		try {
			userService.updateUser(loggedUser);
			return "success";
		} catch (NoSuchAlgorithmException e) {
			return "failed";
		}
	}
	
	public String deleteUser(User user){
		userService.deleteUser(user);
		return logout();
	}
	
	public void giveAdminRights(User user){
		user.setAdmin((byte)1);
		try {
			userService.updateUser(user);
		} catch (NoSuchAlgorithmException e) {
			return;
		}
	}
	
	public void giveCommonRights(User user){
		user.setAdmin((byte)0);
		try {
			userService.updateUser(user);
		} catch (NoSuchAlgorithmException e) {
			return;
		}
	}

	public String loginUser() {
		try {
			if (userService.logIn(user)) {
				loggedUser = userService.findUserByEmail(user.getEmail());
				return "success";
			} else {
				return "failed";
			}
		} catch (NoSuchAlgorithmException e) {
			return "failed";
		}
	}
	
	public boolean isUserLoggedIn(){
		return userService.isUserLoggedIn();
	}
	
	public boolean isAdminLoggedIn(){
		return userService.isAdminLoggedIn();
	}
	
	public String logout(){
		userService.logOut(user);
		return "logout_success";
	}
	
	public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
		
	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String seeAllUsers(){
		return "see_all_users";
	}
	
	public String redirectUserLogin(){
		return "redirect_user_login";
	}
	
	public String redirectEditProfile(){
		return "redirect_edit_user_profile";
	}
	
	public String redirectHomePage(){
		return "redirect_homepage";
	}
}
