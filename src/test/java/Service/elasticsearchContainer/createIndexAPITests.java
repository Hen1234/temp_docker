package Service.elasticsearchContainer;

import static org.junit.Assert.*;


import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
//import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class createIndexAPITests {
	
//	private static final String XContentType = null;
	static RestHighLevelClient client = new RestHighLevelClient(
	        RestClient.builder(
	                new HttpHost("localhost", 9200, "http"),
	                new HttpHost("localhost", 9201, "http")));

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		
		//TODO change henush in the end
		CreateIndexRequest createRrequest = new CreateIndexRequest("henush");
		
		//add index settings
		createRrequest.settings(Settings.builder() 
			    .put("index.number_of_shards", 3)
			    .put("index.number_of_replicas", 2)
			);
		
		//add index alias
		createRrequest.alias(new Alias("hen")); 
		
		client.indices().create(createRrequest, RequestOptions.DEFAULT);
	   
	}

	
	@Test
	  public void getIndexTest() throws IOException {
		  
			GetIndexRequest request = new GetIndexRequest("henush");
			boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
		
			assertTrue(exists);
	}
	
//	@Test 
//	public void getMappingTest() {
//		
//		PutMappingRequest request = new PutMappingRequest("henush");
//		request.source(
//			    "{\n" +
//			    "  \"properties\": {\n" +
//			    "    \"message\": {\n" +
//			    "      \"type\": \"text\"\n" +
//			    "    }\n" +
//			    "  }\n" +
//			    "}", 
//			    XContentType.JSON);
//	}
	
	@Test 
	public void getSettingsTest() throws IOException {
		
		GetSettingsRequest request = new GetSettingsRequest().indices("henush");
		GetSettingsResponse getSettingsResponse = client.indices().getSettings(request, RequestOptions.DEFAULT);
		String numberOfShardsString = getSettingsResponse.getSetting("henush", "index.number_of_shards"); 
		assertEquals("3", numberOfShardsString);
		//TODO check replicas too- in another test??
		
		
	}
	
	@Test
	public void getAliasTest() throws IOException {
		GetAliasesRequest request = new GetAliasesRequest().indices("henush");
		GetAliasesResponse response = client.indices().getAlias(request, RequestOptions.DEFAULT);
		Map<String, Set<AliasMetadata>> aliases = response.getAliases();
		//TODO check
		assertEquals("hen", aliases.get("henush").iterator().next().alias());
		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		
		try {
		    DeleteIndexRequest request = new DeleteIndexRequest("henush");
		    client.indices().delete(request, RequestOptions.DEFAULT);
		} catch (ElasticsearchException exception) {
		    if (exception.status() == RestStatus.NOT_FOUND) {
		    	System.out.println("Index not Found");
		        
		    }
		}
		
		client.close();

	}
	

}
