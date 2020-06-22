package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Funcao;
import model.services.FuncaoService;

public class FuncaoFormController implements Initializable {

	private Funcao entity;

	private FuncaoService service;

	// intanciando a lista
	private List<DataChangeListener> dataChageListeners = new ArrayList<>();

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

	public void setFuncaoService(FuncaoService service) {
		this.service = service;
	}

	// sobrecrever a lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChageListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity é nula");
		}
		if (service == null) {
			throw new IllegalStateException("Service é nulo");
		}
		try {
			// pega da tabela
			entity = getFormData();
			// salva no banco
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			// fecha a tela
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	// metodo para atulizar a lista de departamento
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChageListeners) {
			listener.onDataChanged();
		}
	}

	private Funcao getFormData() {
		Funcao obj = new Funcao();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());

		return obj;

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
