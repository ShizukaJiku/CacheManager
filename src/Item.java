public class Item implements Identifiable<Integer> {
    private final Integer id;
    private final String value;

    public Item(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Item{id=" + id + ", value='" + value + "'}";
    }
}
