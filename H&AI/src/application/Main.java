package application;
	
import java.sql.Connection;

import developement.FileProcess;
import developement.sqliteConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;



public class Main extends Application {

	private Stage primaryStage;
	public static Connection connection = null;
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}



	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
	
	public  Main(){
		
	}


	@Override
	public void start(Stage primaryStage) {
		try {
			
			 	AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("/frames/h&ai.fxml"));
	            Scene scene = new Scene(page);
	            primaryStage.setScene(scene);
	            primaryStage.setTitle("FXML is Simple");
	            primaryStage.show();
	    
	            connection = sqliteConnection.dbConnector();
	          
	      
	            
	           
	            
	            
	 //     Controller con = new Controller();
	 //     con.adminCheck();
	            
	         //   Button admin;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
	//	launch(args);
		
		Application.launch(Main.class, (java.lang.String[])null);
	}
}
