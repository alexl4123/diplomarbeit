package Gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertThreadJob implements Runnable
{
  private Alert currentAlert;
  private String headerText;
  private String contentText;
  private String titleText;
  private String buttonText;
  private Alert.AlertType _type;
  private ButtonType _theButton;
  private Boolean buttonPressed;
  
  public AlertThreadJob(Alert.AlertType type, String headerText, String contentText, String titleText, String buttonText)
  {
    _type = type;
    this.headerText = headerText;
    this.contentText = contentText;
    this.buttonText = buttonText;
    this.titleText = titleText;
    setButtonPressed(Boolean.valueOf(false));
  }
  




  public void run()
  {
    currentAlert = new Alert(_type);
    System.out.println("Ausgeführt!!");
    currentAlert.setTitle(titleText);
    currentAlert.setContentText(contentText);
    currentAlert.setHeaderText(headerText);
    
    _theButton = new ButtonType(buttonText);
    currentAlert.getButtonTypes().setAll(new ButtonType[] { _theButton });
  }
  




  public String getHeaderText()
  {
    return headerText;
  }
  
  public String getContentText()
  {
    return contentText;
  }
  
  public String getTitleText()
  {
    return titleText;
  }
  
  public void setHeaderText(String text)
  {
    headerText = text;
  }
  
  public void setContentText(String text) {
    contentText = text;
  }
  
  public void setTitleText(String text) {
    titleText = text;
  }
  
  public Alert getAlert() {
    return currentAlert;
  }
  
  public String getButtonText()
  {
    return buttonText;
  }
  
  public void setButtonText(String buttonText)
  {
    this.buttonText = buttonText;
  }
  
  public Boolean getButtonPressed()
  {
    return buttonPressed;
  }
  
  public void setButtonPressed(Boolean buttonPressed)
  {
    this.buttonPressed = buttonPressed;
  }
}
