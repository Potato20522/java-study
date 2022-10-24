package chaoter2.builder;

import lombok.Setter;

// JavaBeans Pattern - allows inconsistency, mandates mutability
// JavaBean 写法，让对象变得可变，这种不一致导致bug可能性较高，如果想要不可变就不方便了
@Setter
public class NutritionFactsOld2 {
    // Parameters initialized to default values (if any)
    private int servingSize = -1; // Required; no default value
    private int servings = -1; // Required; no default value
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public static void main(String[] args) {
        NutritionFactsOld2 cocaCola = new NutritionFactsOld2();
        cocaCola.setServingSize(240);
        cocaCola.setServings(8);
        cocaCola.setCalories(100);
        cocaCola.setSodium(35);
        cocaCola.setCarbohydrate(27);
    }
}
