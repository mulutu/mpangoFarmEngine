package com.mpangoEngine.core.controller;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mpangoEngine.core.dao.ChartOfAccountsDao;
import com.mpangoEngine.core.dao.CustomerDao;
import com.mpangoEngine.core.dao.ExpenseDao;
import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.dao.IncomeDao;
import com.mpangoEngine.core.dao.PaymentMethodDao;
import com.mpangoEngine.core.dao.ProjectDao;
import com.mpangoEngine.core.dao.SupplierDao;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Customer;
import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.PaymentMethod;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.model.Supplier;
import com.mpangoEngine.core.service.SecurityService;
import com.mpangoEngine.core.service.UserService;
import com.mpangoEngine.core.util.CustomErrorType;
import com.mpangoEngine.core.util.EmailService;
import com.mpangoEngine.core.validator.UserValidator;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	ExpenseDao expenseDao;
	@Autowired
	IncomeDao incomeDao;
	@Autowired
	ProjectDao projectDao;
	@Autowired
	FarmDao farmDao;
	@Autowired
	PaymentMethodDao paymentMethodDao;
	@Autowired
	ChartOfAccountsDao chartOfAccountsDao;
	@Autowired
	SupplierDao supplierDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private EmailService emailService;

	@InitBinder
	public void setupDefaultInitBinder(WebDataBinder binder) {
		binder.setDisallowedFields("*password");
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/*
	 * Process confirmation link
	 */
	@RequestMapping(value = "/confirmtoken/{token}", method = RequestMethod.GET)
	public ResponseEntity<?> showConfirmationPage(@PathVariable("token") String token) {
		MyUser user = userService.findByConfirmationToken(token);
		if (user == null) {
			return new ResponseEntity(new CustomErrorType("invalidToken"), HttpStatus.NOT_FOUND);
		} else {
			userService.enableUser(user);
		}
		return new ResponseEntity<String>(user.getConfirmationToken(), HttpStatus.OK);
	}

	/*
	 * Login
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
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

	@RequestMapping(value = "/register2", method = RequestMethod.POST)
	public ResponseEntity<?> registration(@RequestBody MyUser user, BindingResult bindingResult,
			HttpServletRequest request, UriComponentsBuilder ucBuilder) {

		System.out.println("userForm >>> " + user.getEmail() + " ::: " + user.getPassword());

		logger.info("RestApiController ---> registration() >>>> email {}", user.getEmail());
		logger.info("RestApiController ---> registration() >>>> getPassword {}", user.getPassword());

		userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new CustomErrorType("Error creating user  " + user.getEmail() + "."),
					HttpStatus.CONFLICT);
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

	@RequestMapping(value = "/user/id/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getUserDetails(@PathVariable("userid") int userid) {

		List<MyUser> userDetails = userService.getUserDetails(userid);
		return new ResponseEntity<List<MyUser>>(userDetails, HttpStatus.OK);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getAllUsers() {

		List<MyUser> allUsers = userService.getAllUsers();
		return new ResponseEntity<List<MyUser>>(allUsers, HttpStatus.OK);
	}

	/* -------------Retrieve All farms for a user ------------- */
	@RequestMapping(value = "/farm/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<Farm>> listAllFarmsByUser(@PathVariable("userid") int userid) {
		List<Farm> farms = farmDao.findAllFarmsByUserId(userid);
		if (farms.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Farm>>(farms, HttpStatus.OK);
	}

	// ---------------- Create a Payment Method --------------//
	@RequestMapping(value = "/paymethod/", method = RequestMethod.POST)
	public ResponseEntity<?> createPayMethod(@RequestBody PaymentMethod paymentMethod, UriComponentsBuilder ucBuilder) {
		logger.info("Creating createPayMethod >>>> ", paymentMethod);
		if (paymentMethodDao.existsById(paymentMethod.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating farm id:  " + paymentMethod.getId() + "."),
					HttpStatus.CONFLICT);
		}
		paymentMethodDao.save(paymentMethod);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/paymenthod/{id}").buildAndExpand(paymentMethod.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ---------------- Create a farm --------------//
	@RequestMapping(value = "/farm/", method = RequestMethod.POST)
	public ResponseEntity<?> createFarm(@RequestBody Farm farm, UriComponentsBuilder ucBuilder) {
		logger.info("Creating farm >>>> ", farm);
		if (expenseDao.existsById(farm.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating farm id:  " + farm.getId() + "."),
					HttpStatus.CONFLICT);
		}
		farmDao.save(farm);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/farm/{id}").buildAndExpand(farm.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	/* -----------retrieve all expenses ----------- */
	@RequestMapping(value = "/expense/", method = RequestMethod.GET)
	public ResponseEntity<List<Expense>> listAllExpenses() {
		List<Expense> expenses = expenseDao.findAll();
		if (expenses.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	/* -----------retrieve all projects ----------- */
	@RequestMapping(value = "/projects/", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> listAllProjects() {
		List<Project> projects = projectDao.findAll();
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	/* -----------retrieve a project ----------- */
	@RequestMapping(value = "/project/{projid}", method = RequestMethod.GET)
	public ResponseEntity<Project> getProject(@PathVariable("projid") int projid) {
		Project project = projectDao.findProjectById(projid);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	/* -----------retrieve chart of accounts ----------- */
	@RequestMapping(value = "/chartofaccounts/", method = RequestMethod.GET)
	public ResponseEntity<List<ChartOfAccounts>> chartOfAccounts() {
		List<ChartOfAccounts> chartofaccounts = chartOfAccountsDao.findAllCOA();
		return new ResponseEntity<List<ChartOfAccounts>>(chartofaccounts, HttpStatus.OK);
	}

	/* -----------retrieve all payment methods ----------- */
	@RequestMapping(value = "/paymentmethods/", method = RequestMethod.GET)
	public ResponseEntity<List<PaymentMethod>> listAllPayementMethods() {
		List<PaymentMethod> paymentmethod = paymentMethodDao.findAll();
		return new ResponseEntity<List<PaymentMethod>>(paymentmethod, HttpStatus.OK);
	}

	/* -----------retrieve all suppliers ----------- */
	@RequestMapping(value = "/suppliers/", method = RequestMethod.GET)
	public ResponseEntity<List<Supplier>> listAllSuppliers() {
		List<Supplier> suppliers = supplierDao.findAll();
		return new ResponseEntity<List<Supplier>>(suppliers, HttpStatus.OK);
	}

	/* -----------retrieve all customers ----------- */
	@RequestMapping(value = "/customers/", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> listAllCustomers() {
		List<Customer> customers = customerDao.findAll();
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	/*--------------------------------------------------------------------------------- 
	 * @@@@@@ @listAllExpenses()
	 * 	  -- Return : ResponseEntity<List<Expense>> 
	 *    -- Retrieve All expenses for a user 
	 * --------------------------------------------------------------------------------*/
	@RequestMapping(value = "/expense/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<Expense>> listAllExpenses(@PathVariable("userid") int userid) {
		logger.info("Fetching Expenses for  userid {}", userid);
		List<Expense> expenses = expenseDao.findAllExpensesByUserId(userid);

		if (expenses.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	/*--------------------------------------------------------------------------------- 
	 * @@@@@@ @listAllIncomes()
	 * 	  -- Return : ResponseEntity<List<Income>> 
	 *    -- Retrieve All incomes for a user 
	 * --------------------------------------------------------------------------------*/
	@RequestMapping(value = "/income/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<Income>> listAllIncomes(@PathVariable("userid") int userid) {
		logger.info("Fetching Expenses for  userid {}", userid);
		List<Income> incomes = incomeDao.findAllIncomesByUserId(userid);

		if (incomes.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Income>>(incomes, HttpStatus.OK);
	}

	// -------------------Retrieve Single income
	@RequestMapping(value = "/income/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getIncome(@PathVariable("id") int id) {
		logger.info("Fetching Income with id {}", id);
		Income income = incomeDao.findById(id);

		if (income == null) {
			logger.error("Income with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Income with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Income>(income, HttpStatus.OK);
	}

	// -------------------Retrieve Single expense
	@RequestMapping(value = "/expense/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getExpense(@PathVariable("id") int id) {
		logger.info("Fetching Expense with id {}", id);
		Expense expense = expenseDao.findById(id);

		if (expense == null) {
			logger.error("Expense with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Expense with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Expense>(expense, HttpStatus.OK);
	}

	// ---------------- Create an Expense --------------//
	@RequestMapping(value = "/expense/", method = RequestMethod.POST)
	public ResponseEntity<?> createExpense(@RequestBody Expense expense, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Expense >>>> ", expense);
		if (expenseDao.existsById(expense.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating expense id:  " + expense.getId() + "."),
					HttpStatus.CONFLICT);
		}
		expenseDao.save(expense);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/expense/{id}").buildAndExpand(expense.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ---------------- Create a project ------------- //
	@RequestMapping(value = "/project/", method = RequestMethod.POST)
	public ResponseEntity<?> createProject(@RequestBody Project project, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Project >>>>> ", project);
		if (projectDao.existsById(project.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating project  " + project.getId() + "."),
					HttpStatus.CONFLICT);
		}
		projectDao.save(project);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/project/{id}").buildAndExpand(project.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ---------------- Create a supplier ------------- //
	@RequestMapping(value = "/supplier/", method = RequestMethod.POST)
	public ResponseEntity<?> createSupplier(@RequestBody Supplier supplier, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Supplier >>>>> ", supplier);
		if (supplierDao.existsById(supplier.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating project  " + supplier.getId() + "."),
					HttpStatus.CONFLICT);
		}
		supplierDao.save(supplier);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/supplier/{id}").buildAndExpand(supplier.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ---------------- Create a customer ------------- //
	@RequestMapping(value = "/customer/", method = RequestMethod.POST)
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
		logger.info("Creating customer >>>>> ", customer);
		if (customerDao.existsById(customer.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating project  " + customer.getId() + "."),
					HttpStatus.CONFLICT);
		}
		customerDao.save(customer);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/customer/{id}").buildAndExpand(customer.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update an expense ------------------------------
	@RequestMapping(value = "/expense/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateExpense(@PathVariable("id") int id, @RequestBody Expense expense) {

		Expense currentExpense = expenseDao.findById(id);

		if (currentExpense == null) {
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		currentExpense.setId(id);
		currentExpense.setExpenseDate(expense.getExpenseDate());
		currentExpense.setAccountId(expense.getAccountId());
		currentExpense.setPaymentMethodId(expense.getPaymentMethodId());
		currentExpense.setProjectId(expense.getProjectId());
		currentExpense.setSupplierId(expense.getSupplierId());
		currentExpense.setAmount(expense.getAmount());
		currentExpense.setAccountId(expense.getAccountId());
		currentExpense.setNotes(expense.getNotes());

		expenseDao.save(currentExpense);

		return new ResponseEntity<Expense>(currentExpense, HttpStatus.OK);
	}

	// ------------------- Delete a User-----------------------------------------

	// @RequestMapping(value = "/expense/{id}", method = RequestMethod.DELETE)
	// public ResponseEntity<?> deleteExpense(@PathVariable("id") int id) {
	// logger.info("Fetching & Deleting User with id {}", id);

	// Expense expense = expenseService.findById(id);
	// if (expense == null) {
	// logger.error("Unable to delete. User with id {} not found.", id);
	// return new ResponseEntity(new CustomErrorType("Unable to delete. User with id
	// " + id + " not found."),
	//// HttpStatus.NOT_FOUND);
	// }
	// expenseService.deleteExpenseById(id);
	// return new ResponseEntity<Expense>(HttpStatus.NO_CONTENT);
	// }

	// ------------------- Delete All Users-----------------------------

	// @RequestMapping(value = "/users/", method = RequestMethod.DELETE)
	// public ResponseEntity<Expense> deleteAllExpenses() {
	// logger.info("Deleting All Users");

	// expenseService.deleteAllExpenses();
	// return new ResponseEntity<Expense>(HttpStatus.NO_CONTENT);
	// }

}
