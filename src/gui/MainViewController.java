package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemMergulhador;
	@FXML
	private MenuItem menuItemFuncao;
	@FXML
	private MenuItem menuItemCadastrar;
	
	@FXML
	public void onMenuItemMergulhadorAction() {
		System.out.println("onMenuItemMergulhadorAction");
	}
	
	@FXML
	public void onMenuItemFuncaoAction() {
		System.out.println("onMenuItemFuncaoAction");
	}
	
	@FXML
	public void onMenuItemCadastrarAction() {
		System.out.println("onMenuItemCadastrarAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

}
