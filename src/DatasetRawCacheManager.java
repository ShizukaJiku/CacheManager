import java.util.*;
import java.util.stream.Collectors;

public class DatasetRawCacheManager<ID extends Comparable<ID>, T extends Identifiable<ID>> {
    private ID minId = null;
    private ID maxId = null;
    private int expectedTotal = -1;
    private final NavigableMap<ID, Optional<T>> datasetMap = new TreeMap<>();
    private int knownCount = 0; // Cache del número de elementos conocidos para eficiencia

    private static final Optional<?> EMPTY = Optional.empty();

    @SuppressWarnings("unchecked")
    private Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public void updateFromPage(List<T> items, ID firstId, ID lastId, int total) {
        if (items == null || items.isEmpty()) return;

        // Guardar el total si aún no se conoce
        if (expectedTotal == -1) {
            expectedTotal = total;
        } else if (expectedTotal != total) {
            System.out.println("[WARN] Total esperado cambiado: antes=" + expectedTotal + ", ahora=" + total);
            expectedTotal = total;
        }

        // Actualizar los extremos conocidos
        if (minId == null || firstId.compareTo(minId) < 0) minId = firstId;
        if (maxId == null || lastId.compareTo(maxId) > 0) maxId = lastId;

        // Registrar explícitamente los extremos como vacíos si aún no están presentes
        datasetMap.putIfAbsent(firstId, empty());
        datasetMap.putIfAbsent(lastId, empty());

        // Agregar los datos recibidos
        for (T item : items) {
            ID id = item.getId();
            Optional<T> existing = datasetMap.get(id);
            if (existing == null || existing.isEmpty()) {
                knownCount++;
                datasetMap.put(id, Optional.of(item));
            }
        }
    }

    public List<ID> getKnownIds() {
        return datasetMap.entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .map(Map.Entry::getKey)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    public List<ID> getExpectedOrderedIds() {
        return Collections.unmodifiableList(new ArrayList<>(datasetMap.keySet()));
    }

    public List<ID[]> getGaps() {
        List<ID[]> gaps = new ArrayList<>();
        ID currentGapStart = null;

        for (Map.Entry<ID, Optional<T>> entry : datasetMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                if (currentGapStart == null) {
                    currentGapStart = entry.getKey();
                }
            } else {
                if (currentGapStart != null) {
                    gaps.add((ID[]) new Comparable[]{currentGapStart, entry.getKey()});
                    currentGapStart = null;
                }
            }
        }

        if (currentGapStart != null) {
            gaps.add((ID[]) new Comparable[]{currentGapStart, datasetMap.lastKey()});
        }

        return gaps;
    }

    public boolean isComplete() {
        if (expectedTotal != -1 && knownCount != expectedTotal) return false;
        return !datasetMap.containsValue(empty());
    }

    public int getCompletionPercentage() {
        if (expectedTotal <= 0) return 0;
        return (int) ((knownCount * 100.0) / expectedTotal);
    }

    public void invalidate(ID id) {
        Optional<T> current = datasetMap.get(id);
        if (current != null && current.isPresent()) {
            datasetMap.put(id, empty());
            knownCount--;
        }
    }

    public void clear() {
        datasetMap.clear();
        knownCount = 0;
        expectedTotal = -1;
        minId = maxId = null;
    }

    public void printState() {
        System.out.println("Expected Ordered IDs: " + getExpectedOrderedIds());
        System.out.println("Known IDs: " + getKnownIds());
        System.out.println("Gaps: " + getGaps().stream()
                .map(Arrays::toString)
                .toList());
        System.out.println("Completion: " + getCompletionPercentage() + "%");
        System.out.println("Is Complete: " + isComplete());
    }

    // Simulación de uso masiva
    public static void main(String[] args) {
        DatasetRawCacheManager<Integer, Item> manager = new DatasetRawCacheManager<>();

        int total = 1000;
        int pageSize = 20;

        for (int page = 0; page < total / pageSize; page++) {
            List<Item> pageItems = new ArrayList<>();
            int start = page * pageSize + 1;
            int end = start + pageSize;
            for (int i = start; i < end; i++) {
                pageItems.add(new Item(i, "val" + i));
            }
            manager.updateFromPage(pageItems, 1, total, total);
            manager.printState();
        }
    }
}
