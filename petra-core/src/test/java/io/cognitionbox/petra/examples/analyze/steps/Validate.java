package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.SunlightData;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.isBetweenExclusive;

public class Validate extends PEdge<SunlightData> {
	{
		type(SunlightData.class);
		kase(
				sunlightData-> isBetweenExclusive(1,sunlightData.imageData.averageIntensity,100) ,
				sunlightData-> sunlightData.validated==true );
		func(sunlightData -> {
			sunlightData.validated = true;
		});
	}
}