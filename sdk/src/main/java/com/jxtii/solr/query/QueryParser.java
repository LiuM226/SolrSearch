package com.jxtii.solr.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 查询语法转换
 * Created by guolf on 17/7/7.
 */
public class QueryParser {

    protected static final String CRITERIA_VALUE_SEPERATOR = " ";

    private final List<PredicateProcessor> critieraEntryProcessors = new ArrayList<PredicateProcessor>();
    private final PredicateProcessor defaultProcessor = new DefaultProcessor();

    {
        critieraEntryProcessors.add(new WildcardProcessor());
        critieraEntryProcessors.add(new BetweenProcessor());
    }

    /**
     * 根据Criteria生成查询条件语句
     * @param node
     * @return
     */
    public String createQueryString(Node node) {
        return this.createQueryStringFromNode(node, 0);
    }

    private String createQueryStringFromNode(Node node, int position) {

        StringBuilder query = new StringBuilder();
        if (position > 0) {
            query.append(node.isOr() ? " OR " : " AND ");
        }

        if (node.hasSiblings()) {
            if (node.isNegating()) {
                query.append("-");
            }
            if (!node.isRoot() || (node.isRoot() && node.isNegating())) {
                query.append('(');
            }

            int i = 0;
            for (Node nested : node.getSiblings()) {
                query.append(createQueryStringFromNode(nested, i++));
            }

            if (!node.isRoot() || (node.isRoot() && node.isNegating())) {
                query.append(')');
            }
        } else {
            query.append(createQueryFragmentForCriteria((Criteria) node));
        }
        return query.toString();
    }

    private String getNullsafeFieldName(Field field) {
        if (field == null || field.getName() == null) {
            return "";
        }
        return field.getName();
    }

