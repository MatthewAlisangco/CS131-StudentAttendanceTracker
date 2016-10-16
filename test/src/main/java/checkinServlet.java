
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class checkinServlet
 */
public class checkinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String studentid = request.getParameter("instudentid");
		String key = request.getParameter("inKey");
		
		System.out.println(studentid);
		System.out.println(key);
	}
	
	/** Application name. */
	  private static final String APPLICATION_NAME =
		        "Google Sheets API Java Quickstart";

		    /** Directory to store user credentials for this application. */
		    private static final java.io.File DATA_STORE_DIR = new java.io.File(
		        System.getProperty("user.home"), ".credentials//sheets.googleapis.com-java-quickstart.json");

		    /** Global instance of the {@link FileDataStoreFactory}. */
		    private static FileDataStoreFactory DATA_STORE_FACTORY;

		    /** Global instance of the JSON factory. */
		    private static final JsonFactory JSON_FACTORY =
		        JacksonFactory.getDefaultInstance();

		    /** Global instance of the HTTP transport. */
		    private static HttpTransport HTTP_TRANSPORT;

	
		    
	
	
	
	
	 /** Global instance of the scopes required by this quickstart.
    *
    * If modifying these scopes, delete your previously saved credentials
    * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
    */
   private static final List<String> SCOPES =
       Arrays.asList( SheetsScopes.SPREADSHEETS );

   static {
       try {
           HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
           DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
       } catch (Throwable t) {
           t.printStackTrace();
           System.exit(1);
       }
   }

	
   /**
    *  * DO-NOT-EDIT!!!!!!
    * Creates an authorized Credential object.
    * @return an authorized Credential object.
    * @throws IOException
    */
	
    public static Credential authorize() throws IOException {
        // Load client secrets.
        // Place client_secret.json file location here
        InputStream in = checkinServlet.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
	
	
	
    /**
     * DO-NOT-EDIT!!!!!!
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
	
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowCount = 0;
		String studentid = request.getParameter("instudentid");
		String key = request.getParameter("inKey");
		String timeStamp = new SimpleDateFormat("M/dd/yyyy").format(new Date());
		response.sendRedirect("Success.jsp");
		
		System.out.println(studentid);
		System.out.println(key);
		System.out.println(timeStamp);
		
		//* Sample Code *//
	    // Build a new authorized API client service.
        Sheets service = getSheetsService();
        // (OLD) Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        String spreadsheetId = "11irer29652Pz5vqpINgG-xo5fZ0k9fAkJuOb6JZlkZQ"; //PUT YOUR GOOGLE SHEET ID HERER
        String range = "Sheet1!A1:Z";
               
        //search
        ValueRange response2 = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
            List<List<Object>> valuesSearch = response2.getValues();
            if (valuesSearch == null || valuesSearch.size() == 0) {
                System.out.println("No data found.");
            } else {
   
            	//for (List row : valuesSearch) {
              for (int i = 0 ; i < valuesSearch.size(); i++) {
         
            	//  System.out.println((row.get(0).equals(studentid)));  
            	  //if studentid matches user input, get
            	  if(valuesSearch.get(i).get(3).equals(studentid)){
            		  rowCount = i;
            		  System.out.println("if statement" + rowCount);
            		  String getSID =  (String) valuesSearch.get(i).get(3);
            		  String getLastname = (String) valuesSearch.get(i).get(0);
            		  String getFirstname = (String) valuesSearch.get(i).get(1);
 
            		  System.out.println( "Welcome to class, " + getFirstname + " " + getLastname);
            	  }//endif
    	  
            	}//endfor
 
        // Create requests object
        List<Request> requests = new ArrayList<Request>();
        
        // Create values object
        List<CellData> values = new ArrayList<CellData>();
        
        // Add string 6/21/2016 value
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue((timeStamp))));

        // Prepare request with proper row and column and its value
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(0)     // set the row to row 0 
                                .setColumnIndex(6)) // set the new column 6 to value 9/12/2016 at row 0
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
        
         BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
     	        .setRequests(requests);
     	service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
     	        .execute();
     	
     	List<CellData> valuesNew = new ArrayList<CellData>();
         // Add string 6/21/2016 value
         valuesNew.add(new CellData()
                 .setUserEnteredValue(new ExtendedValue()
                         .setStringValue(("Y"))));

         // Prepare request with proper row and column and its value
         requests.add(new Request()
                 .setUpdateCells(new UpdateCellsRequest()
                         .setStart(new GridCoordinate()
                                 .setSheetId(0)
                                 .setRowIndex(rowCount)     // set the row to row 1
                                 .setColumnIndex(6)) // set the new column 6 to value "Y" at row 1
                         .setRows(Arrays.asList(
                                 new RowData().setValues(valuesNew)))
                         .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));        
         BatchUpdateSpreadsheetRequest batchUpdateRequestNew = new BatchUpdateSpreadsheetRequest()
     	        .setRequests(requests);
     	service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequestNew)
     	        .execute();    
		
		//samplecodeend		
	}

}}
