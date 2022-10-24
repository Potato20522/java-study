package chaoter2.builder;

// Telescoping constructor pattern - does not scale well!
// 提供可伸缩构造方法，但参数过多时，不方便
public class NutritionFactsOld1 {
    private final int servingSize; // (mL) required
    private final int servings; // (per container) required
    private final int calories; // (per serving) optional
    private final int fat; // (g/serving) optional
    private final int sodium; // (mg/serving) optional
    private final int carbohydrate; // (g/serving) optional

    public NutritionFactsOld1(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFactsOld1(int servingSize, int servings,
                              int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFactsOld1(int servingSize, int servings,
                              int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFactsOld1(int servingSize, int servings,
                              int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFactsOld1(int servingSize, int servings,
                              int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }

    public static void main(String[] args) {
        NutritionFactsOld1 cocaCola = new NutritionFactsOld1(240, 8, 100, 0, 35, 27);
    }
}