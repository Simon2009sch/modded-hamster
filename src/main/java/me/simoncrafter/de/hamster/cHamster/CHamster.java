package me.simoncrafter.de.hamster.cHamster;

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
import me.simoncrafter.de.hamster.styles.controller.UIStyleController;
import me.simoncrafter.de.hamster.simulation.controller.SimulationController;
import me.simoncrafter.de.hamster.simulation.model.LogEntry;
import me.simoncrafter.de.hamster.simulation.model.SimulationModel;
import me.simoncrafter.de.hamster.simulation.view.SimulationPanel;
import me.simoncrafter.de.hamster.simulation.view.SimulationTools;
import me.simoncrafter.de.hamster.workbench.Workbench;
import me.simoncrafter.de.hamster.workbench.WorkbenchModel;
import me.simoncrafter.de.hamster.workbench.WorkbenchView;
import org.jruby.RubyProcess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public SimulationTools simTools;
    public SimulationPanel simPanel;

    private boolean highModeSelectedBefore = false; //by Simon

    int reiheHamster;
    int spalteHamster;

    int reiheTerritorium;
    int spaltenTerritorium;
    int koernerTerritorium;

    String calledText;

    String error_code_std = "E-9265358"; // unknown error
    String error_code_nc = "E-240711"; // no common usage
    String error_code_ni = "E-2412"; // non initialized

    String readFile(String path)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = br.readLine();
            }
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void cHinit()
    {
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
        simTools = simController.getSimulationTools();
        simPanel = simController.getSimulationPanel();

        hamster = Hamster.getStandardHamster();

        reiheHamster = hamster.getReihe();
        spalteHamster = hamster.getSpalte();

        reiheTerritorium = Territorium.getAnzahlReihen();
        spaltenTerritorium = Territorium.getAnzahlSpalten();
        koernerTerritorium = Territorium.getAnzahlKoerner();

        calledText = "Hamster-Anzahl " +
                Territorium.getAnzahlHamster()  + "\n" +
                "Anzahl Reihen: " + reiheTerritorium + "\n" +
                "Anzahl Spalten: " + spaltenTerritorium + "\n" +
                "Anzahl Körner: " + koernerTerritorium + "\n" +
                "Mod Menu made by Fabio, UI made by Simon\n";
    }

    public boolean simulationState()
    {
        // switch-case is slower lol
        if (simModel.getState() == SimulationModel.RUNNING) {
            return true;
        } else {
            return false;
        }
    }

    public static void Log(String tLog)
    {
        int i = 100;
        i++;
        Workbench.getWorkbench().getSimulationController().getLogPanel().logEntry(tLog, "", false, 0);
    }

    @Override
    public void start(Stage primaryStage) {
        cHinit();
        if (wbBody == null)  { Log("Error. " + error_code_ni); System.out.println("Error. " + error_code_ni); System.exit(0); }


        VBox layout = new VBox(10);
        Label label = new Label("CHamster V1 Main Menu");
        Button button = new Button("Update UI");
        CheckBox checkBox = new CheckBox("High Mode");

        Label label2 = new Label(calledText);
        Button updateButton = new Button("Update");
        Button button2 = new Button("New Hamster");
        Button button3 = new Button("Log");
        Button button4 = new Button("Clear Log");
        Button button5 = new Button("Zoom In");
        Button button6 = new Button("Zoom out");
        Button button8 = new Button("Remove Hamster");
        Button button9 = new Button("Test");
        Button button7  = new Button("Compile & Run");
        TextField textField = new TextField();
        textField.setPromptText("Debug Log...");

        updateButton.setOnAction(event -> {
            cHinit();
            label2.setText(calledText);
        });
        checkBox.setSelected(false);
        button.setOnAction(e -> UIStyleController.update());
        button2.setOnAction(e -> {hamster.clone();});
        button3.setOnAction(e -> {
            String msgLog = textField.getText();
            Log(msgLog);
        });
        button4.setOnAction(e -> {
            simController.getLogPanel().clearLog();
        });
        button5.setOnAction(e -> {
            simPanel.zoomIn();
        });
        button6.setOnAction(e -> {
            simPanel.zoomOut();
        });
        button8.setOnAction(e -> {
            simModel.removeHamster();
        });
        button7.setOnAction(e -> {
            simModel.start();
        });

        if(simModel.getState() == SimulationModel.RUNNING)
        {
            if(!hamster.maulLeer())
            {
                switch(hamster.getAnzahlKoerner())
                {
                    case 1:
                        for (int i=0; i<10;i++)
                        {
                            hamster.linksUm();
                            Log("High auf Cannabis.");
                        }
                    case 2:
                        for(int i=0; i<10;i++)
                        {
                            UIStyleController.update();
                            Log("High auf Koks.");
                        }
                    case 3:
                        for(int i=0; i<10;i++)
                        {
                            while(hamster.vornFrei())
                            {
                                hamster.vor();
                            }
                        }
                    case 4:

                    default:

                }
            }
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            if (checkBox.isSelected()) {
                UIStyleController.setRandomColorToEverything();
                highModeSelectedBefore  = true;
            } else if (highModeSelectedBefore) {
                UIStyleController.applyStyle(false);
                highModeSelectedBefore = false; //make it so only applies once because of performance
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        layout.getChildren().addAll(
                label,
                label2,
                updateButton,
                button,
                checkBox,
                button2,
                button5,
                button6,
                button8,
                textField,
                button3, // log btn under the text field
                button4,
                button7
        );

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setTitle("CHamster V1.0 by Fabio and Simon");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void cLoadWindow(String[] args) {
        launch(args);
    }
}