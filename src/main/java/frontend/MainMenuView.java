package frontend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//Author: @Smit_Thakkar
import java.util.Scanner;

import com.csci5408project.Queries.*;
import com.csci5408project.validation.IdentifyQuery;
import com.csci5408project.validation.ValidateQuery;

import transactions.ExecuteTransaction;
import transactions.update;

public final class MainMenuView {

	private final MainPrinter printer;
	private final Scanner scanner;
	private final Session userSession;

	public MainMenuView(final MainPrinter printer, final Scanner scanner, final Session userSession) {
		this.printer = printer;
		this.scanner = scanner;
		this.userSession = userSession;
	}

	public void displayMainMenu() throws IOException {
		printer.printScreenTitle("Main Menu");
		while (true) {
			printer.printContent("1. Execute SQL Query.");
			printer.printContent("2. Generate SQL Dump.");
			printer.printContent("3. Generate ERD.");
			printer.printContent("4. Generate Data Dictionary.");
			printer.printContent("5. View Meta Data.");
			printer.printContent("6. Import SQL Dump.");
			printer.printContent("7. Logout.");
			printer.printContent("8. Transaction.");
			printer.printContent("Select an option:");
			final String input = scanner.nextLine();
			switch (input) {
			case "1":
				String userName = userSession.getLoggedInUser().toString();
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter use database query");
				String query = sc.nextLine();
				String databaseName = query.split(" ")[1];
				if(query.split(" ")[0].equalsIgnoreCase("use") && Files.isDirectory(Paths.get("bin/Databases/"+databaseName)))
				{
					int exitFlag = 0;
					while(exitFlag == 0)
					{
					System.out.println("Enter query : ");
					String newQuery = sc.nextLine();
					if(newQuery.equalsIgnoreCase("exit")) {
						exitFlag = 1;
						break;
					}
					IdentifyQuery iq = new IdentifyQuery();
					if(newQuery.split(" ")[0].equalsIgnoreCase("begin") && newQuery.split(" ")[1].equalsIgnoreCase("transaction"))
					{
						ExecuteTransaction transaction = new ExecuteTransaction();
						transaction.beginTransaction(databaseName);
						exitFlag = 1;
						break;
					}
					String queryType = iq.identifyQuery(newQuery).toString();
					System.out.println(queryType);
					
					
					if(queryType.equalsIgnoreCase("select"))
					{
						select select = new select();
						select.selectquery(newQuery, databaseName, userName);
					}
					
					if(queryType.equalsIgnoreCase("insert"))
					{
						insert insert = new insert();
						insert.insertQuery(newQuery, databaseName, userName);
					}
					
					if(queryType.equalsIgnoreCase("update"))
					{
						com.csci5408project.Queries.update update = new com.csci5408project.Queries.update();
						update.updateQuery(newQuery, databaseName, userName);
					}
					if(queryType.equalsIgnoreCase("delete"))
					{
						com.csci5408project.Queries.delete delete = new com.csci5408project.Queries.delete();
						delete.deleteQuery(newQuery, databaseName, userName);
					}
					if(queryType.equalsIgnoreCase("droptable")){
						deleteTable deleteTable = new deleteTable();
						deleteTable.deleteTableQuery(newQuery,databaseName);
					}
					if(queryType.equalsIgnoreCase("dropdatabase")){
						deleteDatabase deleteDatabase = new deleteDatabase();
						deleteDatabase.deleteDatabaseQuery(newQuery,databaseName);
					}
					if(queryType.equalsIgnoreCase("createdatabase")){
						createDatabase createDatabase = new createDatabase();
						createDatabase.createDatabaseQuery(newQuery,databaseName);
					}
					if(queryType.equalsIgnoreCase("createTable")){
						createTable createTable = new createTable();
						createTable.createTableQuery(newQuery,databaseName);
					}
					// check use database , database exists ?
					
					// 
					ValidateQuery validate = new ValidateQuery();
					//validate.getError(query , userSession.getLoggedInUser().toString())
					}
				}
				else
				{
					System.out.println("Please select a database");
				}
				break;
			case "2":

				break;
			case "3":

				break;
			case "4":

				break;
			case "5":

				break;
			case "6":

				break;
			case "7":
				userSession.destroyUserSession();
				return;
			case "8":
			default:
				break;
			}
		}
	}
}