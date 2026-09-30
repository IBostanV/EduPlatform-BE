package com.play.quiz.iq;

/**
 * The measurement model behind the IQ test: Rasch (one-parameter logistic).
 *
 * <p>Every item has a difficulty and every person an ability, both on the same scale (logits).
 * The chance of getting an item right is {@code 1 / (1 + e^-(ability - difficulty))} — nothing
 * else. That is what makes the model worth using here: a score out of twenty says little when
 * everyone answers different items, while an ability estimate is comparable across people who
 * saw different questions, which is the whole point of a test that adapts as it goes.
 *
 * <p>Ability is estimated by {@link #estimate expected a posteriori} over a grid, with a standard
 * normal prior. Two things follow from that prior, and both matter for what the number means:
 * an all-right or all-wrong run still gives a finite answer (maximum likelihood would not), and
 * the estimate comes out in population standard deviations, which is the scale IQ is quoted on —
 * as long as the takers really are a cross-section, which is what the norming in
 * {@link IqService} exists to stop us having to assume.
 */
final class Rasch {

    /** The ability grid the posterior is worked out over: ±4 SD is past anyone we will measure. */
    private static final double MIN = -4.0;
    private static final double MAX = 4.0;
    private static final double STEP = 0.05;

    private Rasch() {
    }

    /** What an ability estimate came to, and how far it can be trusted. */
    record Ability(double theta, double standardError) {
    }

    /** The chance of answering an item of this difficulty right. */
    static double probability(final double theta, final double difficulty) {
        return 1.0 / (1.0 + Math.exp(difficulty - theta));
    }

    /**
     * How much an item tells us about someone of this ability. Highest where the item is as hard
     * as the person is able — a question they would get right nine times in ten says little about
     * where exactly they stand.
     */
    static double information(final double theta, final double difficulty) {
        double p = probability(theta, difficulty);
        return p * (1.0 - p);
    }

    /**
     * Ability from a run of answers, as the mean of the posterior, with its standard deviation as
     * the standard error.
     *
     * @param difficulties the items answered, in order
     * @param correct      whether each was right
     */
    static Ability estimate(final double[] difficulties, final boolean[] correct) {
        double weightSum = 0;
        double thetaSum = 0;
        double squareSum = 0;

        for (double theta = MIN; theta <= MAX; theta += STEP) {
            double weight = prior(theta) * likelihood(theta, difficulties, correct);
            weightSum += weight;
            thetaSum += weight * theta;
            squareSum += weight * theta * theta;
        }

        if (weightSum == 0) {
            return new Ability(0, 1);
        }
        double mean = thetaSum / weightSum;
        double variance = Math.max(0, squareSum / weightSum - mean * mean);

        return new Ability(mean, Math.sqrt(variance));
    }

    private static double likelihood(final double theta, final double[] difficulties, final boolean[] correct) {
        double product = 1.0;
        for (int i = 0; i < difficulties.length; i++) {
            double p = probability(theta, difficulties[i]);
            product *= correct[i] ? p : 1 - p;
        }
        return product;
    }

    /** The standard normal density, the prior: most people are somewhere in the middle. */
    private static double prior(final double theta) {
        return Math.exp(-0.5 * theta * theta);
    }

    /**
     * The estimate with the prior's pull taken back out.
     *
     * <p>A posterior mean is always short of the truth: the prior drags it toward the middle, by
     * more the less the answers say. With a standard normal prior that pull is exactly
     * {@code 1 - se²}, so dividing it out puts the extremes back where the answers put them —
     * which matters here, because an IQ of 130 quietly reported as 124 is the kind of error a
     * test is judged on.
     *
     * <p>Only worth doing where the score is read off the scale directly. Where it is read off
     * where other people landed, the pull changes nobody's rank and so changes nothing.
     */
    static double unshrunk(final Ability ability) {
        double pull = 1 - ability.standardError() * ability.standardError();

        // An estimate this vague has nothing to stretch: a handful of answers, and the prior is
        // most of what the number is made of.
        return pull < 0.2 ? ability.theta() : ability.theta() / pull;
    }

    /** The share of a standard normal below this point, for turning an ability into a percentile. */
    static double normalBelow(final double theta) {
        // Abramowitz & Stegun 7.1.26 on the error function; good to about 1e-7, which is four
        // decimal places more than a percentile needs.
        double t = 1.0 / (1.0 + 0.2316419 * Math.abs(theta));
        double density = Math.exp(-0.5 * theta * theta) / Math.sqrt(2 * Math.PI);
        double tail = density * t * (0.319381530 + t * (-0.356563782 + t * (1.781477937
                + t * (-1.821255978 + t * 1.330274429))));

        return theta >= 0 ? 1 - tail : tail;
    }

    /**
     * The other way round: which point of a standard normal has this share below it. Found by
     * halving the interval rather than with a rational approximation — fifty steps over a range
     * we already know cost nothing, and there are no magic constants to get wrong.
     */
    static double normalQuantile(final double share) {
        double low = MIN;
        double high = MAX;
        for (int step = 0; step < 50; step++) {
            double middle = (low + high) / 2;
            if (normalBelow(middle) < share) {
                low = middle;
            } else {
                high = middle;
            }
        }
        return (low + high) / 2;
    }
}
