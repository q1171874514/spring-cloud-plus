package com.example.controller;

import com.example.comment.TestAnnotation;
import com.example.dto.TestDTO;
import com.example.page.PageData;
import com.example.service.TestService;
import com.example.utils.Result;
import com.example.validator.AssertUtils;
import com.example.validator.ValidatorUtils;
import com.example.validator.group.AddGroup;
import com.example.validator.group.DefaultGroup;
import com.example.validator.group.UpdateGroup;
import com.example.vo.BusCusMoneyVo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    private Integer a = 0;

    @Autowired
    private TestService testService;

    @TestAnnotation("11")
    @PostMapping("/post")
    public Map<String, Object> post(@RequestBody Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    //    @TestAnnotation("11")
    @PostMapping("/body")
    public Map<String, Object> body(@RequestBody Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @PostMapping("/params")
    public Map<String, Object> params(@RequestParam Map<String, Object> map) {
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @PostMapping("/headers")
    public Map<String, Object> headers(ServletRequest request, @RequestHeader Map<String, Object> map) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        map.put("test", a++);
        return map;
    }

    @TestAnnotation("11")
    @GetMapping("/headers")
    public Map<String, Object> headerss(ServletRequest request, @RequestHeader Map<String, Object> map) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        map.put("test", a++);
        return map;
    }

    @GetMapping("page")
    @RequiresPermissions("test:page")
    public Result<PageData<TestDTO>> page(@RequestParam Map<String, Object> params) {
        //字典类型
        PageData<TestDTO> page = testService.page(params);

        return new Result<PageData<TestDTO>>().ok(page);
    }

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresPermissions("test:list")
    public Result<List<TestDTO>> list() {
        List<TestDTO> list = testService.list(new HashMap<>(1));

        return new Result<List<TestDTO>>().ok(list);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("test:info")
    public Result<TestDTO> get(@PathVariable("id") Long id) {
        TestDTO data = testService.get(id);

        return new Result<TestDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @RequiresPermissions("test:save")
    public Result save(@RequestBody TestDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        testService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @RequiresPermissions("test:update")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public Result update(@RequestBody TestDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        testService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除")
    @RequiresPermissions("test:delete")
    public Result delete(@PathVariable("id") Long[] id) {
        //效验数据
        AssertUtils.isNull(id, "id");

        testService.delete(id);

        return new Result();
    }

    @PostMapping("/busToCus")
    public Result busToCus(@RequestBody BusCusMoneyVo busCusMoneyVo) {
        testService.busToCusMoney(busCusMoneyVo.getBusId(), busCusMoneyVo.getCusId(), busCusMoneyVo.getMoney());
        return new Result();
    }

}
