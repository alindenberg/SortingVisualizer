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
    private final int lineWidth;
    private final int offsetMultiple;
    private final int heightMultiple;
    private final int delay;

    public Sorter(int[] dataPoints, GraphicsContext gc, int lineWidth, int offsetMultiple, int heightMultiple) {
        this.dataPoints = dataPoints;
        this.gc = gc;
        this.lineWidth = lineWidth;
        this.offsetMultiple = offsetMultiple;
        this.heightMultiple = heightMultiple;
        this.delay = 50;
    }

    public void runSort(String sort) {;
        switch (sort) {
            case "Bubble Sort":
                System.out.println("Bubble sort");
                new Thread(() -> {
                    BubbleSort(this.dataPoints);
                }).start();
                break;
            case "Merge Sort":
                new Thread( () -> {
                    MergeSort(this.dataPoints, 0, this.dataPoints.length - 1);
                }).start();
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

    private void MergeSort(int[] dataPoints, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2;

            MergeSort(dataPoints, begin, mid);
            MergeSort(dataPoints, mid + 1, end);

            Merge(dataPoints, begin, mid, end);
        }
    }

    private void Merge(int[] dataPoints, int begin, int mid, int end) {
        int leftArrLength = mid - begin + 1;
        int rightArrLength = end - mid;

        int[] leftArr = new int[leftArrLength];
        int[] rightArr = new int[rightArrLength];

        for(int i = 0; i < leftArrLength; i++)
            leftArr[i] = dataPoints[begin + i];
        for(int j = 0; j < rightArrLength; j++)
            rightArr[j] = dataPoints[mid+j+1];

        int leftIndex = 0;
        int rightIndex = 0;
        int arrIndex = begin;

        while (leftIndex < leftArrLength && rightIndex < rightArrLength) {
            if (leftArr[leftIndex] <= rightArr[rightIndex]) {
                dataPoints[arrIndex] = leftArr[leftIndex];
                leftIndex++;
            } else {
                dataPoints[arrIndex] = rightArr[rightIndex];
                rightIndex++;
            }
            DrawDataPoint(arrIndex, Color.RED, this.delay);
            arrIndex++;
        }
        while (leftIndex < leftArrLength) {
            dataPoints[arrIndex] = leftArr[leftIndex];
            DrawDataPoint(arrIndex, Color.RED, this.delay);

            leftIndex++;
            arrIndex++;
        }
        while (rightIndex < rightArrLength) {
            dataPoints[arrIndex] = rightArr[rightIndex];
            DrawDataPoint(arrIndex, Color.RED, this.delay);

            rightIndex++;
            arrIndex++;
        }
    }

    private void BubbleSort(int[] dataPoints) {
        for (int i = 0; i < dataPoints.length - 1; i++) {
            for (int j = 0; j < dataPoints.length - i - 1; j++) {
                if (dataPoints[j] > dataPoints[j + 1]) {
                    final int index = j;
                    final int indexOffset = this.offsetMultiple + index * this.offsetMultiple;
                    final int indexHeight = dataPoints[index] * this.heightMultiple;

                    final int nextIndex = j + 1;
                    final int nextIndexOffset = this.offsetMultiple + nextIndex * this.offsetMultiple;
                    final int nextIndexHeight = dataPoints[nextIndex] * this.heightMultiple;

//                    DrawAllDataPoints(Color.RED);
                    // swap data points
                    Swap(index, nextIndex);
                    // redraw new points, highlighting the one that has been swapped forward
                    DrawDataPoint(index, Color.RED);
                    DrawDataPoint(nextIndex, Color.YELLOW, this.delay);
                    DrawDataPoint(nextIndex, Color.RED);
                }
            }
        }
        for (int i = 0; i < dataPoints.length; i++) {
            DrawDataPoint(i, Color.RED);
        }
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

    private void DrawDataPoint(int index, Color color, int delay) {
        int xOffset = 300 + offsetMultiple + index * offsetMultiple;
        int height = dataPoints[index] * heightMultiple;

        ClearDataPoint(index);
        gc.setFill(color);
        // draw new datapoint
        gc.fillRect(xOffset, 500 - height, lineWidth, height);

        try { Thread.sleep(delay); }
        catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
    }

    private void DrawDataPoint(int index, Color color) {
        int xOffset = 300 + this.offsetMultiple + index * this.offsetMultiple;
        int height = dataPoints[index] * this.heightMultiple;

        ClearDataPoint(index);
        gc.setFill(color);
        // draw new datapoint
        gc.fillRect(xOffset, 500 - height, lineWidth, height);
    }

    private void ClearDataPoint(int index) {
        int xOffset = 300 + this.offsetMultiple + index*this.offsetMultiple;
        gc.clearRect(xOffset, 0 , this.lineWidth, 500);
    }

    private void DrawAllDataPoints(Color color) {
        for (int i = 0; i < dataPoints.length; i++)
            DrawDataPoint(i, Color.RED);
    }

    private void ClearAllDataPoints() {
        for(int i = 0; i < this.dataPoints.length; i++)
            ClearDataPoint(i);
    }
}
