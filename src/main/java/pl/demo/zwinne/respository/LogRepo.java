package pl.demo.zwinne.respository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pl.demo.zwinne.model.LogMessage;

public interface LogRepo extends ElasticsearchRepository<LogMessage, String> {

}
