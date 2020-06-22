package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Funcao;
import model.services.FuncaoService;

public class FuncaoListController implements Initializable, DataChangeListener {

	private FuncaoService service;

	@FXML
	private TableView<Funcao> tableViewFuncao;

	@FXML
	private TableColumn<Funcao, Integer> tableColumnId;

	@FXML
	private TableColumn<Funcao, Double> tableColumnNome;

	@FXML
	private TableColumn<Funcao, Funcao> tableColumnEDIT;

	@FXML
	private Button btNova;

	private ObservableList<Funcao> obsList;

	@FXML
	public void onBtNovaAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Funcao obj = new Funcao();
		createDialogForm(obj, "/gui/FuncaoForm.fxml", parentStage);
	}

	public void setFuncaoService(FuncaoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// padr�o para iniciar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));

		// macete para que a tableView acompanhe a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncao.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Servi�o foi nulo");
		}

		List<Funcao> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFuncao.setItems(obsList);
		initEditButtons();
	}

	private void createDialogForm(Funcao obj, String absoluteName, Stage parentStage) {
		try {
			// Instanciando FXMLLoader para poder abrir nova tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carregando o pane
			Pane pane = loader.load();

			// pegou o controller da tela que foi carregada acima
			FuncaoFormController controller = loader.getController();
			// setando o comtrolador
			controller.setFuncao(obj);
			// setando o FuncaoService
			controller.setFuncaoService(new FuncaoService());
			// evento que faz a atualiza��o da lista quando adcionado um novo departamento
			controller.subscribeDataChangeListener(this);
			// carregar o obj no formulario
			controller.upDateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Funcao data");
			dialogStage.setScene(new Scene(pane));
			// n�o pode ser rederizada
			dialogStage.setResizable(false);
			// chamando o palco
			dialogStage.initOwner(parentStage);
			// s� pode fazer outra coisa se for fechada
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a tela", e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	// metodo para colocar os bot�es de atualiza��o
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Funcao, Funcao>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Funcao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/FuncaoForm.fxml", Utils.currentStage(event)));
			}
		});
	}
}
