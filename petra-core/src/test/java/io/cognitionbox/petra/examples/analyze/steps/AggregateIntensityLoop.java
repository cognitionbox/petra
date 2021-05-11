package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;

public class AggregateIntensityLoop extends PGraph<ImageData> {
	{
		type(ImageData.class);
		iterations(imageData -> imageData.noOfpixels);
		kase(imageData->imageData.noOfpixels > 0, imageData -> imageData.hasTotalIntensity() );
		invariant(imageData -> imageData.notHasTotalIntensity() ^ imageData.totalIntensity > 0);
		begin();
		step(imageData->imageData, AggregateIntensity.class);
		end();
	}
}