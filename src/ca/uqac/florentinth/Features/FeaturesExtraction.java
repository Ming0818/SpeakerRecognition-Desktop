package ca.uqac.florentinth.Features;

import ca.uqac.florentinth.Features.Windowing.HammingWindow;
import ca.uqac.florentinth.Features.Windowing.Window;

/**
 * Copyright 2016 Florentin Thullier.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FeaturesExtraction extends ExtractFeatureFromWindow {

    private int poles;
    private Window window;
    private LinearPredictiveCoding linearPredictiveCoding;

    public FeaturesExtraction(float sampleRate, int poles) {
        super(sampleRate);
        this.poles = poles;
        this.window = new HammingWindow(windowSize);
        this.linearPredictiveCoding = new LinearPredictiveCoding(windowSize, poles);
    }

    public double[] extractVoiceFeatures(double[] voiceSample) {
        double[] voiceFeatures = new double[poles];
        double[] audioWindow = new double[windowSize];
        int counter = 0;

        for (int i = 0; (i + windowSize) <= voiceSample.length; i += (windowSize / 2)) {
            System.arraycopy(voiceSample, i, audioWindow, 0, windowSize);
            window.applyWindow(audioWindow);
            double[] LPCCoefficients = linearPredictiveCoding.processLPC(audioWindow)[0];

            for (int j = 0; j < poles; j++) {
                voiceFeatures[j] += LPCCoefficients[j];
            }

            counter++;
        }

        if (counter > 1)
            for (int i = 0; i < poles; i++) {
                voiceFeatures[i] /= counter;
            }

        return voiceFeatures;
    }
}
