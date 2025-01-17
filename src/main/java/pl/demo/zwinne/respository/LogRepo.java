package pl.demo.zwinne.respository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Map;

public interface LogRepo extends ElasticsearchRepository<Map<String, String>, String> {

}
