package Service.elasticsearchContainer;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class deleteIndexAPITests {

	static RestHighLevelClient client = new RestHighLevelClient(
	        RestClient.builder(
	                new HttpHost("localhost", 9200, "http"),
	                new HttpHost("localhost", 9201, "http")));

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		
		//TODO change henush in the end
		CreateIndexRequest createRrequest = new CreateIndexRequest("henush");
		
		client.indices().create(createRrequest, RequestOptions.DEFAULT);
		DeleteIndexRequest request = new DeleteIndexRequest("henush");
		client.indices().delete(request, RequestOptions.DEFAULT);
	   
	}
	
	@Test
	public void deleteIndexTest() throws IOException {
		
		GetIndexRequest request = new GetIndexRequest("henush");
		boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
	
		assertTrue(!exists);
	}
	
	
	
	@Test
	public void deleteUnexistsIndexTest() throws IOException {
		
		try {
		    DeleteIndexRequest request = new DeleteIndexRequest("henush");
		    client.indices().delete(request, RequestOptions.DEFAULT);
		} catch (ElasticsearchException exception) {
		    assertEquals (exception.status(),RestStatus.NOT_FOUND);
		    	 
		}
	}
	
	
	
	
	
	
	
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		
		client.close();

	}


}
