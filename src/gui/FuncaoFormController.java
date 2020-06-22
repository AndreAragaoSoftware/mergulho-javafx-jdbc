package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Funcao;

public class FuncaoFormController implements Initializable {

	private Funcao entity;
	
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
	public void setFuncao(Funcao entity) {
		this.entity = entity;
	}
	
	@FXML
	public void onBtSalvarAction() {
		System.out.println("onBtSalvarAction");
	}
	
	@FXML
	public void onBtCancelarAction() {
		System.out.println("onBtCancelarAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	// retrinções 
		private void initializeNodes() {
			// o txtId só aceita numero Inteiro
			Constraints.setTextFieldInteger(txtId);
			// o txtName só pode ter no maximo 60 caracters
			Constraints.setTextFieldMaxLength(txtName, 60);
		}
		
	public void upDateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity é nula");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}	

}
