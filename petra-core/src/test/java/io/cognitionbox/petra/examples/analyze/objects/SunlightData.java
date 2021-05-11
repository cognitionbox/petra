package io.cognitionbox.petra.examples.analyze.objects;

public class SunlightData {
    public ImageData imageData;
    public boolean validated = false;

    public SunlightData(ImageData imageData) {
        this.imageData = imageData;
    }

    public boolean isGathered() {
        return isNotValid() && hasPixels();
    }

    public boolean isNotValid() {
        return validated==false;
    }

    public boolean hasPixels() {
        return imageData.noOfpixels > 0;
    }
}
