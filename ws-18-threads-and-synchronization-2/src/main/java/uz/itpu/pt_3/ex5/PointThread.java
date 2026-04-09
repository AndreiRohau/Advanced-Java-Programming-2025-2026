package uz.itpu.pt_3.ex5;

/**
 * The PointThread class is a thread of the Thread type.
 * Depending on the value of the writeStatus variable,
 * it either calculates the distance from a point to the origin or changes the coordinates of a point.
 */
public class PointThread extends Thread {
    private PointManager pointManager;
    private boolean writeStatus;
    private Point point;
    public PointThread(PointManager pointManager, Point point, boolean writeStatus) {
        this.pointManager = pointManager;
        this.point = point;
        this.writeStatus = writeStatus;
    }
    @Override
    public void run() {
        if (writeStatus) {
            pointManager.randomChangePoint(point);
        } else {
            pointManager.length(point);
        }
    }
}
