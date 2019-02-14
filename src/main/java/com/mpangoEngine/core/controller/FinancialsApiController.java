package com.mpangoEngine.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.mpangoEngine.core.model.COAAccountType;
import com.mpangoEngine.core.model.ChartOfAccounts;
import com.mpangoEngine.core.model.Customer;
import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Farm;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.Project;
import com.mpangoEngine.core.model.ReportObject;
import com.mpangoEngine.core.model.Supplier;
import com.mpangoEngine.core.model.Transaction;
import com.mpangoEngine.core.util.CustomErrorType;
import com.mpangoEngine.core.util.ResponseModel;

@RestController
@RequestMapping("/api/financials")
public class FinancialsApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	private ExpenseDao expenseDao;
	@Autowired
	private IncomeDao incomeDao;
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private FarmDao farmDao;
	@Autowired
	private PaymentMethodDao paymentMethodDao;
	@Autowired
	private ChartOfAccountsDao chartOfAccountsDao;
	@Autowired
	private SupplierDao supplierDao;
	@Autowired
	CustomerDao customerDao;

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
	@RequestMapping(value = "/expense", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<ResponseModel> createExpense(@RequestBody Expense expense) {
		// logger.info("Creating Expense >>>> {}", expense);
		int rows = expenseDao.save(expense);
		int status = 1;
		String res = "FAILED";
		if (rows > 0) {
			res = "CREATED";
			status = 0;
		}
		ResponseModel response = new ResponseModel(status, res);
		return ResponseEntity.ok(response);
	}

	// ---------------- Create an Income --------------//
	@RequestMapping(value = "/income", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<?> createIncome(@RequestBody Income income, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Income >>>> ", income);
		int rows = incomeDao.save(income);
		int status = 1;
		String res = "FAILED";
		if (rows > 0) {
			res = "CREATED";
			status = 0;
		}
		ResponseModel response = new ResponseModel(status, res);
		return ResponseEntity.ok(response);
	}

	// ---------------- Create a a COA account --------------//
	@RequestMapping(value = "/coa/add", method = RequestMethod.POST)
	public ResponseEntity<?> createCOA(@RequestBody ChartOfAccounts coa, UriComponentsBuilder ucBuilder) {
		logger.info("FinancialsApiController->createCOA() :: >>>> ", coa);
		if (chartOfAccountsDao.existsById(coa.getId())) {
			return new ResponseEntity(new CustomErrorType("Error creating farm id:  " + coa.getId() + "."),
					HttpStatus.CONFLICT);
		}
		chartOfAccountsDao.save(coa);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/coa/{id}").buildAndExpand(coa.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	/* -----------retrieve COA ACCOUNT TYPES ----------- */
	@RequestMapping(value = "/coa/types", method = RequestMethod.GET)
	public ResponseEntity<List<COAAccountType>> getCOAAccountTypes() {
		List<COAAccountType> coa = chartOfAccountsDao.fetchAllAccountTypes();
		return new ResponseEntity<List<COAAccountType>>(coa, HttpStatus.OK);
	}

	/* -----------retrieve COA by a user ----------- */
	@RequestMapping(value = "/coa/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<ChartOfAccounts>> getCOAByUser(@PathVariable("userId") int userId) {
		List<ChartOfAccounts> coa = chartOfAccountsDao.findAllCOAByUser(userId);
		return new ResponseEntity<List<ChartOfAccounts>>(coa, HttpStatus.OK);
	}

	/* -----------retrieve suppliers by a user ----------- */
	@RequestMapping(value = "/suppliers/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Supplier>> getSuppliersByUser(@PathVariable("userId") int userId) {
		List<Supplier> suppliers = supplierDao.findAllSuppliersByUserId(userId);
		return new ResponseEntity<List<Supplier>>(suppliers, HttpStatus.OK);
	}

	/* -----------retrieve customers by a user ----------- */
	@RequestMapping(value = "/customers/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getCustomersByUser(@PathVariable("userId") int userId) {
		List<Customer> customers = customerDao.findAllCustomersByUserId(userId);
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	/* -----------retrieve details of a farm ----------- */
	@RequestMapping(value = "/farmdetails/{farmid}", method = RequestMethod.GET)
	public ResponseEntity<Farm> getFarm(@PathVariable("farmid") int farmid) {
		Farm farm = farmDao.findFarmById(farmid);
		return new ResponseEntity<Farm>(farm, HttpStatus.OK);
	}

	/* -----------retrieve farms by a user ----------- */
	@RequestMapping(value = "/farms/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<Farm>> getFarmsByUser(@PathVariable("userId") int userId) {
		List<Farm> farms = farmDao.findAllFarmsByUserId(userId);
		return new ResponseEntity<List<Farm>>(farms, HttpStatus.OK);
	}

	/* -----------retrieve details of a project ----------- */
	@RequestMapping(value = "/projectdetails/{projid}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getProject(@PathVariable("projid") int projid) {
		List<Map<String, Object>> projDetails = projectDao.findProjectDetails(projid);
		return new ResponseEntity<List<Map<String, Object>>>(projDetails, HttpStatus.OK);
	}

	/* ----------- Retrieve project expenses ----------------- */
	@RequestMapping(value = "/project/expenses/{projid}", method = RequestMethod.GET)
	public ResponseEntity<List<Expense>> listAllExpensesByProject(@PathVariable("projid") int projid) {
		logger.info("FinancialsApiController -> listAllExpensesByProject()  projid {}", projid);
		List<Expense> projExpenses = projectDao.findAllExpensesByProject(projid);
		if (projExpenses.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Expense>>(projExpenses, HttpStatus.OK);
	}

	/* ----------- Retrieve project incomes ----------------- */
	@RequestMapping(value = "/project/incomes/{projid}", method = RequestMethod.GET)
	public ResponseEntity<List<Income>> listAllIncomesByProject(@PathVariable("projid") int projid) {
		logger.info("FinancialsApiController -> listAllIncomesByProject()  projid {}", projid);
		List<Income> projIncomes = projectDao.findAllIncomesByProject(projid);

		if (projIncomes.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Income>>(projIncomes, HttpStatus.OK);
	}

	/* -------------Retrieve All projects for a user ------------- */
	@RequestMapping(value = "/projects/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> listAllProjectsByUser(@PathVariable("userid") int userid) {
		logger.info("Fetching projects for  userid {}", userid);
		List<Project> projects = projectDao.findAllProjectsByUser(userid);

		if (projects.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

	/*--------------------------------------------------------------------------------- 
	 * @@@@@@ @listAllIncomes()
	 * 	  -- Return : ResponseEntity<List<Transaction>> 
	 *    -- Retrieve All transactions for a user 
	 * --------------------------------------------------------------------------------*/
	@RequestMapping(value = "/transactions/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<Transaction>> userTransactions(@PathVariable("userid") int userid) {

		List<Transaction> transactionsArray = new ArrayList<>();

		logger.info("Fetching Expenses for  userid {}", userid);
		List<Income> incomes = incomeDao.findAllIncomesByUserId(userid);

		logger.info("Fetching Expenses for  userid {}", userid);
		List<Expense> expenses = expenseDao.findAllExpensesByUserId(userid);

		if (!incomes.isEmpty()) {
			for (Income income : incomes) {
				Transaction trx = new Transaction();

				String trxDesc = income.getProjectName() + " | " + income.getNotes() + " | " + income.getAccount();

				trx.setTransactionAmount(income.getAmount());
				trx.setTransactionDate(income.getIncomeDate());
				trx.setTransactionDescription(trxDesc);
				trx.setTransactionID(income.getId());
				trx.setTransactionType("INCOME");
				transactionsArray.add(trx);
			}
		}

		if (!expenses.isEmpty()) {
			for (Expense expense : expenses) {
				Transaction trx = new Transaction();

				String trxDesc = expense.getProjectName() + " | " + expense.getNotes() + " | " + expense.getAccount();

				trx.setTransactionAmount(expense.getAmount());
				trx.setTransactionDate(expense.getExpenseDate());
				trx.setTransactionDescription(trxDesc);
				trx.setTransactionID(expense.getId());
				trx.setTransactionType("EXPENSE");
				transactionsArray.add(trx);
			}
		}
		return new ResponseEntity<List<Transaction>>(transactionsArray, HttpStatus.OK);
	}

	/* -------------Retrieve All expenses for a user ------------- */
	@RequestMapping(value = "/expense/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<ReportObject>> listAllExpenses(@PathVariable("userid") int userid) {
		logger.info("Fetching Expenses for  userid {}", userid);
		List<ReportObject> expenses = expenseDao.findFinancialExpensesByUserId(userid);

		if (expenses.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<ReportObject>>(expenses, HttpStatus.OK);
	}

	/* -------------Retrieve All incomes for a user ------------- */
	@RequestMapping(value = "/income/user/{userid}", method = RequestMethod.GET)
	public ResponseEntity<List<ReportObject>> listAllIncomes(@PathVariable("userid") int userid) {
		logger.info("Fetching Incomes for  userid {}", userid);

		List<ReportObject> incomesx = incomeDao.findFinancialIncomesByUserId(userid);

		if (incomesx.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<ReportObject>>(incomesx, HttpStatus.OK);
	}
}
