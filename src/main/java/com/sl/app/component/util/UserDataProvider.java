package com.sl.app.component.util;

import static java.util.Comparator.naturalOrder;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.sl.entity.User;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;

// Person data provider
public class UserDataProvider extends AbstractBackEndDataProvider<User, CrudFilter> {

	private static final long serialVersionUID = -5972145006789859720L;

// A real app should hook up something like JPA
   public final List<User> DATABASE = User.createDemoData(10);

    private Consumer<Long> sizeChangeListener;

    @Override
    protected Stream<User> fetchFromBackEnd(Query<User, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<User> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream.filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<User, CrudFilter> query) {
        // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    private static Predicate<User> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<User>) person -> {
                    try {
                        Object value = valueOf(constraint.getKey(), person);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).reduce(Predicate::and).orElse(e -> true);
    }

    private static Comparator<User> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream().map(sortClause -> {
            try {
                Comparator<User> comparator = Comparator.comparing(
                        person -> (Comparable) valueOf(sortClause.getKey(),
                                person));

                if (sortClause.getValue() == SortDirection.DESCENDING) {
                    comparator = comparator.reversed();
                }

                return comparator;

            } catch (Exception ex) {
                return (Comparator<User>) (o1, o2) -> 0;
            }
        }).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, User person) {
        try {
            Field field = User.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

   public void persist(User item) {
        if (item.getUserId() == null) {
            item.setUserId(DATABASE.stream().map(User::getUserId).max(naturalOrder())
                    .orElse("0") + 1);
        }

        final Optional<User> existingItem = find(item.getUserId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            DATABASE.add(position, item);
        } else {
            DATABASE.add(item);
        }
    }

    Optional<User> find(String id) {
        return DATABASE.stream().filter(entity -> entity.getUserId().equals(id))
                .findFirst();
    }

    public void delete(User item) {
        DATABASE.removeIf(entity -> entity.getUserId().equals(item.getUserId()));
    }

}