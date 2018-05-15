package HelloWorld;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Sorter {

    private int[] dataPoints;
    private GraphicsContext gc;
    private int delay;

    private final int lineWidth;
    private final int menuOffset;
    private final int offsetMultiple;
    private final int heightMultiple;

    private final Color dataPointColor;
    private final Color highlightColor;
    private final Color backgroundColor;

    public Sorter(int[] dataPoints, GraphicsContext gc, int lineWidth,
                  int offsetMultiple, int heightMultiple, int menuWidth) {
        this.dataPoints = dataPoints;
        this.gc = gc;
        this.lineWidth = lineWidth;
        this.offsetMultiple = offsetMultiple;
        this.heightMultiple = heightMultiple;
        this.delay = 50;
        this.menuOffset = menuWidth + 30;

        this.dataPointColor = Color.BLACK;
        this.highlightColor = Color.YELLOW;
        this.backgroundColor = Color.AQUA;
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

                DrawDataPoint(arrIndex, this.highlightColor, this.delay);
                DrawDataPoint(arrIndex, this.dataPointColor);
            } else {
                dataPoints[arrIndex] = rightArr[rightIndex];
                rightIndex++;

                DrawDataPoint(arrIndex, this.highlightColor, this.delay);
                DrawDataPoint(arrIndex, this.dataPointColor);
            }
            arrIndex++;
        }
        while (leftIndex < leftArrLength) {
            dataPoints[arrIndex] = leftArr[leftIndex];

            DrawDataPoint(arrIndex, this.highlightColor, this.delay);
            DrawDataPoint(arrIndex, this.dataPointColor);

            leftIndex++;
            arrIndex++;
        }
        while (rightIndex < rightArrLength) {
            dataPoints[arrIndex] = rightArr[rightIndex];

            DrawDataPoint(arrIndex, this.highlightColor, this.delay);
            DrawDataPoint(arrIndex, this.dataPointColor);

            rightIndex++;
            arrIndex++;
        }
    }

    private void BubbleSort(int[] dataPoints) {
        for (int i = 0; i < dataPoints.length - 1; i++) {
            for (int j = 0; j < dataPoints.length - i - 1; j++) {
                if (dataPoints[j] > dataPoints[j + 1]) {
                    int index = j;
                    int nextIndex = j + 1;

                    // swap data points
                    Swap(index, nextIndex);
                    // redraw new points, highlighting the one that has been swapped forward
                    DrawDataPoint(index, this.dataPointColor);
                    DrawDataPoint(nextIndex, this.highlightColor, this.delay);
                    DrawDataPoint(nextIndex, this.dataPointColor);
                }
            }
        }
//        for (int i = 0; i < dataPoints.length; i++) {
//            DrawDataPoint(i, this.dataPointColor);
//        }
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
                DrawDataPoint(i, this.highlightColor);
                DrawDataPoint(partitionIndex, this.highlightColor, this.delay);
                DrawDataPoint(partitionIndex, this.dataPointColor);
                DrawDataPoint(i, this.dataPointColor);
                // Increment partition index
                partitionIndex++;
            }
        }
        // Swap element at partition index with the pivot (which is at the end)
        Swap(partitionIndex, end);
        // Redraw final points
        DrawDataPoint(partitionIndex, this.dataPointColor);
        DrawDataPoint(end, this.dataPointColor);

        return partitionIndex;
    }

    private void InsertionSort(int dataPoints[]) {
        for(int i = 1; i < dataPoints.length; i++) {
            int value = dataPoints[i];
            int j = i - 1;

            while (j >= 0 && dataPoints[j] > value) {
                dataPoints[j + 1] = dataPoints[j];
                DrawDataPoint(j + 1, this.highlightColor, this.delay);
                DrawDataPoint(j + 1, this.dataPointColor);

                j--;
            }
            dataPoints[j + 1] = value;
            DrawDataPoint(j+1, this.dataPointColor);
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
        gc.setFill(Color.AQUA);
        gc.fillRect(xOffset, 0 , this.lineWidth, 500);
    }

    private void DrawAllDataPoints(Color color) {
        for (int i = 0; i < dataPoints.length; i++)
            DrawDataPoint(i, color);
    }

    private void ClearAllDataPoints() {
        for(int i = 0; i < this.dataPoints.length; i++)
            ClearDataPoint(i);
    }

    public void SetAnimationDelay(int value) {
        this.delay = value;
        System.out.println("New animation delay is: " + this.delay);
    }
}
