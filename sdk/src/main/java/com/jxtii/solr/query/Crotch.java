package com.jxtii.solr.query;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by guolf on 17/7/7.
 */
public class Crotch extends Criteria implements Serializable {

    private List<Criteria> siblings = new ArrayList<Criteria>();
    private Node mostRecentSibling = null;

    Crotch() {
    }

    @Override
    public Field getField() {
        if (this.mostRecentSibling instanceof Criteria) {
            return ((Criteria) this.mostRecentSibling).getField();
        }
        return null;
    }

    @Override
    public Crotch is(Object o) {
        mostRecentSibling.is(o);
        return this;
    }

    @Override
    public Crotch not() {

        mostRecentSibling.not();
        return this;
    }


    @Override
    public Crotch endsWith(String postfix) {
        mostRecentSibling.endsWith(postfix);
        return this;
    }

    @Override
    public Crotch startsWith(String prefix) {
        mostRecentSibling.startsWith(prefix);
        return this;
    }

    @Override
    public Crotch contains(String value) {
        mostRecentSibling.contains(value);
        return this;
    }

    @Override
    public Crotch is(Object... values) {
        mostRecentSibling.is(values);
        return this;
    }

    @Override
    public Crotch is(Iterable<?> values) {
        mostRecentSibling.is(values);
        return this;
    }

    @Override
    public Crotch isNull() {
        mostRecentSibling.isNull();
        return this;
    }

    @Override
    public Crotch isNotNull() {
        mostRecentSibling.isNotNull();
        return this;
    }

    @Override
    public Crotch contains(String... values) {
        mostRecentSibling.contains(values);
        return this;
    }

    @Override
    public Crotch contains(Iterable<String> values) {
        mostRecentSibling.contains(values);
        return this;
    }

    @Override
    public Crotch startsWith(String... values) {
        mostRecentSibling.startsWith(values);
        return this;
    }

    @Override
    public Crotch startsWith(Iterable<String> values) {
        mostRecentSibling.startsWith(values);
        return this;
    }

    @Override
    public Crotch endsWith(String... values) {
        mostRecentSibling.endsWith(values);
        return this;
    }

    @Override
    public Crotch endsWith(Iterable<String> values) {
        mostRecentSibling.endsWith(values);
        return this;
    }

    @Override
    public Crotch between(Object lowerBound, Object upperBound) {
        mostRecentSibling.between(lowerBound, upperBound);
        return this;
    }

    @Override
    public Crotch between(Object lowerBound, Object upperBound, boolean includeLowerBound, boolean includeUpperBound) {
        mostRecentSibling.between(lowerBound, upperBound, includeLowerBound, includeUpperBound);
        return this;
    }

    @Override
    public Crotch lessThan(Object upperBound) {
        mostRecentSibling.lessThan(upperBound);
        return this;
    }

    @Override
    public Crotch lessThanEqual(Object upperBound) {
        mostRecentSibling.lessThanEqual(upperBound);
        return this;
    }

    @Override
    public Crotch greaterThan(Object lowerBound) {
        mostRecentSibling.greaterThan(lowerBound);
        return this;
    }

    @Override
    public Crotch greaterThanEqual(Object lowerBound) {
        mostRecentSibling.greaterThanEqual(lowerBound);
        return this;
    }

    @Override
    public Crotch in(Object... values) {
        mostRecentSibling.in(values);
        return this;
    }

    @Override
    public Crotch in(Iterable<?> values) {
        mostRecentSibling.in(values);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.isOr() ? " OR " : " AND ");
        sb.append('(');
        boolean first = true;
        for (Node node : this.siblings) {
            String s = node.toString();
            if (first) {
                s = s.replaceFirst("OR", "").replaceFirst("AND", "");
                first = false;
            }
            sb.append(s);
        }
        sb.append(')');
        return sb.toString();
    }

    // ------- NODE STUFF --------
    void add(Node node) {

        if (!(node instanceof Criteria)) {
            throw new IllegalArgumentException("Can only add instances of Criteria");
        }

        node.setParent(this);
        this.siblings.add((Criteria) node);
        this.mostRecentSibling = node;
    }

    @Override
    public Collection<Criteria> getSiblings() {
        return Collections.unmodifiableCollection(siblings);
    }

    @Override
    public Crotch and(Node part) {
        add(part);
        return this;
    }

    @Override
    public Crotch or(Node part) {
        part.setPartIsOr(true);
        add(part);
        return this;
    }

    @Override
    public Crotch and(String fieldname) {
        if (this.mostRecentSibling instanceof Crotch) {
            ((Crotch) mostRecentSibling).add(new Criteria(fieldname));
        } else {
            and(new Criteria(fieldname));
        }
        return this;
    }

    @Override
    public Crotch or(String fieldname) {
        Criteria criteria = new Criteria(fieldname);
        criteria.setPartIsOr(true);

        if (this.mostRecentSibling instanceof Crotch) {
            ((Crotch) mostRecentSibling).add(criteria);
        } else {
            or(new Criteria(fieldname));
        }
        return this;
    }
}
