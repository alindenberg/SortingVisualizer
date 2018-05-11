package HelloWorld;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;

import java.awt.*;

public class Sorter {

    private int[] dataPoints;
    private GraphicsContext gc;

    public Sorter(int[] dataPoints, GraphicsContext gc) {
        this.dataPoints = dataPoints;
        this.gc = gc;
    }

    public void runSort(String sort) {
        switch (sort) {
            case "Bubble Sort":
                System.out.println("Bubble sort");
                BubbleSort(this.dataPoints);
                break;
            case "Merge Sort":
                System.out.println("Merge sort");
                break;
            case "Quick Sort":
                System.out.println("Quick sort");
                break;
            case "InsertionSort":
                System.out.println("Insertion sort");
                break;
            default:
                break;
        }
    }

    private void MergeSort() {
        System.out.println("Merge sort");
    }

    private void BubbleSort(int[] dataPoints) {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int width = 5;
                int offsetMultiple = 6;
                int heightMultiple = 5;
                for(int i = 0; i < dataPoints.length-1; i++) {
                    for(int j = 0; j < dataPoints.length - i - 1; j++) {
                        if (dataPoints[j] > dataPoints[j + 1]) {

                            final int index = j;
                            final int indexOffset = offsetMultiple + index * offsetMultiple;
                            final int indexHeight = dataPoints[index] * heightMultiple;

                            final int nextIndex = j + 1;
                            final int nextIndexOffset = offsetMultiple + nextIndex * offsetMultiple;
                            final int nextIndexHeight = dataPoints[nextIndex] * heightMultiple;

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // recolor all datapoints as red, removing the highlighted one from previous iteration
                                    for (int i = 0; i < dataPoints.length; i++) {
                                        DrawDataPoint(i, width, offsetMultiple, heightMultiple, Color.RED);
                                    }
                                    // clear both points to be swapped
                                    gc.clearRect(300 + indexOffset, 500 - indexHeight, width, indexHeight);
                                    gc.clearRect(300 + nextIndexOffset, 500 - indexHeight, width, nextIndexHeight);

                                    // swap data points
                                    Swap(index, nextIndex);

                                    // redraw new points, highlighting the one that has been swapped forward
                                    DrawDataPoint(nextIndex, width, offsetMultiple, heightMultiple, Color.YELLOW);
                                    DrawDataPoint(index, width, offsetMultiple, heightMultiple, Color.RED);
                                }
                            });
                            Thread.sleep(300);
                        }
                    }
                }
                for(int i = 0; i < dataPoints.length; i++) {
                    DrawDataPoint(i, width, offsetMultiple, heightMultiple, Color.RED);
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void QuickSort() {
        System.out.println("Quick sort");
    }

    private void InsertionSort() {
        System.out.println("Insertion Sort");
    }

    private void Swap(int index, int nextIndex) {
        int temp = this.dataPoints[index];
        this.dataPoints[index] = this.dataPoints[nextIndex];
        this.dataPoints[nextIndex] = temp;
    }

    private void DrawDataPoint(int index, int width, int offsetMultiple, int heightMultiple, Color color) {
        int xOffset = 300 + offsetMultiple + index*offsetMultiple;
        int height = dataPoints[index]*heightMultiple;

        gc.setFill(color);
        gc.clearRect(xOffset, 500 - height, width, height);
        gc.fillRect(xOffset, 500 - height, width, height);
    }
}
