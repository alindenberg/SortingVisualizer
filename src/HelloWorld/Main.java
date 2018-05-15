package HelloWorld;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Main extends Application {

    private static final int menuHeight = 300;
    private static final int menuWidth = 200;

    private static final int lineWidth = 5;

//    the multipliers for offset and height of each datapoint
    private static final int offsetMultiple = 6;
    private static final int heightMultiple = 4;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gary the Goat Rulez");
        int[] dataPoints = IntStream.range(1, 101).toArray();

        Group root = new Group();
        Scene scene = new Scene(root, 1000, 600);
        Canvas canvas = new Canvas(1000, 600);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
//        Background color
        gc.setFill(Color.AQUA);
        gc.fillRect(0, 0, 1000, 600);

        DrawMenu(root, dataPoints, gc);
        DrawGraph(gc, dataPoints);

        stage.setScene(scene);
        stage.show();
    }

    private void DrawGraph(GraphicsContext gc, int[] dataPoints) {
        gc.setFill(Color.BLACK);
        int xOffset = 30;
        for(int i = 0; i < dataPoints.length; i++) {
            int height = dataPoints[i]*this.heightMultiple;
            xOffset += offsetMultiple;
            gc.fillRect(menuWidth + xOffset, 500 - height, lineWidth, height);
        }
    }

    private void ClearGraph(GraphicsContext gc, int[] dataPoints) {
        gc.setFill(Color.AQUA);
        int xOffset = 30;
        for(int i = 0; i < dataPoints.length; i++) {
            xOffset += offsetMultiple;
            gc.fillRect(menuWidth + xOffset, 0, lineWidth, 500);
        }
    }

    private void DrawMenu(Group root, int[] dataPoints, GraphicsContext gc) {
        VBox menu = new VBox();
        menu.setMaxHeight(menuHeight);
        menu.setMaxWidth(menuWidth);

        Sorter sorter = new Sorter(dataPoints, gc, lineWidth, offsetMultiple, heightMultiple, menuWidth);

        ObservableList<String> sorts = FXCollections.observableArrayList("Bubble Sort", "Merge Sort", "Quick Sort", "Insertion Sort", "Selection Sort");
        ListView sortList = new ListView(sorts);
        sortList.getSelectionModel().select(0);

//        Animation delay slider & label
        Slider animationDelaySlider = new Slider(0, 100, 50);
        animationDelaySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sorter.SetAnimationDelay(100 - (int)animationDelaySlider.getValue());
            }
        });

        Label sliderLabel = new Label("Sort speed");
        sliderLabel.setLabelFor(animationDelaySlider);
        sliderLabel.setUnderline(true);

        Button sortBtn = new Button("Sort Data");
        sortBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedSort = (String)sortList.getSelectionModel().getSelectedItem();
                sorter.runSort(selectedSort);
//                System.out.println("run time is: " + runTime);
            }
        });

        Button randomizeBtn = new Button("Randomize Data");
        randomizeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClearGraph(gc, dataPoints);
                RandomizeData(dataPoints);
                DrawGraph(gc, dataPoints);
            }
        });

        sortBtn.setMinWidth(menuWidth);
        randomizeBtn.setMinWidth(menuWidth);

        menu.getChildren().addAll(sortList, sliderLabel, animationDelaySlider, sortBtn, randomizeBtn);
        menu.setSpacing(10);

        root.getChildren().add(menu);
    }

    private void RandomizeData(int[] dataPoints) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = dataPoints.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int temp = dataPoints[index];
            dataPoints[index] = dataPoints[i];
            dataPoints[i] = temp;
        }
    }
}
