package gui;

import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Mergulhador;
import model.services.MergulhadorService;

public class MergulhadorListController implements Initializable, DataChangeListener {

	private MergulhadorService service;

	@FXML
	private TableView<Mergulhador> tableViewMergulhador;
	@FXML
	private TableColumn<Mergulhador, Integer> tableColumnId;
	@FXML
	private TableColumn<Mergulhador, String> tableColumnName;
	@FXML
	private TableColumn<Mergulhador, Mergulhador> tableColumnEDIT;
	@FXML
	private TableColumn<Mergulhador, Mergulhador> tableColumnREMOVE;
	@FXML
	private Button btNew;

	private ObservableList<Mergulhador> obsList;

	@FXML
	public void onBtAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Mergulhador obj = new Mergulhador();
	}

	public void setMergulhadorService(MergulhadorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// padrão para iniciar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// macete para que a tableView acompanhe a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMergulhador.prefHeightProperty().bind(stage.heightProperty());
	}

	// metodo responsavel por pegar os serviços e jogar na obsList
	public void updateTableView() {
		// se o programador esquecer de instanciar o service
		if (service == null) {
			throw new IllegalStateException("Sevice was null");
		}
		// list vai receber todos os dados do findAll
		List<Mergulhador> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		// pega os items e joga na tela
		tableViewMergulhador.setItems(obsList);
		// chamando o metodo de atulização dos botões
		initEditButtons();
		// chamando o metodo de remoção
		initRemoveButtons();
	}

//		private void createDialogForm(Mergulhador obj, String absoluteName, Stage parentStage) {
//			try {
//				// Instanciando FXMLLoader para poder abrir nova tela
//				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//				// carregando o pane
//				Pane pane = loader.load();
	//
//				// pegou o controller da tela que foi carregada acima
//				MergulhadorFormController controller = loader.getController();
//				// setando o comtrolador
//				controller.setMergulhador(obj);
//				// setando o MergulhadorService
//				controller.setMergulhadorService(new MergulhadorService());
//				// evento que faz a atualização da lista quando adcionado um novo departamento
//				controller.subscribeDataChangeListener(this);
//				// carregar o obj no formulario
//				controller.upDateFormData();
	//
//				Stage dialogStage = new Stage();
//				dialogStage.setTitle("Enter Mergulhador data");
//				dialogStage.setScene(new Scene(pane));
//				// não pode ser rederizada
//				dialogStage.setResizable(false);
//				// chamando o palco
//				dialogStage.initOwner(parentStage);
//				// só pode fazer outra coisa se for fechada
//				dialogStage.initModality(Modality.WINDOW_MODAL);
//				dialogStage.showAndWait();
	//
//			} catch (IOException e) {
//				Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//			}
//		}
	
	@Override
	public void onDataChanged() {
		// Aparti do momento que esse evento for disparado ele vai atualizar
		updateTableView();
	}

	// metodo para colocar os botões de atualização
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Mergulhador, Mergulhador>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Mergulhador obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
//					button.setOnAction(
//							event -> createDialogForm(obj, "/gui/MergulhadorForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Mergulhador, Mergulhador>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Mergulhador obj, boolean empty) {
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

	// esse metodo tem que ser criado fora do initRemoveButtons()
	private void removeEntity(Mergulhador obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmatio", "Are you sure to delete?");

		// comfirmação que o usuario apertou no botão ok
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				// excluindo o departamento
				service.remove(obj);
				// atualizando a tabela
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing objecrt", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

}
