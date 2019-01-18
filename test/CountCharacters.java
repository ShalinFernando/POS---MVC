import java.util.function.IntPredicate;

public class CountCharacters {

    public static void main(String[] args) {
        String sql = "UPDATE Customer SET id=? WHERE name=?";
        int parametersCount = getParametersCount1(sql);
        System.out.println(parametersCount);
    }

    public static int getParametersCount1(String sql){
        return (int) sql.chars().filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == "?".codePointAt(0);
            }
        }).count();
    }

    public static int getParametersCount2(String sql){
        return sql.concat(" ").split("[?]").length -1;
    }

    public static int getParametersCount3(String sql){
        int count = 0;
        for (char c : sql.toCharArray()) {
            if (c == '?'){
                count++;
            }
        }
        return count;
    }

}
