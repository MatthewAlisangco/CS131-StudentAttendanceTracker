
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
import java.text.ParseException;
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
	 /** Global instance of the scopes required by this quickstart.
    *
    * If modifying these scopes, delete your previously saved credentials
    * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
    */
		private int colCount = 6;	
		private static final long serialVersionUID = 1L;
		/** Application name. */
		private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
		/** Directory to store user credentials for this application. */
	    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials//sheets.googleapis.com-java-quickstart.json");
	    /** Global instance of the {@link FileDataStoreFactory}. */ 
	    private static FileDataStoreFactory DATA_STORE_FACTORY;
	    /** Global instance of the JSON factory. */
	    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		/** Global instance of the HTTP transport. */
	    private static HttpTransport HTTP_TRANSPORT;
	    private static final List<String> SCOPES = Arrays.asList( SheetsScopes.SPREADSHEETS );
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
	//	String studentid = request.getParameter("instudentid");
	//	String key = request.getParameter("inKey");
		
		//System.out.println(studentid);
		//System.out.println(key);
	}

   //private static final List<String> SCOPES =
   //    Arrays.asList( SheetsScopes.SPREADSHEETS );
/** google method DO-NOT-EDIT */
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
    * DO-NOT-EDIT!!!!!!
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
	 * checkDate - compares current date, and date from spreadsheet.
	 * @return 0 if it equals the date, 1 if it is before the date
	 */
   private int checkDate(String oldDate,String timeStamp){
	   int ans = -1;
	   SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
   	try {
		Date date1 = sdf.parse(oldDate);
		Date date2 = sdf.parse(timeStamp);
		if(date1.equals(date2) && colCount <=26){
			ans = 0;
			
		} 
		if(date1.before(date2) && colCount <=26){
			ans = 1;
			colCount = colCount + 1;
		}
		if(colCount == 26){
			colCount = 6;
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	   return ans;
   }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowCount = 0;
		String studentid = request.getParameter("instudentid");
		String key = request.getParameter("inKey");
		String timeStamp = new SimpleDateFormat("M/dd/yyyy").format(new Date());
		System.out.println(studentid);
		System.out.println(key);
		System.out.println(timeStamp);
		//* Sample Code *//
        Sheets service = getSheetsService(); // Build a new authorized API client service.
        String spreadsheetId = "ZEf3s6WbhBiSegAEbRRFDHHtrWwpYHgLeYHU1iKtnks"; //PUT YOUR GOOGLE SHEET ID HERE
        String range = "Sheet1!A1:Z";
    
        ValueRange response2 = service.spreadsheets().values().get(spreadsheetId, range).execute(); //search 
            List<List<Object>> valuesSearch = response2.getValues();
            if (valuesSearch == null || valuesSearch.size() == 0) {
                System.out.println("No data found.");
            } else {
            	String oldDate = (String) valuesSearch.get(0).get(colCount);
            	int curdate = checkDate(oldDate,timeStamp); //check if old date is before or equal to current date
            	System.out.println("currentdate" + curdate);
            	System.out.println("colCount " + colCount);
              for (int i = 0 ; i < valuesSearch.size(); i++) {	 
            	  if(valuesSearch.get(i).get(3).equals(studentid)){
            		  rowCount = i;
            		  String getSID =  (String) valuesSearch.get(i).get(3);
            		  String getLastname = (String) valuesSearch.get(i).get(0);
            		  String getFirstname = (String) valuesSearch.get(i).get(1); //getFirstname
            		  request.setAttribute("getFirstname", getFirstname);
            		  request.setAttribute("getLastname", getLastname);
            		  request.getRequestDispatcher("/Success.jsp").forward(request, response);
            		  
            	  }//endif
            	}//endfor
        List<Request> requests = new ArrayList<Request>(); // Create requests object
        List<CellData> values = new ArrayList<CellData>(); // Create values object
    	List<CellData> valuesNew = new ArrayList<CellData>(); // Create values new object
        values.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue((timeStamp))));  // Add string 6/21/2016 value

        // Prepare request with proper row and column and its value
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(0)     // set the row to row 0 
                                .setColumnIndex(colCount)) // set the new column 6 to value 9/12/2016 at row 0  //6
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));
        
         BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
         service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
     	
         valuesNew.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(("Y")))); //add string values
        // // Prepare request with proper row and column and its value
         requests.add(new Request()                                            
                 .setUpdateCells(new UpdateCellsRequest()
                         .setStart(new GridCoordinate()
                                 .setSheetId(0)
                                 .setRowIndex(rowCount)     // set the row to row 1
                                 .setColumnIndex(colCount)) // set the new column 6 to value "Y" at row 1
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
