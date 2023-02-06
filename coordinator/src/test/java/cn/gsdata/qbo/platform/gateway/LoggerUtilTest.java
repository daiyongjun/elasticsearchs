package cn.gsdata.qbo.platform.gateway;

import cn.gsdata.qbo.platform.gateway.utils.LoggerUtil;
import org.junit.jupiter.api.Test;

public class LoggerUtilTest {
    private String[] responses = {
            "{\"took\":20,\"timed_out\":false,\"_shards\":{\"total\":5,\"successful\":5,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":0,\"max_score\":null,\"hits\":[]}}",
            "{\"took\":75,\"timed_out\":false,\"_shards\":{\"total\":60,\"successful\":60,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":0,\"max_score\":null,\"hits\":[]}}",
            "{\"responseHeader\":{\"zkConnected\":true,\"status\":0,\"QTime\":755,\"params\":{\"q\":\"*:*\",\"tolerant\":\"true\",\"json.nl\":\"map\",\"shards.tolerant\":\"true\",\"fq\":\"posttime:[\\\"2018-09-09 00:00:00\\\" TO \\\"2019-09-09 23:59:59\\\"] AND wx_name:\\\"gh_db2f636971be\\\"\",\"sort\":\"posttime desc\",\"collection\":\"wx_documents_2018_09,wx_documents_2018_10,wx_documents_2018_11,wx_documents_2018_12,wx_documents_2019_01,wx_documents_2019_02,wx_documents_2019_03,wx_documents_2019_04,wx_documents_2019_05,wx_documents_2019_06,wx_documents_2019_07,wx_documents_2019_08,wx_documents_2019_09\",\"wt\":\"json\"}},\"response\":{\"numFound\":468,\"start\":0,\"docs\":[{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":0,\"read_num_3\":0,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":0,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":56,\"real_read_num_2\":0,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":56,\"push_date\":\"20190910\",\"readnum_pm\":1,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv15hxgK0cm4PgQtfrnicdDGEicus38m0v6gmg9js9ZG4X1tJML6hxYzwHlWy8Dj8DdRBQmQNNRXXKT6w/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"0\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"0\",\"status_3\":\"0\",\"posttime\":\"2019-09-09 16:32:40\",\"posttime_date\":\"20190909\",\"post_month\":\"2019-09\",\"group_week\":1,\"group_hour\":16,\"title\":\"庆祝人民政协成立70周年，汪洋主席强调要这么干！\",\"content\":\"\",\"top\":1,\"readnum\":43,\"sn\":\"bbbcd49178ca507cf8c7ece83556f878\",\"types\":\"政务\",\"readnum_newest\":56,\"realreadnum_week\":0,\"likenum\":1,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490400&idx=1&sn=bbbcd49178ca507cf8c7ece83556f878&scene=0\",\"like_num_2\":0,\"like_num_1\":1,\"readnum_week\":0,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":0,\"like_num_6\":0,\"likenum_pm\":0,\"like_num_5\":0,\"realreadnum\":43,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":0,\"likenum_newest\":1,\"add_time\":\"2019-09-09 18:28:53\",\"solr_create_time\":\"2019-09-10 05:17:16\",\"copyright\":\"无\",\"get_time_newest\":\"2019-09-10 04:45:38\",\"get_time\":\"2019-09-09 18:35:53\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/ODqFFf11eI8WeQwWkJ86IUSJTDSIqqAFRPZZicdXW4vwypIeMAJJ9ib2bwlLLHhBZctWpTzIZFFPULSe8tqEz4ZQ/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/ODqFFf11eI8WeQwWkJ86IUSJTDSIqqAF1SeBefYib1dMTxxI6icQ3kH6tXchPYFhM8omiaNOH0CrMVpgibOvHq3S4A/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/ODqFFf11eI8WeQwWkJ86IUSJTDSIqqAFcZBSy6CnwaHBRsj4pdic2jK7ebeSiaseretkJUicnZTZ0Tm5ibOcmlicic7g/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_1\":\"1568061938000\",\"_version_\":1644234104957304832},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":0,\"read_num_3\":0,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":0,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":10,\"real_read_num_2\":0,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":10,\"push_date\":\"20190910\",\"readnum_pm\":1,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv15hxgK0cm4PgQtfrnicdDGEicibAu5UZBkHHAceicb8pOXGPMkwpxzo72iamnpI36ag4y7acEkIW7kO2wA/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"0\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"0\",\"status_3\":\"0\",\"posttime\":\"2019-09-09 16:32:40\",\"posttime_date\":\"20190909\",\"post_month\":\"2019-09\",\"group_week\":1,\"group_hour\":16,\"title\":\"党史｜毛泽东的四大崇高品德：人民性 求是性 包容性 坚韧性\",\"content\":\"周恩来逝世后，郭沫若在《念奴娇·怀念周总理》词中写道：“光明磊落，与导师，协力、同心、共命。”这是描绘毛泽东\",\"top\":2,\"readnum\":7,\"sn\":\"d301d8759125dc06c8ee4c7b2510f5ee\",\"types\":\"政务\",\"readnum_newest\":10,\"realreadnum_week\":0,\"likenum\":1,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490400&idx=2&sn=d301d8759125dc06c8ee4c7b2510f5ee&scene=0\",\"like_num_2\":0,\"like_num_1\":1,\"readnum_week\":0,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":0,\"like_num_6\":0,\"likenum_pm\":0,\"like_num_5\":0,\"realreadnum\":7,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":0,\"likenum_newest\":1,\"add_time\":\"2019-09-09 18:28:54\",\"solr_create_time\":\"2019-09-10 05:17:52\",\"copyright\":\"无\",\"get_time_newest\":\"2019-09-10 04:45:50\",\"get_time\":\"2019-09-09 18:35:41\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_1\":\"1568061950000\",\"_version_\":1644234143531270144},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":37,\"read_num_3\":37,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":37,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":22,\"real_read_num_2\":37,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":22,\"push_date\":\"20190909\",\"readnum_pm\":22,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymeoWsd7FHCkv2XmXZrqS8b7k0n5xsbCCRs1XN4dJByfoE0Y21WViad8w/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"101\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-06 22:41:57\",\"posttime_date\":\"20190906\",\"post_month\":\"2019-09\",\"group_week\":5,\"group_hour\":22,\"title\":\"资讯｜金华市政协来盐考察\",\"content\":\"19月4日至5日，浙江省金华市政协党组副书记、副主席傅利常一行来盐考察我市实施长三角一体化国家战略的经验和做\",\"top\":3,\"readnum\":1,\"sn\":\"3da32b654a611de6a327e43e709ff58f\",\"types\":\"政务\",\"readnum_newest\":37,\"realreadnum_week\":37,\"likenum\":0,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490394&idx=3&sn=3da32b654a611de6a327e43e709ff58f&scene=0\",\"like_num_2\":1,\"like_num_1\":1,\"readnum_week\":37,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":1,\"like_num_6\":0,\"likenum_pm\":1,\"like_num_5\":0,\"realreadnum\":1,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":1,\"likenum_newest\":1,\"add_time\":\"2019-09-06 22:54:27\",\"solr_create_time\":\"2019-09-09 11:13:42\",\"copyright\":\"无\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_gif/Ljib4So7yuWiaSuK63azsxdkr4BKjZsPrWTn76LK3Le7rjEwrRXt1w6Q6UXvLCHgQialvxlpLV5djIl7ozLwnmsuQ/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_gif/Ljib4So7yuWiaSuK63azsxdkr4BKjZsPrWTn76LK3Le7rjEwrRXt1w6Q6UXvLCHgQialvxlpLV5djIl7ozLwnmsuQ/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymlhibNROjEIcribl1pibiaGabUBEqPD6ec6gsHVNe9ZCcECwatlxV48eYxA/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_newest\":\"2019-09-09 10:32:06\",\"get_time_1\":\"1567794440000\",\"get_time_2\":\"1567891623000\",\"get_time_pm\":\"2019-09-07 02:27:20\",\"get_time_week\":\"2019-09-08 05:27:03\",\"_version_\":1644165933408714752},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":13,\"read_num_3\":13,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":13,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":10,\"real_read_num_2\":13,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":10,\"push_date\":\"20190909\",\"readnum_pm\":10,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymJtD9yrrK51kgibfvo2ibAxKDJ94fbViciaibMMmsyKicQsBRK5mnN2wvDDPQ/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"101\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-06 22:41:57\",\"posttime_date\":\"20190906\",\"post_month\":\"2019-09\",\"group_week\":5,\"group_hour\":22,\"title\":\"智库建设｜周劲：坚持“四向四调” 驱动智库创新\",\"content\":\"党的十八大以来，中央对新型智库建设愈加重视与支持，从2015年中办国办推出《关于加强中国特色新型智库建设的意\",\"top\":4,\"readnum\":1,\"sn\":\"c6ba6bc01d80b0cb800f76b799e88434\",\"types\":\"政务\",\"readnum_newest\":13,\"realreadnum_week\":13,\"likenum\":0,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490394&idx=4&sn=c6ba6bc01d80b0cb800f76b799e88434&scene=0\",\"like_num_2\":1,\"like_num_1\":1,\"readnum_week\":13,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":1,\"like_num_6\":0,\"likenum_pm\":1,\"like_num_5\":0,\"realreadnum\":1,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":1,\"likenum_newest\":1,\"add_time\":\"2019-09-06 22:54:28\",\"solr_create_time\":\"2019-09-09 11:14:01\",\"copyright\":\"无\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_newest\":\"2019-09-09 10:32:06\",\"get_time_1\":\"1567794440000\",\"get_time_2\":\"1567891613000\",\"get_time_pm\":\"2019-09-07 02:27:20\",\"get_time_week\":\"2019-09-08 05:26:53\",\"_version_\":1644165953460633600},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":62,\"read_num_3\":63,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":63,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":37,\"real_read_num_2\":62,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":37,\"push_date\":\"20190909\",\"readnum_pm\":37,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymoB7Bicgg2PIgViceX6mBrrmGmGvicI7OhmshaSGrGYwzY8QuKy542zCZQ/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"101\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-06 22:41:57\",\"posttime_date\":\"20190906\",\"post_month\":\"2019-09\",\"group_week\":5,\"group_hour\":22,\"title\":\"资讯｜市政协委员视察市属国企转型工作\",\"content\":\"19月5日下午，市政协经济科技科协界委员到盐城国投集团开展“委员之家”活动，视察市属国有企业转型发展工作。市\",\"top\":2,\"readnum\":1,\"sn\":\"e8038f64f62db8aaf9808819457d0c28\",\"types\":\"政务\",\"readnum_newest\":63,\"realreadnum_week\":62,\"likenum\":0,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490394&idx=2&sn=e8038f64f62db8aaf9808819457d0c28&scene=0\",\"like_num_2\":2,\"like_num_1\":2,\"readnum_week\":62,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":2,\"like_num_6\":0,\"likenum_pm\":2,\"like_num_5\":0,\"realreadnum\":1,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":2,\"likenum_newest\":2,\"add_time\":\"2019-09-06 22:54:28\",\"solr_create_time\":\"2019-09-09 11:13:15\",\"copyright\":\"无\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFym0hzuEu8ZgWPyzqLa8NW8Ctic6TCtR6PhjSsWgZsZ1GIt2EOpjjU7pjw/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_newest\":\"2019-09-09 10:32:08\",\"get_time_1\":\"1567794441000\",\"get_time_2\":\"1567891613000\",\"get_time_pm\":\"2019-09-07 02:27:21\",\"get_time_week\":\"2019-09-08 05:26:53\",\"_version_\":1644165904583360512},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":0,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":125,\"read_num_3\":149,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":149,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":0,\"real_read_num_1\":68,\"real_read_num_2\":125,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":68,\"push_date\":\"20190909\",\"readnum_pm\":68,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymY2qYZMZKkGaFgrUaTQ2HyADoezibR098YhibnUokEMiaKwicWcmb8motyg/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"101\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"0\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-06 22:41:57\",\"posttime_date\":\"20190906\",\"post_month\":\"2019-09\",\"group_week\":5,\"group_hour\":22,\"title\":\"资讯｜省政协来盐调研改善苏北农村住房工作 黄莉新参加\",\"content\":\"长按二维码识别关注我们\",\"top\":1,\"readnum\":1,\"sn\":\"17f2e3b1ba73976f5adbc568cbcd1b79\",\"types\":\"政务\",\"readnum_newest\":149,\"realreadnum_week\":125,\"likenum\":0,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490394&idx=1&sn=17f2e3b1ba73976f5adbc568cbcd1b79&scene=0\",\"like_num_2\":7,\"like_num_1\":3,\"readnum_week\":125,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":7,\"like_num_6\":0,\"likenum_pm\":3,\"like_num_5\":0,\"realreadnum\":1,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":7,\"likenum_newest\":7,\"add_time\":\"2019-09-06 22:54:29\",\"solr_create_time\":\"2019-09-09 11:13:42\",\"copyright\":\"无\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymHsJG1poovL9QG9bh1BZhWwbKKkA6RGur98cfTpLKRVkibDCQTNHwz4A/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFym4w0ZnQb7gj6rxAk9rgkA9TPdKiaftu8oicK9LE6awy9EI98ayjGCHU5A/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymtIx7s3sqPmPaEl4FA0wn7sBFHpS2ZqDAjE4uxmNPnlEyamFocGnzQw/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymV77jHxypYzCNHicJdCqaM3mO1lCVTWiat1PbtgVQXdw1PEnWKWtibic4kg/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFym6C66J6nlDBibzvbrAFKbgfncNZP7lClQEnBPDcxQ217ibgkpKBaiar0hA/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymc7IIBM9b6SDhLCvxaG1VYFYDWhBAfUEcykVfgAJQYPTdN6WGianXViag/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymk2XDfE84jbOIfmRicicuyqbs445zkQkNiarn9XZib3jWOpRNZ0hMxEc4kg/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymvQsm4wGkennytR4woI0GnKTrH6SvGiaU93Caia8iba5lSrEO9ovEMRGMQ/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymIuFvPyibCPOl3pCiaeZ00W9XS5lmTsFByZdrribKBcV0EicczBlf0yomow/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFym1jt2HTaNq6l0ypnVVJHFuHeMibfsD88SPlxdZoQv66GygSx6Rlx9qOg/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymichKMiaVV9VsWIAs2wcHzgf8bYoMf5hWjStLLhyxZ4eo8XbLvtZecPuA/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17KEbVLpzHjibicepcLXYaFymeSvmjuBAvDcsflJWhLricnt8nTA0Z9xmNIgSs6WTGRCtQ9LUrNwQxiag/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_newest\":\"2019-09-09 10:32:07\",\"get_time_1\":\"1567794441000\",\"get_time_2\":\"1567891616000\",\"get_time_pm\":\"2019-09-07 02:27:21\",\"get_time_week\":\"2019-09-08 05:26:56\",\"_version_\":1644165933476872192},{\"copyright\":\"无\",\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":23,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":22,\"read_num_3\":22,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":22,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":23,\"real_read_num_1\":19,\"real_read_num_2\":22,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":19,\"push_date\":\"20190909\",\"readnum_pm\":19,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17efwmS5DEDmHDDsTk6wkbBicsVzPPxAnPtlZWiaWwuc7HBiaoxrkoeS0sccxiau7ojcLMleJk6ibnyp5Q/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"1\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"1\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-05 19:40:40\",\"posttime_date\":\"20190905\",\"post_month\":\"2019-09\",\"group_week\":4,\"group_hour\":19,\"title\":\"汪洋强调：围绕凝聚共识进行积极探索\",\"content\":\"全国政协重大专项工作委员宣讲活动3日在湖北武汉举行，中共中央政治局常委、全国政协主席汪洋出席并讲话。他强调，\",\"top\":1,\"readnum\":15,\"sn\":\"ee91434da5dafe881cabcbf5cd4161fe\",\"types\":\"政务\",\"readnum_newest\":23,\"realreadnum_week\":22,\"likenum\":1,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490366&idx=1&sn=ee91434da5dafe881cabcbf5cd4161fe&scene=0\",\"like_num_2\":1,\"like_num_1\":1,\"readnum_week\":22,\"ispush\":1,\"like_num_4\":1,\"like_num_3\":1,\"like_num_6\":0,\"likenum_pm\":1,\"like_num_5\":0,\"realreadnum\":15,\"wx_district\":\"\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/dClwJRBjL66MMb4R87aDVgzo17XHpLyCdwotMmcjArciaZeahmB5rXqg9oNvmYtmVrX0NG92xPk30FHsN5bfiaqA/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_jpg/dClwJRBjL66MMb4R87aDVgzo17XHpLyCb28J2ibvicAj9VeHB99iaiaNNEjUsJrKkhhibOUoBOFaXlHhN1me0rn5B6Q/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":1,\"likenum_newest\":1,\"add_time\":\"2019-09-05 20:01:31\",\"solr_create_time\":\"2019-09-09 14:25:36\",\"get_time_newest\":\"2019-09-09 14:04:42\",\"get_time\":\"2019-09-05 21:45:22\",\"get_time_1\":\"1567705372000\",\"get_time_2\":\"1567805181000\",\"get_time_pm\":\"2019-09-06 01:42:52\",\"get_time_week\":\"2019-09-08 06:56:14\",\"get_time_4\":\"1568009082000\",\"_version_\":1644178006934028288},{\"copyright\":\"无\",\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":9,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":7,\"read_num_3\":8,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":8,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":9,\"real_read_num_1\":7,\"real_read_num_2\":7,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":7,\"push_date\":\"20190909\",\"readnum_pm\":7,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17efwmS5DEDmHDDsTk6wkbBbdicKKHh0TJsR9m4ovRLxZgjgnA2Y3MCibAicEUQxUA9ed7lMpq5lqmyg/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"1\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"1\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-05 19:40:40\",\"posttime_date\":\"20190905\",\"post_month\":\"2019-09\",\"group_week\":4,\"group_hour\":19,\"title\":\"荐读｜“中国之治”的政治保证\",\"content\":\"坚定不移地走中国特色社会主义政治发展道路。\",\"top\":3,\"readnum\":3,\"sn\":\"576a3d60cacbb44ccaaa3f8297bfdd8a\",\"types\":\"政务\",\"readnum_newest\":9,\"realreadnum_week\":8,\"likenum\":1,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490366&idx=3&sn=576a3d60cacbb44ccaaa3f8297bfdd8a&scene=0\",\"like_num_2\":1,\"like_num_1\":1,\"readnum_week\":8,\"ispush\":1,\"like_num_4\":1,\"like_num_3\":1,\"like_num_6\":0,\"likenum_pm\":1,\"like_num_5\":0,\"realreadnum\":3,\"wx_district\":\"\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":1,\"likenum_newest\":1,\"add_time\":\"2019-09-05 20:01:32\",\"solr_create_time\":\"2019-09-09 14:26:15\",\"get_time_newest\":\"2019-09-09 14:04:39\",\"get_time\":\"2019-09-05 21:45:21\",\"get_time_1\":\"1567705371000\",\"get_time_2\":\"1567805186000\",\"get_time_pm\":\"2019-09-06 01:42:51\",\"get_time_week\":\"2019-09-08 06:56:13\",\"get_time_4\":\"1568009079000\",\"_version_\":1644178047592562688},{\"copyright\":\"无\",\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":49,\"realreadnum_pm\":1,\"read_num_5\":0,\"read_num_2\":43,\"read_num_3\":46,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":46,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":49,\"real_read_num_1\":27,\"real_read_num_2\":43,\"real_read_num_7\":0,\"real_read_num_5\":0,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":27,\"push_date\":\"20190909\",\"readnum_pm\":27,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17efwmS5DEDmHDDsTk6wkbBZw6OVEY87ppO2RPBt5LZS4rvHs7beibuxaptbXk17Xgg0T0dWXBNDrg/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"0\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"1\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"1\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-05 19:40:40\",\"posttime_date\":\"20190905\",\"post_month\":\"2019-09\",\"group_week\":4,\"group_hour\":19,\"title\":\"党派｜九三学社盐城市委举办庆祝新中国成立70周年红歌合唱活动\",\"content\":\"为庆祝新中国成立70周年，大力弘扬爱国奉献精神，积极传承优良传统，9月3日，九三学社盐城市委举办红歌合唱活动\",\"top\":2,\"readnum\":19,\"sn\":\"a2363fd1ed6559654be06bb102b9f885\",\"types\":\"政务\",\"readnum_newest\":49,\"realreadnum_week\":46,\"likenum\":1,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490366&idx=2&sn=a2363fd1ed6559654be06bb102b9f885&scene=0\",\"like_num_2\":4,\"like_num_1\":3,\"readnum_week\":46,\"ispush\":1,\"like_num_4\":4,\"like_num_3\":4,\"like_num_6\":0,\"likenum_pm\":3,\"like_num_5\":0,\"realreadnum\":19,\"wx_district\":\"\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv17efwmS5DEDmHDDsTk6wkbBLtgVIEUsZlcFjuPib9jOE4obzibXnujpqJ1aOjLoxgT0YqFhLRvwqZHA/640?wx_fmt=jpeg;\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":4,\"likenum_newest\":4,\"add_time\":\"2019-09-05 20:01:32\",\"solr_create_time\":\"2019-09-09 14:23:20\",\"get_time_newest\":\"2019-09-09 14:04:40\",\"get_time\":\"2019-09-05 21:45:22\",\"get_time_1\":\"1567705375000\",\"get_time_2\":\"1567805181000\",\"get_time_pm\":\"2019-09-06 01:42:55\",\"get_time_week\":\"2019-09-08 06:56:14\",\"get_time_4\":\"1568009080000\",\"_version_\":1644177864439889920},{\"read_num_6\":0,\"read_num_7\":0,\"read_num_4\":42,\"realreadnum_pm\":1,\"read_num_5\":43,\"read_num_2\":41,\"read_num_3\":42,\"wx_province\":\"江苏省\",\"type\":\"49\",\"real_read_num_3\":42,\"wx_name\":\"gh_db2f636971be\",\"real_read_num_4\":42,\"real_read_num_1\":27,\"real_read_num_2\":41,\"real_read_num_7\":0,\"real_read_num_5\":43,\"nickname_id\":10690891,\"real_read_num_6\":0,\"read_num_1\":27,\"push_date\":\"20190909\",\"readnum_pm\":27,\"author\":\"\",\"can_reward\":0,\"picurl\":\"http://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv174G0a2bWkialibp2wZgvFRZ4ZPe2pXrYWnkHEzWcU3KljULVdibyDWS1zMsuyYCSiaU9ujs5odo2iaNtQ/0?wx_fmt=jpeg\",\"sourceurl\":\"\",\"status_6\":\"0\",\"status_5\":\"1\",\"status_7\":\"0\",\"name\":\"盐城政协\",\"reward_total_count\":100001,\"status_2\":\"101\",\"comment_enabled\":\"0\",\"status_1\":101,\"wx_city\":\"盐城市\",\"status_4\":\"101\",\"status\":\"1\",\"status_3\":\"101\",\"posttime\":\"2019-09-04 20:38:12\",\"posttime_date\":\"20190904\",\"post_month\":\"2019-09\",\"group_week\":3,\"group_hour\":20,\"title\":\"党派｜民盟上海市青浦区委与民盟盐城市委缔结友好合作协议\",\"content\":\"8月26日，为了深入贯彻习近平总书记关于推动长三角更高质量一体化发展的重要指示精神，发挥民主党派智力密集优势\",\"top\":2,\"readnum\":23,\"sn\":\"01d1f31ad10c393f89762a432c922fd3\",\"types\":\"政务\",\"readnum_newest\":43,\"realreadnum_week\":42,\"likenum\":0,\"url\":\"http://mp.weixin.qq.com/s?__biz=MzU5NjI0NzYxMw==&mid=2247490362&idx=2&sn=01d1f31ad10c393f89762a432c922fd3&scene=0\",\"like_num_2\":0,\"like_num_1\":0,\"readnum_week\":42,\"ispush\":1,\"like_num_4\":0,\"like_num_3\":0,\"like_num_6\":0,\"likenum_pm\":0,\"like_num_5\":0,\"realreadnum\":23,\"wx_district\":\"\",\"like_num_7\":0,\"status_con\":\"1\",\"likenum_week\":0,\"likenum_newest\":0,\"add_time\":\"2019-09-04 20:59:15\",\"solr_create_time\":\"2019-09-09 17:18:54\",\"copyright\":\"无\",\"get_time_newest\":\"2019-09-09 16:46:47\",\"get_time\":\"2019-09-04 22:40:04\",\"img_urls\":\"https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv174G0a2bWkialibp2wZgvFRZ4r7r6Evs71JxzrWnzVCBmrR4qktcKzWxcelH3M22BeCyO9kDfEJeh7A/640?wx_fmt=jpeg;https://mmbiz.qpic.cn/mmbiz_gif/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RKyGpJH0eKGRAkdGzSZ3HlLKxSAvSUuiaF4WPfRMKVy7BObzicVq4waJA/640?wx_fmt=gif;https://mmbiz.qpic.cn/mmbiz_jpg/w3q8MwSUv14gibdCdZLBicTB3A0r9jrU8RBSDapwyD6ejsGYKicuDV1z5RrcaYjxCia3I9eobhh6OiaaxRFia7VZYjKA/640?wx_fmt=jpeg;\",\"get_time_1\":\"1567622781000\",\"get_time_2\":\"1567716130000\",\"get_time_pm\":\"2019-09-05 02:46:21\",\"get_time_4\":\"1567907806000\",\"get_time_week\":\"2019-09-08 09:56:46\",\"get_time_5\":\"1568018807000\",\"_version_\":1644188909760937984}]}}",
            "{\"pageIndex\":1,\"pageSize\":10,\"data\":[{\"platformId\":30078,\"platform\":\"web\",\"platformName\":\"中国地质大学(北京)\",\"platformDomainSec\":\"cugb.edu.cn\",\"platformDomainPri\":\"cugb.edu.cn\",\"platformProvince\":\"北京市\",\"platformCounty\":\"\",\"platformCity\":\"北京市\",\"platformRankGlobal\":52504,\"platformRankLocal\":999999999,\"platformIsOversea\":\"0\",\"platformCountry\":\"中国\",\"platformPicurl\":\"\",\"domainSecPlatform\":\"web_cugb.edu.cn\",\"addTime\":\"2019-07-2523:39:05\",\"updateTime\":\"2019-08-2300:08:55\"}],\"totalCount\":1}",
            "{\"_scroll_id\":\"DnF1ZXJ5VGhlbkZldGNoCgAAAAADaXWsFnMtajZmQ19aUjUyNnlwVnpwQUZnMGcAAAAAA2l1qhZzLWo2ZkNfWlI1MjZ5cFZ6cEFGZzBnAAAAAAMURowWSUF3N3doX3BURWVLTUtSb3liUGEtZwAAAAADaXWrFnMtajZmQ19aUjUyNnlwVnpwQUZnMGcAAAAAA18XDhZQMXlKNWhsTlNNcUFORm9JSHloWDNRAAAAAANpdakWcy1qNmZDX1pSNTI2eXBWenBBRmcwZwAAAAADFEaOFklBdzd3aF9wVEVlS01LUm95YlBhLWcAAAAAAxRGjRZJQXc3d2hfcFRFZUtNS1JveWJQYS1nAAAAAAMURo8WSUF3N3doX3BURWVLTUtSb3liUGEtZwAAAAADXxcPFlAxeUo1aGxOU01xQU5Gb0lIeWhYM1E=\",\"took\":158,\"timed_out\":false,\"_shards\":{\"total\":10,\"successful\":10,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":1,\"max_score\":null,\"hits\":[{\"_index\":\"forum_201909\",\"_type\":\"documents\",\"_id\":\"9e9d9dbf1dd3b1e8f5bd6cb0419bc533\",\"_score\":null,\"_source\":{\"news_postdate\":\"2019-09-09\",\"news_channel2\":\"\",\"news_channel1\":\"\",\"origin_author_uid\":\"\",\"news_read_count\":0,\"scheme_type_id\":0,\"wx_name\":\"\",\"news_weight\":0,\"hash_code\":\"\",\"media_type\":\"\",\"scheme_id\":0,\"news_pub_device\":\"\",\"mblog_uid\":\"0\",\"mblog_influence\":0,\"icp_code\":\"\",\"news_posttime\":\"2019-09-0910:44:24\",\"alert_keywords_list\":\"\",\"mblog_bci\":0,\"origin_news_like_count\":0,\"news_province\":\"未知\",\"news_url\":\"https://zhuanlan.zhihu.com/p/81764531\",\"origin_news_reposts_count\":0,\"scheme_category\":\"\",\"news_match_position\":\"0\",\"news_country\":\"\",\"alert_keywords\":\"\",\"user_id\":0,\"content_location\":\"广东省深圳市龙岗区|浙江省绍兴市越城区|北京市北京市\",\"is_recommend\":0,\"scheme_news_match\":0,\"status\":1,\"content_city_tier\":[\"0\",\"2\"],\"news_positive\":0.26,\"origin_picurl\":\"\",\"solr_create_time\":\"2019-09-0915:29:24\",\"news_like_count\":0,\"news_comment_count\":0,\"domain_pri\":\"zhihu.com\",\"news_posthour\":\"2019090910\",\"scheme_id_str\":\"\",\"mblog_id\":\"\",\"mblog_statues_count\":0,\"news_uuid\":\"9e9d9dbf1dd3b1e8f5bd6cb0419bc533\",\"content_province\":[\"广东省\",\"浙江省\",\"北京市\"],\"is_sensitive\":0,\"app_name_search\":\"知乎\",\"video_from\":\"知乎\",\"origin_news_pub_device\":\"\",\"news_propgation_level\":1,\"wx_wci\":0,\"enterprise_id\":0,\"news_reporter\":\"\",\"wx_biz\":\"\",\"app_name\":\"知乎\",\"news_is_origin\":0,\"sim_hash\":\"c2d8300b453300561a0de49689d6e8f6\",\"from_oversea\":\"0\",\"rank_local\":\"\",\"negative_keywords\":\"潜入,原始,更加难以,更加难以保护,陷阱,黑手,毫不,不奏效,违规,感染,骗局,严重威胁,怀疑,被盗,非法行为,勾结,丢,牟利,非法,不明,下手,恶意程序,攻击,违章,毁损,不堪,诱饵,黑幕,强制,损害,窃取,泄漏,诈骗案,手段,私自,丢失,风险,伪装,涉案,恶意,犯罪团伙,诈骗,被害人,所谓,造成,漏洞,泄露,故意,危害,罪,威胁,涉嫌,不法分子,被告,侦查,难以,无处不在,无法,再次利用\",\"mblog_verified_type\":\"未知\",\"origin_mblog_mid\":\"\",\"wx_verified_type\":\"未知\",\"collection_from\":\"monitor_zhihu_key\",\"app_uuid\":209999,\"news_fetch_time\":\"2019-09-0915:29:01\",\"news_reposts_count\":0,\"news_media\":\"forum\",\"language\":\"zh-CN\",\"news_local_url\":\"\",\"content_country\":[],\"origin_author_name\":\"\",\"icp_owner\":\"\",\"rank_global\":\"\",\"news_nav\":\"\",\"news_language\":\"未知\",\"news_author\":\"\",\"news_keywords_list\":[\"信息\",\"黑客\",\"用户\",\"手机\",\"网络\",\"个人\",\"平台\",\"短信\",\"数据\",\"团伙\",\"个人信息\",\"密码\",\"恶意\",\"账户\",\"程序\",\"网络安全\",\"链接\",\"精准\",\"客服\",\"运营商\",\"技术\",\"互联网\",\"手段\",\"客户\",\"网店\",\"行为\",\"账号\",\"网站\",\"恶意程序\",\"苹果\"],\"origin_news_comment_count\":0,\"wx_news_position\":0,\"picurl\":\"\",\"mblog_friends_count\":0,\"domain_sec\":\"zhuanlan.zhihu.com\",\"icp_type\":\"\",\"origin_video_urls\":\"\",\"news_title\":\"焦点访谈:你的信息,他的生意;你的数据,他的资源\",\"news_city\":\"未知\",\"author_gender\":\"未知\",\"news_postmonth\":\"201909\",\"news_postweek\":20190915,\"sensitive_keywords\":\"\",\"pending\":1,\"content_cate_list\":\"\",\"news_continent\":\"\",\"has_video\":0,\"origin_news_imgs\":\"\",\"news_keywords\":\"{\\\"账户\\\":6,\\\"账号\\\":4,\\\"用户\\\":18,\\\"手机\\\":17,\\\"手段\\\":4,\\\"个人信息\\\":7,\\\"精准\\\":5,\\\"苹果\\\":4,\\\"恶意\\\":6,\\\"平台\\\":10,\\\"个人\\\":12,\\\"短信\\\":9,\\\"客户\\\":4,\\\"客服\\\":5,\\\"黑客\\\":26,\\\"技术\\\":4,\\\"信息\\\":34,\\\"网络\\\":14,\\\"网站\\\":4,\\\"网店\\\":4,\\\"恶意程序\\\":4,\\\"程序\\\":6,\\\"网络安全\\\":5,\\\"密码\\\":7,\\\"数据\\\":9,\\\"链接\\\":5,\\\"互联网\\\":4,\\\"团伙\\\":7,\\\"运营商\\\":5,\\\"行为\\\":4}\",\"content_cate\":\"科技\",\"news_negative\":0.74,\"news_county\":\"\",\"app_code\":\"guai-gou\",\"news_dislike_count\":0,\"origin_news_posttime\":\"\",\"is_sticky\":0,\"news_editor\":\"\",\"content_district\":[\"龙岗区\",\"越城区\",\"\"],\"news_digest\":\"从个人生活到企业生产，再到城市管理，互联网已经无处不在。截至2019年6月，我国网民规模已达8.54亿。互联网方便了人们的工作生活，但是信息泄露也随之而来，让人不堪其扰。我们的各种账户、各种记录在内的种种信息成为不法分子下手的目标，甚至直接导致精准诈骗。谁是幕后黑手?这里面不乏的影子。\",\"origin_news_url\":\"\",\"extra_data\":\"\",\"mblog_followers_count\":0,\"news_level\":0,\"content_city\":[\"深圳市\",\"绍兴市\",\"北京市\"],\"news_category\":\"\",\"content_continent\":[],\"force_update\":\"0\"},\"sort\":[991083]}]}}",
           "{\"news_postdate\":\"2019-09-09\",\"news_channel2\":\"\",\"news_channel1\":\"财经\",\"origin_author_uid\":\"\",\"news_read_count\":\"0\",\"media_tags\":\"\",\"media_is_verified\":\"0\",\"news_collection_from\":\"mirror_refine_search_news.so.com\",\"news_contain_img\":\"0\",\"wx_name\":\"\",\"news_imgs\":\"\",\"news_origin_reposts_count\":\"0\",\"news_weight\":\"0\",\"hash_code\":\"\",\"media_type\":\"客户端,news\",\"media_picurl\":\"\",\"news_pub_device\":\"\",\"mblog_uid\":\"0\",\"media_id\":\"-1\",\"icp_code\":\"京ICP证000007-12\",\"mblog_influence\":\"0.0\",\"news_posttime\":\"2019-09-09 15:34:18\",\"mblog_bci\":\"0.0\",\"origin_news_like_count\":\"0\",\"platform_county\":\"\",\"platform_province\":\"北京市\",\"news_province\":\"北京市\",\"platform_domain_pri\":\"sina.cn\",\"news_origin_imgs\":\"\",\"news_url\":\"http://finance.sina.cn/stock/relnews/dongmiqa/2019-09-09/detail-iicezueu4534088.d.html\",\"origin_news_reposts_count\":\"0\",\"news_audio_urls\":\"\",\"news_match_position\":\"0\",\"news_country\":\"中国\",\"is_recommend\":\"0\",\"platform_rank_local\":\"999999999\",\"news_is_sticky\":\"0\",\"content_city_tier\":\"\",\"news_contain_audio\":\"0\",\"origin_picurl\":\"\",\"news_contain_video\":\"0\",\"media_field\":\"\",\"news_is_recommend\":\"0\",\"domain_pri\":\"sina.cn\",\"news_comment_count\":\"0\",\"news_like_count\":\"0\",\"news_posthour\":\"2019090915\",\"mblog_id\":\"\",\"scheme_id_str\":\"\",\"mblog_statues_count\":\"0\",\"news_uuid\":\"122642c865c83487142d470d35e54deb\",\"is_sensitive\":\"0\",\"channel_name\":\"\",\"news_img_urls\":\"\",\"media_followers_count\":\"0\",\"origin_news_pub_device\":\"\",\"video_from\":\"\",\"news_propgation_level\":\"1\",\"wx_wci\":\"0.0\",\"news_origin_video\":\"\",\"news_reporter\":\"\",\"platform_is_oversea\":\"0\",\"wx_biz\":\"\",\"app_name\":\"新浪新闻\",\"news_is_origin\":\"0\",\"from_oversea\":\"0\",\"platform_picurl\":\"http://p18.qhimg.com/t0101b846fdb4247b45.png\",\"rank_local\":\"999999999\",\"platform_rank_global\":\"999999999\",\"mblog_verified_type\":\"未知\",\"media_city\":\"\",\"origin_mblog_mid\":\"\",\"platform_domain_sec\":\"finance.sina.cn\",\"wx_verified_type\":\"未知\",\"media_level\":\"\",\"collection_from\":\"mirror_refine_search_news.so.com\",\"media_province\":\"\",\"app_uuid\":\"1\",\"news_fetch_time\":\"2019-09-09 15:34:19\",\"news_reposts_count\":\"0\",\"language\":\"\",\"news_media\":\"app\",\"news_headimg_url\":\"\",\"news_local_url\":\"docs/2019/09/09/15/122642c865c83487142d470d35e54deb.html\",\"media_rank_global\":\"0\",\"news_origin_content\":\"\",\"media_friends_count\":\"0\",\"news_origin\":\"问董秘\",\"media_statues_count\":\"0\",\"origin_author_name\":\"问董秘\",\"icp_owner\":\"北京新浪互联信息服务有限公司\",\"media_gender\":\"\",\"media_name\":\"\",\"news_nav\":\"首页>新浪财经\",\"rank_global\":\"3085\",\"media_verifiedtype\":\"\",\"media_county\":\"\",\"news_language\":\"chinese\",\"news_author\":\"\",\"origin_news_comment_count\":\"0\",\"wx_news_position\":\"0\",\"picurl\":\"\",\"mblog_friends_count\":\"0\",\"news_position\":\"0\",\"domain_sec\":\"finance.sina.cn\",\"icp_type\":\"企业\",\"origin_video_urls\":\"\",\"video_urls\":\"\",\"channel_nav\":\"首页>新浪财经\",\"news_title\":\"投资者提问：美国工厂现在合格率多少？美国工厂为什么会出现较大规模的合格率不...\",\"media_is_oversea\":\"0\",\"news_city\":\"北京市\",\"platform_city\":\"北京市\",\"author_gender\":\"未知\",\"news_postmonth\":\"201909\",\"news_postweek\":\"20190915\",\"media_rank_local\":\"0\",\"media_identity\":\"\",\"news_continent\":\"亚洲\",\"media_continent\":\"\",\"has_video\":\"0\",\"platform\":\"app\",\"news_origin_url\":\"\",\"origin_news_imgs\":\"\",\"media_country\":\"\",\"news_origin_pub_device\":\"\",\"news_origin_comment_count\":\"0\",\"platform_name\":\"新浪财经\",\"news_county\":\"\",\"news_origin_author_name\":\"\",\"app_code\":\"\",\"channel_level\":\"2\",\"news_dislike_count\":\"0\",\"origin_news_posttime\":\"\",\"is_sticky\":\"0\",\"media_discription\":\"\",\"news_editor\":\"\",\"channel_field\":\"\",\"content_district\":\"\",\"media_CI\":\"0\",\"news_origin_author_uid\":\"\",\"platform_country\":\"中国\",\"origin_news_url\":\"\",\"news_origin_posttime\":\"\",\"news_origin_like_count\":\"0\",\"mblog_followers_count\":\"0\",\"platform_id\":\"63136\",\"news_video_urls\":\"\",\"hbase_create_time\":\"2019-09-09 15:34:24\",\"news_level\":\"1\",\"news_category\":\"\",\"force_update\":\"0\"}",
            "{}",
            null
};

    @Test
    void getDataSizeTest(){
        for(String response : responses){
            System.out.println(LoggerUtil.parseRet(response,new String[]{"took","size","xxx"}));
            System.out.println(LoggerUtil.getDataSize(response));
        }
    }

}
