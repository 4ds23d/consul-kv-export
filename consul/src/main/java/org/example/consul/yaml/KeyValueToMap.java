package org.example.consul.yaml;

import lombok.AllArgsConstructor;
import org.example.consul.KValue;

import java.util.*;
import java.util.regex.Pattern;


@AllArgsConstructor
public class KeyValueToMap {
    static Pattern containerPattern = Pattern.compile("^([\\w\\d]+)([\\.\\[]{1}.+)");
    static Pattern tablePattern = Pattern.compile("^\\[(\\d+)\\](.*)");
    private final List<KValue> keys;

    Map<String, Object> toMap() {
        var map = new HashMap<String, Object>();

        keys.forEach(key -> {
            var actions = buildActionsToApply(key);
            actions.apply(map);
        });

        return map;
    }

    private Actions buildActionsToApply(KValue kValue) {
        var actions = new Actions();

        if (kValue.hasValue()) {
            addNestedKey(kValue.getKey().name(), actions, kValue);
        }

        return actions;
    }

    private void addNestedKey(String name, Actions actions, KValue kValue) {
        if (name.isEmpty()) {
            actions.addValue(kValue);
            return;
        }
        if (name.charAt(0) == '.') {
            addNestedKey(name.substring(1), actions, kValue);
            return;
        }

        if (name.charAt(0) == '[') {
            var match = tablePattern.matcher(name);
            if (match.matches()) {
                var tableIdx = match.group(1);
                var left = match.group(2);
                actions.addList(Integer.valueOf(tableIdx));
                addNestedKey(left, actions, kValue);
            }
            return;
        }

        var container = containerPattern.matcher(name);
        if (container.matches()) {
            var nesting = container.group(1);
            var left = container.group(2);
            actions.addContainer(nesting);
            addNestedKey(left, actions, kValue);
            return;
        }

        actions.addContainer(name);
        actions.addValue(kValue);
    }

    @AllArgsConstructor
    static class Actions {
        final List<Action> actions = new ArrayList<>();

        private void add(Action action) {
            this.actions.add(action);
        }

        void apply(HashMap<String, Object> map) {
            Container lastContainer = new RootContainer(map);

            for (var action : actions) {
                lastContainer = action.call(lastContainer);
            }
        }

        public void addContainer(String key) {
            add(new NestingContainer(key));
        }

        public void addList( Integer idx) {
            add(new NestingList(idx));
        }

        public void addValue(KValue kValue) {
            add(new Value(kValue));
        }
    }

    interface Action {
        Container call(Container lastAction);
    }

    interface Container {
        void addValue(String value);

        Container newNesting(String name);

        Container newList(Integer idx);

    }

    @AllArgsConstructor
    static class RootContainer implements Container {
        private final Map<String, Object> container;

        @Override
        public void addValue(String value) {
            throw new IllegalStateException("Illegal state");
        }

        @Override
        public Container newNesting(String name) {
            return new MapNesting(container, name);
        }

        @Override
        public Container newList(Integer idx) {
            throw new IllegalStateException("Illegal state");
        }
    }

    @AllArgsConstructor
    static class MapNesting implements Action, Container {
        private final Map<String, Object> container;
        private final String name;

        @Override
        public Container call(Container lastAction) {
            return null;
        }

        @Override
        public void addValue(String value) {
            container.put(name, value);
        }

        @Override
        public Container newNesting(String name) {
            container.putIfAbsent(this.name, new HashMap<String, Object>());
            return new MapNesting((Map<String, Object>) container.get(this.name), name);
        }

        @Override
        public Container newList(Integer idx) {
            container.putIfAbsent(this.name, new ArrayList<>());
            var list = (List)container.get(this.name);
            return new ListContainer(list, idx);
        }
    }

    @AllArgsConstructor
    static class ListContainer implements Container {
        private final List<Object> container;
        private final Integer idx;

        @Override
        public void addValue(String value) {
            if (container.size() == idx) {
                container.add(value);
            } else {
                container.set(idx, value);
            }
        }

        @Override
        public Container newNesting(String name) {
            if (container.size() == idx) {
                container.add(new HashMap<>());
            }
            var prevContainer = (Map<String, Object>)container.get(idx);
            return new MapNesting(prevContainer, name);
        }

        @Override
        public Container newList(Integer idx) {
            if (container.size() == this.idx) {
                container.add(new ArrayList<>());
            }
            var newContainer = (List<Object>) container.get(this.idx);
            return new ListContainer(newContainer, idx);
        }
    }

    @AllArgsConstructor
    static class NestingContainer implements Action {
        private final String name;

        @Override
        public Container call(Container lastAction) {
            return lastAction.newNesting(name);
        }

    }

    @AllArgsConstructor
    static class NestingList implements Action {
        private final Integer idx;

        @Override
        public Container call(Container container) {
            return container.newList(idx);
        }

    }

    @AllArgsConstructor
    static class Value implements Action {
        private final KValue kValue;

        @Override
        public Container call(Container container) {
            container.addValue(kValue.getValueAsString());
            return container;
        }
    }
}