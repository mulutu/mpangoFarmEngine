package com.mpangoEngine.core.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mpangoEngine.core.dao.AccountDao;
import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.dao.PrivilegeDao;
import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.dao.RoleDao;
import com.mpangoEngine.core.dao.TaskDao;
import com.mpangoEngine.core.dao.TransactionDao;
import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.Account;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Privilege;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.model.Task;
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
	private TaskDao taskDao;
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
	@Autowired
	private PrivilegeDao privilegeDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;	
	@Autowired
	private RoleDao RoleDao;	
	
	/*
	 * TRANSACTIONS
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.POST)
	public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
		logger.info("Creating transaction >>>>> ", transaction);
		int rows = transactionDao.saveTransaction(transaction);
		return ResponseEntity.ok(response(rows));
	}
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
	
	
	/*
	 * TASKS 
	 */
	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	public ResponseEntity<?> createTask(@RequestBody Task task) {
		logger.info("Creating task >>>> ", task);
		int rows = taskDao.saveTask(task);
		return ResponseEntity.ok(response(rows));
	}
	@RequestMapping(value = "/tasks", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTask(@RequestBody Task task) {
		logger.info("Updating task >>>> ", task);
		int rows = taskDao.updateTask(task);
		return ResponseEntity.ok(response(rows));
	}	
	@RequestMapping(value = "/tasks/{taskid}", method = RequestMethod.GET)
	public ResponseEntity<Task> getTask(@PathVariable("taskid") int taskid) {
		Task task = taskDao.getTaskById(taskid);
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}
	@RequestMapping(value = "/tasks/{taskid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTask(@PathVariable("taskid") int taskid) {
		int rows = taskDao.deleteTask(taskid);
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
	@RequestMapping(value = "/farms", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFarm(@RequestBody Farm farm) {
		logger.info("Updating farm >>>> ", farm);
		int rows = farmDao.updateFarm(farm);
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
	@RequestMapping(value = "/projects", method = RequestMethod.POST)
	public ResponseEntity<?> createProject(@RequestBody Project project) {
		logger.info("Creating Project >>>>> ", project);
		int rows = projectDao.save(project);
		return ResponseEntity.ok(response(rows));
	}
	@RequestMapping(value = "/projects", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProject(@RequestBody Project project ) {
		logger.info("Updating project >>>>> {} ", project);
		int rows = projectDao.updateProject(project);
		return ResponseEntity.ok(response(rows));
	}
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
	
	@RequestMapping(value = "/projects/{projid}/tasks", method = RequestMethod.GET)
	public ResponseEntity<List<Task>> getProjectTasks(@PathVariable("projid") int projid) {
		List<Task> tasks = taskDao.getTasksForProject(projid);
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
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
			return new ResponseEntity(HttpStatus.NOT_FOUND); // You many decide to return HttpStatus.NOT_FOUND
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
				return o2.getTransactionDate().compareTo(o1.getTransactionDate());
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
		UserDetails userDetails = userDao.loadUserByUsername(username);

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
		logger.info("RestApiController ---> User Registration() >>>> user {}", user);

		//userValidator.validate(user);
	
		user.setConfirmationToken(UUID.randomUUID().toString());		
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);

		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

		Role role = RoleDao.findByRole("ROLE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(role)));

		int rows = userDao.saveUser(user);
		
		// create some accounts
		if(rows > 0) {
			user.setId(rows);
			userDao.createAccounts(user);
		}

		//String appUrl = request.getScheme() + "://" + request.getServerName();
		//SimpleMailMessage registrationEmail = new SimpleMailMessage();
		//registrationEmail.setTo(user.getEmail());
		//registrationEmail.setSubject("Registration Confirmation");
		//registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl
		//		+ "/confirm?token=" + user.getConfirmationToken());
		//registrationEmail.setFrom("noreply@domain.com");

		//logger.info("RestApiController ---> registration() >>>> sendEmail ");

		//emailService.sendEmail(registrationEmail);

		// model.addAttribute("confirmationMessage", "A confirmation e-mail has been
		// sent to " + user.getEmail());

		// securityService.autologin(user.getEmail(), user.getPassword());

		//logger.info("RestApiController ---> registration() >>>> email sent ");

		return ResponseEntity.ok(response(rows));
	}
	
	private Privilege createPrivilegeIfNotFound(String name) {
		Privilege privilege = null;

		try {
			privilege = privilegeDao.findPrivilegeByName(name);
		} catch (NoResultException e) {
			System.out.println("privilege not found >>>>>>>>>>>>>   " + name);
		}
		if (privilege == null) {
			Privilege newprivilege = new Privilege(name);
			privilegeDao.savePrivilege(newprivilege);
		}
		return privilege;
	}

	private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
		Role role = null;

		try {
			role = RoleDao.findByRole(name);
		} catch (NoResultException e) {
		}
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			RoleDao.saveRole(role);
		}
		return role;
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
