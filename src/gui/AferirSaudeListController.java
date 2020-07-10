package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.AferirSaude;
import model.services.AferirSaudeService;
import model.services.MergulhadorService;

public class AferirSaudeListController implements Initializable, DataChangeListener {

	private AferirSaudeService service;

	@FXML
	private TableView<AferirSaude> tableViewAferirSaude;
	@FXML
	private TableColumn<AferirSaude, Integer> tableColumnId;
	@FXML
	private TableColumn<AferirSaude, String> tableColumnAferirSaude;
	@FXML
	private TableColumn<AferirSaude, Double> tableColumnPressaoArterialSistolica;
	@FXML
	private TableColumn<AferirSaude, Double> tableColumnPressaoArterialDiastolica;
	@FXML
	private TableColumn<AferirSaude, Double> tableColumnPulsacao;
	@FXML
	private TableColumn<AferirSaude, Double> tableColumnTemperaturaCorporal;
	@FXML
	private TableColumn<AferirSaude, Double> tableColumnImc;
	@FXML
	private TableColumn<AferirSaude, Date> tableColumnDataAfericao;
	@FXML
	private TableColumn<AferirSaude, Boolean> tableColumnSintomas;
	@FXML
	private TableColumn<AferirSaude, AferirSaude> tableColumnEDIT;
	@FXML
	private TableColumn<AferirSaude, AferirSaude> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<AferirSaude> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		AferirSaude obj = new AferirSaude();
		createDialogForm(obj, "/gui/AferirSuadeForm.fxml", parentStage);
	}
	
	public void setAferirSaudeService(AferirSaudeService service) {
		this.service = service;
	}
	
	private void createDialogForm(AferirSaude obj, String absoluteName, Stage parentStage) {
		try {
			// Instanciando FXMLLoader para poder abrir nova tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carregando o pane
			Pane pane = loader.load();

			// pegou o controller da tela que foi carregada acima
			AferirSaudeFormController controller = loader.getController();
			// setando o comtrolador
			controller.setAferirSaude(obj);
			// setando o AferirSaudeService
			controller.setAferirSuadeService(new AferirSaudeService(), new MergulhadorService());
			controller.loadAssociatedObjects();
			// evento que faz a atualização da lista quando adcionado um novo departamento
			controller.subscribeDataChangeListener(this);
			// carregar o obj no formulario
			controller.upDateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Aferição de Saúde");
			dialogStage.setScene(new Scene(pane));
			// não pode ser rederizada
			dialogStage.setResizable(false);
			// chamando o palco
			dialogStage.initOwner(parentStage);
			// só pode fazer outra coisa se for fechada
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		// padrão para iniciar o comportamento das colunas
		// O nome entre parentese tem que esta igual ao atributo do pacote
		// model.entities
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnPressaoArterialSistolica.setCellValueFactory(new PropertyValueFactory<>("pressaoArterialSistolica"));
		tableColumnPressaoArterialDiastolica.setCellValueFactory(new PropertyValueFactory<>("pressaoArterialDiastolica"));
		tableColumnPulsacao.setCellValueFactory(new PropertyValueFactory<>("pulsacao"));
		tableColumnTemperaturaCorporal.setCellValueFactory(new PropertyValueFactory<>("temperaturaCorporal"));
		tableColumnImc.setCellValueFactory(new PropertyValueFactory<>("imc"));
		Utils.formatTableColumnDate(tableColumnDataAfericao, "dd/MM/yyyy");

		Utils.formatTableColumnDouble(tableColumnPressaoArterialSistolica, 1);

		// macete para que a tableView acompanhe a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAferirSaude.prefHeightProperty().bind(stage.heightProperty());
	}
	
	// metodo responsavel por pegar os serviços e jogar na obsList
		public void updateTableView() {
			// se o programador esquecer de instanciar o service
			if (service == null) {
				throw new IllegalStateException("Sevice was null");
			}
			// list vai receber todos os dados do findAll
			List<AferirSaude> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			// pega os items e joga na tela
			tableViewAferirSaude.setItems(obsList);
			// chamando o metodo de atulização dos botões
			initEditButtons();
			// chamando o metodo de remoção 
			initRemoveButtons();
		}

	@Override
	public void onDataChanged() {
		updateTableView();

	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<AferirSaude, AferirSaude>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(AferirSaude obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
				event -> createDialogForm(obj, "/gui/AferirSaudeForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<AferirSaude, AferirSaude>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(AferirSaude obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	private void removeEntity(AferirSaude obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Você tem certeza que quer deletar?");

		// comfirmação que o usuario apertou no botão ok
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("O serviço foi nulo");
			}
			try {
				// excluindo o aferição
				service.remove(obj);
				// atualizando a tabela
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

}
