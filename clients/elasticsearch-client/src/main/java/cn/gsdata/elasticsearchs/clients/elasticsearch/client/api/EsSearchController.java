package cn.gsdata.elasticsearchs.clients.elasticsearch.client.api;

import cn.gsdata.elasticsearchs.clients.elasticsearch.client.service.EsQueryService;
import cn.gsdata.elasticsearchs.commons.base.common.model.es.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ES查询接口
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2022/07/8 14:11
 */
@Api(tags = "搜索api")
@RestController
@RequestMapping(value = {"/es/search"}, produces = "application/json;charset=UTF-8")
public class EsSearchController {
    private final EsQueryService queryService;

    public EsSearchController(EsQueryService queryService) {
        this.queryService = queryService;
    }

    @ApiOperation("查询接口,索引有通过stamp进行拆分")
    @GetMapping({"/sharding/{source}"})
    public String getShardingEsSearch(@PathVariable("source") String source, @Valid Condition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.query(condition, source);
    }
    
    @ApiOperation("查询接口,索引有通过stamp进行拆分")
    @PostMapping({"/sharding/{source}"})
    public String postShardingEsSearch(@PathVariable("source") String source, @RequestBody @Valid Condition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.query(condition, source);
    }


    @ApiOperation(value = "中台服务Es分析接口")
    @GetMapping("/analyze/{source}")
    public String esAnalyzeSearch(@PathVariable("source") String source, @Valid EsAnalyzeCondition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.analyzeQuery(condition, source);
    }

    @ApiOperation(value = "中台服务Es分析接口1")
    @PostMapping("/analyze/{source}")
    public String esPostAnalyzeSearch(@PathVariable("source") String source, @RequestBody @Valid EsAnalyzeCondition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.analyzeQuery(condition, source);
    }
}
