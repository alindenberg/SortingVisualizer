package HelloWorld;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Main extends Application {

    private static final int screenHeight = 1000;
    private static final int screenWidth = 1000;

    private static final double menuHeight = Double.MAX_VALUE;
    private static final int menuWidth = 300;

    private static final int lineWidth = 5;

    private static final int offsetMultiple = 6;
    private static final int heightMultiple = 5;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gary the Goat Rulez");
        int[] dataPoints = IntStream.range(1, 51).toArray();

        Group root = new Group();
        Scene scene = new Scene(root, 1000, 1000);
        Canvas canvas = new Canvas(1000, 1000);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        DrawMenu(root, dataPoints, gc);
        DrawGraph(gc, dataPoints);

        stage.setScene(scene);
        stage.show();
    }

    private void DrawGraph(GraphicsContext gc, int[] dataPoints) {
        int xOffset = 0;
        for(int i = 0; i < dataPoints.length; i++) {
            int height = dataPoints[i]*5;
            xOffset += offsetMultiple;

            gc.setFill(Color.RED);
            gc.clearRect(menuWidth + xOffset, 500 - height, lineWidth, height);
            gc.fillRect(menuWidth + xOffset, 500 - height, lineWidth, height);
        }
    }

    private void ClearGraph(GraphicsContext gc, int[] dataPoints) {
        int xOffset = 0;
        for(int i = 0; i < dataPoints.length; i++) {
            int height = dataPoints[i]*5;
            xOffset += offsetMultiple;

            gc.clearRect(menuWidth + xOffset, 500 - height, lineWidth, height);
        }
    }

    private void DrawMenu(Group root, int[] dataPoints, GraphicsContext gc) {
        VBox menu = new VBox();
        menu.setMinHeight(menuHeight);
        menu.setMinWidth(menuWidth);

        Sorter sorter = new Sorter(dataPoints, gc, lineWidth, offsetMultiple, heightMultiple);

        ObservableList<String> sorts = FXCollections.observableArrayList("Bubble Sort", "Merge Sort", "Quick Sort", "Insertion Sort");
        ListView sortList = new ListView(sorts);
        sortList.getSelectionModel().select(0);

        Button sortBtn = new Button("Sort");
        sortBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Sorting");
                String selectedSort = (String)sortList.getSelectionModel().getSelectedItem();
                sorter.runSort(selectedSort);
            }
        });

        Button randomizeBtn = new Button("Randomize Data");
        randomizeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Randomizing");
                ClearGraph(gc, dataPoints);
                RandomizeData(dataPoints);
                DrawGraph(gc, dataPoints);
            }
        });

        menu.getChildren().addAll(sortList, sortBtn, randomizeBtn);
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
