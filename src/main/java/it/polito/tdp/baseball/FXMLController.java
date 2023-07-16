package it.polito.tdp.baseball;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

//import it.polito.tdp.baseball.model.Grado;
import it.polito.tdp.baseball.model.Model;
import it.polito.tdp.baseball.model.People;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnesse;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnDreamTeam;

    @FXML
    private Button btnGradoMassimo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtYear;

    
    
    @FXML
    void doCalcolaConnesse(ActionEvent event) {
    	String msg = "";
    	msg = this.model.doCalcolaConnesse();
    	txtResult.appendText(msg);
    }

    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int anno = 0;
    	double salario = 0.0;
    	try {
    		anno = Integer.parseInt( this.txtYear.getText() );
    		salario = Double.parseDouble( this.txtSalary.getText() ) * 1000000;
    	}catch(NumberFormatException e) {
    		txtResult.setText("L'anno ed il salario devono essere dei numeri");
    		return;
    	}
    	if (salario<=0) {
    		txtResult.setText("Il salario deve essere un numero positivo.");
    		return;
    	}
    	if (anno<1871 || anno > 2019) {
    		txtResult.setText("L'anno deve essere compreso tra il 1871 ed il 2019.");
    		return;
    	}
    	
    	
    	//Abbiamo implementato diverse versioni di metodo per creare il grafo
    	this.model.creaGrafo_con_aggregazione_in_java(anno, salario); 	//<- VERSIONE 1
//    	this.model.creaGrafo_con_aggregazione_via_query(anno, salario);	//<- VERSIONE 2
    	
    	this.txtResult.setText("Grafo creato.\n");
    	this.txtResult.appendText("Ci sono " + this.model.nVertici() + " vertici\n");
    	this.txtResult.appendText("Ci sono " + this.model.nArchi() + " archi\n\n");
    	
    	
    	if(this.model.nVertici()==0) {
    		this.btnConnesse.setDisable(true);
    		this.btnGradoMassimo.setDisable(true);
    		this.btnDreamTeam.setDisable(true);
    		this.txtResult.appendText("Non è possibile fare alcuna analisi ulteriore!\n");
    	} else {
    		this.btnConnesse.setDisable(false);
    		this.btnGradoMassimo.setDisable(false);
    		this.btnDreamTeam.setDisable(false);
    	}
    	
    }

    
    @FXML
    void doDreamTeam(ActionEvent event) {
    	
    }

    
    @FXML
    void doGradoMassimo(ActionEvent event) {
    	String msg = "";
    	msg = this.model.doGradoMassimo();
    	txtResult.appendText(msg);
    }

    
    @FXML
    void initialize() {
        assert btnConnesse != null : "fx:id=\"btnConnesse\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGradoMassimo != null : "fx:id=\"btnGradoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