    private boolean containsFunctionCriteria(Set<Criteria.Predicate> chainedCriterias) {
        for (Criteria.Predicate entry : chainedCriterias) {
            if (StringUtils.equals(Criteria.OperationKey.WITHIN.getKey(), entry.getKey())) {
                return true;
            } else if (StringUtils.equals(Criteria.OperationKey.NEAR.getKey(), entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    protected static final String DELIMINATOR = ":";


    protected String createQueryFragmentForCriteria(Criteria part) {
        Criteria criteria = part;
        StringBuilder queryFragment = new StringBuilder();
        boolean singeEntryCriteria = (criteria.getPredicates().size() == 1);

        String fieldName = getNullsafeFieldName(criteria.getField());

        if (!StringUtils.isEmpty(fieldName) && !containsFunctionCriteria(criteria.getPredicates())) {
            if("id".equals(fieldName) || "filePath".equals(fieldName)) {
                queryFragment.append(fieldName);
            } else {
                queryFragment.append("attr_" + fieldName);
            }
            queryFragment.append(DELIMINATOR);
        }

        // no criteria given is defaulted to not null
        if (criteria.getPredicates().isEmpty()) {
            queryFragment.append("[* TO *]");
            return queryFragment.toString();
        }

        if (!singeEntryCriteria) {
            queryFragment.append("(");
        }

        CriteriaQueryStringValueProvider valueProvider = new CriteriaQueryStringValueProvider(criteria);
        while (valueProvider.hasNext()) {
            queryFragment.append(valueProvider.next());
            if (valueProvider.hasNext()) {
                queryFragment.append(CRITERIA_VALUE_SEPERATOR);
            }
        }

        if (!singeEntryCriteria) {
            queryFragment.append(")");
        }
        return queryFragment.toString();
    }

    public interface PredicateProcessor {

        /**
         * @param predicate
         * @return true if predicate can be processed by this parser
         */
        boolean canProcess(Criteria.Predicate predicate);

        /**
         * Create query string representation of given {@link Criteria.Predicate}
         *
         * @param predicate
         * @param field
         * @return
         */
        Object process(Criteria.Predicate predicate, Field field);

    }


    abstract class BasePredicateProcessor implements PredicateProcessor {

        protected static final String DOUBLEQUOTE = "\"";

        protected final String[] RESERVED_CHARS = {DOUBLEQUOTE,"+",  "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]",
                "^", "~", "*", "?", ":", "\\"};
        protected String[] RESERVED_CHARS_REPLACEMENT = {"\\" + DOUBLEQUOTE, "\\+","\\-", "\\&\\&", "\\|\\|", "\\!",
                "\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\^", "\\~", "\\*", "\\?", "\\:", "\\\\"};

        @Override
        public Object process(Criteria.Predicate predicate, Field field) {
            if (predicate == null || predicate.getValue() == null) {
                return null;
            }
            return doProcess(predicate, field);
        }

        protected Object filterCriteriaValue(Object criteriaValue) {
            String value = escapeCriteriaValue((String) criteriaValue);
            return processWhiteSpaces(value);
        }

        private String escapeCriteriaValue(String criteriaValue) {
            return StringUtils.replaceEach(criteriaValue, RESERVED_CHARS, RESERVED_CHARS_REPLACEMENT);
        }

        private String processWhiteSpaces(String criteriaValue) {
            if (StringUtils.contains(criteriaValue, CRITERIA_VALUE_SEPERATOR)) {
                return DOUBLEQUOTE + criteriaValue + DOUBLEQUOTE;
            }
            return criteriaValue;
        }

        protected abstract Object doProcess(Criteria.Predicate predicate, Field field);

    }

    class DefaultProcessor extends BasePredicateProcessor {

        @Override
        public boolean canProcess(Criteria.Predicate predicate) {
            return true;
        }

        @Override
        public Object doProcess(Criteria.Predicate predicate, Field field) {
            return filterCriteriaValue(predicate.getValue());
        }

    }

    class CriteriaQueryStringValueProvider implements Iterator<String> {

        private final Criteria criteria;
        private Iterator<Criteria.Predicate> delegate;

        CriteriaQueryStringValueProvider(Criteria criteria) {

            this.criteria = criteria;
            this.delegate = criteria.getPredicates().iterator();
        }

        @SuppressWarnings("unchecked")
        private <T> T getPredicateValue(Criteria.Predicate predicate) {
            PredicateProcessor processor = findMatchingProcessor(predicate);
            return (T) processor.process(predicate, criteria.getField());
        }

        private PredicateProcessor findMatchingProcessor(Criteria.Predicate predicate) {
            for (PredicateProcessor processor : critieraEntryProcessors) {
                if (processor.canProcess(predicate)) {
                    return processor;
                }
            }

            return defaultProcessor;
        }

        @Override
        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        @Override
        public String next() {
            Object o = getPredicateValue(this.delegate.next());
            String s = o != null ? o.toString() : null;
            return s;
        }

        @Override
        public void remove() {
            this.delegate.remove();
        }
    }

    /**
     * CONTAINS 处理
     */
    class WildcardProcessor extends BasePredicateProcessor {

        @Override
        public boolean canProcess(Criteria.Predicate predicate) {
            return Criteria.OperationKey.CONTAINS.getKey().equals(predicate.getKey())
                    || Criteria.OperationKey.STARTS_WITH.getKey().equals(predicate.getKey())
                    || Criteria.OperationKey.ENDS_WITH.getKey().equals(predicate.getKey());
        }

        @Override
        protected Object doProcess(Criteria.Predicate predicate, Field field) {
            Object filteredValue = filterCriteriaValue(predicate.getValue());
            if (Criteria.OperationKey.CONTAINS.getKey().equals(predicate.getKey())) {
                return Criteria.WILDCARD + filteredValue + Criteria.WILDCARD;
            } else if (Criteria.OperationKey.STARTS_WITH.getKey().equals(predicate.getKey())) {
                return filteredValue + Criteria.WILDCARD;
            } else if (Criteria.OperationKey.ENDS_WITH.getKey().equals(predicate.getKey())) {
                return Criteria.WILDCARD + filteredValue;
            }
            return filteredValue;
        }
    }

    class BetweenProcessor extends BasePredicateProcessor {

        private static final String RANGE_OPERATOR = " TO ";

        @Override
        public boolean canProcess(Criteria.Predicate predicate) {
            return Criteria.OperationKey.BETWEEN.getKey().equals(predicate.getKey());
        }

        @Override
        public Object doProcess(Criteria.Predicate predicate, Field field) {
            Object[] args = (Object[]) predicate.getValue();
            String rangeFragment = ((Boolean) args[2]).booleanValue() ? "[" : "{";
            rangeFragment += createRangeFragment(args[0], args[1]);
            rangeFragment += ((Boolean) args[3]).booleanValue() ? "]" : "}";
            return rangeFragment;
        }

        protected String createRangeFragment(Object rangeStart, Object rangeEnd) {
            String rangeFragment = "";
            rangeFragment += (rangeStart != null ? filterCriteriaValue(rangeStart)+"" : Criteria.WILDCARD);
            rangeFragment += RANGE_OPERATOR;
            rangeFragment += (rangeEnd != null ? filterCriteriaValue(rangeEnd)+"" : Criteria.WILDCARD);
            return rangeFragment;
        }

    }
}
