package io.cognitionbox.petra.examples.analyze.objects;

import java.util.List;

public class ImageData {
    public int noOfpixels;
    public double averageIntensity;
    public double totalIntensity;
    public List<Pixel> pixels;

    public ImageData(List<Pixel> pixels) {
        this.pixels = pixels;
        this.noOfpixels = pixels.size();
    }

    public Pixel getPixel(int loopIteration) {
        return pixels.get(loopIteration);
    }

    public boolean hasTotalIntensity() {
        return totalIntensity>0;
    }

    public boolean notHasTotalIntensity() {
        return totalIntensity==0;
    }

    public boolean notHasAverageIntensity() {
        return averageIntensity==0;
    }

    public boolean hasAverageIntensity() {
        return averageIntensity>0;
    }
}
