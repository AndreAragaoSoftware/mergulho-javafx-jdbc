package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.exception.ValidationException;
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
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
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
		ValidationException exception = new ValidationException("Erro de Validação");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Erro no campo name");
		}

		obj.setName(txtName.getText());

		// testando pra ve se existe pelomenos um erro
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

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

	// dando um set na labelErrorName caso exista um erro
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
