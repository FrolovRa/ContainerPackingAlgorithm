import java.util.ArrayList;
import java.util.List;

public class BottleDivider {
    static private int[] boxPack = {6, 8, 10, 12, 16, 25};
    static private int orderQuantity;
    private static List<Integer> bestResult;
    private static boolean flag = true;


    private static void recursion(int sum, int boxCapacity, List<Integer> selectionHistory) {
        List<Integer> currentSelectionHistory = new ArrayList<>(selectionHistory);
        currentSelectionHistory.add(boxCapacity);
        sum = sum + boxCapacity;
        if (flag) {
            if (sum == orderQuantity) {
                bestResult = currentSelectionHistory;
                flag = false;
            } else if (sum < orderQuantity) {
                for (int i = boxPack.length - 1; i >= 0; i--) {
                    recursion(sum, boxPack[i], currentSelectionHistory);
                }
            }
        }
    }

    private static void approximateRecursion(int sum, int boxCapacity, List<Integer> selectionHistory, int d) {
        List<Integer> currentSelectionHistory = new ArrayList<>(selectionHistory);
        currentSelectionHistory.add(boxCapacity);
        sum = sum + boxCapacity;
        if (flag) {

            if (sum - orderQuantity <= d & sum - orderQuantity > 0) {
                bestResult = currentSelectionHistory;
                flag = false;
            } else if (sum < orderQuantity) {
                for (int i = boxPack.length - 1; i >= 0; i--) {
                    approximateRecursion(sum, boxPack[i], currentSelectionHistory, d);
                }
            }
        }
    }

    private static List<Integer> findOptimalPacking(List<Map> beerOrder) {
        int difference = 1;
        orderQuantity = 0;

        for (Map e : beerOrder) {
            orderQuantity += (int) Math.ceil(e.getQuantity() * e.getCell());
        }

        for (int i = boxPack.length - 1; i >= 0; i--) recursion(0, boxPack[i], new ArrayList<>());        

        if (bestResult == null) {
            while (true) {
                for (int i = boxPack.length - 1; i >= 0; i--) {
                    approximateRecursion(0, boxPack[i], new ArrayList<>(), difference);
                }
                if (bestResult == null) difference++;
                else break;
            }
        }
        flag = true;
        return bestResult;
    }

    public static List<SortedMap> divideOrder(List<Map> beerOrder) {
        findOptimalPacking(beerOrder);
        int i = 0;
        int free;
        SortedMap temp = null;
        List<SortedMap> result = new ArrayList<>();
        free = bestResult.get(i);

        for (Map n : beerOrder) {
            double left = n.getQuantity() * n.getCell();
            while (left != 0) {
                if (temp == null || free == 0) {
                    temp = new SortedMap(bestResult.get(i), new ArrayList<>());
                    free = bestResult.get(i);
                    i++;
                }
                if (free == left) {
                    temp.getContent().add(new Map(n.getSku(), n.getCell(), (int) Math.ceil(left)));
                    free = 0;
                    left = 0;
                } else if (free > left) {
                    temp.getContent().add(new Map(n.getSku(), n.getCell(), (int) Math.ceil(left)));
                    free = free - (int) Math.ceil(left);
                    left = 0;
                } else {
                    if (n.getCell() == 1) temp.getContent().add(new Map(n.getSku(), n.getCell(), free));
                    else temp.getContent().add(new Map(n.getSku(), n.getCell(), free * 2));
                    left = left - free;
                    free = 0;
                    result.add(temp);
                }
            }
            if (free == 0) result.add(temp);
        }
        if (result.isEmpty() || result.size() != bestResult.size()) result.add(temp);
        bestResult = null;
        return result;
    }

    public static void main(String[] args) {
        List<Map> test = new ArrayList<>();
        test.add(new Map("beer1", 1, 3));
        test.add(new Map("beer2", 0.5, 2));
        test.add(new Map("beer3", 1, 1));

            System.out.println("Пример первый из задания: ");
            System.out.println("Исходные данные: " + test);
            System.out.println("Оптимальный подбор коробок: " + findOptimalPacking(test));
            System.out.println("Результат: " + divideOrder(test));
            System.out.println();
            System.out.println();


        List<Map> test2 = new ArrayList<>();
        test2.add(new Map("beer1", 1, 15));
        test2.add(new Map("beer2", 0.5, 3));
        test2.add(new Map("beer3", 1, 10));

            System.out.println("Второй пример из задания: ");
            System.out.println("Исходные данные: " + test2);
            System.out.println("Оптимальный подбор коробок: " + findOptimalPacking(test2));
            System.out.println("Результат: " + divideOrder(test2));
    }
}

class Map {
    private String sku;
    private double cell;
    private int quantity;

    Map(String sku, double cell, int quantity) {
        this.sku = sku;
        this.cell = cell;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "[" +
                "sku: \"" + sku + '\"' +
                ", cell: " + cell +
                ", quantity: " + quantity +
                ']';
    }
}

class SortedMap {
    private int boxPack;
    private List<Map> content;

    SortedMap(int boxPack, List<Map> content) {
        this.content = content;
        this.boxPack = boxPack;
    }

    public int getBoxPack() {
        return boxPack;
    }

    public void setBoxPack(int boxPack) {
        this.boxPack = boxPack;
    }

    public List<Map> getContent() {
        return content;
    }

    public void setContent(List<Map> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "[" +
                "boxPack: " + boxPack +
                ", content: " + content +
                ']';
    }
}
