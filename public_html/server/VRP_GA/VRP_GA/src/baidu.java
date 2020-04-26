import java.util.Random;

public class baidu {

	public static void main(String[] args) {
		test();
		test();
		test();
		
	}
	
	public static void test() {
		Random rand = new Random();
		for(int i = 0; i < 10; i++) {
			System.out.print(rand.nextInt() + " ");
		}
		System.out.println();
	}
}
