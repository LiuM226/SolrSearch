package com.jxtii.solr.query;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by guolf on 17/7/7.
 */
public abstract class Node {

    private Node parent;
    private boolean isOr = false;
    private boolean negating = false;

    protected Node() {
    }

    // ------- TREE ---------
    protected void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Define {@literal or} nature of {@link Node}
     *
     * @param isOr
     */
    public void setPartIsOr(boolean isOr) {
        this.isOr = isOr;
    }

    /**
     * @return true in case {@link Node} has no parent.
     */
    public boolean isRoot() {
        return this.parent == null;
    }

    /**
     * @return true in case {@link Node} has {@literal or} nature.
     */
    public boolean isOr() {
        return this.isOr;
    }

    /**
     * Get parent {@link Node}.
     *
     * @return null in case no parent set.
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * @return true if {@link Node} has siblings.
     */
    public boolean hasSiblings() {
        return !getSiblings().isEmpty();
    }

    /**
     * @return empty collection if {@link Node} does not have siblings.
     */
    public Collection<Criteria> getSiblings() {
        return Collections.emptyList();
    }

    /**
     * @return true if {@code not()} criteria
     * @since 1.4
     */
    public boolean isNegating() {
        return this.negating;
    }

    /**
     * @param negating
     * @since 1.4
     */
    protected void setNegating(boolean negating) {
        this.negating = negating;
    }

    // ------ CONJUNCTIONS --------

    /**
     * Combine two {@link Node}s using {@literal and}.
     *
     * @param part
     * @return
     */
    public abstract <T extends Node> T and(Node part);

    /**
     * Combine node with new {@link Node} for given {@literal fieldname} using {@literal and}.
     *
     * @param part
     * @return
     */
    public abstract <T extends Node> T and(String fieldname);

    /**
     * Combine two {@link Node}s using {@literal or}.
     *
     * @param part
     * @return
     */
    public abstract <T extends Node> T or(Node part);

    /**
     * Combine node with new {@link Node} for given {@literal fieldname} using {@literal and}.
     * @param fieldname
     * @param <T>
     * @return
     */
    public abstract <T extends Node> T or(String fieldname);

    // ------- COMMANDS ----------
    public abstract Node is(Object value);

    public abstract Node is(Object... values);

    public abstract Node is(Iterable<?> values);

    public abstract Node isNull();

    public abstract Node isNotNull();

    public abstract Node contains(String value);

    public abstract Node contains(String... values);

    public abstract Node contains(Iterable<String> values);

    public abstract Node startsWith(String prefix);

    public abstract Node startsWith(String... values);

    public abstract Node startsWith(Iterable<String> values);

    public abstract Node endsWith(String postfix);

    public abstract Node endsWith(String... values);

    public abstract Node endsWith(Iterable<String> values);

    public abstract Node not();

    public abstract Node between(Object lowerBound, Object upperBound);

    public abstract Node between(Object lowerBound, Object upperBound, boolean includeLowerBound,
                                 boolean includeUpperBound);

    public abstract Node lessThan(Object upperBound);

    public abstract Node lessThanEqual(Object upperBound);

    public abstract Node greaterThan(Object lowerBound);

    public abstract Node greaterThanEqual(Object lowerBound);

    public abstract Node in(Object... values);

    public abstract Node in(Iterable<?> values);

}