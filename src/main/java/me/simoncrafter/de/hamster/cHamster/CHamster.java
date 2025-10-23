package me.simoncrafter.de.hamster.cHamster;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import me.simoncrafter.de.hamster.compiler.controller.CompilerController;
import me.simoncrafter.de.hamster.debugger.controller.DebuggerController;
import me.simoncrafter.de.hamster.editor.controller.EditorController;
import me.simoncrafter.de.hamster.interpreter.Hamster;
import me.simoncrafter.de.hamster.interpreter.Territorium;
import me.simoncrafter.de.hamster.mod.UIStyleController;
import me.simoncrafter.de.hamster.simulation.controller.SimulationController;
import me.simoncrafter.de.hamster.simulation.model.LogEntry;
import me.simoncrafter.de.hamster.simulation.model.SimulationModel;
import me.simoncrafter.de.hamster.workbench.Workbench;
import me.simoncrafter.de.hamster.workbench.WorkbenchModel;
import me.simoncrafter.de.hamster.workbench.WorkbenchView;
import org.jruby.RubyProcess;

import javax.swing.*;

public class CHamster extends Application {
    public Hamster hamster;
    public SimulationModel simModel;
    public SimulationController simController;
    public SimulationController simBody;
    public Workbench wbBody;
    public EditorController editorBody;
    public CompilerController compilerBody;
    public DebuggerController dbgBody;
    public WorkbenchModel wbModel;
    public WorkbenchView wbView;
    public DebuggerController dbgController;

    public static void Log(String tLog)
    {
        int i = 999;
        i++;
        Workbench.getWorkbench().getSimulationController().getLogPanel().logEntry(tLog, "", false, 0);
    }

    @Override
    public void start(Stage primaryStage) {
        wbBody = Workbench.getWorkbench();
        editorBody = wbBody.getEditor();
        simModel = wbBody.getSimulation().getSimulationModel();
        simController = wbBody.getSimulationController();
        compilerBody = wbBody.getComiler();
        dbgBody = wbBody.getDebugger();
        wbModel = wbBody.getModel();
        simBody = wbBody.getSimulation();
        wbView = wbBody.getView();
        dbgController = wbBody.getDebuggerController();

        hamster = Hamster.getStandardHamster();

        int reiheHamster = hamster.getReihe();
        int spalteHamster = hamster.getSpalte();

        VBox layout = new VBox(10);
        Label label = new Label("CHamster V1 Main Menu");
        Button button = new Button("Update UI");
        CheckBox checkBox = new CheckBox("High Mode");

        Label label2 = new Label("FOLGENDE FUNKTIONEN NUR WÄHREND DER SIMULATION VERWENDEN.");
        Button button2 = new Button("New Hamster");
        Button button3 = new Button("Log");
        Button button4 = new Button("Clear Log");
        Button button5 = new Button("Test");
        TextField textField = new TextField();
        textField.setPromptText("Debug Log...");

        checkBox.setSelected(false);
        button.setOnAction(e -> UIStyleController.init());
        button2.setOnAction(e -> {hamster.clone();});
        button3.setOnAction(e -> {
            String msgLog = textField.getText();
            Log(msgLog);
        });
        button4.setOnAction(e -> {
            simController.getLogPanel().clearLog();
        });
        button5.setOnAction(e -> {

        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            if (checkBox.isSelected()) {
                UIStyleController.init();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        layout.getChildren().addAll(
                label,
                button,
                checkBox,
                button2,
                button3,
                button4,
                textField
        );

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setTitle("CHamster V1 by Fabio and Simon");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Optional, falls du das Fenster von einer anderen Klasse starten willst:
    public static void cLoadWindow(String[] args) {
        launch(args);
    }
}