package com.mpangoEngine.core.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mpangoEngine.core.dao.ChartOfAccountsDao;
import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.dao.TransactionDao;
import com.mpangoEngine.core.model.COAAccountType;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Customer;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.ReportObject;
import com.mpangoEngine.core.model.Supplier;
import com.mpangoEngine.core.model.Transaction;
import com.mpangoEngine.core.service.SecurityService;
import com.mpangoEngine.core.service.UserService;
import com.mpangoEngine.core.util.CustomErrorType;
import com.mpangoEngine.core.util.EmailService;
import com.mpangoEngine.core.util.ResponseModel;
import com.mpangoEngine.core.validator.UserValidator;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private FarmDao farmDao;
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private EmailService emailService;

	

	/*
	 * FARMS 
	 */
	@RequestMapping(value = "/farms", method = RequestMethod.POST)
	public ResponseEntity<?> createFarm(@RequestBody Farm farm) {
		logger.info("Creating farm >>>> ", farm);
		int rows = farmDao.save(farm);
		return ResponseEntity.ok(response(rows));
	}
	
	@RequestMapping(value = "/farms/{farmid}", method = RequestMethod.GET)
	public ResponseEntity<Farm> getFarm(@PathVariable("farmid") int farmid) {
		Farm farm = farmDao.findFarmById(farmid);
		return new ResponseEntity<Farm>(farm, HttpStatus.OK);
	}

	/*
	 * PROJECTS
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> listAllProjects() {
		List<Project> projects = projectDao.findAll();
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/projects/{projid}", method = RequestMethod.GET)
	public ResponseEntity<Project> getProject(@PathVariable("projid") int projid) {
		Project project = projectDao.findProjectById(projid);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.POST)
	public ResponseEntity<?> createProject(@RequestBody Project project) {
		logger.info("Creating Project >>>>> ", project);
		int rows = projectDao.save(project);
		return ResponseEntity.ok(response(rows));
	}
	
	@RequestMapping(value = "/projects/{projid}/details", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getProjectDetails(@PathVariable("projid") int projid) {
		List<Map<String, Object>> projDetails = projectDao.findProjectDetails(projid);
		return new ResponseEntity<List<Map<String, Object>>>(projDetails, HttpStatus.OK);
	}
	

	/*
	 * COA 
	 */
	@RequestMapping(value = "/coa/", method = RequestMethod.GET)
	public ResponseEntity<List<ChartOfAccounts>> chartOfAccounts() {
		List<ChartOfAccounts> chartofaccounts = chartOfAccountsDao.findAllCOA();
		return new ResponseEntity<List<ChartOfAccounts>>(chartofaccounts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/coa/types", method = RequestMethod.GET)
	public ResponseEntity<List<COAAccountType>> getCOAAccountTypes() {
		List<COAAccountType> coa = chartOfAccountsDao.fetchAllAccountTypes();
		return new ResponseEntity<List<COAAccountType>>(coa, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/coa", method = RequestMethod.POST)
	public ResponseEntity<?> createCOA(@RequestBody ChartOfAccounts coa, UriComponentsBuilder ucBuilder) {
		logger.info("FinancialsApiController->createCOA() :: >>>> ", coa);
		if (chartOfAccountsDao.existsById(coa.getId())) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
		chartOfAccountsDao.save(coa);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/coa/{id}").buildAndExpand(coa.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	
	/*
	 * USERS
	 */
	@RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getUserDetails(@PathVariable("userid") int userid) {
		List<MyUser> userDetails = userService.getUserDetails(userid);
		return new ResponseEntity<List<MyUser>>(userDetails, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getAllUsers() {
		List<MyUser> allUsers = userService.getAllUsers();
		return new ResponseEntity<List<MyUser>>(allUsers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userid}/farms", method = RequestMethod.GET)
	public ResponseEntity<List<Farm>> listAllFarmsByUser(@PathVariable("userid") int userid) {
		List<Farm> farms = farmDao.findAllFarmsByUserId(userid);
		if (farms.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Farm>>(farms, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userId}/coa", method = RequestMethod.GET)
	public ResponseEntity<List<ChartOfAccounts>> getCOAByUser(@PathVariable("userId") int userId) {
		List<ChartOfAccounts> coa = chartOfAccountsDao.findAllCOAByUser(userId);
		return new ResponseEntity<List<ChartOfAccounts>>(coa, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userId}/farms", method = RequestMethod.GET)
	public ResponseEntity<List<Farm>> getFarmsByUser(@PathVariable("userId") int userId) {
		List<Farm> farms = farmDao.findAllFarmsByUserId(userId);
		return new ResponseEntity<List<Farm>>(farms, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userid}/projects", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> listAllProjectsByUser(@PathVariable("userid") int userid) {
		logger.info("Fetching projects for  userid {}", userid);
		List<Project> projects = projectDao.findAllProjectsByUser(userid);
		if (projects.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userid}/transactions", method = RequestMethod.GET)
	public ResponseEntity<List<Transaction>> userTransactions(@PathVariable("userid") int userid) {
		logger.info("Fetching Transactions for  userid {}", userid);
		List<Transaction> transactionsArray = transactionDao.findUserTransactions(userid);

		Collections.sort(transactionsArray, new Comparator<Transaction>() {
			@Override
			public int compare(Transaction o1, Transaction o2) {
				if (o1.getTransactionDate() == null || o2.getTransactionDate() == null)
					return 0;
				return o1.getTransactionDate().compareTo(o2.getTransactionDate());
			}
		});

		return new ResponseEntity<List<Transaction>>(transactionsArray, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/token/{token}", method = RequestMethod.GET)
	public ResponseEntity<?> showConfirmationPage(@PathVariable("token") String token) {
		MyUser user = userService.findByConfirmationToken(token);
		if (user == null) {
			return new ResponseEntity(new CustomErrorType("invalidToken"), HttpStatus.NOT_FOUND);
		} else {
			userService.enableUser(user);
		}
		return new ResponseEntity<String>(user.getConfirmationToken(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody MyUser user) {
		String username = user.getUsername();
		String password = user.getPassword();

		MyUser myUser = userService.findUserByUserName(username);
		UserDetails userDetails = userService.loadUserByUsername(username);

		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();

		String role = "";
		for (GrantedAuthority authority : authorities) {
			role = authority.getAuthority();
		}		
		myUser.setUserType(role);

		return ResponseEntity.ok(myUser);
	}
	
	@RequestMapping(value = "/users/register", method = RequestMethod.POST)
	public ResponseEntity<?> registration(@RequestBody MyUser user, BindingResult bindingResult,
			HttpServletRequest request, UriComponentsBuilder ucBuilder) {

		System.out.println("userForm >>> " + user.getEmail() + " ::: " + user.getPassword());

		logger.info("RestApiController ---> registration() >>>> email {}", user.getEmail());
		logger.info("RestApiController ---> registration() >>>> getPassword {}", user.getPassword());

		userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}

		// Generate random 36-character string token for confirmation link
		user.setConfirmationToken(UUID.randomUUID().toString());

		logger.info("RestApiController ---> registration() >>>> randomUUID {}", UUID.randomUUID().toString());

		userService.saveUser(user);

		String appUrl = request.getScheme() + "://" + request.getServerName();
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl
				+ "/confirm?token=" + user.getConfirmationToken());
		registrationEmail.setFrom("noreply@domain.com");

		logger.info("RestApiController ---> registration() >>>> sendEmail ");

		emailService.sendEmail(registrationEmail);

		// model.addAttribute("confirmationMessage", "A confirmation e-mail has been
		// sent to " + user.getEmail());

		// securityService.autologin(user.getEmail(), user.getPassword());

		logger.info("RestApiController ---> registration() >>>> email sent ");

		return new ResponseEntity<String>(user.getEmail(), HttpStatus.CREATED);
	}

	private ResponseModel response(int rows) {
		int status = 1;
		String res = "FAILED";
		if (rows > 0) {
			res = "CREATED";
			status = 0;
		}
		return new ResponseModel(status, res);
	}
}
