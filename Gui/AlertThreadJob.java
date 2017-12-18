package Gui;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertThreadJob implements Runnable {

	private Alert currentAlert;
	private String headerText, contentText, titleText, buttonText;
	private AlertType _type;
	private ButtonType _theButton;
	private Boolean buttonPressed;
	
	
	public AlertThreadJob(AlertType type, String headerText, String contentText, String titleText, String buttonText){
		
		this._type = type;
		this.headerText = headerText;
		this.contentText = contentText;
		this.buttonText = buttonText;
		this.titleText = titleText;
		setButtonPressed(false);
		
	}
	
	
	public void run() {
		
		
		
		currentAlert = new Alert(_type);
		System.out.println("Ausgeführt!!");
		currentAlert.setTitle(titleText);
		currentAlert.setContentText(contentText);
		currentAlert.setHeaderText(headerText);
		
		_theButton = new ButtonType(buttonText);
		currentAlert.getButtonTypes().setAll(_theButton);
		
		//setButtonPressed(true);
		
		
		
	}
	
	public String getHeaderText(){
		return headerText;
		
	}
	
	public String getContentText(){
		return contentText;
		
	}
	
	public String getTitleText(){
		return titleText;
		
	}
	
	public void setHeaderText(String text){
		this.headerText=text;
	}
	
	public void setContentText(String text){
		this.contentText=text;
	}
	
	public void setTitleText(String text){
		this.titleText=text;
	}

	public Alert getAlert(){
		return this.currentAlert;
	}


	public String getButtonText() {
		return buttonText;
	}


	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}


	public Boolean getButtonPressed() {
		return buttonPressed;
	}


	public void setButtonPressed(Boolean buttonPressed) {
		this.buttonPressed = buttonPressed;
	}
}
