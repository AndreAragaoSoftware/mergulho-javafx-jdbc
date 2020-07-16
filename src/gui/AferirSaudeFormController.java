package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import model.entities.AferirSaude;
import model.entities.Mergulhador;
import model.exception.ValidationException;
import model.services.AferirSaudeService;
import model.services.MergulhadorService;

public class AferirSaudeFormController implements Initializable {
	
	private AferirSaude entity;
	
	private AferirSaudeService service;
	
	private MergulhadorService mergulhadorService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtPressaoArterialSistolica;
	@FXML
	private TextField txtPressaoarterialDiastolica;
	@FXML
	private TextField txtPulsacao;
	@FXML
	private TextField txtTemperaturaCorporal;
	@FXML
	private TextField txtImc;
	@FXML
	private DatePicker dpDataAfericao;
	@FXML
	private RadioButton rbSintomasSim;
	@FXML
	private RadioButton rbSinomasNao;
	@FXML 
	private ToggleGroup grupo;
	@FXML
	private ComboBox<Mergulhador> comboBoxMergulhador;
	@FXML
	private Label labelErrorPAS;
	@FXML
	private Label labelErrorPAD;
	@FXML
	private Label labelErrorPul;
	@FXML
	private Label labelErrorTC;
	@FXML
	private Label labelErrorImc;
	@FXML
	private Label labelErrorSintomas;
	@FXML
	private Label labelErrorData;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	@FXML
	private ObservableList<Mergulhador> obsList;
	
	public void setAferirSaude(AferirSaude entity) {
		this.entity = entity;
	}
	
	public void setAferirSuadeService(AferirSaudeService service, MergulhadorService mergulhadorService) {
		this.service = service;
		this.mergulhadorService = mergulhadorService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			// o getFormData retorna o objeto pelo na tabela departmentForm.fxml
			entity = getFormData();
			// salvar no banco de dados
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			// Fechar a janela
			Utils.currentStage(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessagens(e.getErrors());
		}

	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		// Fechar a janela
		Utils.currentStage(event).close();
	}
	
	
	// metodo para atulizar a lista 
		private void notifyDataChangeListeners() {
			for (DataChangeListener listener : dataChangeListeners) {
				listener.onDataChanged();
			}

		}
		
		private AferirSaude getFormData() {
			// instanciando o obj no Seller
			AferirSaude obj = new AferirSaude();

			// instanciando a exception
			ValidationException exception = new ValidationException("Error de validação");

			// pegando o texto da caixa txtId
			// foi utilizado o metodo tryParse para tranformar em int
			obj.setId(Utils.tryParseToInt(txtId.getText()));
	
			if (txtPressaoArterialSistolica.getText() == null || txtPressaoArterialSistolica.getText().trim().equals("")) {
				// foi add um erro caso o campo estiver vazio
				exception.addError("pressaoArterialSistolica", "O campo não pode estar vazio");
			}
			//foi o usado o metodo try para converte o salario
			obj.setPressaoArterialSistolica(Utils.tryParseToDouble(txtPressaoArterialSistolica.getText()));;
			
			if (txtPressaoarterialDiastolica.getText() == null || txtPressaoarterialDiastolica.getText().trim().equals("")) {
				exception.addError("pressaoArterialDiastolica", "O campo não pode estar vazio");
			}
			//foi o usado o metodo try para converte o salario
			obj.setPressaoArterialDiastolica(Utils.tryParseToDouble(txtPressaoArterialSistolica.getText()));;
			
			if (txtPulsacao.getText() == null || txtPulsacao.getText().trim().equals("")) {
				exception.addError("pulsacao", "O campo não pode estar vazio");
			}
			obj.setPulsacao(Utils.tryParseToDouble(txtPulsacao.getText()));;
			
			if (txtTemperaturaCorporal.getText() == null || txtTemperaturaCorporal.getText().trim().equals("")) {
				exception.addError("temperaturaCorporal", "O campo não pode estar vazio");
			}
			obj.setTemperaturaCorporal(Utils.tryParseToDouble(txtTemperaturaCorporal.getText()));;
			
			if (txtImc.getText() == null || txtImc.getText().trim().equals("")) {
				exception.addError("imc", "O campo não pode estar vazio");
			}
			obj.setImc(Utils.tryParseToDouble(txtImc.getText()));
			
			if(dpDataAfericao.getValue() == null) {
				exception.addError("dataAfericao", "O campo não pode estar vazio");
			}
			else {
				Instant instant = Instant.from(dpDataAfericao.getValue().atStartOfDay(ZoneId.systemDefault()));
				obj.setDataAfericao(Date.from(instant));
			}
			
			RadioButton rbButton = (RadioButton) grupo.getSelectedToggle();
			
			if (rbButton.getText() == null || rbButton.getText().trim().equals(""))  {
				// foi add um erro caso o campo estiver vazio
				exception.addError("sintomas", "O campo não pode estar vazio");
			}
			
			obj.setSintomas(rbButton.getText());
			
			obj.setMergulhador(comboBoxMergulhador.getValue());
			
			// testando pra ve se existe pelomenos um erro
			if (exception.getErrors().size() > 0) {
				throw exception;
			}

			return obj;
		}
		
