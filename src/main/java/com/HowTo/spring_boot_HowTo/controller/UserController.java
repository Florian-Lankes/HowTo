package com.HowTo.spring_boot_HowTo.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.changepassword.OnChangePasswordEvent;
import com.HowTo.spring_boot_HowTo.changepasswordloggedin.OnChangePasswordLoggedInEvent;
import com.HowTo.spring_boot_HowTo.config.google2fa.CustomAuthenticationProvider;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.VerificationToken;
import com.HowTo.spring_boot_HowTo.model.Wallet;
import com.HowTo.spring_boot_HowTo.registration.OnRegistrationCompleteEvent;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.service.WalletServiceI;
import com.HowTo.spring_boot_HowTo.validator.UserAlreadyExistException;
import com.HowTo.spring_boot_HowTo.validator.UserValidator;
import com.cloudinary.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

	private static final String authorizationRequestBaseUri = "oauth2/authorization";
	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

	private UserServiceI userService;

	private ChannelServiceI channelService;
	
	private WalletServiceI walletService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	


	public UserController(UserServiceI userService, ChannelServiceI channelService, WalletServiceI walletService) {
		super();
		this.userService = userService;
		this.channelService = channelService;
		this.walletService = walletService;
	}
	
	@InitBinder("user")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator());
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
	}
	
	//shows all user (pagination)
	@GetMapping(value = { "/user/admin", "/user/admin/all" })
	public String showUserList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size) {
		try {
			logger.info("Showing user list. Keyword: {}, Page: {}, Size: {}", keyword, page, size);
			List<User> users = new ArrayList<User>();

			// the first page is 1 for the user, 0 for the database.
			Pageable paging = PageRequest.of(page - 1, size);
			Page<User> pageUsers;
			// getting the page from the database….
			pageUsers = userService.getAllUsers(keyword, paging);

			model.addAttribute("keyword", keyword);

			users = pageUsers.getContent();
			model.addAttribute("users", users);
			// here are the variables for the paginator in the user-all view
			model.addAttribute("entitytype", "user");
			model.addAttribute("currentPage", pageUsers.getNumber() + 1);
			model.addAttribute("totalItems", pageUsers.getTotalElements());
			model.addAttribute("totalPages", pageUsers.getTotalPages());
			model.addAttribute("pageSize", size);
			logger.info("User list shown successfully. Total Users: {}", pageUsers.getTotalElements());
		} catch (Exception e) {
			logger.error("Error showing user list: ", e);
			model.addAttribute("message", e.getMessage());
		}
		return "users/user-all";
	}
	
	//admin deletes user
	@GetMapping("/user/admin/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
		logger.info("Deleting user with ID: {}", id);
		User user = userService.getUserById(id);
		userService.delete(user);
		redirectAttributes.addFlashAttribute("deleted", "User deleted!");
		logger.info("User deleted successfully: {}", id);
		return "redirect:/user/admin/all";
	}
	
	//user Account delete
	@GetMapping("/user/delete")
	public String deleteAccount( Model model, RedirectAttributes redirectAttributes) {
		logger.info("Deleting user with ID: {}", getCurrentUserId());
		User user = userService.getUserById(getCurrentUserId());
		userService.delete(user);
		redirectAttributes.addFlashAttribute("deleted", "User deleted!");
		logger.info("User deleted successfully: {}", getCurrentUserId());
		return "redirect:/logout";
	}
	
	
	
	
	//admin updates user form
	@GetMapping("/user/admin/update/{id}")
	public String showUpdateUserForm(@PathVariable Long id, Model model, HttpServletRequest request) {
		logger.info("Fetching user for update with ID: {}", id);
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		request.getSession().setAttribute("userSession", user);
		logger.info("Fetched user for update: {}", id);
		return "users/user-update";
	}
	
	//admin updates user
	@PostMapping("/user/admin/update")
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {
		if (results.hasErrors()) {
			logger.warn("Validation errors while updating user: {}", results.getAllErrors());
			return "users/user-update";
		}
		userService.updateUser(user);
		redirectAttributes.addFlashAttribute("updated", "user updated!");
		logger.info("User updated successfully: {}", user.getUserId());
		return "redirect:/user/admin/all";

	}
	
	@GetMapping("/user/admin/add")
	public String showUserAdForm(Model model, HttpServletRequest request) {
		logger.info("Showing user add form");
		User userForm = new User();
		userForm.setUserId((long) -1);
		LocalDate date = LocalDate.now();
		userForm.setBirthDate(date);
		request.getSession().setAttribute("userSession", userForm);
		model.addAttribute("user", userForm);
		logger.info("User add form shown successfully");
		return "users/user-add";
	}
	
	@PostMapping("/user/admin/add")
	public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		logger.info("Adding new user");
		if (result.hasErrors()) {
			logger.warn("Validation errors while adding new user: {}", result.getAllErrors());
			return "users/user-add";
		}

		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("added", "User added!");
		logger.info("User added successfully: {}", user.getUserId());
		return "redirect:/user/admin/all";
	}

	//register form
	@GetMapping("/register")
	public String showUserRegisterForm(Model model, HttpServletRequest request) {
		logger.info("Showing user registration form");
		User userForm = new User();
		LocalDate date = LocalDate.now();
		userForm.setBirthDate(date);

		request.getSession().setAttribute("userSession", userForm);
		model.addAttribute("user", userForm);
		logger.info("User registration form shown successfully");
		return "register";
	}
	
	//confirm token and email and enables him
	@GetMapping("/registrationConfirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {

		// Locale locale = request.getLocale();
		logger.info("Confirming registration with token: {}", token);
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
//	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
//	        model.addAttribute("message", message);
			logger.warn("Invalid registration token: {}", token);
			return "redirect:/badUser"; // ?lang=" + locale.getLanguage();
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
//	        model.addAttribute("message", messageValue);
			logger.warn("Expired registration token: {}", token);
			return "redirect:/badUser"; // ?lang=" + locale.getLanguage();
		}
		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		logger.info("User registration confirmed: {}", user.getUserId());
		return "redirect:/login"; // + request.getLocale().getLanguage();
	}
	
	//registers the user and sends him mail for verification
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		logger.info("Registering new user");
		if (result.hasErrors()) {
			logger.warn("Validation errors while registering user: {}", result.getAllErrors());
			return "register";
		}
		try {
			User registered = userService.saveUser(user);
			String appUrl = request.getContextPath();
			//sends him mail with verification token
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
			logger.info("User registered successfully: {}", user.getUserId());
		} catch (UserAlreadyExistException uaeEx) {
			logger.warn("User already exists: {}", user.getUsername());
			redirectAttributes.addFlashAttribute("register", "An account for that username/email already exists.");
			return "redirect:/";
		}
		redirectAttributes.addFlashAttribute("added", "User added! Please confirm per E-mail");
		return "redirect:/login";
	}
	
	
	//changes passsword form
	@GetMapping("/user/changemypassword")
	public String changePassword(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		logger.info("Changing password for user");
		User user = userService.getUserById(getCurrentUserId());
		model.addAttribute("user", user);
		logger.info("Password change form shown successfully for user: {}", user.getUserId());
		return "changepassword";
	}
	
	//change password
	@PostMapping("/user/passwordchanged")
	public String passwordchanged(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("Password changed for user: {}", user.getUserId());
		userService.changePassword(user);
		eventPublisher.publishEvent(new OnChangePasswordLoggedInEvent(user));
		return "redirect:/login";
	}
	
	//shows form for forgotten password
	@GetMapping("/user/forgotmypassword")
	public String forgotmyPassword(RedirectAttributes redirectAttributes, HttpServletRequest request) {
		logger.info("Showing forgot password form");
		return "forgotpassword";
	}
	
	//sends him a mail with a token , which is used for verfication on the form 
	@PostMapping("/user/forgotmypassword")
	public String forgotmyPasswordemail(@RequestParam("email") String email, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		logger.info("Processing forgot password for email: {}", email);
		Optional<User> u = userService.getUserByEmail(email);
		try {
			User user = u.get();
			if (!user.isEnabled()) {
				logger.warn("Account with email {} wasn't confirmed", email);
				redirectAttributes.addFlashAttribute("deleted", "Account with this E-Mail wasn't confirmed");
				return "redirect:/login";
			}
			String appUrl = request.getContextPath();
			//this event sends the email with the token
			
			eventPublisher.publishEvent(new OnChangePasswordEvent(user, request.getLocale(), appUrl));
			redirectAttributes.addFlashAttribute("email", "E-Mail for password change has been sent");
			logger.info("Password change email sent for user: {}", user.getUserId());
			return "redirect:/login";
		} catch (NoSuchElementException e) {
			logger.warn("No account found with email: {}", email);
			redirectAttributes.addFlashAttribute("deleted", "No account with such E-Mail");
			return "redirect:/login";
		}
	}
	
	//checks the token and then enables you to change the password
	@GetMapping("/confirmPassword")

	public String confirmpassword(WebRequest request, Model model, @RequestParam("token") String token) {
		// Locale locale = request.getLocale();
		logger.info("Confirming password with token: {}", token);
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
//	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
//	        model.addAttribute("message", message);
			logger.warn("Invalid password confirmation token: {}", token);
			return "redirect:/badUser"; // ?lang=" + locale.getLanguage();
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
//	        model.addAttribute("message", messageValue);
			logger.warn("Expired password confirmation token: {}", token);
			return "redirect:/badUser"; // ?lang=" + locale.getLanguage();
		}

		model.addAttribute("user", user);
		model.addAttribute("token", token);
		logger.info("Password confirmation successful for user: {}", user.getUserId());
		return "changeforgottenpassword"; // + request.getLocale().getLanguage();
	}

	//changed the password
	@PostMapping("/user/forgottenpasswordchanged")
	public String forgottenpasswordchanged(@Valid @ModelAttribute User user, @RequestParam("token") String token,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		logger.info("Changing forgotten password for user: {}", user.getUserId());
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			logger.warn("Invalid password reset token: {}", token);
			return "redirect:/badUser";
		}

		User u = verificationToken.getUser();
		User check = userService.getUserById(user.getUserId());
		if (check.equals(u)) {
			u.setUsing2FA(false);
			userService.changePassword(user);
			logger.info("Password changed successfully for user: {}", user.getUserId());
		} else { 
			logger.warn("Password reset token does not match user: {}", user.getUserId()); 
		}
		return "redirect:/login";
	}
	
	//activates 2fa
	@GetMapping("user/my/activate2fa")
	public String activate2fa(Model model) throws UnsupportedEncodingException {
		logger.info("Activating 2FA for user");
		User user = userService.getUserById(getCurrentUserId());
		String qr = userService.generateQRUrl(user);
		model.addAttribute("qr", userService.generateQRUrl(user));
		logger.info("QR code generated for 2FA: {}", qr);
		user.setUsing2FA(true);
		userService.saveRegisteredUser(user);
		logger.info("2FA activated for user: {}", user.getUserId());
		return "users/qrcode";

	}
	
	
	//deactivate 2fa
	@GetMapping("user/my/deactivate2fa")
	public String deactivate2fa(Model model) {
		logger.info("Deactivating 2FA for user");
		User user = userService.getUserById(getCurrentUserId());
		user.setUsing2FA(false);
		userService.saveRegisteredUser(user);
		logger.info("2FA deactivated for user: {}", user.getUserId());
		return "redirect:/logout";

	}

	//shows login form
	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.GET)
	public String showLoginForm(Model model) {
		logger.info("Showing login form");
		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			// TODO log 
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		}

		clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
				authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
		model.addAttribute("urls", oauth2AuthenticationUrls);
		logger.info("Login form shown successfully with OAuth2 URLs");
		return "login_register";
	}
	
	
	//login only for google and github
	@GetMapping("/loginSuccess")
	public String getLoginInfo(HttpServletRequest request, Model model, OAuth2AuthenticationToken authentication2) {
		logger.info("Fetching login info for OAuth2 authentication");
		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication2.getAuthorizedClientRegistrationId(), authentication2.getName());

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

			HttpEntity<String> entity = new HttpEntity<String>("", headers);

			ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity,
					Map.class);
			Map userAttributes = response.getBody();
			model.addAttribute("name", userAttributes.get("name"));
			String userName;
			if (userAttributes.get("name") == null) {
				userName = userAttributes.get("login").toString();
			} else {
				userName = userAttributes.get("name").toString();
			}
			User u = userService.saveO2authUser(userAttributes.get("email").toString(), userName);
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u.getUsername(),
					u.getUsername());

			// Authenticate the user
			Authentication authentication = customAuthenticationProvider.authenticate(authRequest);
			SecurityContext securityContext = SecurityContextHolder.getContext();
			SecurityContextHolder.getContext().setAuthentication(authentication);
			securityContext.setAuthentication(authentication);

			// Create a new session and add the security context.
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
			logger.info("OAuth2 login info fetched successfully for user: {}", u.getUserId());
		}

		return "redirect:/home";
	}

	
	@GetMapping("/logout")
	public String logout() {
		logger.info("Logging out user");
		return "redirect:/";
	}
	
	//shows profile of current user
	@GetMapping("/user/my")
	public String showUserProfile(Model model, RedirectAttributes redirectAttributes) {
		logger.info("Showing user profile");
		User current_user = userService.getUserById(getCurrentUserId());
		model.addAttribute("user", current_user);

		Channel my_channel = channelService.getChannelById(getCurrentUserId());
		model.addAttribute("channel", my_channel);
		
		Wallet wallet = walletService.getWalletById(getCurrentUserId());
		model.addAttribute("wallet", wallet);
		// Change password
		// Change channelname and discription
		// Enable 2fa
		logger.info("User profile shown successfully for user: {}", current_user.getUserId());
		return "users/profile";
	}

}
