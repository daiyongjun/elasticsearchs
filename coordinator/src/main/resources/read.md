eureka:
  client:
    serviceUrl:
      defaultZone: http://qingbo:1qaz2wsx@qingbo:1111/eureka/
logging:
  level:
    com:
      ctrip:
        framework:
          apollo:
            use:
              cases:
                spring:
                  boot:
                    logger: info
                  cloud:
                    logger: info
    root: info
management:
  endpoint:
    health:
      show-details: always
    shutdown:
      enabled: true
  endpoints:
    web:
      exposure:
        include: '*'
qbo:
  auth:
    common:
      expiration: 1
      iss: echisan
      remember: 36500
      role-claims: rol
      secret: jwtsecretdemo
      token-header: Authorization
      token-prefix: Bearer
  gateway:
    url-ignore: /auth/login
    url-permission: '{''ROLE_USER'':''/auth/register''}'
    url-prefix: /auth
spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      plugin:
        config:
          exception-json-handler: false
          log-request: true
          read-request-data: true
          read-response-data: true
        grey:
          enable: true
          grey-ribbon-rule: WEIGHT_RESPONSE
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/auth/**
        - id: elastic-search-interface
          uri: lb://elastic-search-interface
          predicates:
            - Path=/es/search/custom,/es/search/msearch,/es/search/native,/es/search/scroll
        - id: hbase-interface
          uri: lb://hbase-interface
          predicates:
            - Path=/hbase/**
        - id: crawler-interface
          predicates:
            - Path=/crawler/**
          uri: lb://crawler-interface
        - id: mg-elastic-search-interface
          uri: lb://mg-elastic-search-interface
          predicates:
            - Path=/mg/**
        - id: qbo-sidebar
          uri: lb://qbo-sidebar
          predicates:
            - Path=/sb/**
        - id: mg-database
          uri: lb://mg-database
          predicates:
            - Path=/mgd/**
        - id: mg-elastic-search-interface
          uri: lb://mg-elastic-search-interface
          predicates:
            - Path=/cg/**
        - id: dynamic-es-one-get
          uri: lb://elastic-search-interface
          predicates:
            - Path=/es/search/aliEs
          filters:
            - StripPrefix=3
            - PrefixPath=/es/search/get/one
        - id: dynamic-es-one-scroll
          uri: lb://elastic-search-interface
          predicates:
            - Path=/es/search/ali/scroll
          filters:
            - StripPrefix=4
            - PrefixPath=/es/search/get/scroll/one
        - id: dynamic-es-two-get
          uri: lb://elastic-search-interface
          predicates:
            - Path=/es/search/zkdyes
          filters:
            - StripPrefix=3
            - PrefixPath=/es/search/get/two
        - id: dynamic-es-two-scroll
          uri: lb://elastic-search-interface
          predicates:
            - Path=/es/search/zkdyes/scroll
          filters:
            - StripPrefix=4
            - PrefixPath=/es/search/get/scroll/two
        - id: solr-interface-wx-acticle
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/limitSearch
          filters:
            - name: CustomRequestRateLimiter
              args:
                key-resolver: '#{@keyResolver}'
                rate-limiter: '#{@systemRedisRateLimiter}'
            - RewritePath=/solr/search/limitSearch, /solr/search/sharding/one
        - id: solr-interface-wx-account
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/wx/account
          filters:
            - AddRequestParameter=collection, wx_official_accounts
            - RewritePath=/solr/search/wx/account, /solr/search/three
        - id: solr-interface-rank-of-day
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/rankOfDay
          filters:
            - AddRequestParameter=collection, rankOfDay
            - RewritePath=/solr/search/rankOfDay, /solr/search/three
        - id: solr-interface-xuanya
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/xuanya
          filters:
            - AddRequestParameter=collection, shunya_news_001
            - RewritePath=/solr/search/xuanya, /solr/search/one
        - id: solr-interface-qbpub
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/qbpub/1
          filters:
            - AddRequestParameter=collection, qbpub_news_v3_470_1
            - RewritePath=/solr/search/qbpub/1, /solr/search/four
        - id: solr-interface
          uri: lb://solr-interface
          predicates:
            - Path=/solr/search/**
ratelimiter-conf:
    rateLimitMap:
        solr-interface-wx-acticle:
            API:
                burstCapacity: 8
                replenishRate: 4
            boxiaotongxiangmu:
                burstCapacity: 5
                replenishRate: 3
            default:
                burstCapacity: 5
                replenishRate: 5
            diaoshishiyong:
                burstCapacity: 1
                replenishRate: 1
            hongmaiweixinfenzuwenzhangtuisong:
                burstCapacity: 2
                replenishRate: 1
            jinanxinmeitizhongxin:
                burstCapacity: 3
                replenishRate: 2
            jingdongqingboguanjia:
                burstCapacity: 8
                replenishRate: 4
            kaipuyunxiangmuzhuanyong:
                burstCapacity: 3
                replenishRate: 1
            langfangguangdianzhongxinrongmeitixitong:
                burstCapacity: 5
                replenishRate: 3
            qingbokaifangpingtai:
                burstCapacity: 10
                replenishRate: 5
            qingbozhishuxiangmu:
                burstCapacity: 20
                replenishRate: 10
            shujuqianyifuwu:
                burstCapacity: 2
                replenishRate: 1
            xunfeiyousheng:
                burstCapacity: 10
                replenishRate: 5
            zhishuyidongban:
                burstCapacity: 15
                replenishRate: 8
            zhongkeyuanwenxianqingbaozhongxin:
                burstCapacity: 3
                replenishRate: 2
            zhongqingdapingxiangmu:
                burstCapacity: 6
                replenishRate: 3
            zijietiaodong:
                burstCapacity: 6
                replenishRate: 3
