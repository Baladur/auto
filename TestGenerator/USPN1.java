
@TestInfo(name = "USPN1. Login.", description = "Login by specified role.")
public class USPN1 extends BaseTest {
	private String role;
	private Integer timeout;
	private String link;
	
	public USPN1 withRole(String role) { this.role = role; }
	
	public USPN1 withTimeout(Integer timeout) { this.timeout = timeout; }
	
	public USPN1 withLink(String link) { this.link = link; }
	
	
	@Test
	public void test() {
		execute();
		driver.close();
	}
	
	@StepInfo(name = "iaowj")
	public void step1() {
		assertThat(role.length() > "eee".length()).describedAs("Input param role must not be empty.").isTrue();
		Boolean checkBool = (4 + 6) > (7 + 4) - (9 / 4);
		driver.go(link);
		driver.click(null.LOG_OUT)
		.orElse(() -> {
			driver.select(null.menu).option("info").duringSeconds(timeout).end();
		}).end();
		driver.fill(null.USERNAME).withText(role).duringSeconds(timeout).end();
		driver.fill(null.PASSWORD).withText(role).duringSeconds(timeout).end();
		driver.click(null.LOGIN).end();
		if (role starts with "a") {
			Double ab = 4 - 3;
		} else {
			Integer i = 0;
			while (i < 10) {
				driver.click(null.ffff).end();
			}
		}
		
		@StepInfo(name = "iiiiii")
		public void step2() {
			driver.click(null.efef).end();
		}
