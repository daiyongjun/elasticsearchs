package cn.gsdata.elasticsearchs.open.sql.core.expression.env;

/**
 * 定义环境接口，该接口可以基于预定方法(resolve)输入参数类型(E)，返回参数类型(V)
 *
 * @author daiyongjun
 */
public interface Environment<E, V> {

    /**
     * Environment<E, V> 核心方法(指定逻辑)，基于预定的E类型返回V类型
     *
     * @param var E
     * @return V
     */
    V resolve(E var);
}
