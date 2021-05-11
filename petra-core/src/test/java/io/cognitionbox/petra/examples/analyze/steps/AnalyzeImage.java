package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.*;

public class AnalyzeImage extends PGraph<ImageData> {
	{
		type(ImageData.class);
		kase(imageData-> greatThan(imageData.noOfpixels,0) , imageData-> lessThan(1,imageData.averageIntensity) && lessThan(imageData.averageIntensity,100) );
		begin();
		step(seq(),imageData->imageData, AggregateIntensityLoop.class);
		step(seq(),imageData->imageData, AverageIntensity.class);
		end();
	}
}