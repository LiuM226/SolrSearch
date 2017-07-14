package com.jxtii.solr.query;


import java.io.Serializable;
import java.util.*;

/**
 * Created by guolf on 17/7/7.
 */
public class Criteria extends Node implements Serializable {

    public static final String WILDCARD = "*";
    public static final String CRITERIA_VALUE_SEPERATOR = " ";

    private Field field;
    private float boost = Float.NaN;

    private Set<Predicate> predicates = new LinkedHashSet<Predicate>();

    public Criteria() {
    }


    /**
     * Creates a new Criteria for the Filed with provided name
     *
     * @param fieldname
     */
    public Criteria(String fieldname) {
        this(new SimpleField(fieldname));
    }


    public Criteria(Field field) {
        this.field = field;
    }

    /**
     * Static factory method to create a new Criteria for field with given name
     *
     * @param fieldname 字段名称不能为空
     * @return
     */
    public static Criteria where(String fieldname) {
        return where(new SimpleField(fieldname));
    }


    /**
     * Static factory method to create a new Criteria for provided field
     *
     * @param field must not be null
     * @return
     */
    public static Criteria where(Field field) {
        return new Criteria(field);
    }

    /**
     * Crates new {@link Predicate} without any wildcards. Strings with blanks will be escaped
     * {@code "string\ with\ blank"}
     *
     * @param o
     * @return
     */
    public Criteria is(Object o) {
        if (o == null) {
            return isNull();
        }
        predicates.add(new Predicate(OperationKey.EQUALS, o));
        return this;
    }

    /**
     * Crates new {@link Predicate} without any wildcards for each entry
     *
     * @param values
     * @return
     */
    public Criteria is(Object... values) {
        return in(values);
    }

    /**
     * Creates new {@link Predicate} without any wildcards for each entry
     *
     * @param values
     * @return
     */
    public Criteria is(Iterable<?> values) {
        return in(values);
    }

    /**
     * Crates new {@link Predicate} for {@code null} values
     *
     * @return
     */
    public Criteria isNull() {
        return between(null, null).not();
    }

    /**
     * Crates new {@link Predicate} for {@code !null} values
     *
     * @return
     */
    public Criteria isNotNull() {
        return between(null, null);
    }

    /**
     * Crates new {@link Predicate} with leading and trailing wildcards <br/>
     * <strong>NOTE: </strong>mind your schema as leading wildcards may not be supported and/or execution might be slow.
     * <strong>NOTE: </strong>Strings will not be automatically split on whitespace.
     *
     * @param s
     * @return
     */
    public Criteria contains(String s) {
        predicates.add(new Predicate(OperationKey.CONTAINS, s));
        return this;

    }

    /**
     * Crates new {@link Predicate} with leading and trailing wildcards for each entry<br/>
     * <strong>NOTE: </strong>mind your schema as leading wildcards may not be supported and/or execution might be slow.
     *
     * @param values
     * @return
     */
    public Criteria contains(String... values) {
        return contains(Arrays.asList(values));
    }

    /**
     * Crates new {@link Predicate} with leading and trailing wildcards for each entry<br/>
     * <strong>NOTE: </strong>mind your schema as leading wildcards may not be supported and/or execution might be slow.
     *
     * @param values
     * @return
     */
    public Criteria contains(Iterable<String> values) {
        for (String value : values) {
            contains(value);
        }
        return this;
    }

    /**
     * Crates new {@link Predicate} with trailing wildcard <br/>
     * <strong>NOTE: </strong>Strings will not be automatically split on whitespace.
     *
     * @param s
     * @return
     */
    public Criteria startsWith(String s) {
        predicates.add(new Predicate(OperationKey.STARTS_WITH, s));
        return this;

    }

    /**
     * Crates new {@link Predicate} with trailing wildcard for each entry
     *
     * @param values
     * @return
     */
    public Criteria startsWith(String... values) {
        return startsWith(Arrays.asList(values));
    }

    /**
     * Crates new {@link Predicate} with trailing wildcard for each entry
     *
     * @param values
     * @return
     */
    public Criteria startsWith(Iterable<String> values) {
        for (String value : values) {
            startsWith(value);
        }
        return this;
    }

    /**
     * Crates new {@link Predicate} with leading wildcard <br />
     * <strong>NOTE: </strong>mind your schema and execution times as leading wildcards may not be supported.
     * <strong>NOTE: </strong>Strings will not be automatically split on whitespace.
     *
     * @param s
     * @return
     */
    public Criteria endsWith(String s) {
        predicates.add(new Predicate(OperationKey.ENDS_WITH, s));
        return this;
    }

    /**
     * Crates new {@link Predicate} with leading wildcard for each entry<br />
     * <strong>NOTE: </strong>mind your schema and execution times as leading wildcards may not be supported.
     *
     * @param values
     * @return
     */
    public Criteria endsWith(String... values) {
        return endsWith(Arrays.asList(values));
    }

    /**
     * Crates new {@link Predicate} with leading wildcard for each entry<br />
     * <strong>NOTE: </strong>mind your schema and execution times as leading wildcards may not be supported.
     *
     * @param values
     * @return
     */
    public Criteria endsWith(Iterable<String> values) {
        for (String value : values) {
            endsWith(value);
        }
        return this;

    }

