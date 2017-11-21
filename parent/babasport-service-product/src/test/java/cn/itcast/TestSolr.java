package cn.itcast;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Junit + Spring
 * 
 * @author Administrator
 * 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestSolr {
	
	@Autowired
	private SolrServer solrServer;

	@Test
	public void createIndex1() throws SolrServerException, IOException {
//
//		HttpSolrServer solrServer = new HttpSolrServer(
//				"http://192.168.56.101:8080/solr/collection1");
//
//		SolrInputDocument document = new SolrInputDocument();
//
//		document.addField("id", "1");
//		document.addField("name_ik", "范冰冰最爱的男人");
//		
//		solrServer.add(document);
//		solrServer.commit();// 提交

	}
	
	
	@Test
	public void createIndex2() throws SolrServerException, IOException {


		SolrInputDocument document = new SolrInputDocument();

		document.addField("id", "3");
		document.addField("name_ik", "范冰冰最爱的男人3");
		
		solrServer.add(document);
		solrServer.commit();// 提交

	}
	
	

}
