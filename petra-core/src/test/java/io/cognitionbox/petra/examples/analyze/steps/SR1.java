package io.cognitionbox.petra.examples.analyze.steps;

import io.cognitionbox.petra.examples.analyze.objects.SunlightData;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.*;

public class SR1 extends PGraph<SunlightData> {
	{
		type(SunlightData.class);
		kase(
				sunlightData->  sunlightData.isGathered() ,
				sunlightData-> sunlightData.validated == isBetweenExclusive(1, sunlightData.imageData.averageIntensity,100) );
		begin();
		step(seq(), sunlightData -> sunlightData.imageData, AnalyzeImage.class);
		step(seq(), sunlightData -> sunlightData, Validate.class);
		end();
	}
}