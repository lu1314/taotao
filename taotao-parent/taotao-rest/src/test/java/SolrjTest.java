import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrjTest {

    @Test
    public void testAddDoc() throws Exception {
        //创建连接
        SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8090/solr7/new_core").withConnectionTimeout(2000).build();
        //创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test001");
        document.addField("item_title", "测试商品2");
        document.addField("item_price", 54312);
        //把文档对象写入索引库
        UpdateResponse response = solrClient.add(document);
        System.out.println(response.getStatus());
        //提交
        solrClient.commit();
        solrClient.close();
    }

    @Test
    public void testDel() throws IOException, SolrServerException {
        //创建连接
        SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8090/solr7/new_core").withConnectionTimeout(2000).build();
        UpdateResponse response = solrClient.deleteById("test001");
        System.out.println(response.getStatus());
        solrClient.commit();
        solrClient.close();
    }
}
