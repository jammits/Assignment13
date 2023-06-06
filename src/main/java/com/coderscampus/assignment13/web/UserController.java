package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AccountService accountService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());

		}
		
		return "users";
	}

	@PostMapping("/users")
	public String postSingleUserView(User user, ModelMap model) {
		User userOne = userService.findById(user.getUserId());
		Address address = addressService.findById(userOne.getUserId());
		address.setUser(userOne);
		address.setUserId(userOne.getUserId());
		userOne.setAddress(address);
		postOneUser(userOne);

		Set<User> users = userService.findAll();
		//populating the model again to prevent no users in thymeleaf error, doesn't affect the user model only users.
		//Allows the user model to persist and show old information
		model.put("users", users);

		return "users";
	}


	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		Address address = addressService.findById(userId);
		address.setUser(user);
		address.setUserId(userId);
		user.setAddress(address);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		model.put("address", user.getAddress());
		return "users";
	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser (User user) {
		user.setAddress(addressService.save(user.getAddress()));
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@GetMapping("/users/{usersId}/accounts/{accountId}")
	public String showAccounts(ModelMap model, @PathVariable Long accountId, @PathVariable Long usersId) {
		Account account = accountService.findById(accountId);
		User user = userService.findById(usersId);
		model.put("accUser", user);
		model.put("account", account);
		return "accounts";
	}


	@PostMapping("/users/{userId}/accounts")
	public String postCreateAccount(@PathVariable Long userId){
		User user = userService.findById(userId);
		Account account = new Account();
		account.setAccountName("Account #" + (user.getAccounts().size() + 1));
		account.getUsers().add(user);
		user.getAccounts().add(account);
		accountService.save(account);
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId()+"/accounts/"+account.getAccountId();
	}

	@PostMapping("/users/{usersId}/accounts/{accountId}")
	public String saveAccount(Account account, @PathVariable Long usersId) {
		account.getUsers().add(userService.findById(usersId));
		accountService.save(account);
		return "redirect:/users/"+ usersId+"/accounts/"+account.getAccountId();

	}
}
