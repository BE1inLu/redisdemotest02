package com.redisdemo02.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redisdemo02.Result.Result;
import com.redisdemo02.entity.SysGod;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author enluba
 * @since 2023-05-31
 */
@RestController
@RequestMapping("/sysGod")
public class SysGodController extends baseController {

    @GetMapping("/godList")
    @SaCheckRole(value = { "ROLE_ADMIN", "ROLE_USER", "ROLE_SHOP" }, mode = SaMode.OR)
    public Result godList() {
        // 获取商品列表
        List<Map<String, Object>> godMap = sysGodService.getGodMap();
        return Result.succ(godMap);
    }

    @SaCheckRole("ROLE_ADMIN")
    @GetMapping("/godSave")
    public Result save(@Validated @RequestBody SysGod sysGod) {
        sysGodService.saveGodMap(sysGod);
        return Result.succ("save succ");
    }

    @SaCheckRole("ROLE_ADMIN")
    @GetMapping("/godDelete")
    public Result delete(@RequestBody int id) {
        sysGodService.removeGodMap(id);
        return Result.succ("delete god : " + id + " success");
    }

    @SaCheckRole("ROLE_ADMIN")
    @GetMapping("/godUpdate")
    public Result Update(@Validated @RequestBody SysGod sysGod) {
        sysGodService.updateGodMap(sysGod);
        return Result.succ("update succ");
    }

}
