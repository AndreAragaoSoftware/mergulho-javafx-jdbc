package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.FuncaoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemMergulhador;
	@FXML
	private MenuItem menuItemFuncao;
	@FXML
	private MenuItem menuItemCadastrarClinico;

	@FXML
	public void onMenuItemMergulhadorAction() {
		System.out.println("onMenuItemMergulhadorAction");
	}

	@FXML
	public void onMenuItemFuncaoAction() {
		// loadView("/gui/FuncaoList.fxml");
		loadView("/gui/FuncaoList.fxml", (FuncaoListController controller) -> {
			controller.setFuncaoService(new FuncaoService());
			controller.updateTableView();
		});
		;
	}

	@FXML
	public void onMenuItemCadastrarClinicoAction() {
		loadView("/gui/CadastrarClinico.fxml", x -> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			// Instanciando FXMLLoader para poder abrir nova tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			// o getRoot pega o primeiro elemento da View(no caso o ScrollPane)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// Primeiro filho do VBox na janela principal(mainMenu)
			Node mainMenu = mainVBox.getChildren().get(0);
			// limpando todos os filhos do main VBox
			mainVBox.getChildren().clear();

			// add os mainMenu e o newVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loadding view", e.getMessage(), AlertType.ERROR);
		}
	}

}
