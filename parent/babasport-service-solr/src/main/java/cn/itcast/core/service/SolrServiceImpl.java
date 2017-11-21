package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务实现类
 * 
 * @author Administrator
 *
 */
@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Autowired
	private HttpSolrServer solrServer;

	@Override
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Long brandId, Float pa, Float pb, Integer pageNum, Integer pageSize)
			throws SolrServerException {

		// 创建SolrQuery对象，并设置查询条件
		SolrQuery solrQuery = new SolrQuery("name_ik:" + keyword);

		// 品牌过滤
		if (brandId != null) {
			solrQuery.addFilterQuery("brandId:" + brandId);
		}

		// 价格过滤
		if (pa != null && pb != null) {
			if (pb == -1) {
				solrQuery.addFilterQuery("price:[" + pa + " TO * ]");
			} else {
				solrQuery.addFilterQuery("price:[" + pa + " TO " + pb + "]");
			}
		}

		// 设置排序
		// solrQuery.setSort("price", ORDER.desc);

		if (sort != null && sort.trim().length() > 5) {
			SortClause sortClause = new SortClause(sort.split(" ")[0],
					sort.split(" ")[1]);
			solrQuery.setSort(sortClause);
		}

		// 设置分页
		Page page = new Page(pageNum, pageSize);

		solrQuery.setStart(page.getStartRow());
		solrQuery.setRows(page.getPageSize());

		// 设置高亮的格式。。
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		solrQuery.addHighlightField("name_ik");

		QueryResponse response = solrServer.query(solrQuery);

		// 获得高亮的数据
		// 大map的key 是 id（商品id） 小map的key 是 高亮的列名
		Map<String, Map<String, List<String>>> highlighting = response
				.getHighlighting();

		SolrDocumentList results = response.getResults();

		// 查询的总数量
		long numFound = results.getNumFound();
		page.setTotal(numFound);

		List<SuperPojo> al = new ArrayList<SuperPojo>();

		for (SolrDocument solrDocument : results) {
			
			SuperPojo superPojo = new SuperPojo();

			String id = (String) solrDocument.getFieldValue("id");
			superPojo.setProperty("id", id);

			// String name = (String) solrDocument.getFieldValue("name_ik");

			// 获得高亮的文字内容
			Map<String, List<String>> map = highlighting.get(id);
			String name = map.get("name_ik").get(0);

			superPojo.setProperty("name", name);

			Long brandId2 = (Long) solrDocument.getFieldValue("brandId");
			superPojo.setProperty("brandId", brandId2);

			Float price = (Float) solrDocument.getFieldValue("price");
			superPojo.setProperty("price", price);

			String url = (String) solrDocument.getFieldValue("url");
			superPojo.setProperty("url", url);

			al.add(superPojo);

		}

		page.setResult(al);

		return page;
	}

}
