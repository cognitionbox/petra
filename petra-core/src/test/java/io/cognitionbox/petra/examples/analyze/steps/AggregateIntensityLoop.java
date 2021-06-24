package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.greatThan;

public class AggregateIntensityLoop extends PGraph<ImageData> {
	{
		type(ImageData.class);
		iterations(imageData -> imageData.noOfpixels);
		kase(
				imageData-> imageData.noOfpixels > 0,
				imageData -> imageData.hasTotalIntensity() );
		invariant(imageData -> imageData.notHasTotalIntensity() ^ greatThan(imageData.totalIntensity,0));

		step(imageData->imageData, AggregateIntensity.class);
		esak();
	}
}