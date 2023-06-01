package com.redisdemo02;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.redisdemo02.entity.SysGod;
import com.redisdemo02.service.SysGodService;
import com.redisdemo02.service.UserShopCarService;

import cn.hutool.core.lang.Console;

@SpringBootTest
class Redisdemo02ApplicationTests {

	@Autowired
	SysGodService sysGodService;

	@Autowired
	UserShopCarService userShopCarService;

	private final SysGod testgod() {
		SysGod god = new SysGod();
		god.setId(4);
		god.setGoodName("test");
		god.setGodNum(99);
		god.setGodCost(33);
		return god;
	}

	@Test
	void contextLoads() {
	}

	@Test
	void test01() {
		Console.log(sysGodService.getGodMap());
	}

	@Test
	void test02() {
		SysGod god = testgod();
		sysGodService.saveGodMap(god);
	}

	@Test
	void test03() {
		sysGodService.removeGodMap(4);
	}

	@Test
	void test04() {
		SysGod god = testgod();
		god.setGoodName("test-test");
		sysGodService.updateGodMap(god);
	}

	@Test
	void test05() {
		Console.log(sysGodService.getGodItemByMap(3));
		Console.log(sysGodService.getGodItemByMap(3).get("id"));
	}

	@Test
	void test06() {
		Console.log(userShopCarService.addShopCarItem(1, 3));
	}

	@Test
	void test07() {
		Console.log(userShopCarService.delShopCarItem(1, 3));
	}

}
