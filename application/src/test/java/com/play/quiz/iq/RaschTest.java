package com.play.quiz.iq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * The measurement, checked the only way it can be: simulate people of known ability answering
 * items of known difficulty, and see whether the estimate finds them again.
 *
 * <p>This is what the IQ number rests on, so it is worth pinning down — everything else in the
 * feature is bookkeeping around these two functions.
 */
class RaschTest {

    @Test
    void given_an_item_as_hard_as_the_person_then_it_is_a_coin_toss() {
        assertEquals(0.5, Rasch.probability(0.0, 0.0), 1e-9);
        assertTrue(Rasch.probability(2.0, 0.0) > 0.85, "An easy item for an able person");
        assertTrue(Rasch.probability(-2.0, 0.0) < 0.15, "The same item, the other way round");
    }

    @Test
    void given_an_item_then_it_tells_us_most_about_someone_of_its_own_difficulty() {
        assertTrue(Rasch.information(0.0, 0.0) > Rasch.information(0.0, 1.5));
        assertTrue(Rasch.information(0.0, 0.0) > Rasch.information(0.0, -1.5));
        assertEquals(0.25, Rasch.information(1.0, 1.0), 1e-9);
    }

    @Test
    void given_simulated_people_then_their_ability_is_recovered() {
        // One simulated taker says nothing — the model is about chances, and a run of thirty
        // coin tosses lands wide often enough. Two hundred of them say whether the estimator is
        // aimed at the right place: the average estimate should sit on the ability it was given,
        // pulled a little toward the middle by the prior, and never past it.
        for (double trueTheta = -2.0; trueTheta <= 2.0; trueTheta += 0.5) {
            double total = 0;
            double unshrunkTotal = 0;
            double totalError = 0;
            int takers = 200;
            Random random = new Random(Double.hashCode(trueTheta));

            for (int taker = 0; taker < takers; taker++) {
                double[] difficulties = new double[30];
                boolean[] correct = new boolean[30];
                for (int i = 0; i < difficulties.length; i++) {
                    difficulties[i] = -3.0 + 6.0 * i / (difficulties.length - 1);
                    correct[i] = random.nextDouble() < Rasch.probability(trueTheta, difficulties[i]);
                }
                Rasch.Ability ability = Rasch.estimate(difficulties, correct);
                total += ability.theta();
                unshrunkTotal += Rasch.unshrunk(ability);
                totalError += Math.abs(ability.theta() - trueTheta);
            }

            double mean = total / takers;
            double unshrunkMean = unshrunkTotal / takers;
            // Raw, the estimate is short of the truth at the extremes and never past it: that is
            // the prior's pull, and it is what unshrunk() takes back out.
            assertTrue(Math.abs(mean) <= Math.abs(trueTheta) + 0.1, "Overstated " + trueTheta);
            assertTrue(Math.abs(unshrunkMean - trueTheta) < 0.2,
                    "Average corrected estimate " + unshrunkMean + " for an ability of " + trueTheta);
            assertTrue(totalError / takers < 0.55, "Typical miss for " + trueTheta + ": " + totalError / takers);
        }
    }

    @Test
    void given_abler_people_then_they_score_higher() {
        // Whatever the noise on one run, the scale has to run the right way.
        double[] difficulties = {-2, -1, 0, 1, 2, -1.5, 0.5, 1.5};
        boolean[] two = {true, true, false, false, false, true, false, false};
        boolean[] four = {true, true, true, true, false, true, true, false};
        boolean[] six = {true, true, true, true, true, true, true, false};

        assertTrue(Rasch.estimate(difficulties, two).theta() < Rasch.estimate(difficulties, four).theta());
        assertTrue(Rasch.estimate(difficulties, four).theta() < Rasch.estimate(difficulties, six).theta());
    }

    @Test
    void given_more_answers_then_the_estimate_gets_firmer() {
        double[] few = {0, 0, 0, 0, 0};
        double[] many = new double[40];
        boolean[] fewCorrect = {true, false, true, false, true};
        boolean[] manyCorrect = new boolean[40];
        for (int i = 0; i < many.length; i++) {
            manyCorrect[i] = i % 2 == 0;
        }

        assertTrue(Rasch.estimate(many, manyCorrect).standardError()
                < Rasch.estimate(few, fewCorrect).standardError());
    }

    @Test
    void given_every_answer_right_then_the_estimate_stays_finite() {
        // Maximum likelihood would say "infinitely able" here; the prior is what stops it.
        double[] difficulties = {1, 1.5, 2, 2.5, 3};
        boolean[] correct = {true, true, true, true, true};

        Rasch.Ability ability = Rasch.estimate(difficulties, correct);
        assertTrue(ability.theta() > 1.5 && ability.theta() < 4, "Able, but a number: " + ability.theta());
    }

    @Test
    void given_a_normal_curve_then_shares_and_points_agree() {
        assertEquals(0.5, Rasch.normalBelow(0), 1e-6);
        assertEquals(0.8413, Rasch.normalBelow(1), 1e-3);
        assertEquals(0.0228, Rasch.normalBelow(-2), 1e-3);

        for (double share = 0.05; share < 0.96; share += 0.05) {
            assertEquals(share, Rasch.normalBelow(Rasch.normalQuantile(share)), 1e-4);
        }
    }
}
