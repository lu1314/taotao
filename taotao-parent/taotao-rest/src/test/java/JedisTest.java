import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    //Jedis单机版测试
    @Test
    public void testJedisSingle(){
        Jedis jedis = new Jedis("192.168.1.107",6379);
        jedis.auth("Lulu123!");
        jedis.set("lulu","123");
        String lulu = jedis.get("lulu");
        System.out.println(lulu);
        jedis.close();
    }

    //Jedis单机版连接池测试
    @Test
    public void testJedisPool(){
        JedisPool jedisPool = new JedisPool("192.168.1.107",6379);
        Jedis jedis = jedisPool.getResource();
        jedis.auth("Lulu123!");
        jedis.set("hello","world");
        String str = jedis.get("hello");
        System.out.println(str);
        jedis.close();
        jedisPool.close();
    }

    //jedis集群测试
    @Test
    public void testJedisCluster() throws IOException {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.1.107", 7001));
        nodes.add(new HostAndPort("192.168.1.107", 7002));
        nodes.add(new HostAndPort("192.168.1.107", 7003));
        nodes.add(new HostAndPort("192.168.1.107", 7004));
        nodes.add(new HostAndPort("192.168.1.107", 7005));
        nodes.add(new HostAndPort("192.168.1.107", 7006));
        JedisCluster cluster = new JedisCluster(nodes);
        String s = cluster.get("hello");
        System.out.println(s);
        cluster.close();
    }

    //spring配置下jedis单机版测试
    @Test
    public void testSpringJedisSingle(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisPool jedisPool = (JedisPool) context.getBean("redisClient");
        Jedis jedis = jedisPool.getResource();
        jedis.set("aaa", "789");
        System.out.println(jedis.get("aaa"));
        jedis.close();
        jedisPool.close();
    }

    //spring配置下jedis集群测试
    @Test
    public void testSpringJedisCluster() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisCluster jedisCluster = (JedisCluster) context.getBean("redisClient");
        System.out.println(jedisCluster.get("hello"));
        jedisCluster.close();
    }
}
