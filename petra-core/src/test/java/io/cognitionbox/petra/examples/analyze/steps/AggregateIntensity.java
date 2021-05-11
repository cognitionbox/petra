package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.lang.PEdge;

public class AggregateIntensity extends PEdge<ImageData> {
	{
		type(ImageData.class);
		kase(imageData -> true, imageData -> true );
		func(imageData -> {
			imageData.totalIntensity = imageData.totalIntensity + imageData.getPixel(getParent().loopIteration()).getIntensity();
		});
	}
}