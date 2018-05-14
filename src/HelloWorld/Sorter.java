package HelloWorld;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.xml.internal.bind.v2.model.annotation.Quick;
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
    private final int menuOffset;

    public Sorter(int[] dataPoints, GraphicsContext gc, int lineWidth, int offsetMultiple, int heightMultiple) {
        this.dataPoints = dataPoints;
        this.gc = gc;
        this.lineWidth = lineWidth;
        this.offsetMultiple = offsetMultiple;
        this.heightMultiple = heightMultiple;
        this.delay = 20;
        this.menuOffset = 330;
    }

    public void runSort(String sort) {
        new Thread(() -> {
            switch (sort) {
                case "Bubble Sort":
                    BubbleSort(this.dataPoints);
                    break;
                case "Merge Sort":
                    MergeSort(this.dataPoints, 0, this.dataPoints.length - 1);
                    break;
                case "Quick Sort":
                    QuickSort(this.dataPoints, 0, this.dataPoints.length - 1);
                    break;
                case "Insertion Sort":
                    InsertionSort(this.dataPoints);
                    break;
                default:
                    break;
            }
        }).start();
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

    private void QuickSort(int[] dataPoints, int start, int end) {
        if(start < end) {
            int partitionIndex = Partition(dataPoints, start, end);
            QuickSort(dataPoints, start, partitionIndex - 1);
            QuickSort(dataPoints, partitionIndex + 1, end);
        }
    }

    private int Partition(int[] dataPoints, int start, int end) {
        int pivot = dataPoints[end];
        int partitionIndex = start;

        for(int i = start; i < end; i++) {
            if(dataPoints[i] <= pivot) {
                Swap(i, partitionIndex);
                // Drawing animation
                DrawDataPoint(i, Color.YELLOW);
                DrawDataPoint(partitionIndex, Color.YELLOW, this.delay);
                DrawDataPoint(partitionIndex, Color.RED);
                DrawDataPoint(i, Color.RED);
                // Increment partition index
                partitionIndex++;
            }
        }
        // Swap element at partition index with the pivot (which is at the end)
        Swap(partitionIndex, end);
        // Redraw final points
        DrawDataPoint(partitionIndex, Color.RED);
        DrawDataPoint(end, Color.RED);

        return partitionIndex;
    }

    private void InsertionSort(int dataPoints[]) {
        for(int i = 1; i < dataPoints.length; i++) {
            int value = dataPoints[i];
            int j = i - 1;

            while (j >= 0 && dataPoints[j] > value) {
                dataPoints[j + 1] = dataPoints[j];
                DrawDataPoint(j + 1, Color.YELLOW, this.delay);
                DrawDataPoint(j + 1, Color.RED);

                j--;
            }
            dataPoints[j + 1] = value;
            DrawDataPoint(j+1, Color.RED);
        }
    }

    private void Swap(int index, int nextIndex) {
        int temp = this.dataPoints[index];
        this.dataPoints[index] = this.dataPoints[nextIndex];
        this.dataPoints[nextIndex] = temp;
    }

    private void DrawDataPoint(int index, Color color, int delay) {
        int xOffset = this.menuOffset + offsetMultiple + index * offsetMultiple;
        int height = dataPoints[index] * heightMultiple;

        ClearDataPoint(index);
        gc.setFill(color);
        // draw new datapoint
        gc.fillRect(xOffset, 500 - height, lineWidth, height);

        try { Thread.sleep(delay); }
        catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
    }

    private void DrawDataPoint(int index, Color color) {
        int xOffset = this.menuOffset + this.offsetMultiple + index * this.offsetMultiple;
        int height = dataPoints[index] * this.heightMultiple;

        ClearDataPoint(index);
        gc.setFill(color);
        // draw new datapoint
        gc.fillRect(xOffset, 500 - height, lineWidth, height);
    }

    private void ClearDataPoint(int index) {
        int xOffset = this.menuOffset + this.offsetMultiple + index*this.offsetMultiple;
        gc.clearRect(xOffset, 0 , this.lineWidth, 500);
    }

    private void DrawAllDataPoints(Color color) {
        for (int i = 0; i < dataPoints.length; i++)
            DrawDataPoint(i, color);
    }

    private void ClearAllDataPoints() {
        for(int i = 0; i < this.dataPoints.length; i++)
            ClearDataPoint(i);
    }
}
