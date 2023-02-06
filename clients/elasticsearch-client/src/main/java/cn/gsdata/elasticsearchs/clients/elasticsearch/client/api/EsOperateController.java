package cn.gsdata.elasticsearchs.clients.elasticsearch.client.api;

import cn.gsdata.elasticsearchs.clients.elasticsearch.client.service.EsQueryService;
import cn.gsdata.elasticsearchs.commons.base.common.model.es.InsertShardingCondition;
import cn.gsdata.elasticsearchs.commons.base.common.model.es.PutShardingCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ES非查询操作接口
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2022/07/8 14:11
 */
@Api(tags = "操作api")
@RestController
@RequestMapping(value = {"/es/operate"}, produces = "application/json;charset=UTF-8")
public class EsOperateController {
    private final EsQueryService queryService;

    public EsOperateController(EsQueryService queryService) {
        this.queryService = queryService;
    }

    @ApiOperation("更新数据")
    @PutMapping({"/sharding/{source}"})
    public String update(@PathVariable("source") String source, @RequestBody @Valid PutShardingCondition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.update(condition, source);
    }

    @ApiOperation("录入数据")
    @PostMapping({"/sharding/{source}"})
    public String insert(@PathVariable("source") String source, @RequestBody @Valid InsertShardingCondition condition, @SuppressWarnings("unused") BindingResult bindingResult) throws Exception {
        return queryService.insert(condition, source);
    }
}
