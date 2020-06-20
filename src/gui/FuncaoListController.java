package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Funcao;

public class FuncaoListController implements Initializable {

	@FXML
	private TableView<Funcao> tableViewFuncao;
	
	@FXML
	private TableColumn<Funcao, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Funcao, Double> tableColumnNome;
	
	@FXML
	private Button btNova;
	
	@FXML
	public void onBtNovaAction() {
		System.out.println("onBtNovaAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		//padrão para iniciar o comportamento das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));

		//macete para que a tableView acompanhe a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncao.prefHeightProperty().bind(stage.heightProperty());

	}
	
}