    /**
     * Negates current criteria usinng {@code -} operator
     *
     * @return
     */
    public Criteria not() {
        setNegating(true);
        return this;
    }


    /**
     * Crates new {@link Predicate} for {@code RANGE [lowerBound TO upperBound]}
     *
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public Criteria between(Object lowerBound, Object upperBound) {
        return between(lowerBound, upperBound, true, true);
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE [lowerBound TO upperBound]}
     *
     * @param lowerBound
     * @param upperBound
     * @param includeLowerBound
     * @param includeUppderBound
     * @return
     */
    public Criteria between(Object lowerBound, Object upperBound, boolean includeLowerBound, boolean includeUppderBound) {
        predicates.add(new Predicate(OperationKey.BETWEEN, new Object[]{lowerBound, upperBound, includeLowerBound,
                includeUppderBound}));
        return this;
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE [* TO upperBound&#125;}
     *
     * @param upperBound
     * @return
     */
    public Criteria lessThan(Object upperBound) {
        between(null, upperBound, true, false);
        return this;
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE [* TO upperBound]}
     *
     * @param upperBound
     * @return
     */
    public Criteria lessThanEqual(Object upperBound) {
        between(null, upperBound);
        return this;
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE &#123;lowerBound TO *]}
     *
     * @param lowerBound
     * @return
     */
    public Criteria greaterThan(Object lowerBound) {
        between(lowerBound, null, false, true);
        return this;
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE [lowerBound TO *]}
     *
     * @param lowerBound
     * @return
     */
    public Criteria greaterThanEqual(Object lowerBound) {
        between(lowerBound, null);
        return this;
    }

    /**
     * Crates new {@link Predicate} for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values
     * @return
     */
    public Criteria in(Object... values) {
        return (Criteria) in(Arrays.asList(values));
    }

    /**
     * Crates new {@link Predicate} for multiple values {@code (arg0 arg1 arg2 ...)}
     *
     * @param values the collection containing the values to match against
     * @return
     */
    public Criteria in(Iterable<?> values) {
        for (Object value : values) {
            if (value instanceof Collection) {
                in((Collection<?>) value);
            } else {
                is(value);
            }
        }
        return this;
    }

    public Field getField() {
        return this.field;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(this.isOr() ? "OR " : "AND ");
        sb.append(this.isNegating() ? "!" : "");
        sb.append(this.field != null ? this.field.getName() : "");

        if (this.predicates.size() > 1) {
            sb.append('(');
        }
        for (Predicate ce : this.predicates) {
            sb.append(ce.toString());
        }
        if (this.predicates.size() > 1) {
            sb.append(')');
        }
        sb.append(' ');
        return sb.toString();
    }

    // -------- PREDICATE STUFF --------

    public enum OperationKey {
        EQUALS("$equals"), CONTAINS("$contains"), STARTS_WITH("$startsWith"), ENDS_WITH("$endsWith"), EXPRESSION(
                "$expression"), BETWEEN("$between"), NEAR("$near"), WITHIN("$within"), FUZZY("$fuzzy"), SLOPPY("$sloppy"), FUNCTION(
                "$function");

        private final String key;

        private OperationKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

    }

    /**
     * Single entry to be used when defining search criteria
     *
     * @author Christoph Strobl
     * @author Francisco Spaeth
     */
    public static class Predicate {

        private String key;
        private Object value;

        public Predicate(OperationKey key, Object value) {
            this(key.getKey(), value);
        }

        public Predicate(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return null if not set
         */
        public String getKey() {
            return key;
        }

        /**
         * set the operation key to be applied when parsing query
         *
         * @param key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return null if not set
         */
        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ":" + value;
        }

    }

    // -------- NODE STUFF ---------

    /**
     * Explicitly connect {@link Criteria} with another one allows to create explicit bracketing.
     *
     * @return
     * @since 1.4
     */
    public Criteria connect() {
        Crotch c = new Crotch();
        c.add(this);
        return c;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Crotch and(Node node) {

        if (!(node instanceof Criteria)) {
            throw new IllegalArgumentException("Can only add instances of Criteria");
        }

        Crotch crotch = new Crotch();
        crotch.setParent(this.getParent());
        crotch.add(this);
        crotch.add((Criteria) node);
        return crotch;
    }

    @SuppressWarnings("unchecked")
    public Crotch and(String fieldname) {
        Criteria node = new Criteria(fieldname);
        return and(node);
    }

    @SuppressWarnings("unchecked")
    public Crotch or(Node node) {

        if (!(node instanceof Criteria)) {
            throw new IllegalArgumentException("Can only add instances of Criteria");
        }

        node.setPartIsOr(true);

        Crotch crotch = new Crotch();
        crotch.setParent(this.getParent());
        crotch.add(this);
        crotch.add((Criteria) node);
        return crotch;
    }

    @SuppressWarnings("unchecked")
    public Crotch or(String fieldname) {
        Criteria node = new Criteria(fieldname);
        node.setPartIsOr(true);
        return or(node);
    }

    public Set<Predicate> getPredicates() {
        return Collections.unmodifiableSet(this.predicates);
    }

}
