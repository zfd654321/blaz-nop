package test;

import com.bl.nop.dao.game.GameDao;
import com.bl.nop.entity.game.Game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTest extends BaseJunit4Test {

	@Autowired
	private GameDao gameDao;

	@Test
	public void test() {
		try {
			System.out.println(null != gameDao);
			Game game = gameDao.selectByPrimaryKey("V100310");
			System.out.println(null != game ? game.getName() : "游戏为空");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