		// retrinções
		private void initializeNodes() {
			// o txtId só aceita numero Inteiro
			Constraints.setTextFieldInteger(txtId);
			Constraints.setTextFieldDouble(txtPressaoArterialSistolica);
			Constraints.setTextFieldDouble(txtPressaoarterialDiastolica);
			Constraints.setTextFieldDouble(txtPulsacao);
			Constraints.setTextFieldDouble(txtTemperaturaCorporal);
			Constraints.setTextFieldDouble(txtImc);
			Utils.formatDatePicker(dpDataAfericao, "dd/MM/yyyy");
			// metodo que inicializa o comboBox
			initializeComboBoxMergulhador();
		}
		
		public void upDateFormData() {
			if (entity == null) {
				throw new IllegalStateException("Entity was null");
			}
			// valueOf foi pra converter o id para String
			txtId.setText(String.valueOf(entity.getId()));
			Locale.setDefault(Locale.US);
			txtPressaoArterialSistolica.setText(String.format("%.2f", entity.getPressaoArterialSistolica()));
			txtPressaoarterialDiastolica.setText(String.format("%.2f", entity.getPressaoArterialDiastolica()));
			txtPulsacao.setText(String.format("%.2f", entity.getPulsacao()));
			txtTemperaturaCorporal.setText(String.format("%.1f", entity.getTemperaturaCorporal()));
			txtImc.setText(String.format("%.2f", entity.getImc()));
			
			if (entity.getDataAfericao() != null) {
				// Esse formato permite que o programa capture a data da maquina do usuario
				dpDataAfericao.setValue(LocalDate.ofInstant(entity.getDataAfericao().toInstant(), ZoneId.systemDefault()));
			}
			if(entity.getSintomas() == "Sim") {
				rbSintomasSim.setSelected(true);
			}
			else {
				rbSinomasNao.setSelected(true);
			}
			
			// testando se o mergulhador é novo
			if (entity.getMergulhador() == null) {
				// pega o primeiro elemento do comboBox
				comboBoxMergulhador.getSelectionModel().selectFirst();
			} else {
				comboBoxMergulhador.setValue(entity.getMergulhador());
			}
		}
		
		// carregar os objetos associados a comboBox
		public void loadAssociatedObjects() {
			if (mergulhadorService == null) {
				throw new IllegalAccessError("MergulhadorService é nulo");
			}
			List<Mergulhador> list = mergulhadorService.findAll();
			obsList = FXCollections.observableArrayList(list);
			comboBoxMergulhador.setItems(obsList);
		}
		private void setErrorMessagens(Map<String, String> errors) {
			Set<String> fields = errors.keySet();
			
			labelErrorPAS.setText((fields.contains("pressaoArterialSistolica") ? errors.get("pressaoArterialSistolica") : ""));
			labelErrorPAD.setText((fields.contains("pressaoArterialDiastolica") ? errors.get("pressaoArterialDiastolica") : ""));
			labelErrorPul.setText((fields.contains("pulsacao") ? errors.get("pulsacao") : ""));
			labelErrorTC.setText((fields.contains("temperaturaCorporal") ? errors.get("temperaturaCorporal") : ""));
			labelErrorImc.setText((fields.contains("imc") ? errors.get("imc") : ""));
			labelErrorData.setText((fields.contains("dataAfericao") ? errors.get("dataAfericao") : ""));
			labelErrorSintomas.setText((fields.contains("sintomas") ? errors.get("sintomas") : ""));
		}

	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeComboBoxMergulhador() {
		Callback<ListView<Mergulhador>, ListCell<Mergulhador>> factory = lv -> new ListCell<Mergulhador>() {
			@Override
			protected void updateItem(Mergulhador item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxMergulhador.setCellFactory(factory);
		comboBoxMergulhador.setButtonCell(factory.call(null));
	}
}
