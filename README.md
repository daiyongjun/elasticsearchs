# *Elastic-Search[S]*



## *介绍*

*Elastic-Search[S] 是基于开源Elasitc-Search源代码二次迭代开发.完善开源Elastic-Search中的不足.丰富多Elastic-Search实例管理.Elastic-Search[S]内置索引拆分、语法告警、搜索限流、精确搜索、分析强化等功能.*

*Elasitc-Search是基于开源搜索引擎Lucence多实例管理,*Elastic-Search[S]则是基于Elasitc-Searc多实例管理*

## *语法内容*

*Elastic-Search[S]内置了Elastic-Search原生语法内容、Solr语法内容、SQL语法内容.*



## *解决问题*

### *语法统计*

*我们可以解析,整个应用高频使用的语法内容,以及高频使用的聚合语法内容.*

### *语法兼容*

*版本之间语法兼容.es取消语法内容一般会在下一个版本进行通知,如某些参数弃用了,再到再下一个版本该参数就取消,如5.x版本中的"disable_coord":false属性.在6.x版本中会提示deprecated.在7.x版本则直接报错.我们基于定制化可以取消相关内容.*



### *索引拆分*

*我们配置特定的索引拆分规则,如时间、按月按日拆分,我们只需要配置相关拆分规则,Elastic-Search[S]会自动解析请求的内容,按配置的索引规则自动配置相应拆分后的索引内容.*

#### *智能化搜索*

*在DSL语句中我们可以逻辑查询的数量分为单逻辑(QueryBuilder),和多逻辑(BoolQueryBuilder).*

```
GET /web_202207/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "news_posttime": {
              "from": "2022-07-20 00:00:00",
              "to": "2022-07-21 00:00:00"
            }
          }
        },
        {
          "range": {
            "news_posttime": {
              "from": "2022-07-20 00:00:00",
              "to": "2022-07-23 00:00:00"
            }
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "news_posttime": {
              "from": "2022-07-23 00:00:00",
              "to": "2022-07-24 00:00:00"
            }
          }
        }
      ]
    }
  }
}
```
*比如:搜索内容是内置逻辑与(must)和逻辑或(must_not),实际起到作用只有must最小范围的搜索内容.实际搜索2022-07-20 00:00:00-2022-07-21 00:00:00.*

##### *单逻辑(QueryBuilder)*

*指定字段,提取range的field提取stamp,并设置SearchBuild设置stamp.*

```
queryBuilder = AbstractQueryBuilder.parseInnerQueryBuilder(parser, context);
this.startStamp = queryBuilder.startStamp();
this.endStamp = queryBuilder.endStamp();
```

##### *多逻辑(BoolQueryBuilder)*

*多逻辑分为逻辑与 (must/filter),逻辑或 (should/must_not).则多逻辑情况可以分为三种情况,仅为逻辑与,仅为逻辑或,含有逻辑与和含有逻辑或.当多逻辑仅有逻辑与,stamp取最小值,startStamps取最大值,endStamps取最小值.当多逻辑仅为逻辑或则stamp取最大值,startStamps取最小值,endStamps取最大值.当多逻辑含逻辑与和逻辑或.stamp取最小值.*

```
//设置stamps
List<Long> andStartStamps = new ArrayList<>();
List<Long> andEndStamps = new ArrayList<>();
List<Long> orStartStamps = new ArrayList<>();
List<Long> orEndStamps = new ArrayList<>();
List<Long> startStamps = new ArrayList<>();
List<Long> endStamps = new ArrayList<>();
//设置andStartStamps、andEndStamps、orStartStamps、orEndStamps......
//and逻辑取最小值
if (andStartStamps.size() > 0) {
    andStartStamps.sort((p1, p2) -> (int) (p2 - p1));
    andEndStamps.sort((p1, p2) -> (int) (p1 - p2));
    startStamps.addAll(andStartStamps);
    endStamps.addAll(andEndStamps);
//or逻辑取最大值
} else if (orStartStamps.size() > 0) {
    orStartStamps.sort((p1, p2) -> (int) (p1 - p2));
    orEndStamps.sort((p1, p2) -> (int) (p2 - p1));
    startStamps.addAll(orStartStamps);
    endStamps.addAll(orEndStamps);
}
boolQuery.startStamp(startStamps.get(0));
boolQuery.endStamp(endStamps.get(0));
```

*注意: should并非所有情况情况都是逻辑与，当minimumShouldMatch= shouldClauses.size()的时候为must*
```
if (minimumShouldMatch !=  null && (Integer.parseInt(minimumShouldMatch) == shouldClauses.size())) {
	for (QueryBuilder queryBuilder : shouldClauses) {
		andEndStamps.add(queryBuilder.startStamp());
		andEndStamps.add(queryBuilder.endStamp());
		boolQuery.should(queryBuilder);
	}
} else {
	for (QueryBuilder queryBuilder : shouldClauses) {
		orStartStamps.add(queryBuilder.startStamp());
		orEndStamps.add(queryBuilder.endStamp());
		boolQuery.should(queryBuilder);
	}
}
```

### *更智能的告警内容*

*我们将内置语法分析权限管理,方便基于接入的用户,进行多维度的告警内容,以及内置语法算法复杂度分析.让超级复杂的语法在搜索前直接不允许访问.*

### **更精确的搜索**

*使用指定分词器,经常存在数据无法召回,如果设置成每个字为一个词,又会增加倒排索引的存储空间,拉低搜索效率,为了结果这个问题,我们内置了同义词管理,使检索A的同时将A无法检索的到B内容一并返回.*

### **更好的分析能力**

*虽然Elastc-Search的分析条件往往基于关键词+维度的聚合,但是对于热词的聚合我们可以进行预聚合,我们只需要使用之前相同的语法内容,得到更快速的分析内容.*

*我们经常会获取热门词汇在各个平台的提及数*

```
select count(*) from 平台 where range(data) and key = A;
```

*如果我们基于平台和关键词每天生成一个聚合数据,聚合数据的结构为:*

| 关键词  | 提及数    | 日期        |
| ------- | --------- | ----------- |
| key : A | 提及: num | data: 日期2 |
| key : B | 提及: num | data: 日期1 |
| key : C | 提及: num | data: 日期1 |
| key : A | 提及: num | data: 日期1 |

*我们可以基于两个query,先从聚合数据中获取数据,如果聚合数据中无数据,再走现在的逻辑.*

*步骤一、获取聚合数据*

```
select sum(num) from top where range(data) and key = A ;
```

*步骤二、若数据无,则继续操作获取原始数据*

```
select count(*) from 平台 where range(data) and key = A;
```

*待补充中.......*

# 目前遇到的问题?
## Es如何汇总数据的?

## Es如何跨集群搜索的?

## 我该如何设计协调节点?

## 增加,索引,路由,聚合,哪些聚合的分析