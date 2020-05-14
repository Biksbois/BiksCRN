package simpleAdder.interpret.TypeCheckers;

public class BiksPair<First, Second> {
    private First first;
    private Second second;

    public BiksPair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public void setKey(First first) {
        this.first = first;
    }

    public void setValue(Second second) {
        this.second = second;
    }

    public First getKey() {
        return first;
    }

    public Second getValue() {
        return second;
    }

    public void set(First first, Second second) {
        setKey(first);
        setValue(second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BiksPair pair = (BiksPair) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
        return second != null ? second.equals(pair.second) : pair.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}