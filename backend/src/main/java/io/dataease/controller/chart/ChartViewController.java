package io.dataease.controller.chart;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.dataease.auth.annotation.DePermission;
import io.dataease.auth.annotation.DePermissionProxy;
import io.dataease.base.domain.ChartViewCacheWithBLOBs;
import io.dataease.base.domain.ChartViewWithBLOBs;
import io.dataease.commons.constants.DePermissionType;
import io.dataease.commons.constants.ResourceAuthLevel;
import io.dataease.controller.request.chart.ChartCalRequest;
import io.dataease.controller.request.chart.ChartExtRequest;
import io.dataease.controller.request.chart.ChartViewCacheRequest;
import io.dataease.controller.request.chart.ChartViewRequest;
import io.dataease.controller.response.ChartDetail;
import io.dataease.dto.chart.ChartViewDTO;
import io.dataease.service.chart.ChartViewCacheService;
import io.dataease.service.chart.ChartViewService;
import io.dataease.service.panel.PanelViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author gin
 * @Date 2021/3/1 1:17 下午
 */
@Api(tags = "视图：视图域")
@ApiSupport(order = 130)
@RestController
@RequestMapping("/chart/view")
public class ChartViewController {
    @Resource
    private ChartViewService chartViewService;

    @Resource
    private ChartViewCacheService chartViewCacheService;

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    @ApiOperation("保存")
    @PostMapping("/save/{panelId}")
    public ChartViewDTO save(@PathVariable String panelId, @RequestBody ChartViewCacheRequest request) {
        return chartViewService.save(request);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    @ApiOperation("新建视图")
    @PostMapping("/newOne/{panelId}")
    public ChartViewWithBLOBs save(@PathVariable String panelId, @RequestBody ChartViewWithBLOBs chartViewWithBLOBs) {
        return chartViewService.newOne(chartViewWithBLOBs);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE)
    @ApiOperation("保存到缓存表")
    @PostMapping("/save2Cache/{panelId}")
    public void save2Cache(@PathVariable String panelId, @RequestBody ChartViewCacheWithBLOBs chartViewWithBLOBs) {
        chartViewService.save2Cache(chartViewWithBLOBs);
    }

    @ApiIgnore
    @ApiOperation("查询")
    @PostMapping("/list")
    public List<ChartViewDTO> list(@RequestBody ChartViewRequest chartViewRequest) {
        return chartViewService.list(chartViewRequest);
    }

    @ApiIgnore
    @ApiOperation("查询组")
    @PostMapping("/listAndGroup")
    public List<ChartViewDTO> listAndGroup(@RequestBody ChartViewRequest chartViewRequest) {
        return chartViewService.listAndGroup(chartViewRequest);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW, paramIndex = 1)
    @ApiOperation("详细信息")
    @PostMapping("/get/{id}/{panelId}")
    public ChartViewDTO get(@PathVariable String id, @PathVariable String panelId,@RequestBody ChartViewRequest viewRequest) {
        ChartViewDTO result = chartViewService.getOne(id,viewRequest.getQueryFrom());
        return result;
    }

    @ApiIgnore
    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        chartViewService.delete(id);
    }

    @DePermissionProxy(value = "proxy", paramIndex = 2)
    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW, paramIndex = 1)
    @ApiOperation("数据")
    @PostMapping("/getData/{id}/{panelId}")
    public ChartViewDTO getData(@PathVariable String id, @PathVariable String panelId,
            @RequestBody ChartExtRequest requestList) throws Exception {
        return chartViewService.getData(id, requestList);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW, paramIndex = 1)
    @ApiOperation("视图详情")
    @PostMapping("chartDetail/{id}/{panelId}")
    public ChartDetail chartDetail(@PathVariable String id, @PathVariable String panelId) {
        return chartViewService.getChartDetail(id);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_MANAGE, paramIndex = 1)
    @ApiOperation("复制")
    @PostMapping("chartCopy/{id}/{panelId}")
    public String chartCopy(@PathVariable String id, @PathVariable String panelId) {
        return chartViewService.chartCopy(id, panelId);
    }

    @ApiIgnore
    @GetMapping("searchAdviceSceneId/{panelId}")
    public String searchAdviceSceneId(@PathVariable String panelId) {
        return chartViewService.searchAdviceSceneId(panelId);
    }

    @ApiIgnore
    @ApiOperation("搜索")
    @PostMapping("search")
    public List<ChartViewDTO> search(@RequestBody ChartViewRequest chartViewRequest) {
        return chartViewService.search(chartViewRequest);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW)
    @ApiOperation("计算结果")
    @PostMapping("/calcData/{panelId}")
    public ChartViewDTO calcData(@PathVariable String panelId, @RequestBody ChartCalRequest request) throws Exception {
        return chartViewService.calcData(request.getView(), request.getRequestList(), false);
    }

    @ApiIgnore
    @ApiOperation("验证视图是否使用相同数据集")
    @GetMapping("/checkSameDataSet/{viewIdSource}/{viewIdTarget}")
    public String checkSameDataSet(@PathVariable String viewIdSource, @PathVariable String viewIdTarget)
            throws Exception {
        return chartViewService.checkSameDataSet(viewIdSource, viewIdTarget);
    }

    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW)
    @ApiOperation("初始化仪表板视图缓存")
    @PostMapping("/initViewCache/{panelId}")
    public void initViewCache(@PathVariable String panelId) {
        chartViewService.initViewCache(panelId);
    }


    @DePermission(type = DePermissionType.PANEL, level = ResourceAuthLevel.PANNEL_LEVEL_VIEW, paramIndex = 1)
    @ApiOperation("重置视图缓存")
    @PostMapping("/resetViewCache/{id}/{panelId}")
    public void resetViewCache(@PathVariable String id, @PathVariable String panelId) {
        chartViewCacheService.refreshCache(id);
    }

    @ApiOperation("校验视图Title")
    @PostMapping("/checkTitle")
    public String checkTitle( @RequestBody ChartViewCacheRequest request) {
        return chartViewService.checkTitle(request);
    }

}
