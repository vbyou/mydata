package cn.itcast.core.service;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务接口
 * 
 * @author Administrator
 *
 */
public interface SolrService {

	/**
	 * 根据关键字搜索商品
	 * 
	 * @param keyWord
	 * @return
	 * @throws SolrServerException
	 */
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Long brandId, Float pa, Float pb, Integer pageNum, Integer pageSize)
			throws SolrServerException;

}
