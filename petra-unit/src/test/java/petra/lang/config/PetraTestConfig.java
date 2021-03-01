/*
 ********************************************************************************************************
 * Copyright © 2016-2020 Cognition Box Ltd. - All Rights Reserved
 *
 * This file is part of the "Petra" system. "Petra" is owned by:
 *
 * Cognition Box Ltd. (10194162)
 * 9 Grovelands Road,
 * Palmers Green,
 * London, N13 4RJ
 * England.
 *
 * "Petra" is Proprietary and Confidential.
 * Unauthorized copying of "Petra" files, via any medium is strictly prohibited.
 *
 * "Petra" can not be copied and/or distributed without the express
 * permission of Cognition Box Ltd.
 *
 * "Petra" includes trade secrets of Cognition Box Ltd.
 * In order to protect "Petra", You shall not decompile, reverse engineer, decrypt,
 * extract or disassemble "Petra" or otherwise reduce or attempt to reduce any software
 * in "Petra" to source code form. You shall ensure, both during and
 * (if you still have possession of "Petra") after the performance of this Agreement,
 * that (i) persons who are not bound by a confidentiality agreement consistent with this Agreement
 * shall not have access to "Petra" and (ii) persons who are so bound are put on written notice that
 * "Petra" contains trade secrets, owned by and proprietary to Cognition Box Ltd.
 *
 * "Petra" is written by Aran Hakki <aran@cognitionbox.io>
 ********************************************************************************************************
 */
package petra.lang.config;

import io.cognitionbox.petra.config.PetraConfig;

public class PetraTestConfig extends PetraConfig implements petra.lang.config.IPetraTestConfig {

    public petra.lang.config.IPetraTestConfig disableExceptionsPassthrough() {
        this.exceptionsPassthrough = false;
        return this;
    }

    public petra.lang.config.IPetraTestConfig allowExceptionsPassthrough() {
        exceptionsPassthrough = true;
        return this;
    }

    public petra.lang.config.IPetraTestConfig enableTestMode() {
        testMode = true;
        return this;
    }

    public IPetraTestConfig setMaxIterations(long maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

}
