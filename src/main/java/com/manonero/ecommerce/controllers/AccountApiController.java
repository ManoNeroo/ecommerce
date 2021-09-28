package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.models.UserRequest;
import com.manonero.ecommerce.services.IUserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {

    @Autowired
    private IUserAccountService accountService;

    @PostMapping("/togglestatus")
	public Response toggleStatus(@RequestBody UserRequest request) {
		accountService.updateStatus(request);
		return new Response(true);
	}

	@PostMapping()
	public Response add(@RequestBody UserRequest request) {
		UserAccount account = accountService.add(request);
		return new Response(account, true);
	}

	@GetMapping("/{id}")
	public Response getById(@PathVariable int id) {
		UserAccount account = accountService.getById(id);
		return new Response(account, true);
	}

	@PutMapping("/basicinfo/{id}")
	public Response updateBasicInfo(@RequestBody UserRequest request) {
		accountService.updateBasicInfo(request);
		return new Response(true);
	}

	@PutMapping("/roles/{id}")
	public Response updateRoles(@RequestBody UserRequest request) {
		accountService.updateRoles(request);
		return new Response(true);
	}

	@PutMapping("/password")
	public Response updatePassword(@RequestBody UserRequest request) {
		boolean rs = accountService.updatePassword(request);
		return new Response(rs);
	}

	@PatchMapping("/avatar")
	public Response updateAvatar(@RequestBody UserRequest request) {
		accountService.updateAvatar(request);
		return new Response(true);
	}
}
