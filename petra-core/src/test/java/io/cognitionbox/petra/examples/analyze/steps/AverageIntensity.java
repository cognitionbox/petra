package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.lang.PEdge;

public class AverageIntensity extends PEdge<ImageData> {
	{
		type(ImageData.class);
		kase(
				imageData -> imageData.hasTotalIntensity() && imageData.notHasAverageIntensity(),
				imageData -> imageData.hasAverageIntensity() );
		func(imageData -> {
			imageData.averageIntensity = imageData.totalIntensity / imageData.noOfpixels;
		});
	}
}