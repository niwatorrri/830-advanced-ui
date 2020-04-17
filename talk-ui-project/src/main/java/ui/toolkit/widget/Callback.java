package ui.toolkit.widget;

@FunctionalInterface
public interface Callback<T> {
    public void update(T newValue);
}