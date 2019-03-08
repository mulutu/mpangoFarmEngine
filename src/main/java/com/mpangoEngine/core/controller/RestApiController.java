package com.mpangoEngine.core.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mpangoEngine.core.dao.AccountDao;
import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.dao.TransactionDao;
import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.Account;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.Transaction;
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
	private ProjectDao projectDao;
	@Autowired
	private FarmDao farmDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TransactionDao transactionDao;
	
	
	/*
	 * TRANSACTIONS
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTransaction(@RequestBody Transaction transaction ) {
		logger.info("Updating transaction >>>>> {} ", transaction);
		int rows = transactionDao.updateTransaction(transaction);
		return ResponseEntity.ok(response(rows));
	}
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)
	public ResponseEntity<List<Transaction>> listAllTransactions() {
		logger.info("List transactions >>>>> ");
		List<Transaction> projects = transactionDao.findAll();
		return new ResponseEntity<List<Transaction>>(projects, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/transactions/{transactionID}", method = RequestMethod.GET)
	public ResponseEntity<Transaction> getTransaction(@PathVariable("transactionID") int transactionID) {
		logger.info("Get transaction >>>>> ", transactionID);
		Transaction transaction = transactionDao.findTransactionById(transactionID);
		return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
		logger.info("Creating transaction >>>>> ", transaction);
		int rows = transactionDao.saveTransaction(transaction);
		return ResponseEntity.ok(response(rows));
	}

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
	

	/*
	 * COA 
	 */
	@RequestMapping(value = "/coa/", method = RequestMethod.GET)
	public ResponseEntity<List<Account>> chartOfAccounts() {
		List<Account> chartofaccounts = accountDao.findAllAccounts();
		return new ResponseEntity<List<Account>>(chartofaccounts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/coa", method = RequestMethod.POST)
	public ResponseEntity<?> createCOA(@RequestBody Account coa, UriComponentsBuilder ucBuilder) {
		logger.info("FinancialsApiController->createCOA() :: >>>> ", coa);
		if (accountDao.existsById(coa.getId())) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
		accountDao.save(coa);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/coa/{id}").buildAndExpand(coa.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	
	/*
	 * USERS
	 */
	@RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
	public ResponseEntity<MyUser> getUserDetails(@PathVariable("userid") int userid) {
		MyUser userDetails = userDao.getUserDetails(userid);
		return new ResponseEntity<MyUser>(userDetails, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getAllUsers() {
		List<MyUser> allUsers = userDao.getAllUsers();
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
	
	@RequestMapping(value = "/users/{userId}/accounts", method = RequestMethod.GET)
	public ResponseEntity<List<Account>> getCOAByUser(@PathVariable("userId") int userId) {
		List<Account> coa = accountDao.findAllAccountsByUser(userId);
		return new ResponseEntity<List<Account>>(coa, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userId}/coa", method = RequestMethod.GET)
	public ResponseEntity<List<ChartOfAccounts>> getUserCAO(@PathVariable("userId") int userId) {
		List<ChartOfAccounts> coa = accountDao.fetchUserCoa(userId);
		return new ResponseEntity<List<ChartOfAccounts>>(coa, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userid}/projects/summary", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> listAllProjectsSummaryByUser(@PathVariable("userid") int userid) {
		logger.info("Fetching projects for  userid {}", userid);
		List<Project> projects = projectDao.findAllProjectsSummaryByUser(userid);
		if (projects.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
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
		MyUser user = userDao.findByConfirmationToken(token);
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

		MyUser myUser = userDao.findUserByUserName(username);
		UserDetails userDetails = userService.loadUserByUsername(username);

		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();

		String role = "";
		for (GrantedAuthority authority : authorities) {
			role = authority.getAuthority();
		}		
		myUser.setUserType(role);

		return ResponseEntity.ok(myUser);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<?> registration(@RequestBody MyUser user, HttpServletRequest request) {

		logger.info("RestApiController ---> registration() >>>> email {}", user.getEmail());
		logger.info("RestApiController ---> registration() >>>> getPassword {}", user.getPassword());

		//userValidator.validate(user);
		
		// Generate random 36-character string token for confirmation link
		user.setConfirmationToken(UUID.randomUUID().toString());

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
