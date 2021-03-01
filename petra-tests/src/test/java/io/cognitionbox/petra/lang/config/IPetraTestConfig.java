package io.cognitionbox.petra.lang.config;

import io.cognitionbox.petra.config.IPetraConfig;

public interface IPetraTestConfig extends IPetraConfig {
    IPetraTestConfig disableExceptionsPassthrough();
    IPetraTestConfig allowExceptionsPassthrough();
    IPetraTestConfig enableTestMode();
    IPetraTestConfig setMaxIterations(long maxIterations);
}
