package stars;

import java.io.*;

/**
 * Stores the bright bodies within a single timestamp of a BinaryTracker based on a passed binary mask. Adjacent
 * positives will be grouped into single bright bodies and extractable as a sorted BrightBodyList.
 */
public class BinaryTrackerInstance {
    // TODO: Add a CSV Generator
    public int index;
    private float[][] image_;
    private int[][] binary_image_;
    private BrightBodyList bright_bodies_;

    /**
     * Constructs a binary tracker instance given the original cube, binary mask cube and index cube
     * @param original_cube original bright body field
     * @param binary_cube masked image containing positives at regions to search
     * @param index index of the cube to check
     * TODO: Determine if it makes sense to pass the images instead of cubes and the index
     */
    public BinaryTrackerInstance(float[][][] original_cube, int[][][] binary_cube, int index) {
        this.index = index;
        image_ = original_cube[index];
        binary_image_ = binary_cube[index];
        bright_bodies_ = BrightBodyLocator.binaryLocate(image_, binary_image_);
        bright_bodies_.sortByArea();
    }

    /**
     * Generates a serialization of the sorted bright bodies
     * @param filename .ser file location
     * @throws IOException
     */
    public void serialize(String filename) throws IOException {
        FileOutputStream file_out = new FileOutputStream(filename);
        ObjectOutputStream object_out = new ObjectOutputStream(file_out);
        object_out.writeObject(bright_bodies_);
        object_out.close();
        file_out.close();
    }

    /**
     * Generates a text output of the sorted bright bodies
     * @param filename .txt file location
     * @throws IOException
     */
    public void toTextFile(String filename)throws IOException {
        File f = new File(filename);
        if(!f.exists()) {
            f.createNewFile();
        }
        FileWriter fwriter = new FileWriter(f.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fwriter);
        writer.write("Index: " + index + " | Bright Bodies: " + bright_bodies_.size());
        writer.newLine();
        writer.newLine();
        for(BrightBody b : bright_bodies_) {
            writer.write(b.toString());
            writer.newLine();
        }
        writer.close();
    }
}
